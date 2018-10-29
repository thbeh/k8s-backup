/*
 * Copyright 2017-2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.topic;

import io.fabric8.kubernetes.api.model.HasMetadata;

public class InvalidConfigMapException extends OperatorException {
    public InvalidConfigMapException(HasMetadata involvedObject, String message) {
        super(involvedObject, message);
    }
}
