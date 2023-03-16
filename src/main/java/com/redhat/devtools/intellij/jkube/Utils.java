package com.redhat.devtools.intellij.jkube;

import java.net.ServerSocket;

public class Utils {
    public static int findFirstAvailablePort(int startingPort) {
        for (int port = startingPort; port <= 65535; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                return port;
            } catch (Exception ex) {
            }
        }
        return startingPort; // No available port found
    }
}
