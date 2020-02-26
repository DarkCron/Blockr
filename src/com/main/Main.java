package com.main;

import com.blockr.ui.MyCanvasWindow;
import com.mediator.Command;
import com.mediator.CommandHandler;
import com.mediator.Mediator;
import com.mediator.MediatorImpl;

import javax.swing.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World").show()
        );
    }
}
