package com.redhat.devtools.intellij.jkube.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import java.util.ArrayList;
import java.util.List;

public class LocalServiceDialog extends DialogWrapper {
    private JTextField serviceNameText;
    private JTextField portText;
    private JPanel contentPane;

    private final DocumentAdapter adapter = new DocumentAdapter() {
        @Override
        protected void textChanged(@NotNull DocumentEvent e) {
            validate();
        }
    };

    public LocalServiceDialog(Project project) {
        super(project, false, IdeModalityType.PROJECT);
        init();
        setTitle("Local service");
        serviceNameText.getDocument().addDocumentListener(adapter);
        portText.getDocument().addDocumentListener(adapter);
        validate();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    public void validate() {
        super.validate();
        setOKActionEnabled(true);
        setErrorText(null, serviceNameText);
        setErrorText(null, portText);
        List<ValidationInfo> infos = new ArrayList<>();
        if (serviceNameText.getText().isEmpty()) {
            infos.add(new ValidationInfo("Service name cannot be empty", serviceNameText));
        }
        try {
            Integer.parseInt(portText.getText());
        } catch (NumberFormatException e) {
            infos.add(new ValidationInfo("Port number must be an integer", portText));
        }
        if (!infos.isEmpty()) {
            setErrorInfoAll(infos);
            setOKActionEnabled(false);
        }
    }

    public String getServiceName() {
        return serviceNameText.getText();
    }

    public int getPort() {
        return Integer.parseInt(portText.getText());
    }
}
