package com.redhat.devtools.intellij.jkube.window;

public class MessageNode<T> implements Node<T> {
    private final T parent;
    private final String label;

    public MessageNode(String label, T parent) {
        this.label = label;
        this.parent = parent;
    }

    @Override
    public T getParent() {
        return parent;
    }

    public String getLabel() {
        return label;
    }
}
