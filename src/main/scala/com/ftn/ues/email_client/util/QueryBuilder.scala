package com.ftn.ues.email_client.util

import com.ftn.ues.email_client.model.searchable_fields.MessageSearchableFields.{Account, ParentFolder}
import com.ftn.ues.email_client.model.searchable_fields.{ContactSearchableFields, MessageSearchableFields, NestedField}
import play.api.libs.json.{JsObject, Json}

object QueryBuilder {

  def createMessagesQuery(userId: Long, params: Map[String, String]): JsObject = {
    val (filterFields, shouldFields) = params
      .flatMap {
        case (key, value) => MessageSearchableFields.parseFieldFromString(key).map(_ -> value)
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
        case (searchableTerm: NestedField, value) => searchableTerm.fields.map(x => nestedQuery(searchableTerm.path, matchQuery(x, value)))
        case (searchableTerm, value) => searchableTerm.fields.map(matchQuery(_, value))
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

  def createContactQuery(userId: Long, params: Map[String, String]): JsObject = {
    val shouldFields = params.flatMap {
      case (key, value) => ContactSearchableFields.parseFieldFromString(key).map(_ -> value)
    }

    val shouldQueries = shouldFields
      .toSeq
      .flatMap {
        case (searchableTerm, value) => searchableTerm.fields.map(matchQuery(_, value))
      }

    val filterQueries = Seq(
      termQuery("userId", userId.toString)
    )

    Json.obj(
      "size" -> 10000,
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

  private def nestedQuery(path: String, query: JsObject) = Json.obj(
    "nested" -> Json.obj(
      "path" -> path,
      "query" -> query
    )
  )

  private def matchQuery(field: String, value: String, operator: String = "or", minimumShouldMatch: Int = 1) = Json.obj(
    "match" -> Json.obj(
      field -> Json.obj(
        "query" -> value,
        "operator" -> operator,
        "minimum_should_match" -> minimumShouldMatch
      )
    )
  )

}



