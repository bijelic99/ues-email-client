package com.ftn.ues.email_client.dao.elastic

case class Attachment(
                       id: Long,
                       userId: Long,
                       path: String,
                       mimeType: String,
                       name: String,
                       messageId: Long,
                       data: String,
                       metadata: Option[AttachmentMetadata],
                       deleted: Boolean
                     )


case class AttachmentMetadata(
                               dateTime: String,
                               contentType: String,
                               language: String,
                               content: String,
                               contentLength: Long
                             )