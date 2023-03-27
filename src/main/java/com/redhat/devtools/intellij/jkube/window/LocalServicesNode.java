package com.redhat.devtools.intellij.jkube.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocalServicesNode extends MessageNode<RootNode> {

    private List<MessageNode<LocalServicesNode>> childs = new ArrayList<>();

    public LocalServicesNode(RootNode parent) {
        super("Local services", parent);
    }

    public void addChild(MessageNode<LocalServicesNode> child) {
        childs.add(child);
        getParent().getStructure().fireAdded(child);
    }

    public void removeChild(MessageNode<LocalServicesNode> child) {
        childs.remove(child);
        getParent().getStructure().fireRemoved(child);
    }

    public List<MessageNode<LocalServicesNode>> getChilds() {
        return childs;
    }
}
