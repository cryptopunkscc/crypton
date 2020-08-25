#!/bin/sh

# this script MUST be run from project root

gpg --symmetric \
  --batch \
  --yes \
  --cipher-algo AES256 \
  --passphrase="$1" \
  ./signing.properties

mv signing.properties.gpg ops/data/
