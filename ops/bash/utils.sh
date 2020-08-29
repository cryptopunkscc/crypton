#!/bin/bash

function android-migration-test() {
  adb shell am instrument \
    -w -r -e debug false \
    -e class 'MigrationTest' \
    cc.cryptopunks.crypton.data.test/androidx.test.runner.AndroidJUnitRunner
}

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

function git-head-sha() {
  local line=${1:-0}
  git rev-parse HEAD~"$line"
}

function release-snapshot() {
  local currentBranch=$(git rev-parse --abbrev-ref HEAD)
  git checkout dev
  git pull
  git tag -fa latest -m "$(version-full)"
  git push -f origin latest
  git checkout "$currentBranch"
}

function relese-version() {
  local currentBranch=$(git rev-parse --abbrev-ref HEAD)
  local tag="v$(version-name)"
  git checkout master
  git pull
  git tag -fa "$tag" -m "$(version-full)"
  git push -f origin "$tag"
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

function ci-configure-git() {
  git config user.name "cryptoyang"
  git config user.email "cryptopunkyang@pm.me"
}

function ci-set-git-token() {
  git remote set-url origin "https://x-access-token:$1@github.com/$GITHUB_REPOSITORY"
}

function ci-prepare-snapshot() {
  ops "$CRYPTON_ROOT" generate notes snapshot
  git add "$CRYPTON_ROOT"
  git commit \
    -m "Snapshot $(version-full)" \
    -m "$(cat latest_notes.md)"
  #    -m "$(ops "$CRYPTON_ROOT" print changes)"
  git push origin dev
}

function ci-prepare-version() {
  ops "$CRYPTON_ROOT" generate notes version
  git add "$CRYPTON_ROOT"
  git commit \
    -m "Version $(version-full)" \
    -m "$(cat release_notes.md)"
  git push origin master
}

function prepare-and-release-snapshot() {
  ci-prepare-snapshot && release-snapshot
}

function prepare-and-release-version() {
  ci-prepare-version && release-version
}

function ops-rebuild() {
  "$CRYPTON_ROOT/gradlew" ops:shadowJar
}

function git-add-amend-push() {
  git add . && git commit -S --amend --no-edit && git push --force
}

function git-update-hooks() {

  local scriptDir=$(dirname "$BASH_SOURCE")
  local opsBash="$(pwd)/$scriptDir"

  hooks=(pre-commit pre-push)

  for hook in ${hooks[*]}; do

    inputPath="$opsBash/$hook".sh
    outputPath="$CRYPTON_ROOT/.git/hooks/$hook"

    if [ -L "$outputPath" ]; then
      rm "$outputPath"
    fi

    ln -s "$inputPath" "$outputPath"

    echo Created sym link to "$inputPath" in "$outputPath"

  done
}

function ops-update-chmod() {
    chmod +x "$CRYPTON_ROOT/ops/bash/*"
}

function git-add-commit() {
  git add .
  case $1 in
  "") git commit -S ;;
  *) git commit -S "$@" ;;
  esac
}
