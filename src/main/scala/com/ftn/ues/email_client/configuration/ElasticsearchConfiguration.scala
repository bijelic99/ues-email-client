package com.ftn.ues.email_client.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

import javax.inject.Inject

@Configuration
class ElasticsearchConfiguration @Inject()(env: Environment) {

  val endpoint: String = env.getProperty("elasticsearch.endpoint", "http://localhost:9200")

  val contactIndexName: String = env.getProperty("elasticsearch.contactIndexName", "contacts")
  val contactIndexTemplateName: String = env.getProperty("elasticsearch.contactIndexTemplateName", "contact")
  val contactIndexTemplateFileName: String = env.getProperty("elasticsearch.contactIndexTemplateFileName", "contactTemplate")

  val messageIndexName: String = env.getProperty("elasticsearch.messageIndexName", "messages")
  val messageIndexTemplateName: String = env.getProperty("elasticsearch.messageIndexTemplateName", "message")
  val messageIndexTemplateFileName: String = env.getProperty("elasticsearch.messageIndexTemplateFileName", "messageTemplate")

  val tagIndexName: String = env.getProperty("elasticsearch.tagIndexName", "tags")
  val tagIndexTemplateName: String = env.getProperty("elasticsearch.tagIndexTemplateName", "tag")
  val tagIndexTemplateFileName: String = env.getProperty("elasticsearch.tagIndexTemplateFileName", "tagTemplate")

  val pipelineName: String = env.getProperty("elasticsearch.pipelineName", "attachment")

}
