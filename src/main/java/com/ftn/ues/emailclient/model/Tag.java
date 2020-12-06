package com.ftn.ues.emailclient.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Tag extends Identifiable{
    private String name;
    private User user;
}
