package com.ftn.ues.email_client.client

import com.ftn.ues.email_client.configuration.ElasticsearchConfiguration
import org.springframework.stereotype.Service
import play.api.libs.ws.StandaloneWSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext

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
  def indexMessage() = {

  }
}
