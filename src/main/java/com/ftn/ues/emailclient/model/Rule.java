package com.ftn.ues.emailclient.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
public class Rule extends Identifiable {
    private RuleCondition condition;
    private String value;
    private RuleOperation operation;
    private Optional<Folder> destinationFolder;
    private Folder folder;
}
