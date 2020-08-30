#!/bin/bash

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
