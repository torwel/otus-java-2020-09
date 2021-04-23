package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData"),
    ALL_CLIENTS("AllClients"),
    SAVE_CLIENT("SaveClient");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MessageType getTypeByName(String name) {
        for (MessageType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such message type: " + name);
    }
}
