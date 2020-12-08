package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "photo")
public class Photo extends Identifiable {
    @NonNull
    @Column(nullable = false)
    private String path;

    @NonNull
    @ManyToOne
    private Contact contact;
}
