package com.mediator;

public interface Mediator {

    <C extends Command<R>, R> R send(C command);
}
