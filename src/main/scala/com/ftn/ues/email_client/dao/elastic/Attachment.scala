package com.ftn.ues.email_client.dao.elastic

import play.api.libs.json.{Json, OFormat}


case class AttachmentMetadata(
                               date: Option[String],
                               language: Option[String],
                               content: Option[String]
                             )
object AttachmentMetadata{
  implicit val format: OFormat[AttachmentMetadata] = Json.format[AttachmentMetadata]
}

case class Attachment(
                       id: Long,
                       path: String,
                       mimeType: String,
                       name: String,
                       messageId: Long,
                       data: Option[String]=None,
                       metadata: Option[AttachmentMetadata],
                       deleted: Boolean
                     )

object Attachment {
  def apply(attachment: com.ftn.ues.email_client.model.Attachment, data: String): Attachment = {
    Attachment(
      attachment.getId,
      attachment.getPath,
      attachment.getMimeType,
      attachment.getName,
      attachment.getMessage.getId,
      Some(data),
      None,
      attachment.getDeleted
    )
  }

  implicit val format: OFormat[Attachment] = Json.format[Attachment]
}

