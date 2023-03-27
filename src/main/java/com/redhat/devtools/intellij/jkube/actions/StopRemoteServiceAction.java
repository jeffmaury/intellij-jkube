package com.redhat.devtools.intellij.jkube.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.redhat.devtools.intellij.common.actions.StructureTreeAction;
import com.redhat.devtools.intellij.jkube.window.PortNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;

public class StopRemoteServiceAction extends StructureTreeAction implements DumbAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopRemoteServiceAction.class);

    public StopRemoteServiceAction() {
        super(PortNode.class);
    }

    @Override
    public boolean isVisible(Object selected) {
        var visible = super.isVisible(selected);
        if (visible) {
            visible = ((PortNode) selected).getHandler() != null;
        }
        return visible;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected) {
            PortNode node = getElement(selected);
            if (node.getHandler() != null) {
                node.getHandler().stop();
            }
    }
}
