package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

@Data
@SuperBuilder
public class Folder extends Identifiable {
    private String name;
    private Set<Folder> children;
    private Optional<Folder> parentFolder;
    private Set<Message> messages;
    private Set<Rule> rules;
}
