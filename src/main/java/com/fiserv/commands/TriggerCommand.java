package com.fiserv.commands;

import com.fiserv.CognosCliCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "trigger", description = "Trigger an event in Cognos Analytics", mixinStandardHelpOptions = true)
public class TriggerCommand implements Runnable {

    @Option(names = {"-t", "--trigger"}, description = "TriggerName", order = 1)
    private String triggerName;

    @ParentCommand
    private CognosCliCommand parentCommand;

    @Override
    public void run() {
        if(parentCommand.verbose){
            System.out.printf("Invoking trigger %s%n", triggerName);
        }
    }
}
