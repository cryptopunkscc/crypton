#!/bin/bash

dir=$(dirname "$BASH_SOURCE")/../..

"$dir/ops/bash/ops" increment version "$dir/version.code"
