#!/bin/sh

gpg --symmetric \
  --batch \
  --yes \
  --cipher-algo AES256 \
  --passphrase="$1" \
  ./signing.properties
