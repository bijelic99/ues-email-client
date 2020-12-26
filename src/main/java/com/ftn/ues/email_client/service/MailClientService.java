package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;
import java.util.Set;

public interface MailClientService {
    Properties getProperties(Account account);
    Session getSession(Account account);
    Store getStore(Account account) throws NoSuchProviderException;
    Set<Folder> fetchFolderStructure(Account account) throws MessagingException;
    Folder refreshFolder(Folder folder) throws MessagingException;
}