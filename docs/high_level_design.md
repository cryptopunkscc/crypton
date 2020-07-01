# Architecture design
This document contains core concepts about architecture design. 
Some of them are working well now, but others may not be implemented yet and some may be work in progress.
All of them should be specified widely in dedicated documents. If not now then in future when time began.

## Goals
* Be not closed for multiple platform support
* Keep it clean and simple as possible

## Local client-server
Crypton is designed to be local client-server application, where: 
1. Client means UI embedded or standalone application which can communicate with application backend using defined asynchronous API
2. Server means embedded or standalone state-full service, which provides set of features taking into account business logic.
Both can work in embedded or standalone versions. 
For example standalone server will be a linux daemon, which can handle bi directional communication through web sockets.
Standalone server could handle multiple clients connections, for example from standalone CLI and GUI apps.
In Android more sense makes application with embedded UI and backend. 

## Modular architecture
Crypton is pretending to be multi-platform client.
To make it possible is important to keep reusable code separated from platform specific modules.
The reusable part is called `:engine` and contains core `java-library` modules for crypton.
More details about Crypton modules can be found in `modules.md` document.

## Internal state
Crypton should contains internal state.
State is represented by set of flags. Each flag is unique and represents different system status element.
Flags in state can be added removed or checked. 
State is used to verify if received command can be performed.
State may be fetched by client and used for other purposes.
