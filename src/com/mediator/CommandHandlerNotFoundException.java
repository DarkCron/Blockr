package com.mediator;

public class CommandHandlerNotFoundException extends RuntimeException {

    private final String className;

    public CommandHandlerNotFoundException(Command<?> command){
        this.className = command.getClass().getSimpleName();
    }

    @Override
    public String getMessage() {
        return "No CommandHandler found for command: " + className;
    }
}
