#!/bin/bash

dir=$(dirname "$BASH_SOURCE")/../..

"$dir/ops/bash/ops" "$dir" increment version "$dir/version.code"

git add "$dir/version.code"
