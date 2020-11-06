## Snapshot v0.6.1 build 18
### Bug fixes
* Incomplete subscription handling
* Incorrect network packet size
* Empty chat address in
* Filter chat messages by chat on Get.Messages and Subscribe.LastMessage
### Refactor
* Rename ':engine:translator' to ':engine:cli'
* Move :app:android:indicator to :platform:util:android:indicator
* Prepare :platform:repo module for ormlite
* Restructure application modules
* Restructure library modules
### CI
* Use snapshot branch instead of dev for building latest_notes.md
* Fix release-version ops function
### Chore
* Add Get.Subscription status action
* Change session connection management (experimental)
* Add delete account command to cli
* Add :platform:repo:test
* Update testing libs
* Add OrmLiteAppRepo
* Add simple file storage abstraction
* Update kotlin to 1.4.10
* Update gradle 6.5
* Update :android:app.puml
* Add ormlite support
* Fix generateReleaseNotes function
* Split `./ops/bash/utils.sh` to `./ops/bash/src/*`
* Move git hooks from `./ops/bash/` to `./ops/bash/git/`
