# Cognos-CLI

Cognos CLI is a command-line utility to perform various tasks in Cognos. The 
initial version requires JRE to run, but in v1.0, individual GraalVM compiled
versions will be provided for each OS.

## Sample usage

```shell
java -jar cognos-cli-0.2.jar -v --url http://localhost:9300/p2pd/servlet/dispatch -u userNameHere -p PassWordHere -n NamespaceHer 
trigger TriggerOne "Trigger with space" Trigger_Two
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
