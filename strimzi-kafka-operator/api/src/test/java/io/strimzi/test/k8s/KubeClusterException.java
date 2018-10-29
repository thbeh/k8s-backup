/*
 * Copyright 2017-2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.test.k8s;

public class KubeClusterException extends RuntimeException {
    public final ProcessResult result;

    public KubeClusterException(ProcessResult result, String s) {
        super(s);
        this.result = result;
    }

    public KubeClusterException(Throwable cause) {
        super(cause);
        this.result = null;
    }

    public static class NotFound extends KubeClusterException {

        public NotFound(ProcessResult result, String s) {
            super(result, s);
        }
    }

    public static class AlreadyExists extends KubeClusterException {

        public AlreadyExists(ProcessResult result, String s) {
            super(result, s);
        }
    }

    public static class InvalidResource extends KubeClusterException {

        public InvalidResource(ProcessResult result, String s) {
            super(result, s);
        }
    }
}
