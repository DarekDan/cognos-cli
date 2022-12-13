# Cognos-CLI

Cognos CLI is a command-line utility to perform various tasks in Cognos. The 
initial version requires JRE to run, but in v1.0, individual GraalVM compiled
versions will be provided for each OS.

## Usage

```shell
java -jar target/cognos-cli-0.2.jar -h                                                                                                  

Usage: cognos-cli [-hvV] [-p[=<userPassword>]]... [-n=<userNamespace>]
                  [-u=<userName>] [--url=<dispatcherUrl>] [COMMAND]
Cognos Analytics CLI
  -h, --help              Show this help message and exit.
  -n, --namespace=<userNamespace>
                          Namespace name to log in with
  -p, --password[=<userPassword>]
                          User password to log in with
  -u, --user=<userName>   User name to log in with
      --url=<dispatcherUrl>
                          URL of a Dispatcher to interact with,
                          i.e. http://localhost:9300/p2pd/servlet/disptach
  -v, --verbose           ...
  -V, --version           Print version information and exit.
Commands:
  trigger  Trigger an event in Cognos Analytics
  verify   Verifies Cognos Analytics connectivity
```

## Sample usage

```shell
java -jar cognos-cli-0.2.jar -v 
     --url http://localhost:9300/p2pd/servlet/dispatch 
     -u userNameHere -p passWordHere -n nameSpaceHere 
     trigger TriggerOne "Trigger with space" Trigger_Two
```

Instead of providing a password in clear text on the command line prompt, you may choose
to be prompted for it:

```shell
java -jar cognos-cli-0.2.jar -v 
     --url http://localhost:9300/p2pd/servlet/dispatch 
     -u userNameHere -p -n nameSpaceHere 
     trigger TriggerOne "Trigger with space" Trigger_Two
```
or

```shell
java -jar target/cognos-cli-0.2.jar -v 
     --url http://l1pvap1431.1dc.com:9300/p2pd/servlet/dispatch 
     -u userNameHere -n nameSpaceHere -p verify
```

## Installation of dependent Cognos Analytics Java SDK libaries

```PowerShell
Get-ChildItem 
  ~/Samples/cognos_sdk_11.1.7/java/lib/ 
  -Filter *.jar | 
  ? {$_.Name -notlike 'test*'} | 
  Select-Object Name, BaseName, FullName |
  foreach-object { 
      mvn org.apache.maven.plugins:maven-install-plugin:install-file
          -D"file=$($_.FullName)" 
          -DgroupId="com.ibm.cognos" 
          -DartifactId="$($_.BaseName)" 
          -Dversion="11.1.7" 
          -Dpackaging=jar 
          -DlocalRepositoryPath=repo-local  
  }
```
