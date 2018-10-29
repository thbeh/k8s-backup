/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.api.kafka.model;

import io.strimzi.crdgenerator.annotations.Description;
import io.sundr.builder.annotations.Buildable;

/**
 * Representation for ephemeral storage.
 */
@Buildable(
        editableEnabled = false,
        generateBuilderPackage = true,
        builderPackage = "io.strimzi.api.kafka.model"
)
public class EphemeralStorage extends Storage {

    @Description("Must be `" + TYPE_EPHEMERAL + "`")
    @Override
    public String getType() {
        return TYPE_EPHEMERAL;
    }
}
