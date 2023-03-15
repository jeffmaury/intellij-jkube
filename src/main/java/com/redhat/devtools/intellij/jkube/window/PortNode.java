package com.redhat.devtools.intellij.jkube.window;

import com.redhat.devtools.intellij.jkube.actions.RemoteDevHandler;
import io.fabric8.kubernetes.api.model.ServicePort;

public class PortNode implements Node<ServiceNode> {
    private final ServiceNode parent;
    private final int port;
    private RemoteDevHandler handler;

    private PortNode(int port, ServiceNode parent) {
        this.port = port;
        this.parent = parent;
    }

    public static PortNode fromServicePort(ServicePort port, ServiceNode parent) {
        if (port.getTargetPort() != null) {
            if (port.getTargetPort().getIntVal() != null) {
                return new PortNode(port.getTargetPort().getIntVal(), parent);
            } else {
                if (port.getTargetPort().getStrVal() != null) {
                    try {
                        return new PortNode(Integer.parseInt(port.getTargetPort().getStrVal()), parent);
                    } catch (NumberFormatException e) {}
                }
            }
        }
        return null;
    }

    public int getPort() {
        return port;
    }

    @Override
    public ServiceNode getParent() {
        return parent;
    }

    public void setHandler(RemoteDevHandler handler) {
        this.handler = handler;
        getParent().getParent().getParent().getStructure().fireModified(this);
    }

    public RemoteDevHandler getHandler() {
        return handler;
    }
}
