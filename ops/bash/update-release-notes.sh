#!/bin/bash

dir=$(dirname "$BASH_SOURCE")/../..

"$dir/ops/bash/ops" "$dir" generate notes release

#git add "$dir/release_notes.md"

#git commit -S -m "Update release_notes.md"
