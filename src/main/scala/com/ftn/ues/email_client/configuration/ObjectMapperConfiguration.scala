package com.ftn.ues.email_client.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.context.annotation.{Bean, Configuration, Primary}
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
class ObjectMapperConfiguration {

  @Bean
  @Primary
  def addScalaModule: ObjectMapper = {
    val om = new ObjectMapper()
    om.registerModules(DefaultScalaModule, new JodaModule)
    om
  }

}
