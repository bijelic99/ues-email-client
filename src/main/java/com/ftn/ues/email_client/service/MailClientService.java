package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.model.MessageRaw;
import org.apache.commons.mail.EmailException;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Set;

public interface MailClientService {
    Properties getProperties(Account account);
    Session getSession(Account account);
    Store getStore(Account account) throws NoSuchProviderException;
    Set<Folder> fetchFolderStructure(Account account) throws MessagingException, MalformedURLException;
    Folder refreshFolder(Folder folder) throws MessagingException, MalformedURLException;
    Message sendMessage(Message message) throws MessagingException;
    Set<MessageRaw> getNewPop3Messages(Account account) throws MessagingException;
    Set<MessageRaw> getNewImapMessages(Account account, Folder folder) throws MessagingException;
}
