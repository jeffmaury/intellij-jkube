package com.redhat.devtools.intellij.jkube.window;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.redhat.devtools.intellij.common.tree.LabelAndIconDescriptor;
import com.redhat.devtools.intellij.common.tree.MutableModel;
import com.redhat.devtools.intellij.jkube.Icons;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class WindowTreeStructure extends AbstractTreeStructure implements MutableModel<Object> {
    private final Project project;

    private RootNode root;

    public WindowTreeStructure(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull Object getRootElement() {
        if (root == null) {
            root = new RootNode();
        }
        return root;
    }

    @Override
    public Object @NotNull [] getChildElements(@NotNull Object element) {
        if (element == root) {
            return new Object[]{new RemoteServicesNode(root)};
        } else if (element instanceof RemoteServicesNode) {
            return root.getClient().services().list().getItems().stream()
                    .map(service -> new ServiceNode(service, (RemoteServicesNode) element))
                    .toArray();
        } else if (element instanceof ServiceNode) {
            return ((ServiceNode) element).getService().getSpec().getPorts().stream()
                    .map(port -> PortNode.fromServicePort(port, (ServiceNode) element))
                    .filter(Objects::nonNull)
                    .toArray();
        }
        return new Object[0];
    }

    @Override
    public @Nullable Object getParentElement(@NotNull Object element) {
        if (element instanceof Node) {
            return ((Node<?>) element).getParent();
        }
        return null;
    }

    @Override
    public @NotNull NodeDescriptor createDescriptor(@NotNull Object element,
                                                    @Nullable NodeDescriptor parentDescriptor) {
        if (element == root) {
            return new LabelAndIconDescriptor(project, element, root.getClient().getMasterUrl().toString(), Icons.CLUSTER,
                    parentDescriptor);
        } else if (element instanceof RemoteServicesNode) {
            return new LabelAndIconDescriptor(project, element, "Remote services",
                    Icons.REMOTE, parentDescriptor);
        } else if (element instanceof ServiceNode) {
            return new LabelAndIconDescriptor(project, element, ((ServiceNode) element).getService().getMetadata().getName(),
                    Icons.SERVICE, parentDescriptor);
        } else if (element instanceof PortNode) {
            return new LabelAndIconDescriptor(project, element, Integer.toString(((PortNode) element).getPort()),
                    Icons.PORT, parentDescriptor);
        }
        return null;
    }

    @Override
    public void commit() {
    }

    @Override
    public boolean hasSomethingToCommit() {
        return false;
    }

    @Override
    public void fireAdded(Object element) {

    }

    @Override
    public void fireModified(Object element) {

    }

    @Override
    public void fireRemoved(Object element) {

    }

    @Override
    public void addListener(Listener<Object> listener) {

    }

    @Override
    public void removeListener(Listener<Object> listener) {

    }
}
