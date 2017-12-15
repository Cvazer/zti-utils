package by.zti.scanner;

/**
 * This class represents a typical command for {@code ConsoleScanner} to work with.
 * It contains all data necessary to identify and execute different parts of code.
 *
 * @author Yan Frankovski
 * @since ZTIU 1.0.1
 * @see by.zti.scanner.ConsoleScanner
 * @see by.zti.scanner.Consumable
 */
public class Command {
    /** This values are used to store command name and description. */
    private String name, description;
    /** This value used to store reference to {@code Consumable} type object */
    private Consumable consumer;

    /**
     * Allocates new {@code Command} object that contains given {@code name} and {@code description} as it's parameters.
     *
     * @param name {@code String} that is the name of command.
     * @param description Short description of command functionality for documentation needs.
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return current {@code Consumer} object for this command.
     */
    public Consumable getConsumer() {
        return consumer;
    }

    /**
     * @param consumer new {@code Consumer} object for this command
     * @return This particular instance of same command. For nested constructor/consumer declarations.
     */
    public Command setConsumer(Consumable consumer) {
        this.consumer = consumer;
        return this;
    }

    /**
     * @return Name of this command.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name New name for this command.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Description for this command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description New desription for this command.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
