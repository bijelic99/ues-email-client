package com.ftn.ues.email_client.util;

import com.ftn.ues.email_client.dao.DirectMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to convert from model object to DirectMapping object
 */
public interface DirectMappingConverter {

    /**
     * Converts model object to object of provided DirectMapping
     * @param object convert from
     * @param eClass model class
     * @param tClass mapping class
     * @param <E> model type
     * @param <T> mapping type
     * @return object of T type
     * @throws NoSuchMethodException possible exception
     * @throws IllegalAccessException possible exception
     * @throws InvocationTargetException possible exception
     * @throws InstantiationException possible exception
     */
    public static <E extends Object, T extends DirectMapping<E>> T toMapping(E object, Class<E> eClass, Class<T> tClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var constructor = tClass.getConstructor(eClass);
        return (T) constructor.newInstance(object);
    }

    /**
     * Converts model objects to objects of provided DirectMapping
     * @param objects convert from
     * @param eClass model class
     * @param tClass mapping class
     * @param <E> model type
     * @param <T> mapping type
     * @return object list of T type
     * @throws NoSuchMethodException possible exception
     * @throws IllegalAccessException possible exception
     * @throws InvocationTargetException possible exception
     * @throws InstantiationException possible exception
     */
    public static <E extends Object, T extends DirectMapping<E>> List<T> toMapping(Collection<E> objects, Class<E> eClass, Class<T> tClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return objects.stream().flatMap(obj-> {
            var resOpt = Optional.empty();
            try {
                resOpt = Optional.of(toMapping(obj, eClass, tClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resOpt.stream().map(o -> (T) o);
        }).collect(Collectors.toList());
    }

    /**
     * Converts from mapping to model object
     * @param mapping convert from
     * @param <E> model type
     * @param <T> mapping type
     * @return object of E type
     */
    public static <E extends Object, T extends DirectMapping<E>> E toModel(T mapping){
        return mapping.getModelObject();
    }

    /**
     * Converts from mapping objects to model objects
     * @param mappings convert from
     * @param <E> model type
     * @param <T> mapping type
     * @return object list of E type
     */
    public static <E extends Object, T extends DirectMapping<E>> List<E> toModel(Collection<T> mappings){
        return mappings.stream().map(t -> t.getModelObject()).collect(Collectors.toList());
    }
}
