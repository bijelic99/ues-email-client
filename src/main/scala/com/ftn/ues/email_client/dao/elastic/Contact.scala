package com.ftn.ues.email_client.dao.elastic

import play.api.libs.json.{Json, OFormat}

import scala.jdk.CollectionConverters.CollectionHasAsScala

case class Contact(
                    id: Long,
                    firstName: String,
                    lastName: String,
                    displayName: String,
                    email: String,
                    note: String,
                    photoIds: Seq[Long],
                    userId: Long,
                    deleted: Boolean
                  )

object Contact {
  def apply(contact: com.ftn.ues.email_client.model.Contact): Contact = Contact(
    contact.getId,
    contact.getFirstName,
    contact.getLastName,
    contact.getDisplayName,
    contact.getEmail,
    contact.getNote,
    contact.getPhotos.asScala.toSeq.map(_.getId),
    contact.getUser.getId,
    contact.getDeleted
  )

  implicit val format: OFormat[Contact] = Json.format[Contact]
}
