package com.ftn.ues.email_client.controller;


import com.ftn.ues.email_client.dao.rest.Message;
import com.ftn.ues.email_client.repository.database.MessageRepository;
import com.ftn.ues.email_client.service.MessageIndexService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageRepository repository;

    @Autowired
    MessageIndexService messageIndexService;

    @GetMapping("/all")
    public List<Message> getAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return DirectMappingConverter.toMapping(repository.findAll(), com.ftn.ues.email_client.model.Message.class, Message.class);
    }
}
