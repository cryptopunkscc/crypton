# Release flow

## Snapshot

### Flow v1 using CI for snapshot notes generation

1. Make sure the `dev` branch is up to date and ready for creating snapshot
1. Run [git-update-snapshot](../ops/bash/src/project.sh) this should force update the `snapshot` branch basing on `dev`.
1. Make sure the [prepare_snapshot](../.github/workflows/prepare_snapshot.yml) action was triggered.
1. Wait until the [prepare_snapshot](../.github/workflows/prepare_snapshot.yml) action will finnish with success.
1. You can ensure the snapshot branch contains new commit with generated snapshot notes.
1. Run [release-snapshot](../ops/bash/src/project.sh)
1. Wait until the [release_snapshot](../.github/workflows/release_snapshot.yml) action will finnish with success.
1. Make sure the snapshot build was properly published

### Flow v2 

1. Make sure the `dev` branch is up to date and ready for creating snapshot
1. Run [git-update-snapshot](../ops/bash/src/project.sh) this should force update the `snapshot` branch basing on `dev`.
1. `git checkout snapshot && git pull`  [VALIDATE!!!]
1. [ci-prepare-snapshot](../ops/bash/src/project.sh) [VALIDATE!!!]
1. TODO...
