#!/bin/bash

function crypton-embedded-build() {
  "$CRYPTON_ROOT/gradlew" :app:jvm:embedded:shadowJar
}

function crypton-embedded-run() {
  java -jar "$CRYPTON_ROOT/app/jvm/embedded/build/libs/embedded-all.jar" "$@"
}

function crypton-server-build() {
  "$CRYPTON_ROOT/gradlew" :app:jvm:server:shadowJar
}

function crypton-server-run() {
  java -jar "$CRYPTON_ROOT/app/jvm/server/build/libs/server-all.jar" "$@"
}

function crypton-cli-build() {
  "$CRYPTON_ROOT/gradlew" :app:jvm:cli:shadowJar
}

function crypton-cli-run() {
  java -jar "$CRYPTON_ROOT/app/jvm/cli/build/libs/cli-all.jar" "$@"
}
