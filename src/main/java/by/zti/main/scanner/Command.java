package by.zti.main.scanner;

public class Command {
    private String name, description;
    private Consumable consumer;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Consumable getConsumer() {
        return consumer;
    }

    public Command setConsumer(Consumable consumer) {
        this.consumer = consumer;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
