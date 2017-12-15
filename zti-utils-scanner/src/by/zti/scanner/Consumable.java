package by.zti.scanner;

import java.util.List;
import java.util.Map;

/**
 * This is the interface to use in order to create logic for your scanner.
 *
 * @author Yan Frankovski
 * @since ZTIU 1.0.0
 * @see by.zti.scanner.Command
 * @see by.zti.scanner.ConsoleScanner
 */
public interface Consumable {
    /**
     * Interface method to be called from command instance for executing logic.
     * @param params A set of key-params values ({@code Map})
     */
    void consume(Map<String, List<String>> params);
}
