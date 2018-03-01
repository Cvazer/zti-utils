package com.github.cvazer.rest;

/**
 * Public interface used by all messengers implementations.
 * @author Yan Frankovski
 * @since ZTIU 1.1.2
 */
public interface RESTMessenger {
    byte[] send(byte[] data, Object[] args);
}
