package com.ftn.ues.email_client.model.searchable_fields

abstract class SearchableField(val fields: Seq[String])
trait NestedField {
  def path: String
}
trait SearchableFields {
  def parseFieldFromString(fieldName: String): Option[SearchableField]
}
