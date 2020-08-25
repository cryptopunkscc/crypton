#!/bin/bash

root=$(dirname "$BASH_SOURCE")/../..

"$root"/gradlew ops:shadowJar
