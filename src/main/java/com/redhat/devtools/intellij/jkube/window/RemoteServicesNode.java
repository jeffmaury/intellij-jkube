package com.redhat.devtools.intellij.jkube.window;

public class RemoteServicesNode implements Node<RootNode> {
    private final RootNode parent;

    public RemoteServicesNode(RootNode parent) {
        this.parent = parent;
    }

    @Override
    public RootNode getParent() {
        return parent;
    }
}
