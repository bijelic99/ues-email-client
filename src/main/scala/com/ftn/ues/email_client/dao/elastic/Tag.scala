package com.ftn.ues.email_client.dao.elastic

case class Tag(id: Long, name: String, userId: Long, deleted: Boolean)

object Tag {
  def apply(tag: com.ftn.ues.email_client.model.Tag): Tag =
    Tag(
      tag.getId,
      tag.getName,
      tag.getId,
      tag.getDeleted
    )
}
