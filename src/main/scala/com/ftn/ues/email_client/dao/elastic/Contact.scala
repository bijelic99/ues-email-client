package com.ftn.ues.email_client.dao.elastic

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
