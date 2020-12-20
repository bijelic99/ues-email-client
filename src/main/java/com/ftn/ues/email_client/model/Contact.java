package com.ftn.ues.email_client.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact extends Identifiable{
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @NonNull
    @Column(nullable = false)
    private String email;

    @NonNull
    @Column
    private String note;

    @NonNull
    @Builder.Default
    @OneToMany(mappedBy = "contact", orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();

    @NonNull
    @ManyToOne
    private User user;
}
