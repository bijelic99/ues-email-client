package com.ftn.ues.email_client.repository.email_client;

import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.InServerType;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Repository;

import javax.mail.*;
import java.util.Properties;

@Repository
public class AccountECRepository {

    private Properties getProperties(@NotNull Account account){
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", account.getSmtpAddress());
        properties.put("mail.smtp.port", account.getSmtpPort());

        switch (account.getInServerType()){
            case POP3: {
                properties.put("mail.pop3.host", account.getInServerAddress());
                properties.put("mail.pop3.port", account.getInServerPort());
                properties.put("mail.pop3.ssl.enable", "true");
                break;
            }
            case IMAP: {
                properties.put("mail.imap.host", account.getInServerAddress());
                properties.put("mail.imap.port", account.getInServerPort());
                properties.put("mail.imap.ssl.enable", "true");
                break;
            }
            default:{
                break;
            }
        }
        return properties;
    }

    public Session getSession(Account account) {
        return Session.getInstance(getProperties(account), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account.getUsername(), account.getPassword());
            }
        });
    }

    public Store getStore(Account account) throws NoSuchProviderException {
        Session session = getSession(account);
        return session.getStore(account.getInServerType() == InServerType.POP3 ? "pop3" : account.getInServerType() == InServerType.IMAP ? "imap" : null );
    }

}
