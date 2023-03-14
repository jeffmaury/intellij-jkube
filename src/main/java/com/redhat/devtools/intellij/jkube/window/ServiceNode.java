package com.redhat.devtools.intellij.jkube.window;

import io.fabric8.kubernetes.api.model.Service;

public class ServiceNode implements Node<RemoteServicesNode> {
    private final Service service;
    private final RemoteServicesNode parent;

    public ServiceNode(Service service, RemoteServicesNode parent) {
        this.service = service;
        this.parent = parent;
    }
    @Override
    public RemoteServicesNode getParent() {
        return parent;
    }

    public Service getService() {
        return service;
    }
}
