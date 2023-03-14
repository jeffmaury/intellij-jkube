package com.redhat.devtools.intellij.jkube.actions;

import com.intellij.ide.browsers.actions.WebPreviewVirtualFile;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.Urls;
import com.redhat.devtools.intellij.common.actions.StructureTreeAction;
import com.redhat.devtools.intellij.jkube.Constants;
import com.redhat.devtools.intellij.jkube.window.PortNode;
import org.eclipse.jkube.kit.common.KitLogger;
import org.eclipse.jkube.kit.common.util.Slf4jKitLogger;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentConfig;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentContext;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentService;
import org.eclipse.jkube.kit.remotedev.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;
import java.util.Collections;

public class StartRemoteDevAction extends StructureTreeAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartRemoteDevAction.class);

    public StartRemoteDevAction() {
        super(PortNode.class);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected) {
            PortNode node = getElement(selected);
            var remoteService = RemoteService.builder()
                    .hostname(node.getParent().getService().getMetadata().getName())
                    .port(node.getPort())
                    .build();
            var config = RemoteDevelopmentConfig.builder().remoteService(remoteService).build();
            var logger = new Slf4jKitLogger(LOGGER);
            var service = new RemoteDevelopmentService(logger, node.getParent().getParent().getParent().getClient(), config);
        try {
            service.start().handle((res, err) -> {
               if (err != null) {
                   Messages.showErrorDialog(anActionEvent.getProject(), "Can't forward locally port " + node.getPort() +
                           "' for service " + node.getParent().getService().getMetadata().getName() + " error:" + err.getLocalizedMessage(), "Error");
               }
               return res;
            });
            var content = "Port " + node.getPort() + " available locally for service " +
                    node.getParent().getService().getMetadata().getName();
            var notification = new Notification(Constants.NOTIFICATION_GROUP_ID, "JKube", content,
                    NotificationType.INFORMATION);
            notification.addAction(NotificationAction.createExpiring("Open browser", (e,n) -> {
                openBrowser(node.getPort(), e.getProject());
            }));
            Notifications.Bus.notify(notification, anActionEvent.getProject());
        } catch (RuntimeException e) {
            Messages.showErrorDialog(anActionEvent.getProject(), "Can't forward locally port " + node.getPort() +
                    "' for service " + node.getParent().getService().getMetadata().getName(), "Error");
        }

    }

    private void openBrowser(int port, Project project) {
        var url = "http://localhost:" + port;
        var file = new LightVirtualFile(url);
        var previewFile = new WebPreviewVirtualFile(file, Urls.newFromEncoded(url));
        FileEditorManager.getInstance(project).openFile(previewFile, true, true);
    }
}
