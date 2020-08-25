#!/bin/bash

root=$(dirname "$BASH_SOURCE")/../..
jar="$root"/ops/build/libs/ops.jar

if [ ! -f "$jar" ]; then
  "$root"/gradlew ops:shadowJar
fi

#java -jar "$dir"/libs/ops.jar "$@"
java -jar "$jar" "$@"
