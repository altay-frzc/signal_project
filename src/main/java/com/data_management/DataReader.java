package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Connects to a WebSocket server and starts receiving real-time data.
     *
     * @param serverUri the URI of the WebSocket server to connect to
     * @param dataStorage the storage where incoming data will be stored
     * @throws IOException if the connection fails
     */
    default void connectToWebSocket(String serverUri, DataStorage dataStorage) throws IOException {
        throw new UnsupportedOperationException("WebSocket not supported by this implementation");
    }
}
