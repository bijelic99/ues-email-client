package com.ftn.ues.email_client.util

import com.ftn.ues.email_client.util.MessageSearchableFields.{Account, ParentFolder}
import play.api.libs.json.{JsObject, Json}

object QueryBuilder {

  def createMessagesQuery(userId: Long, params: Map[String, String]) = {
    val (filterFields, shouldFields) = params
      .flatMap {
        case (key, value) => MessageSearchableFields.parseFieldFromString(key).map((_, value))
      }
      .partition {
        case (key, _) => Seq(Account, ParentFolder).contains(key)
      }

    val filterQueries = filterFields
      .toSeq
      .flatMap {
        case (fieldNames, value) => fieldNames.fields.map(termQuery(_, value))
      } :+ termQuery("userId", userId.toString)

    val shouldQueries = shouldFields
      .toSeq
      .flatMap {
        case (fieldNames, value) => fieldNames.fields.map(termQuery(_, value, Some(5)))
      }

    Json.obj("size" -> 10000,
      "query" -> Json.obj(
        "bool" -> Json.obj(
          "filter" -> filterQueries,
          "should" -> shouldQueries,
          "minimum_should_match" -> (if (shouldQueries.size > 0) 1 else 0)
        )
      )
    )
  }

  private def termQuery(field: String, value: String, boost: Option[Double] = None) = Json.obj(
    "term" -> Json.obj(
      field -> (Json.obj(
        "value" -> value
      ) ++ boost.fold(JsObject.empty)(x => Json.obj(
        "boost" -> x
      )
      )
        )
    )
  )

}

abstract class SearchableField(val fields: Seq[String])

trait SearchableFields {
  def parseFieldFromString(fieldName: String): Option[SearchableField]
}

object MessageSearchableFields extends SearchableFields {

  case object Sender extends SearchableField(Seq("from"))

  case object Recipient extends SearchableField(Seq("to", "cc", "bcc"))

  case object Subject extends SearchableField(Seq("subject"))

  case object Content extends SearchableField(Seq("content"))

  case object Attachment extends SearchableField(Seq("attachments.metadata.content"))

  case object ParentFolder extends SearchableField(Seq("parentFolderId"))

  case object Account extends SearchableField(Seq("accountId"))


  override def parseFieldFromString(fieldName: String): Option[SearchableField] = fieldName match {
    case "sender" => Some(Sender)
    case "recipient" => Some(Recipient)
    case "subject" => Some(Subject)
    case "content" => Some(Content)
    case "attachment" => Some(Attachment)
    case "parentFolder" => Some(ParentFolder)
    case "account" => Some(Account)
    case _ => None
  }
}
