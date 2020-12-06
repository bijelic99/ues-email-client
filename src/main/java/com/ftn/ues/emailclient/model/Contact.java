package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
public class Contact extends Identifiable{
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String note;
    private Set<Photo> photos;
    private User user;
}
