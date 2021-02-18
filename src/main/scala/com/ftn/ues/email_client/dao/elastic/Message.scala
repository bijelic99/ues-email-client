package com.ftn.ues.email_client.dao.elastic

import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Json, OFormat}


case class Message(
                    id: Long,
                    userId: Long,
                    from: String,
                    to: String,
                    cc: String,
                    bcc: String,
                    dateTime: String,
                    subject: String,
                    content: String,
                    unread: Boolean,
                    attachments: Seq[Attachment],
                    tags: Seq[Tag],
                    parentFolderId: Long,
                    accountId: Long,
                    deleted: Boolean,
                    abstractText: String,
                    fullText: String
                  )

object Message {
  import scala.jdk.CollectionConverters._

  private val dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")

  def apply(message: com.ftn.ues.email_client.model.Message, attachments: Seq[Attachment]): Message =
    Message(
      message.getId,
      message.getAccount.getUser.getId,
      message.getFrom,
      message.getTo,
      message.getCc,
      message.getBcc,
      dtf.print(message.getDateTime),
      message.getSubject,
      message.getContent,
      message.getUnread,
      attachments,
      message.getTags.asScala.toSeq.map(Tag.apply),
      message.getParentFolder.getId,
      message.getAccount.getId,
      message.getDeleted,
      message.getContent.substring(0, if (message.getContent.length < 256) message.getContent.length else 256),
      message.getContent
    )

  implicit val format: OFormat[Message] = Json.format[Message]
}
