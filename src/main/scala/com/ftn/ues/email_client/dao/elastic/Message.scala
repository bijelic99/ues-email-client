package com.ftn.ues.email_client.dao.elastic

case class Message(
                    id: Long,
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

  def apply(message: com.ftn.ues.email_client.model.Message, attachments: Seq[Attachment]) =
    Message(
      message.getId,
      message.getFrom,
      message.getTo,
      message.getCc,
      message.getBcc,
      message.getDateTime.formatted("dd-MM-yyyy HH:mm:ss"),
      message.getSubject,
      message.getContent,
      message.getUnread,
      attachments,
      message.getTags.asScala.toSeq.map(Tag.apply),
      message.getParentFolder.getId,
      message.getAccount.getId,
      message.getDeleted,
      message.getContent.substring(0, if(message.getContent.length < 256) message.getContent.length else 256),
      message.getContent
    )
}
