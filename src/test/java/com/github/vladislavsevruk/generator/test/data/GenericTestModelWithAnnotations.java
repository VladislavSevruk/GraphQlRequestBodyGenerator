package com.github.vladislavsevruk.generator.test.data;

import com.github.vladislavsevruk.generator.annotation.GqlDelegate;
import com.github.vladislavsevruk.generator.annotation.GqlEntity;
import com.github.vladislavsevruk.generator.annotation.GqlField;
import com.github.vladislavsevruk.generator.annotation.GqlIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GenericTestModelWithAnnotations<T> {

    @GqlEntity
    private Collection<T> collectionEntity;
    @GqlField
    private Collection<T> collectionField;
    @GqlDelegate
    private T fieldWithDelegateAnnotation;
    @GqlEntity
    private T fieldWithEntityAnnotation;
    @GqlField
    private Long fieldWithFieldAnnotation;
    @GqlIgnore
    private Long fieldWithIgnoreAnnotation;
    private Long fieldWithoutAnnotations;
    @GqlField(name = "idField")
    private Long id;
    @GqlField(name = "id")
    private Long idField;
    @GqlEntity
    private List<T> listEntity;
    @GqlEntity(mandatory = true)
    private T mandatoryEntity;
    @GqlField(mandatory = true)
    private Long mandatoryField;
    @GqlEntity(name = "customNamedEntity")
    private T namedEntity;
    @GqlField(name = "customNamedField")
    private Long namedField;
    @GqlEntity(name = "customNamedMandatoryEntity", mandatory = true)
    private T namedMandatoryEntity;
    @GqlField(name = "customNamedMandatoryField", mandatory = true)
    private Long namedMandatoryField;
    @GqlEntity
    private Queue<T> queueEntity;
    @GqlEntity
    private Set<T> setEntity;
}
