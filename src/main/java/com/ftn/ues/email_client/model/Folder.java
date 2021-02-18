package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "folder")
public class Folder extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false, name = "folder_url")
    private String folderUrl;

    @NonNull
    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Folder> children = new HashSet<>();

    @ManyToOne
    private Folder parentFolder;

    private Boolean isMainInbox;

    @Builder.Default
    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE)
    private Set<Message> messages = new HashSet<>();

    @ManyToOne
    private Account account;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderUrl() {
        return folderUrl;
    }

    public void setFolderUrl(String folderUrl) {
        this.folderUrl = folderUrl;
    }

    public Set<Folder> getChildren() {
        return children;
    }

    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public Boolean getMainInbox() {
        return isMainInbox;
    }

    public void setMainInbox(Boolean mainInbox) {
        isMainInbox = mainInbox;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
