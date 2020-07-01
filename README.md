# Crypton Messenger
Crypton is modular encrypted multi-platform XMPP client for Android and JVM.

## Our goal
Is to provide free and open way to safe encrypted communication basing on decentralized protocol.  

## We are focused to
* be open for multi-platform support.
* create great tool by improving quality over time.
* keeping implementation and architecture design clean and simple as possible.
* provide stable set of important features.

## Current project status
Early work in progress alpha. Some of features already works but are not well tested.

## Installation 
Unstable snapshot of android developer artifact is published in
[latest pre-release](https://github.com/cryptopunkscc/crypton/releases/tag/latest) each commit.

## Features
The first-in-last-out (FILO) queue of all features in project scope. 
Usually the highest priority has `work in progress` section the next it last item from `Road map`.  

### Road map
* Voice
* Terminal GUI client (like midnight commander or vim)
* Create docker image of test server to allow integration testing on CLI
* CLI jvm client
* Internal updates service for android client based on github releases
* Lighting wallet integration 
* Complete error handling

### Work in progress
* Multi user chat

### Current features
* Multiple accounts
* Omemo encryption
* Add existing account
* Register new account
* Delete account from local database
* Remove account from server
* Accept invitation to buddies
* List conversations
* Single user chat
* Send message
* List chat messages

### Documentation
See `docs/` directory in repository root

## License
```
The MIT License

Copyright (c) 2019 cryptopunks

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
