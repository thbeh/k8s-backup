/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.strimzi.crdgenerator.annotations.Description;

/**
 * Describes the logging configuration
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "inline", value = InlineLogging.class),
        @JsonSubTypes.Type(name = "external", value = ExternalLogging.class),
})
public abstract class Logging {

    @Description("Storage type, must be either 'inline' or 'external'.")
    @JsonIgnore
    public abstract String getType();

    // Hack
    private ConfigMap cm;

    @JsonIgnore
    @Deprecated
    public ConfigMap getCm() {
        return cm;
    }

    @Deprecated
    public void setCm(ConfigMap cm) {
        this.cm = cm;
    }
}

