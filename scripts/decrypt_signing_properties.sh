#!/bin/sh

#DIR=`dirname "$BASH_SOURCE"`
#PROJECT_DIR="../$DIR"

#cd "$PROJECT_DIR"

SIGNING_PROPERTIES=./signing.properties

if [ -f "$SIGNING_PROPERTIES" ]; then
  cp "$SIGNING_PROPERTIES" "$SIGNING_PROPERTIES".old
fi

# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet \
  --batch \
  --yes \
  --decrypt \
  --passphrase="$1" \
  --output "$SIGNING_PROPERTIES" "$SIGNING_PROPERTIES".gpg
