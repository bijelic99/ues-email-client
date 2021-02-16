package com.ftn.ues.email_client.configuration

import org.springframework.context.annotation.{Bean, Configuration}

import java.util.concurrent.ForkJoinPool
import scala.concurrent.ExecutionContext

@Configuration
class ExecutionContextConfiguration {

  @Bean
  def ec(): ExecutionContext = {
    ExecutionContext.fromExecutor(
      new ForkJoinPool(6)
    )
  }
}
