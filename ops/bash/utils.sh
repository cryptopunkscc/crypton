#!/bin/bash

function read-version() {
  sed -n "$1"p <version
}

function full-version() {
  echo "$(read-version 2)-$(read-version 1)"
}

function git-head-sha() {
  local line=${1:-0}
  git rev-parse HEAD~"$line"
}

function release-snapshot() {
  local currentBranch=$(git rev-parse --abbrev-ref HEAD)
  git checkout pre-snapshot
  git pull
  git tag -fa latest -m "$(full-version)"
  git push -f origin latest
  git checkout "$currentBranch"
}

function encrypt-signing-properties() {
  local signingProperties="$CRYPTON_ROOT/signing.properties"

  gpg --symmetric \
    --batch \
    --yes \
    --cipher-algo AES256 \
    --passphrase="$1" \
    "$signingProperties"

  mv "$signingProperties.gpg" "$CRYPTON_ROOT/ops/data/"
}

function decrypt-signing-properties() {
  local signingProperties="$CRYPTON_ROOT/signing.properties"
  local signingPropertiesGpg="$CRYPTON_ROOT/ops/data/signing.properties.gpg"

  if [ -f "$signingProperties" ]; then
    cp "$signingProperties" "$signingProperties".old
  fi

  gpg --quiet \
    --batch \
    --yes \
    --decrypt \
    --passphrase="$1" \
    --output "$signingProperties" "$signingPropertiesGpg"
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
  "")
    echo Updating snapshot
    ops "$CRYPTON_ROOT" generate notes latest
    cat "$CRYPTON_ROOT/latest_notes.md"
    ;;
  "1") echo "Update snapshot aborted. Snapshot hash is same as current" ;;
  "2") echo "Update snapshot aborted. Snapshot hash is same as previous" ;;
  *) echo "Update snapshot aborted. Unknown error $code" ;;
  esac
}

function ci-configure-git() {
  git config user.name "cryptoyang"
  git config user.email "cryptopunkyang@pm.me"
}

function ci-set-git-token() {
  git remote set-url origin "https://x-access-token:$1@github.com/$GITHUB_REPOSITORY"
}

function ci-pre-snapshot() {
  ops "$CRYPTON_ROOT" generate notes latest
  git add "$CRYPTON_ROOT"
  git commit \
    -m "Snapshot $(full-version)" \
    -m "$(cat latest_notes.md)"
  #    -m "$(ops "$CRYPTON_ROOT" print changes)"
  git push origin pre-snapshot
}

function prepare-and-release-snapshot() {
  ci-pre-snapshot && release-snapshot
}

function ops-rebuild() {
  "$CRYPTON_ROOT/gradlew" ops:shadowJar
}

function git-add-amend-push() {
  git add . && git commit -S --amend --no-edit && git push --force
}
