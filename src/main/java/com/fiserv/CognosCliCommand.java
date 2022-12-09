package com.fiserv;

import com.fiserv.commands.TriggerCommand;
import com.fiserv.commands.VerifyCommand;
import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cognos-cli", description = "Cognos Analytics CLI",
        mixinStandardHelpOptions = true, subcommands = {TriggerCommand.class, VerifyCommand.class})
public class CognosCliCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    public boolean verbose;

    @Option(names={"--url"}, description = "URL of a Dispatcher to interact with, \ni.e. http://localhost:9300/p2pd/servlet/disptach")
    public String dispatcherUrl;

    @Option(names = {"-u", "--user"}, description = "User name to log in with")
    public String userName;

    @Option(names = {"-p", "--password"}, description = "User password to log in with")
    public String userPassword;

    @Option(names = {"-n", "--namespace"}, description = "Namespace name to log in with")
    public String userNamespace;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(CognosCliCommand.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            System.out.println("Hi!");
        }
    }
}
