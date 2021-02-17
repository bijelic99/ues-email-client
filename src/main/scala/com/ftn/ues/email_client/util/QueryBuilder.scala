package com.ftn.ues.email_client.util

import play.api.libs.json.Json

object QueryBuilder {

  def createMessagesQuery(userId: Long, params: Map[String, String]) =
    Json.obj("size" -> 10000,
      "query" -> Json.obj(
        "bool" -> Json.obj(
          "filter" -> Json.obj(
            "userId" -> Json.obj(
              "value" -> userId
            )
          ),
          "must" -> params.map {
            case (fieldName, value) =>
              Json.obj(
                "term" -> Json.obj(
                  fieldName -> Json.obj(
                    "value" -> value,
                    "boost" -> 2.0
                  )
                )
              )
          }
        )
      )
    )

}
