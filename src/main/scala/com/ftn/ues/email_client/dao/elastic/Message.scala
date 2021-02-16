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
