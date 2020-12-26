package com.ftn.ues.email_client.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ftn.ues.email_client.model.Identifiable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Used when directly mapping dao to type
 * @param <E> the type of object we are mapping to and from
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class DirectMapping<E extends Object> {

    public DirectMapping(E object){

    }

    /**
     * Returns object of E extends Identifiable type created from current object
     * @return
     */
    @JsonIgnore
    public abstract E getModelObject();
}
