package com.ftn.ues.emailclient.dao;

import com.ftn.ues.emailclient.model.Identifiable;
import lombok.Builder;
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
public abstract class DirectMapping<E extends Identifiable> extends Identifiable {

    public DirectMapping(E object){
        id = object.getId();
    }

    /**
     * Returns object of E extends Identifiable type created from current object
     * @return
     */
    public abstract E getModelObject();
}
