#!/bin/bash

jar="$CRYPTON_ROOT/ops/build/libs/ops.jar"

if [ ! -f "$jar" ]; then
  "$CRYPTON_ROOT"/gradlew ops:shadowJar
fi

java -jar "$jar" "$@"
