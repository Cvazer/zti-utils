package by.zti.main.scanner;

import java.util.List;
import java.util.Map;

public interface Consumable {
    void consume(Map<String, List<String>> params);
}
