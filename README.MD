# Cognos-CLI

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
