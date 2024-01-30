#!/bin/bash

java_cmd=$(type -p java)
if [[ $java_cmd ]]; then
    echo found java executable in PATH
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME
    java_cmd="$JAVA_HOME/bin/java"
else
    echo "Java 19 not found, please download and install it first."
fi

if [[ $java_cmd ]]; then
    version=$("$java_cmd" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo $java_cmd
    echo version "$version"
    if [[ "$version" < "19" ]]; then
        echo version is less than 19
        exit 1
    fi
fi

$java_cmd -jar datamasking.jar $@