/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.crdgenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.fabric8.kubernetes.api.model.Affinity;
import io.fabric8.kubernetes.client.CustomResource;
import io.strimzi.crdgenerator.annotations.Crd;
import io.strimzi.crdgenerator.annotations.Description;
import io.strimzi.crdgenerator.annotations.Example;
import io.strimzi.crdgenerator.annotations.KubeLink;
import io.strimzi.crdgenerator.annotations.Minimum;
import io.strimzi.crdgenerator.annotations.Pattern;

import java.util.List;
import java.util.Map;

@Crd(
    apiVersion = "apiextensions.k8s.io/v1beta1",
    spec = @Crd.Spec(
        group = "crdgenerator.strimzi.io",
        names = @Crd.Spec.Names(
            kind = "Example",
            plural = "examples"),
        scope = "Namespaced",
        version = "v1alpha1"))
public class ExampleCrd<T, U extends Number, V extends U> extends CustomResource {

    private String ignored;

    private String string;

    private int intProperty;

    private long longProperty;

    private boolean booleanProperty;

    private ObjectProperty objectProperty;

    private Affinity affinity;

    @Description("Example of field property.")
    public String fieldProperty;

    public String[] arrayProperty;

    public String[][] arrayProperty2;

    public List<Integer> listOfInts;

    public List<List<Integer>> listOfInts2;

    public List<ObjectProperty> listOfObjects;

    public List<PolymorphicTop> listOfPolymorphic;

    public List rawList;

    public List<List> listOfRawList;

    public List<String>[] arrayOfList;

    public List[] arrayOfRawList;

    public List<String[]> listOfArray;

    public T[] arrayOfTypeVar;

    public List<T> listOfTypeVar;

    public U[] arrayOfBoundTypeVar;

    public List<U> listOfBoundTypeVar;

    public V[] arrayOfBoundTypeVar2;

    public List<V> listOfBoundTypeVar2;

    public List<? extends String> listOfWildcardTypeVar1;

    public List<? extends V> listOfWildcardTypeVar2;

    public List<? extends U> listOfWildcardTypeVar3;

    public List<? extends List<? extends U>> listOfWildcardTypeVar4;


    @Description("Example of complex type.")
    public static class ObjectProperty {
        private String foo;
        private String bar;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    private Map<String, Object> mapProperty;

    private PolymorphicTop polymorphicProperty;

    @Description("Example of a polymorphic type")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "discrim")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = PolymorphicLeft.class, name = "left"),
            @JsonSubTypes.Type(value = PolymorphicRight.class, name = "right")
    })
    public static class PolymorphicTop {
        private String discrim;
        private String commonProperty;

        public String getDiscrim() {
            return discrim;
        }

        public void setDiscrim(String discrim) {
            this.discrim = discrim;
        }

        public String getCommonProperty() {
            return commonProperty;
        }

        public void setCommonProperty(String commonProperty) {
            this.commonProperty = commonProperty;
        }
    }

    public static class PolymorphicLeft extends PolymorphicTop {
        private String leftProperty;

        @Description("when descrim=left, the left-hand property")
        public String getLeftProperty() {
            return leftProperty;
        }

        public void setLeftProperty(String leftProperty) {
            this.leftProperty = leftProperty;
        }
    }

    public static class PolymorphicRight extends PolymorphicTop {
        private String rightProperty;

        @Description("when descrim=right, the right-hand property")
        public String getRightProperty() {
            return rightProperty;
        }

        public void setRightProperty(String rightProperty) {
            this.rightProperty = rightProperty;
        }
    }

    @JsonIgnore
    public String getIgnored() {
        return ignored;
    }

    public void setIgnored(String ignored) {
        this.ignored = ignored;
    }

    @JsonProperty(value = "stringProperty", required = true)
    @Pattern(".*")
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Deprecated
    @Description("An example int property")
    @Example("42")
    @Minimum(42)
    public int getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }

    @Description("An example long property")
    @Example("42")
    @Minimum(42)
    public long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public ObjectProperty getObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(ObjectProperty objectProperty) {
        this.objectProperty = objectProperty;
    }

    public Map<String, Object> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<String, Object> mapProperty) {
        this.mapProperty = mapProperty;
    }

    public PolymorphicTop getPolymorphicProperty() {
        return polymorphicProperty;
    }

    public void setPolymorphicProperty(PolymorphicTop polymorphicProperty) {
        this.polymorphicProperty = polymorphicProperty;
    }

    @KubeLink(group = "core", version = "v1", kind = "affinity")
    public Affinity getAffinity() {
        return affinity;
    }

    public void setAffinity(Affinity affinity) {
        this.affinity = affinity;
    }
}
