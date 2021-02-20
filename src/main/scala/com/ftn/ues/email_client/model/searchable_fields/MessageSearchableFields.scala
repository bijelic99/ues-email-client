package com.ftn.ues.email_client.model.searchable_fields

object MessageSearchableFields extends SearchableFields {

  case object Sender extends SearchableField(Seq("from"))

  case object Recipient extends SearchableField(Seq("to", "cc", "bcc"))

  case object Subject extends SearchableField(Seq("subject"))

  case object Content extends SearchableField(Seq("content"))

  case object Attachment extends SearchableField(Seq("attachments.metadata.content")) with NestedField {
    override val path: String = "attachments.metadata"
  }

  case object ParentFolder extends SearchableField(Seq("parentFolderId"))

  case object Account extends SearchableField(Seq("accountId"))

  case object Tag extends SearchableField(Seq("tags.name")) with NestedField {
    override def path: String = "tags"
  }


  override def parseFieldFromString(fieldName: String): Option[SearchableField] = fieldName match {
    case "sender" => Some(Sender)
    case "recipient" => Some(Recipient)
    case "subject" => Some(Subject)
    case "content" => Some(Content)
    case "attachment" => Some(Attachment)
    case "parentFolder" => Some(ParentFolder)
    case "account" => Some(Account)
    case "tag" => Some(Tag)
    case _ => None
  }
}
