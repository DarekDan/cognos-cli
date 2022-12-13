package com.fiserv.commands;

import com.fiserv.CognosCliCommand;
import com.fiserv.cognos.Session;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "verify", description = "Verifies Cognos Analytics connectivity")
public class VerifyCommand implements Runnable {

    @ParentCommand
    CognosCliCommand parentCommand;

    @Override
    public void run() {
        Session session = new Session(parentCommand.dispatcherUrl);
        if (session.loginAnonymous()) {
            System.out.println("Anonymous access is enabled");
        } else {
            if (session.login(parentCommand.userNamespace, parentCommand.userName, String.valueOf(parentCommand.userPassword))) {
                System.out.println("Credentials are valid");
            } else {
                System.out.println("Invalid credentials");
            }
        }
    }
}
