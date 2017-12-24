package by.zti.incubator.http;

import java.net.URI;

/**
 * Utility interface that serves as a lambda provider for {@code HTTPMessenger} object's createContext method
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see by.zti.incubator.http.HTTPMessenger
 */
public interface HTTPConsumable {
    /**
     * Executes given code after {@code HttpServer} instance resolves associated context
     * @param data - byte array from the incoming request input stream
     * @param uri - incoming request uri
     * @return byte array that contains answer from server that will be injected into request body output stream
     */
    byte[] consume (byte[] data, URI uri);
}
