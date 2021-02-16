package com.ftn.ues.email_client.configuration

import akka.actor.ActorSystem
import akka.stream.{Materializer, SystemMaterializer}
import org.springframework.context.annotation.{Bean, Configuration, Scope}
import play.api.libs.ws.StandaloneWSClient
import play.api.libs.ws.ahc.StandaloneAhcWSClient

@Configuration
class WSClientConfiguration {

  @Bean
  @Scope("singleton")
  def materializer: Materializer = {
    implicit val system = ActorSystem()
    system.registerOnTermination{
      System.exit(0)
    }

    SystemMaterializer(system).materializer
  }

  @Bean
  def wsClient(implicit materializer: Materializer): StandaloneWSClient = StandaloneAhcWSClient()

}
