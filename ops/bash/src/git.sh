#!/bin/bash

function git-head-sha() {
  local line=${1:-0}
  git rev-parse HEAD~"$line"
}

function git-update-hooks() {

  local scriptDir=$(dirname "$BASH_SOURCE")
  local opsBash="$(pwd)/$scriptDir"

  for hook in "$CRYPTON_ROOT"/ops/bash/git/*.sh; do

    inputPath="$opsBash/$hook".sh
    outputPath="$CRYPTON_ROOT/.git/hooks/$hook"

    if [ -L "$outputPath" ]; then
      rm "$outputPath"
    fi

    ln -s "$inputPath" "$outputPath"

    echo Created sym link to "$inputPath" in "$outputPath"

  done
}

function git-add-amend-push() {
  git add . && git commit -S --amend --no-edit && git push --force
}

function git-add-amend() {
  git add . && git commit -S --amend --no-edit
}

function git-add-commit() {
  git add .
  case $1 in
  "") git commit -S ;;
  *) git commit -S -m "$@" ;;
  esac
}

function git-add-commit-push() {
  git-add-commit "" && git push
}
