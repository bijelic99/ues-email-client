package com.ftn.ues.email_client.model.searchable_fields

object ContactSearchableFields extends SearchableFields {

  case object FirstName extends SearchableField(Seq("firstName"))
  case object LastName extends SearchableField(Seq("lastName"))
  case object Note extends SearchableField(Seq("note"))

  override def parseFieldFromString(fieldName: String): Option[SearchableField] = fieldName match {
    case "firstName" => Some(FirstName)
    case "lastName" => Some(LastName)
    case "note" => Some(Note)
    case _ => None
  }
}
