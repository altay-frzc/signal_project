package com.data_management;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * WebSocket client that connects to a WebSocket server and receives
 * real-time patient data, storing it in DataStorage as it arrives.
 */
public class WebSocketClientImpl extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;

    /**
     * Creates a WebSocketClientImpl that connects to the given server URI.
     *
     * @param serverUri the URI of the WebSocket server
     * @throws URISyntaxException if the URI is invalid
     */
    public WebSocketClientImpl(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }
    /**
     * Creates a WebSocketClientImpl with a DataStorage already set.
     *
     * @param serverUri the URI of the WebSocket server
     * @param dataStorage the storage where data will be saved
     * @throws URISyntaxException if the URI is invalid
     */
    public WebSocketClientImpl(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        super(new URI(serverUri));
        this.dataStorage = dataStorage;
    }
    /**
     * Called when the connection to the server is established.
     *
     * @param handshake the server handshake data
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server");
    }

    /**
     * Called when a message is received from the server.
     * Parses the message and stores it in DataStorage.
     * Expected format: patientId,timestamp,label,data
     *
     * @param message the raw message received
     */
    @Override
    public void onMessage(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 4) {
                System.err.println("Invalid message format: " + message);
                return;
            }
            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String label = parts[2].trim();
            double data = Double.parseDouble(parts[3].trim());
            dataStorage.addPatientData(patientId, data, label, timestamp);
        } catch (Exception e) {
            System.err.println("Error parsing message: " + message + " - " + e.getMessage());
        }
    }

    /**
     * Called when the connection is closed.
     *
     * @param code the closing code
     * @param reason why the connection was closed
     * @param remote whether the closing was initiated by the remote host
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server. Reason: " + reason);
    }

    /**
     * Called when an error occurs.
     *
     * @param ex the exception that was thrown
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    /**
     * Connects to the WebSocket server and starts receiving data.
     *
     * @param serverUri the URI of the WebSocket server
     * @param dataStorage the storage where data will be saved
     * @throws IOException if the connection fails
     */
    @Override
    public void connectToWebSocket(String serverUri, DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        try {
            this.connect();
        } catch (Exception e) {
            throw new IOException("Failed to connect to WebSocket server: " + e.getMessage());
        }
    }

    /**
     * Not used for WebSocket implementation.
     *
     * @param dataStorage the storage (not used)
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        throw new UnsupportedOperationException("Use connectToWebSocket instead");
    }
}