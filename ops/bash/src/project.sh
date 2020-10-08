#!/bin/bash


function read-version() {
  sed -n "$1"p <version
}

function version-code() {
  read-version 1
}

function version-name() {
  read-version 2
}

function version-hash() {
  read-version 3
}

function version-full() {
  echo "$(version-name)-$(version-code)"
}

function version-snapshot-hash() {
  read-version 4
}

function git-update-snapshot() {
    git push origin dev:snapshot --force
}

function release-snapshot() {
  local currentBranch=$(git rev-parse --abbrev-ref HEAD)
  git checkout snapshot
  git pull
  git tag -fa latest -m "$(version-full)"
  git push -f origin latest
  git checkout "$currentBranch"
}

function release-version() {
  local currentBranch=$(git rev-parse --abbrev-ref HEAD)
  local tag="v$(version-name)"
  git checkout master
  git pull
  git tag -fa "$tag" -m "$(version-full)"
  git push -f --tags
  git checkout "$currentBranch"
}

function assert-snapshot() {
  local changelog="$(read-version 4)"
  local current="$(git-head-sha 0)"
  local previous="$(git-head-sha 1)"
  case "$changelog" in
  "$current") exit 1 ;;
  "$previous") exit 2 ;;
  esac
}

function update-latest-notes() {
  $(assert-snapshot)
  local code=$?
  case $code in
  ""|0)
    echo Updating latest notes
    ops "$CRYPTON_ROOT" generate notes snapshot
    cat "$CRYPTON_ROOT/latest_notes.md"
    ;;
  1) echo "Update latest notes aborted. Snapshot hash is same as current" ;;
  2) echo "Update latest notes aborted. Snapshot hash is same as previous" ;;
  *) echo "Update latest notes aborted. Unknown error $code" ;;
  esac
}

function assert-version() {
  local changelog="$(read-version 3)"
  local current="$(git-head-sha 0)"
  local previous="$(git-head-sha 1)"
  case "$changelog" in
  "$current") exit 1 ;;
  "$previous") exit 2 ;;
  esac
}

function update-release-notes() {
  $(assert-version)
  local code=$?
  case $code in
  ""|0)
    echo Updating release notes
    ops "$CRYPTON_ROOT" generate notes version
    cat "$CRYPTON_ROOT/release_notes.md"
    ;;
  1) echo "Update release notes aborted. Snapshot hash is same as current" ;;
  2) echo "Update release notes aborted. Snapshot hash is same as previous" ;;
  *) echo "Update release notes aborted. Unknown error $code" ;;
  esac
}
