package com.ftn.ues.email_client.service

import com.ftn.ues.email_client.client.MessageESClient
import com.ftn.ues.email_client.dao.elastic.{Attachment, Message}
import com.ftn.ues.email_client.repository.database.MessageRepository
import org.springframework.stereotype.Service

import java.util.Base64
import javax.inject.Inject
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava, MapHasAsScala}

@Service
class MessageIndexService @Inject()(messageESClient: MessageESClient,
                                    messageRepository: MessageRepository,
                                    fileStorageService: FileStorageService)
                                   (implicit ex: ExecutionContext) {
  private def timeout: FiniteDuration = 2.minutes

  def index(messageIds: java.util.Set[java.lang.Long]): java.util.Collection[Message] = Await.result(
    Future.sequence {
      messageRepository.findAllById(messageIds).asScala.map { message =>
        val attachments = fileStorageService
          .getAttachments(message.getAttachments)
          .asScala
          .map(x => (x.getValue0, x.getValue1))
          .map {
            case (attachment, raw) =>
              val base64Data = Base64.getEncoder.encodeToString(raw.getData)
              Attachment(attachment, base64Data)
          }
          .toSeq
        Message.apply(
          message,
          attachments
        )
      }
        .map(messageESClient.putMessage)
    }.map(_.asJavaCollection), timeout)

  def findMessages(userId: Long, params: java.util.Map[String, String]): Seq[Message] = Await.result(
    messageESClient.searchForMessages(userId, params.asScala.toMap),
    timeout
  )
}
