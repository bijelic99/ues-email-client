package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.RuleCondition;
import com.ftn.ues.email_client.model.RuleOperation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Rule extends DirectMapping<com.ftn.ues.email_client.model.Rule> {

    public Rule(com.ftn.ues.email_client.model.Rule rule){
        id = rule.getId();
        condition = rule.getCondition();
        value = rule.getValue();
        operation = rule.getOperation();
        destinationFolder = rule.getDestinationFolder() != null ?  rule.getDestinationFolder().getId() : null;
        folder = rule.getFolder().getId();

    }

    @NonNull
    private Long id;

    @NonNull
    private RuleCondition condition;

    @NonNull
    private String value;

    @NonNull
    private RuleOperation operation;

    private Long destinationFolder;

    @NonNull
    private Long folder;

    @Override
    public com.ftn.ues.email_client.model.Rule getModelObject() {
        var destFolder = new Folder();
        destFolder.setId(destinationFolder);
        var parentFolder = new Folder();
        parentFolder.setId(folder);
        return com.ftn.ues.email_client.model.Rule.builder()
                .id(id)
                .condition(condition)
                .value(value)
                .operation(operation)
                .destinationFolder(destFolder)
                .folder(parentFolder)
                .build();
    }
}