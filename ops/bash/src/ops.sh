#!/bin/bash

function ops-rebuild() {
  "$CRYPTON_ROOT/gradlew" ops:shadowJar
}

function ops-update-chmod() {
    chmod +x "$CRYPTON_ROOT/ops/bash/*"
}
