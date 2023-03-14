package com.redhat.devtools.intellij.jkube.window;

import io.fabric8.kubernetes.api.model.ServicePort;

public class PortNode implements Node<ServiceNode> {
    private final ServiceNode parent;
    private final int port;

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
}
