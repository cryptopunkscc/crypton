#!/bin/bash

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
  git push origin snapshot
}

function ci-prepare-version() {
  ops "$CRYPTON_ROOT" generate notes version
  git add "$CRYPTON_ROOT"
  git commit \
    -m "Version $(version-full)" \
    -m "$(cat release_notes.md)"
  git push origin master{
}
