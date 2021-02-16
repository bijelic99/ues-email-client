package com.ftn.ues.email_client.dao.elastic

case class Attachment(
                       id: Long,
                       path: String,
                       mimeType: String,
                       name: String,
                       messageId: Long,
                       data: String,
                       metadata: Option[AttachmentMetadata],
                       deleted: Boolean
                     )

object Attachment {
  def apply(attachment: com.ftn.ues.email_client.model.Attachment, data: String): Unit = {
    Attachment(
      attachment.getId,
      attachment.getPath,
      attachment.getMimeType,
      attachment.getName,
      attachment.getMessage.getId,
      data,
      None,
      attachment.getDeleted
    )
  }
}

case class AttachmentMetadata(
                               dateTime: String,
                               contentType: String,
                               language: String,
                               content: String,
                               contentLength: Long
                             )
