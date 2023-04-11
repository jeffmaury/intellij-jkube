package com.redhat.devtools.intellij.jkube.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.Messages;
import com.redhat.devtools.intellij.common.actions.StructureTreeAction;
import com.redhat.devtools.intellij.jkube.Constants;
import com.redhat.devtools.intellij.jkube.Utils;
import com.redhat.devtools.intellij.jkube.dialogs.RemotePortDialog;
import com.redhat.devtools.intellij.jkube.window.PortNode;
import com.redhat.devtools.intellij.jkube.window.TerminalLogger;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentConfig;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentService;
import org.eclipse.jkube.kit.remotedev.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;

public class StartRemoteServiceAction extends StructureTreeAction implements DumbAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartRemoteServiceAction.class);

    public StartRemoteServiceAction() {
        super(PortNode.class);
    }

    @Override
    public boolean isVisible(Object selected) {
        var visible = super.isVisible(selected);
        if (visible) {
            visible = ((PortNode) selected).getHandler() == null;
        }
        return visible;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected) {
        PortNode node = getElement(selected);
        var serviceName = node.getParent().getService().getMetadata().getName();
        var dialog = new RemotePortDialog(serviceName, node.getPort(), Utils.findFirstAvailablePort(node.getPort()),
                anActionEvent.getProject());
        if (dialog.showAndGet()) {
            var remoteService = RemoteService.builder()
                    .hostname(serviceName)
                    .port(node.getPort())
                    .localPort(dialog.getLocalPort())
                    .build();
            var config = RemoteDevelopmentConfig.builder().remoteService(remoteService).build();
            var logger = new TerminalLogger(remoteService.getHostname() + ':' + remoteService.getPort(),
                    anActionEvent.getProject());
            var service = new RemoteDevelopmentService(logger, node.getParent().getParent().getParent().getClient(),
                    config);
            var handler = new ServiceHandler(service, logger, remoteService.getLocalPort());
            node.setHandler(handler);
            handler.start().handle((res, err) -> {
                node.setHandler(null);
                if (err != null) {
                    Messages.showErrorDialog(anActionEvent.getProject(), "Can't forward locally port " + node.getPort() +
                            "' for service " + node.getParent().getService().getMetadata().getName() + " error:" + err.getLocalizedMessage(), "Error");
                }
                return res;
            });
            var content = "Port " + remoteService.getLocalPort() + " available locally for service " +
                    serviceName;
            var notification = new Notification(Constants.NOTIFICATION_GROUP_ID, "JKube", content,
                    NotificationType.INFORMATION);
            notification.addAction(NotificationAction.createExpiring("Open browser", (e,n) -> {
                OpenBrowserAction.openBrowser(remoteService.getLocalPort(), e.getProject());
            }));
            Notifications.Bus.notify(notification, anActionEvent.getProject());
        }
    }
}
