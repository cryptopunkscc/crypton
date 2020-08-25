#!/bin/bash

adb shell am instrument -w -r -e debug false -e class 'MigrationTest' cc.cryptopunks.crypton.data.test/androidx.test.runner.AndroidJUnitRunner