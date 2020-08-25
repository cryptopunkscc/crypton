#!/bin/bash

dir=$(dirname "$BASH_SOURCE")/../..

"$dir/ops/bash/ops" "$dir" generate notes latest

git add "$dir/latest_notes.md" \
  "$dir/version.code" \
  "$dir/version.hash" \
  "$dir/version.name"

git commit -S -m "Update latest_notes.md"
