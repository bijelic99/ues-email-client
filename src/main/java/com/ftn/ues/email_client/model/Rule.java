package com.ftn.ues.email_client.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "rule")
public class Rule extends Identifiable {
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name="_condition", nullable = false)
    private RuleCondition condition;

    @NonNull
    @Column(name = "rule_value")
    private String value;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleOperation operation;

    @ManyToOne
    @JoinColumn(name = "destination_folder")
    private Folder destinationFolder;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;

    public RuleCondition getCondition() {
        return condition;
    }

    public void setCondition(RuleCondition condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RuleOperation getOperation() {
        return operation;
    }

    public void setOperation(RuleOperation operation) {
        this.operation = operation;
    }

    public Folder getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(Folder destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
