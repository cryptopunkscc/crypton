#!/bin/bash

set -euxo pipefail

dir=$(dirname "$BASH_SOURCE")
projectDir="$dir/../.."

inputPath="$(pwd)"/"$dir"/pre-commit.sh
outputPath="$projectDir"/.git/hooks/pre-commit


if [ -L "$outputPath" ]; then
  rm "$projectDir"/.git/hooks/pre-commit
fi

ln -s  "$inputPath" "$outputPath"

echo Created sym link to "$inputPath"
