package com.fiserv.commands;

import com.fiserv.CognosCliCommand;
import com.fiserv.cognos.Session;
import java.rmi.RemoteException;
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
        Session session = new Session(parentCommand.dispatcherUrl);
        if(session.login(parentCommand.userNamespace, parentCommand.userName, parentCommand.userPassword)){
            try {
                if(session.getEventService().trigger(triggerName)>0){
                    System.out.printf("Trigger %s has been invoked%n",triggerName);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
