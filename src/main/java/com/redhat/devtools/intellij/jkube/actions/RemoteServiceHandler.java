package com.redhat.devtools.intellij.jkube.actions;

import com.redhat.devtools.intellij.jkube.window.TerminalLogger;
import org.eclipse.jkube.kit.remotedev.RemoteDevelopmentService;

import java.util.concurrent.CompletableFuture;

public class RemoteServiceHandler {
    private final RemoteDevelopmentService service;
    private final TerminalLogger logger;
    private final int port;

    public RemoteServiceHandler(RemoteDevelopmentService service, TerminalLogger logger, int port) {
        this.service = service;
        this.logger = logger;
        this.port = port;
    }

    public CompletableFuture<Void> start() {
        try  {
            logger.start(service);
            return service.start();
        } catch (RuntimeException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public void stop() {
        service.stop();
    }

    public int getLocalPort() {
        return port;
    }

}
