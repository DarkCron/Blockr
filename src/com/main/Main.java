package com.main;


import com.ui.MyCanvasWindow;

import javax.swing.*;
public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World").show()
        );
    }
}
