package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "tag")
public class Tag extends Identifiable{
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @ManyToOne
    private User user;
}
