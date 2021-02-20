package com.ftn.ues.email_client.client

import com.ftn.ues.email_client.client.util.LoadResourceFile
import org.apache.logging.log4j.LogManager
import play.api.libs.json.JsObject
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSClient

import javax.annotation.PostConstruct
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


abstract class ElasticsearchClient(
                                    protected val endpoint: String,
                                    protected val indexName: String,
                                    protected val templateName: String,
                                    protected val templateFilename: String,
                                    wsClient: StandaloneWSClient
                                  )
                                  (implicit ec: ExecutionContext) {
  protected final val log = LogManager.getLogger(classOf[ElasticsearchClient])

  @PostConstruct
  def init: Future[Unit] = {
    def handleTemplate = checkTemplate.flatMap(if (_) Future.successful() else putTemplate)

    def handleIndex = checkIndex.flatMap(if (_) Future.successful() else putIndex)

    pingEndpoint
      .flatMap(_ => handleTemplate)
      .flatMap(_ => handleIndex)
      .recover(t => log.error(t.getMessage))
  }

  def pingEndpoint: Future[Unit] = wsClient
    .url(endpoint)
    .head()
    .collect {
      case res if res.status == 200 =>
        log.info(s"Endpoint '$endpoint' pinged successfully")
      case res =>
        throw new Exception(s"Endpoint '$endpoint' not available, got ${res.status} code")
    }

  def checkTemplate: Future[Boolean] = wsClient
    .url(s"$endpoint/_template/$templateName")
    .head()
    .collect {
      case res if res.status == 200 =>
        true
      case res =>
        log.info(s"Template '$templateName' not available, got ${res.status} code")
        false
    }

  def putTemplate: Future[Unit] =
    LoadResourceFile.loadElasticTemplate(templateFilename) match {
      case Success(template) =>
        wsClient.url(s"$endpoint/_template/$templateName")
          .put(template)
          .collect {
            case res if res.status == 200 =>
              log.info(s"Template '$templateName' put")
            case res =>
              throw new Exception(s"Template '$templateName' put failed got ${res.status} code")
          }
      case Failure(exception) => Future.failed(exception)
    }

  def checkIndex: Future[Boolean] = wsClient
    .url(s"$endpoint/$indexName")
    .head()
    .collect {
      case res if res.status == 200 =>
        true
      case res =>
        log.info(s"Index '${indexName}' not present, got ${res.status} code")
        false
    }

  def putIndex: Future[Unit] = wsClient
    .url(s"$endpoint/$indexName")
    .put(JsObject.empty).collect {
    case res if res.status == 200 =>
      log.info(s"Created index '$indexName'")
    case res =>
      throw new Exception(s"Failed to create index '$indexName', got ${res.status} code")
  }
}
