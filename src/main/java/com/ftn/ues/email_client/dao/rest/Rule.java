package com.ftn.ues.email_client.dao.rest;

import com.ftn.ues.email_client.dao.DirectMapping;
import com.ftn.ues.email_client.model.Folder;
import com.ftn.ues.email_client.model.RuleCondition;
import com.ftn.ues.email_client.model.RuleOperation;
import lombok.Builder;
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
        deleted = rule.getDeleted();

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

    @Builder.Default
    private Boolean deleted = false;

    @Override
    public com.ftn.ues.email_client.model.Rule getModelObject() {
        var destFolder = new Folder();
        destFolder.setId(destinationFolder);
        return com.ftn.ues.email_client.model.Rule.builder()
                .id(id)
                .condition(condition)
                .value(value)
                .operation(operation)
                .destinationFolder(destFolder)
                .deleted(deleted)
                .build();
    }
}
