package by.zti.incubator.http;

import by.zti.incubator.rest.RESTException;
import by.zti.incubator.rest.RESTMessenger;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides communication functionality via HTTP protocol
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see by.zti.incubator.http.HTTPConsumable
 */
public class HTTPMessenger implements RESTMessenger{
    /** Reference to the {@code HttpServer} object **/
    private HttpServer server;

    /**
     * Creates context resolver inside {@code HttpServer} instance that is linked to {@code HttpConsumable} object
     *
     * @param contextURL - {@code String} url path to resolve when server request accepted
     * @param consumable - {@code HTTPConsumable} object to link with specific url
     */
    public void createContext(String contextURL, HTTPConsumable consumable) {
        if (server == null) { throw new NullPointerException("Server have to be started first");}
        server.createContext(contextURL, exchange -> {
            byte[] data = consumable.consume(exchange.getRequestBody().readAllBytes(), exchange.getRequestURI());
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
            exchange.getResponseBody().close();
            exchange.close();
        });
    }

    /**
     * Sends byte array to a specific url
     *
     * @param url - {@code String} that represents request url
     * @param data - Byte array of data that will be send
     * @return Byte array of data containing server response
     */
    public byte[] send(String url, byte[] data){
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.connect();
            connection.getOutputStream().write(data);
            return connection.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RESTMessenger implementation method that sends some byte array data.
     * @param data - {@code byte[]} data to send.
     * @param args - {@code Object[]} implementation specific arguments.
     * @return - {@code byte[]} response data.
     * @throws RESTException - if {@code Object[]} arguments are invalid.
     */
    @Override
    public byte[] send(byte[] data, Object[] args) throws RESTException {
        try { String test = (String)args[0]; }
        catch (Exception e) { throw new RESTException("First argument should be a String instance");}
        return send(((String)args[0]), data);
    }

    /**
     * Overrides full {@code start(int port, int poolSize)} method with poolSize default 10
     *
     * @param port - {@code int} desired server port
     * @return Same instance for cascading invocation
     */
    public HTTPMessenger start(int port){
        return start(port, 10);
    }

    /**
     * Creates {@code HttpServer} instance with poolSize connection pool and binds it to specific port.
     * This method MUST be invoked before creating server context!
     * But it can be ignored if you plan to use {@code send(String url, byte[] data)}  method only
     *
     * @param port - {@code int} desired server port
     * @param poolSize - {@code int} desired connection pool size
     * @return Same instance for cascading invocation
     */
    public HTTPMessenger start(int port, int poolSize){
        try {
            server = HttpServer.create(new InetSocketAddress(port), poolSize);
            server.start();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes server.stop() command on {@code HttpServer} instance after a certain delay
     * @param delay - {@code int} stop delay
     */
    public void stop(int delay) {
        server.stop(delay);
    }

    public HttpServer getServer() {
        return server;
    }
}
