package com.mediamanager.service.ipc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class IPCManager {
    private static Properties configuration;
    private static final Logger logger = LogManager.getLogger(IPCManager.class);
    private Path socketPath;
    private UnixDomainSocketAddress socketAddress;
    private ServerSocketChannel serverChannel;

    public IPCManager(Properties config){
        configuration = config;
        logger.debug("IPCManager created with configuration:");


    }
    public void init() throws Exception {
        logger.info("Initializing IPC connection...");
        validateConfiguration();
        socketPath = Path.of(configuration.getProperty("ipc.socket.path")).resolve("mediamanager.sock");

        if (checkUnixSocketExists()){
            logger.warn("IPC socket already exists");
            logger.info("Deleting existing socket...");
            Files.deleteIfExists(socketPath);


        }

        try{
            socketAddress = UnixDomainSocketAddress.of(socketPath);
            logger.info("IPC socket created successfully");
        } catch (Exception e){
            logger.error("Failed to create socket: {}", e.getMessage());
            throw new Exception("Failed to create socket: " + e.getMessage(), e);
        }
        if (!Files.isDirectory(socketPath.getParent())) {
            logger.info("Creating parent directory for socket...");
            Files.createDirectories(socketPath.getParent());
        }

        serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        logger.info("IPC server channel opened successfully");
        serverChannel.bind(socketAddress);
        logger.info("IPC server channel bound successfully");
        logger.info("IPC server listening on {}", socketPath.toAbsolutePath().toString());


    }
    private void validateConfiguration() throws Exception {
        String[] requiredProperties = {
                "ipc.socket.path"
        };
        for (String property : requiredProperties) {
            if (configuration.getProperty(property) == null) {
                throw new Exception("Missing required configuration property: " + property);
            }
        }
        logger.debug("IPC configuration validated successfully");
    }
    private boolean checkUnixSocketExists() {
        File socketFile = new File(String.valueOf(socketPath));
        return socketFile.exists();
    }

    public Path getSocketPath(){
        return socketPath;
    }

    public void close() throws Exception {
        logger.info("Closing IPC connection...");
        if (serverChannel != null) {
            serverChannel.close();
        }
        File socketFile = new File(String.valueOf(socketPath));
        boolean delete = false;
        if (socketFile.exists()) {
            delete = socketFile.delete();
        }
        if (!delete){
            logger.warn("Failed to delete socket file");
        }else {
            logger.info("IPC socket deleted successfully");
            Files.deleteIfExists(socketPath.getParent());
            logger.info("IPC socket parent directory deleted successfully");

        }

    }
}
