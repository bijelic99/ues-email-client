package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.repository.elastic_search.MessageESRepository;
import com.ftn.ues.email_client.service.IndexingService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Log4j2
@Service
public class IndexingServiceImpl implements IndexingService {

    @Autowired
    MessageESRepository messageESRepository;

    @Override
    public Boolean indexMessage(Message... messages) {
        try {
            var esMessages = DirectMappingConverter.toMapping(Arrays.asList(messages), Message.class, com.ftn.ues.email_client.dao.elastic_search.Message.class);
            messageESRepository.saveAll(esMessages);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }
}
