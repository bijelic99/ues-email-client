package com.ftn.ues.email_client.dao.elastic

import play.api.libs.json.{Json, OFormat}

case class Tag(id: Long, name: String, userId: Long, deleted: Boolean)

object Tag {
  def apply(tag: com.ftn.ues.email_client.model.Tag): Tag =
    Tag(
      tag.getId,
      tag.getName,
      tag.getUser.getId,
      tag.getDeleted
    )

  implicit val format: OFormat[Tag] = Json.format[Tag]
}
