cd /D "%~dp0"
java -jar LD001plugin.jar %* 2>> log.txt

REM https://stackoverflow.com/questions/13215348/how-to-output-java-jar-errors-to-a-text-log-file
