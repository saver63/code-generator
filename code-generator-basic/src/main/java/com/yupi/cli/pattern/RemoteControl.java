package com.yupi.cli.pattern;

import java.util.List;

public class RemoteControl {
    private Command command;



    public void setCommand(Command command){
        this.command = command;
    }

    public void pressButton(){
        command.execute();
    }
}
