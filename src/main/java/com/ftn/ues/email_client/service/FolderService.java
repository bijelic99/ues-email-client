package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;

import javax.mail.MessagingException;
import java.net.MalformedURLException;
import java.util.Set;

public interface FolderService {
    public Set<Folder> fetchFolderStructure(Account account) throws MessagingException, MalformedURLException;
    public Folder refreshFolder(Long id) throws MessagingException;
}
