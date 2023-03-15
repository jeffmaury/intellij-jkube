package com.redhat.devtools.intellij.jkube.window;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.operator.v1.KubeAPIServerBuilder;

public class RootNode implements Node<Void> {
    private final KubernetesClient client;
    private final JKubeTreeStructure structure;

    @Override
    public Void getParent() {
        return null;
    }

    public RootNode(JKubeTreeStructure structure) {
        this.structure = structure;
        this.client = new KubernetesClientBuilder().build();
    }

    public JKubeTreeStructure getStructure() {
        return structure;
    }

    public KubernetesClient getClient() {
        return client;
    }
}
