#!/bin/sh

# this script MUST be run from project root

signingProperties=./signing.properties

if [ -f "$signingProperties" ]; then
  cp "$signingProperties" "$signingProperties".old
fi

gpg --quiet \
  --batch \
  --yes \
  --decrypt \
  --passphrase="$1" \
  --output "$signingProperties" ops/data/"$signingProperties".gpg
