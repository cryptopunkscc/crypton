#!/bin/bash

function full-version() {
  echo "$(cat version.name)-$(cat version.code)"
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

function update-latest-notes() {
  ops "$CRYPTON_ROOT" generate notes latest
}

function ci-configure-git() {
  git config user.name "cryptoyang"
  git config user.email "cryptopunkyang@pm.me"
}

function ci-set-git-token() {
  git remote set-url origin "https://x-access-token:$1@github.com/$GITHUB_REPOSITORY"
}

function ci-pre-snapshot() {
  update-latest-notes
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
