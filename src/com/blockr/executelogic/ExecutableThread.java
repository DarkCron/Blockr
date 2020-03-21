package com.blockr.executelogic;

import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.ui.ViewContext;

import java.util.concurrent.TimeUnit;

public class ExecutableThread extends Thread {
    private final ReadOnlyBlockProgram robp;
    private final ViewContext viewContext;

    public ExecutableThread(ReadOnlyBlockProgram robp, ViewContext viewContext) {
        this.robp = robp;
        this.viewContext = viewContext;
    }


    @Override
    public void run() {
        super.run();
        ReadOnlyStatementBlock current = null;

        while (true){
            current = this.robp.executeNextFromThread(current);
            viewContext.repaint();
            try {
                if(current == null){
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
