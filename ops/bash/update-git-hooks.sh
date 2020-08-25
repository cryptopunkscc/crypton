#!/bin/bash

set -euxo pipefail

dir=$(dirname "$BASH_SOURCE")
projectDir="$dir/../.."

hooks=(pre-commit pre-push)

for hook in ${hooks[*]} ; do

  inputPath="$(pwd)"/"$dir"/"$hook".sh
  outputPath="$projectDir"/.git/hooks/$hook

  if [ -L "$outputPath" ]; then
    rm "$outputPath"
  fi

  ln -s  "$inputPath" "$outputPath"

  echo Created sym link to "$inputPath" in "$outputPath"

done
