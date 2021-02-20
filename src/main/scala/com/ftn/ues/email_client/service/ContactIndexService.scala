package com.ftn.ues.email_client.service

import com.ftn.ues.email_client.client.ContactESClient
import com.ftn.ues.email_client.dao.elastic.Contact
import com.ftn.ues.email_client.repository.database.ContactRepository
import org.springframework.stereotype.Service

import javax.inject.Inject
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava, MapHasAsScala}

@Service
class ContactIndexService @Inject()(
                                     contactESClient: ContactESClient,
                                     contactRepository: ContactRepository
                                   )(implicit ex: ExecutionContext) {
  private def timeout: FiniteDuration = 2.minutes

  def index(contactIds: java.util.Set[java.lang.Long]): java.util.Collection[Contact] = Await.result(
    Future.sequence {
      contactRepository
        .findAllById(contactIds)
        .asScala
        .map(Contact.apply)
        .map(contactESClient.putContact)
    }
      .map(_.flatten)
      .map(_.asJavaCollection),
    timeout
  )

  def findContacts(userId: Long, params: java.util.Map[String, String]): Seq[Contact] = Await.result(
    contactESClient.searchForContacts(userId, params.asScala.toMap),
    timeout
  )

}
