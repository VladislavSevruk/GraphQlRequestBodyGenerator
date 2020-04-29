package com.github.vladislavsevruk.generator.test.data;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GenericTestModelWithoutAnnotations<T> {

    private Collection<T> collectionEntity;
    private Collection<T> collectionField;
    private T fieldWithDelegateAnnotation;
    private T fieldWithEntityAnnotation;
    private Long fieldWithFieldAnnotation;
    private Long fieldWithIgnoreAnnotation;
    private Long fieldWithoutAnnotations;
    private Long id;
    private Long idField;
    private List<T> listEntity;
    private T mandatoryEntity;
    private Long mandatoryField;
    private T namedEntity;
    private Long namedField;
    private T namedMandatoryEntity;
    private Long namedMandatoryField;
    private Queue<T> queueEntity;
    private Set<T> setEntity;
}
