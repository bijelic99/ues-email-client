package com.ftn.ues.email_client.client

import com.ftn.ues.email_client.configuration.ElasticsearchConfiguration
import org.springframework.stereotype.Service
import play.api.libs.ws.StandaloneWSClient

import javax.annotation.PostConstruct
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Service
class TagESClient @Inject()(esConfig: ElasticsearchConfiguration,
                            wsClient: StandaloneWSClient)
                           (implicit ec: ExecutionContext)
  extends ElasticsearchClient(
    endpoint = esConfig.endpoint,
    indexName = esConfig.tagIndexName,
    templateName = esConfig.tagIndexTemplateName,
    templateFilename = esConfig.tagIndexTemplateFileName,
    wsClient
  ) {

}
