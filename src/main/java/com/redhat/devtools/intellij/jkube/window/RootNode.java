package com.redhat.devtools.intellij.jkube.window;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.api.model.operator.v1.KubeAPIServerBuilder;

public class RootNode implements Node<Void> {
    private final KubernetesClient client;
    private final JKubeTreeStructure structure;
    private final MessageNode<RootNode> localServicesNode;
    private final MessageNode<RootNode> remoteServicesNode;

    @Override
    public Void getParent() {
        return null;
    }

    public RootNode(JKubeTreeStructure structure) {
        this.structure = structure;
        this.client = new KubernetesClientBuilder().build();
        this.localServicesNode = new LocalServicesNode(this);
        this.remoteServicesNode = new MessageNode<RootNode>("RemoteServices", this);
    }

    public JKubeTreeStructure getStructure() {
        return structure;
    }

    public KubernetesClient getClient() {
        return client;
    }

    public MessageNode<RootNode> getLocalServicesNode() {
        return localServicesNode;
    }

    public MessageNode<RootNode> getRemoteServicesNode() {
        return remoteServicesNode;
    }
}
