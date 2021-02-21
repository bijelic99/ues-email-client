package com.ftn.ues.email_client.controller;

import com.ftn.ues.email_client.dao.rest.Folder;
import com.ftn.ues.email_client.repository.database.FolderRepository;
import com.ftn.ues.email_client.service.FolderService;
import com.ftn.ues.email_client.util.DirectMappingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/folder")
public class FolderController {

    @Autowired
    FolderService folderService;

    @Autowired
    FolderRepository folderRepository;

    @GetMapping
    public List<Folder> getAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return DirectMappingConverter.toMapping(folderRepository.findAll(), com.ftn.ues.email_client.model.Folder.class, Folder.class);
    }

    @GetMapping("/{id}/refresh")
    public Folder refresh(@PathVariable("id") Long id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MessagingException {
        return DirectMappingConverter.toMapping(folderService.refreshFolder(id), com.ftn.ues.email_client.model.Folder.class, Folder.class);
    }

    @GetMapping("/{id}/childFolders")
    public Set<Folder> getChildFolders(@PathVariable("id") Long id) {
        return folderRepository.findById(id)
                .stream()
                .flatMap(folder -> folder.getChildren().stream())
                .filter(folder -> !folder.getDeleted())
                .map(Folder::new)
                .collect(Collectors.toSet());
    }

}
