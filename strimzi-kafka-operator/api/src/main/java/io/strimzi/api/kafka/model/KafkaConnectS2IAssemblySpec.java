/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.sundr.builder.annotations.Buildable;

@Buildable(
        editableEnabled = false,
        generateBuilderPackage = true,
        builderPackage = "io.strimzi.api.kafka.model"
)
@JsonPropertyOrder({ "replicas", "image",
        "livenessProbe", "readinessProbe", "jvmOptions", "affinity", "metrics"})
public class KafkaConnectS2IAssemblySpec extends KafkaConnectAssemblySpec {

    private boolean insecureSourceRepository = false;

    public boolean isInsecureSourceRepository() {
        return insecureSourceRepository;
    }

    public void setInsecureSourceRepository(boolean insecureSourceRepository) {
        this.insecureSourceRepository = insecureSourceRepository;
    }
}
