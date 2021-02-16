package com.ftn.ues.email_client.service

import com.ftn.ues.email_client.client.MessageESClient
import com.ftn.ues.email_client.dao.elastic.Message
import org.springframework.stereotype.Service

import java.util
import javax.inject.Inject

@Service
class MessageIndexService @Inject()(messageESClient: MessageESClient) {
  def index(messageIds: java.util.Set[java.lang.Long]): java.util.Set[Message] = {
    new util.HashSet[Message]()
  }
}
