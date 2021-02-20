package com.ftn.ues.email_client.client


import com.ftn.ues.email_client.configuration.ElasticsearchConfiguration
import com.ftn.ues.email_client.dao.elastic.Contact
import com.ftn.ues.email_client.util.QueryBuilder
import org.springframework.stereotype.Service
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.JsonBodyReadables.readableAsJson
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSClient

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Service
class ContactESClient @Inject()(esConfig: ElasticsearchConfiguration,
                                wsClient: StandaloneWSClient)
                               (implicit ec: ExecutionContext)
  extends ElasticsearchClient(
    endpoint = esConfig.endpoint,
    indexName = esConfig.contactIndexName,
    templateName = esConfig.contactIndexTemplateName,
    templateFilename = esConfig.contactIndexTemplateFileName,
    wsClient
  ) {

  def putContact(contact: Contact): Future[Option[Contact]] = wsClient
    .url(s"$endpoint/$indexName/_doc/${contact.id}")
    .put(Json.toJson(contact))
    .map{
      case res if Seq(200, 201).contains(res.status)  =>
        log.debug(res)
        Some(contact)
      case res =>
        log.error(s"Indexing failed, got ${res.status}")
        None
    }

  def searchForContacts(userId: Long, params: Map[String, String]): Future[Seq[Contact]] = {
    val query = QueryBuilder.createContactQuery(userId, params)
    log.debug(query.toString)
    wsClient
      .url(s"$endpoint/$indexName/_search")
      .post(query)
      .map {
        case res if res.status == 200 =>
          (res.body[JsValue] \ "hits" \ "hits")
            .asOpt[Seq[JsObject]]
            .getOrElse(Seq.empty)
            .flatMap(_.\("_source").asOpt[JsObject])
            .flatMap(_.asOpt[Contact])
      }
  }
}
