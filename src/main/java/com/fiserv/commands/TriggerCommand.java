package com.fiserv.commands;

import com.fiserv.CognosCliCommand;
import com.fiserv.cognos.Session;
import java.rmi.RemoteException;
import java.util.List;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "trigger", description = "Trigger an event in Cognos Analytics", mixinStandardHelpOptions = true)
public class TriggerCommand implements Runnable {

    @Parameters(description = "A space delimited list of names")
    private List<String> triggerNames;

    @ParentCommand
    private CognosCliCommand parentCommand;

    @Override
    public void run() {
        Session session = new Session(parentCommand.dispatcherUrl);
        if (session.login(parentCommand.userNamespace, parentCommand.userName, parentCommand.userPassword)) {
            try {
                for (String triggerName : triggerNames) {
                    if (parentCommand.verbose) {
                        System.out.printf("Invoking trigger %s%n", triggerName);
                    }
                    if (session.getEventService()
                        .trigger(triggerName) > 0) {
                        System.out.printf("Trigger %s has been invoked%n", triggerName);
                    }
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
