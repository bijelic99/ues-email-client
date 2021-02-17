package com.ftn.ues.email_client.dao.elastic

import play.api.libs.json.{Json, OFormat}

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

object Contact{
  implicit val format: OFormat[Contact] = Json.format[Contact]
}
