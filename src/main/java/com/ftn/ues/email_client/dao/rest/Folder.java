package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Account;
import com.ftn.ues.email_client.model.Identifiable;
import com.ftn.ues.email_client.model.Message;
import com.ftn.ues.email_client.model.Rule;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
public class Folder extends DirectMapping<com.ftn.ues.email_client.model.Folder> {

    public Folder(com.ftn.ues.email_client.model.Folder folder){
        id = folder.getId();
        name = folder.getName();
        children = folder.getChildren().stream().map(Identifiable::getId).collect(Collectors.toSet());
        parentFolder = folder.getParentFolder() != null ? folder.getParentFolder().getId() : null;
        messages = folder.getMessages().stream().map(Identifiable::getId).collect(Collectors.toSet());
        rules = folder.getRules().stream().map(Identifiable::getId).collect(Collectors.toSet());
        account = folder.getAccount().getId();
        deleted = folder.getDeleted();
    }

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Builder.Default
    private Set<Long> children = new HashSet<>();

    private Long parentFolder;

    @Builder.Default
    private Set<Long> messages = new HashSet<>();

    @Builder.Default
    private Set<Long> rules = new HashSet<>();

    private Long account;

    @Builder.Default
    private Boolean deleted = false;

    @Override
    public com.ftn.ues.email_client.model.Folder getModelObject() {
        var parentFldr = new com.ftn.ues.email_client.model.Folder();
        parentFldr.setId(parentFolder);
        var acc = new Account();
        acc.setId(account);
        return com.ftn.ues.email_client.model.Folder.builder()
                .id(id)
                .name(name)
                .children(children.stream().map(ch->{
                    var f = new com.ftn.ues.email_client.model.Folder();
                    f.setId(ch);
                    return f;
                }).collect(Collectors.toSet()))
                .parentFolder(parentFldr)
                .messages(messages.stream().map(msg->{
                    var m = new Message();
                    m.setId(msg);
                    return m;
                }).collect(Collectors.toSet()))
                .rules(rules.stream().map(rule->{
                    var r = new Rule();
                    r.setId(rule);
                    return r;
                }).collect(Collectors.toSet()))
                .account(acc)
                .deleted(deleted)
                .build();
    }
}
