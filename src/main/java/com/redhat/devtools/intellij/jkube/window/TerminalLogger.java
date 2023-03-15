package com.redhat.devtools.intellij.jkube.window;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.redhat.devtools.intellij.common.utils.CommonTerminalExecutionConsole;
import com.redhat.devtools.intellij.common.utils.ExecHelper;
import org.eclipse.jkube.kit.common.KitLogger;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentService;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

public class TerminalLogger implements KitLogger {
    private final CommonTerminalExecutionConsole terminal;

    public TerminalLogger(String title, Project project) {
        terminal = ExecHelper.createTerminalTabForReuse(project, title);
    }
    @Override
    public void debug(String format, Object... params) {
        terminal.print(String.format(format, params) + "\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
    }

    @Override
    public void info(String format, Object... params) {
        terminal.print(String.format(format, params) + "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
    }

    @Override
    public void warn(String format, Object... params) {
        terminal.print(String.format(format, params) + "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
    }

    @Override
    public void error(String format, Object... params) {
        terminal.print(String.format(format, params) + "\n", ConsoleViewContentType.LOG_ERROR_OUTPUT);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    public void start(RemoteDevelopmentService service) {
        var handler = new ProcessHandler() {
            @Override
            protected void destroyProcessImpl() {
                service.stop();
                notifyProcessTerminated(0);
            }

            @Override
            protected void detachProcessImpl() {
                service.stop();
                notifyProcessDetached();
            }

            @Override
            public boolean detachIsDefault() {
                return false;
            }

            @Override
            public @Nullable OutputStream getProcessInput() {
                return null;
            }

            @Override
            public boolean isSilentlyDestroyOnClose() {
                return true;
            }
        };
        handler.startNotify();
        terminal.attachToProcess(handler);
    }
}
