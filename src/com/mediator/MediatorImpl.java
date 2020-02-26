package com.mediator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MediatorImpl implements Mediator{

    private Supplier<Stream<CommandHandler<? , ?>>> commandHandlerSupplier;

    public MediatorImpl(Supplier<Stream<CommandHandler<? , ?>>> commandHandlerSupplier){
        this.commandHandlerSupplier = commandHandlerSupplier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Command<R>, R> R send(C command) {

        var commandHandlers = commandHandlerSupplier.get();

        var handler = commandHandlers
                        .filter(h -> isMatch(h.getClass(), command.getClass()))
                        .map(h -> (CommandHandler<C, R>)h)
                        .findFirst();

        if(handler.isEmpty()){
            throw new CommandHandlerNotFoundException(command);
        }

        return handler.get().handle(command);
    }


    /*

    https://github.com/sizovs/PipelinR/blob/master/src/main/java/an/awesome/pipelinr/FirstGenericArgOf.java
    Owner: sizovs, Use: generic magic

     */
    private static boolean isMatch(Class<?> commandHandler, Class<?> command){

        Type[] interfaces = commandHandler.getGenericInterfaces();
        Type genericSuperclass = commandHandler.getGenericSuperclass();

        ParameterizedType type;
        if (interfaces.length > 0) {
            type = (ParameterizedType) interfaces[0];
        } else {
            type = (ParameterizedType) genericSuperclass;
        }

        Type handlerCommand = type.getActualTypeArguments()[0];
        Class<?> handlerCommandClass;

        if (handlerCommand instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) handlerCommand;
            handlerCommandClass = (Class<?>) parameterized.getRawType();
        } else {
            handlerCommandClass = (Class<?>) handlerCommand;
        }

        return handlerCommandClass.isAssignableFrom(command);
    }
}
