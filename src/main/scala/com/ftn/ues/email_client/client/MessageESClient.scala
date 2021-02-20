package com.ftn.ues.email_client.client

import com.ftn.ues.email_client.configuration.ElasticsearchConfiguration
import com.ftn.ues.email_client.dao.elastic.Message
import com.ftn.ues.email_client.util.QueryBuilder
import org.springframework.stereotype.Service
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.JsonBodyReadables.readableAsJson
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSClient

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Service
class MessageESClient @Inject()(esConfig: ElasticsearchConfiguration,
                                wsClient: StandaloneWSClient)
                               (implicit ec: ExecutionContext)
  extends ElasticsearchClient(
    endpoint = esConfig.endpoint,
    indexName = esConfig.messageIndexName,
    templateName = esConfig.messageIndexTemplateName,
    templateFilename = esConfig.messageIndexTemplateFileName,
    wsClient
  ) {
  def putMessage(message: Message): Future[Option[Message]] = wsClient
    .url(s"$endpoint/$indexName/_doc/${message.id}?pipeline=${esConfig.pipelineName}")
    .put(Json.toJson(message))
    .map{
      case res if Seq(200, 201).contains(res.status) =>
        log.debug(res)
        Some(message)
      case res =>
        log.error(s"Indexing failed, got ${res.status}")
        None
    }

  def searchForMessages(userId: Long, params: Map[String, String]): Future[Seq[Message]] = {
    val query = QueryBuilder.createMessagesQuery(userId, params)
    log.debug(query.toString)
    wsClient
      .url(s"$endpoint/$indexName/_search")
      .post(query)
      .map{
        case res if res.status == 200 =>
          (res.body[JsValue] \ "hits" \ "hits")
            .asOpt[Seq[JsObject]]
            .getOrElse(Seq.empty)
            .flatMap(_.\("_source").asOpt[JsObject])
            .flatMap(_.asOpt[Message])
      }
  }
}
