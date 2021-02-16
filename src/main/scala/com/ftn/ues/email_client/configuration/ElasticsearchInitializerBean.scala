package com.ftn.ues.email_client.configuration

import com.ftn.ues.email_client.client.util.LoadResourceFile
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import play.api.libs.ws.StandaloneWSClient

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Component
class ElasticsearchInitializerBean @Inject()(
                                              config: ElasticsearchConfiguration,
                                              wsClient: StandaloneWSClient
                                            )
                                            (
                                              implicit ec: ExecutionContext
                                            ) extends InitializingBean {


  override def afterPropertiesSet(): Unit =
    isPiplineCreated.flatMap(if (_) Future.successful() else putPipeline).recover( t => t.printStackTrace())

  def isPiplineCreated: Future[Boolean] = wsClient
    .url(s"${config.endpoint}/_ingest/pipeline/${config.pipelineName}")
    .get()
    .collect {
      case res if res.status == 200 =>
        println(s"Pipeline '${config.pipelineName}' present")
        true
      case res =>
        println(s"Pipeline '${config.pipelineName}' not found got '${res.status}'")
        false
    }

  def putPipeline: Future[Unit] = LoadResourceFile
    .loadResourceJsonFile("elasticsearch/pipelineConfig.json") match {
    case Success(template) =>
      wsClient
        .url(s"${config.endpoint}/_ingest/pipeline/${config.pipelineName}")
        .put(template)
        .collect {
          case res if res.status == 200 =>
            println(s"Put pipeline '${config.pipelineName}'")
          case res =>
            throw new Exception(s"Failed to put '${config.pipelineName}', got '${res.status}' code")
        }
    case Failure(exception) =>
      println(s"Failed to get pipeline config for '${config.pipelineName}")
      Future.failed(exception)
  }

}
