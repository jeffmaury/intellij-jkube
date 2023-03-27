package com.redhat.devtools.intellij.jkube.window;

import io.fabric8.kubernetes.api.model.Service;

public class ServiceNode implements Node<MessageNode<RootNode>> {
    private final Service service;
    private final MessageNode parent;

    public ServiceNode(Service service, MessageNode<RootNode> parent) {
        this.service = service;
        this.parent = parent;
    }
    @Override
    public MessageNode<RootNode> getParent() {
        return parent;
    }

    public Service getService() {
        return service;
    }
}
