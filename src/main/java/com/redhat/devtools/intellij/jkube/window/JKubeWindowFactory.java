package com.redhat.devtools.intellij.jkube.window;

import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.Tree;
import com.redhat.devtools.intellij.common.tree.MutableModelSynchronizer;
import com.redhat.devtools.intellij.common.tree.TreeHelper;
import com.redhat.devtools.intellij.jkube.Constants;
import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;

public class JKubeWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        JBPanel<JBPanel> panel = new JBPanel<>();
        panel.setLayout(new BorderLayout());
        Content content = contentFactory.createContent(panel, "", false);
        WindowTreeStructure structure = new WindowTreeStructure(project);
        StructureTreeModel<WindowTreeStructure> model = new StructureTreeModel<>(structure, content);
        new MutableModelSynchronizer<>(model, structure, structure);
        Tree tree = new Tree(new AsyncTreeModel(model, content));
        tree.putClientProperty(Constants.STRUCTURE_PROPERTY, structure);
        tree.setCellRenderer(new NodeRenderer());
        tree.setRootVisible(true);
        PopupHandler.installPopupMenu(tree, "com.redhat.devtools.intellij.jkube.tree", ActionPlaces.POPUP);
        panel.add(new JBScrollPane(tree), BorderLayout.CENTER);
        toolWindow.getContentManager().addContent(content);
        TreeHelper.addLinkSupport(tree);

    }
}
