## Snapshot v0.6.2 build 19
### New features
* Open files from chat messages
* Display images in android app chat
* Upload file for chat from android app
* Upload file using aesgcm (#46)
* Add JvmNetworkSys implementation
* Add AndroidNetworkSysV2 implementation
* Shell command subscription on incoming messages
### Bug fixes
* Fix auto cap sentences in chat input
* Fix issue with missing keyboard in chat
* Broken connection after network interface changes
### Refactor
* Change Message.Status Info & State to Message.Type
* Migrate android app to cli v2
* Add Feature abstraction over handlers, emitters and cli
* Organize Commands.kt structure
### Documentation
* Add release_flow.md
### CI
* Fix release scripts
### Chore
* Fix latest notes generator
* Remove deprecated cli v1
* Get.ChatInfo - add support for one to one chat
* Add timestamp to JvmLogOutput
* Change android debug app launcher color
* Update modules diagrams
