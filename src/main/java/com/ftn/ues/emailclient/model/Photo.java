package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Photo extends Identifiable {
    private String path;
    private Contact contact;
}
