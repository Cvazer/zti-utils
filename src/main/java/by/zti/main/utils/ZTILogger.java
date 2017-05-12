package by.zti.main.utils;

/**
 *Simple interface to provide basic logging mechanism.
 *
 * @author Yan Frankovski
 * @since ZTIU 1.0.5
 * @see by.zti.main.utils.SimpleLogger
 * @see by.zti.main.utils.LogLevel
 */
public interface ZTILogger {
    void log(LogLevel level, String message);
}
