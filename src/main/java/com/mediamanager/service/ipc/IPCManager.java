package com.mediamanager.service.ipc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class IPCManager {
    private final Properties configuration;
    private static final Logger logger = LogManager.getLogger(IPCManager.class);
    private Path socketPath;
    private UnixDomainSocketAddress socketAddress;
    private ServerSocketChannel serverChannel;
    private ExecutorService clientThreadPool;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ConcurrentHashMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>();
    private final AtomicInteger clientIdCounter = new AtomicInteger(0);


    public IPCManager(Properties config){
        configuration = config;
        logger.debug("IPCManager created with configuration:");


    }
    public void init() throws Exception {
        logger.info("Initializing IPC connection...");
        validateConfiguration();
        socketPath = Path.of(configuration.getProperty("ipc.socket.path")).resolve("mediamanager.sock");

        if (Files.exists(socketPath)) {
            logger.warn("Socket file already exists at: {}", socketPath);
            logger.info("Deleting existing socket...");
            Files.deleteIfExists(socketPath);
        }

        Path parentDir = socketPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            logger.info("Creating parent directory for socket: {}", parentDir);
            Files.createDirectories(parentDir);
        }

        try {
            socketAddress = UnixDomainSocketAddress.of(socketPath);
            logger.debug("Socket address created");

            serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
            serverChannel.bind(socketAddress);
            logger.info("Server bound to socket - file created at: {}", socketPath);

            // ESTA É A MUDANÇA CRÍTICA
            // Configura o canal para modo não-bloqueante
            // Isso faz accept() retornar null imediatamente se não houver cliente
            // ao invés de bloquear esperando indefinidamente
            serverChannel.configureBlocking(false);
            logger.debug("Server channel configured for non-blocking mode");

            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
            Files.setPosixFilePermissions(socketPath, perms);
            logger.debug("Socket permissions set to: rw-------");

            clientThreadPool = Executors.newCachedThreadPool(runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("IPC-Client-Handler-" + thread.getId());
                thread.setDaemon(true);
                return thread;
            });
            logger.debug("Client thread pool created");

            running.set(true);

            Thread serverThread = new Thread(this::acceptConnectionsLoop, "IPC-Server-Accept-Thread");
            serverThread.setDaemon(true);
            serverThread.start();
            logger.info("Server thread started - accepting connections");

            logger.info("IPC server initialized successfully on {}", socketPath.toAbsolutePath());

        } catch (IOException e) {
            logger.error("Failed to initialize IPC server: {}", e.getMessage());
            throw new Exception("Failed to initialize IPC server: " + e.getMessage(), e);
        }
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

    public Path getSocketPath(){
        return socketPath;
    }

    public void close() throws Exception {
        if(!running.get()){
            logger.warn("IPC connection is already closed");
        }


        logger.info("Closing IPC connection...");

        running.set(false);
        if (serverChannel != null && serverChannel.isOpen()) {
            serverChannel.close();
            logger.debug("Server channel closed");
        }

        if (clientThreadPool != null) {
            clientThreadPool.shutdown();
            try {
                if (!clientThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    logger.warn("Some client handlers did not finish in time, forcing shutdown");
                    clientThreadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for client handlers", e);
                clientThreadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (socketPath != null && Files.exists(socketPath)) {
            Files.deleteIfExists(socketPath);
            logger.info("Socket file deleted successfully");
        }

        logger.info("IPC server closed successfully");
    }

    private void acceptConnectionsLoop() {
        logger.info("Preparing to accept connections...");

        while (running.get()) {
            try {
                // Em modo não-bloqueante, accept() retorna imediatamente
                // Se há um cliente esperando, retorna o SocketChannel
                // Se não há cliente, retorna null
                SocketChannel clientChannel = serverChannel.accept();

                if (clientChannel != null) {
                    // Um cliente realmente se conectou!
                    int clientId = clientIdCounter.incrementAndGet();
                    logger.info("Client {} connected", clientId);

                    ClientHandler handler = new ClientHandler(clientId, clientChannel);
                    activeClients.put(clientId, handler);

                    clientThreadPool.submit(() -> {
                        try {
                            handler.handle();
                        } finally {
                            activeClients.remove(clientId);
                            logger.info("Client {} disconnected", clientId);
                        }
                    });
                } else {
                    // Nenhum cliente conectado no momento
                    // Dorme por um curto período antes de verificar novamente
                    // Isso evita consumir CPU desnecessariamente em um loop vazio
                    Thread.sleep(100); // 100 milissegundos
                }

            } catch (InterruptedException e) {
                // Thread foi interrompida, provavelmente durante shutdown
                logger.debug("Accept loop interrupted");
                break;

            } catch (IOException e) {
                // Erros de I/O reais devem ser logados
                if (running.get()) {
                    logger.error("Error accepting client connection", e);
                }
                break;
            }
        }

        logger.info("Connection loop stopped gracefully");
    }


    private class ClientHandler {
        private final int clientId;
        private final SocketChannel channel;

        public ClientHandler(int clientId, SocketChannel channel) {
            this.clientId = clientId;
            this.channel = channel;
        }

        /**
         * Método principal que processa a comunicação com o cliente.
         * Aqui é onde vamos ler mensagens JSON, processá-las, e enviar respostas.
         */
        public void handle() {
            logger.debug("Client {} handler thread started", clientId);

            try {
                // TODO: No próximo passo, vamos implementar:
                // 1. Ler mensagens JSON do SocketChannel
                // 2. Parsear o JSON para objetos Java
                // 3. Processar a requisição
                // 4. Criar uma resposta JSON
                // 5. Escrever a resposta de volta no SocketChannel

                // Por enquanto, apenas mantém a conexão aberta brevemente
                // para testar que o sistema de aceitação e threads está funcionando
                Thread.sleep(100);

                logger.debug("Client {} processing complete", clientId);

            } catch (Exception e) {
                logger.error("Error handling client {}", clientId, e);
            } finally {
                // SEMPRE fecha o canal quando terminar
                // O finally garante que isso acontece mesmo se houver exceção
                try {
                    channel.close();
                    logger.debug("Client {} channel closed", clientId);
                } catch (IOException e) {
                    logger.error("Error closing client {} channel", clientId, e);
                }
            }
        }
    }
}



