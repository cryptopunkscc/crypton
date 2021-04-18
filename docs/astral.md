# Astral

Protocol / Library

### Responsibilities

* Connection management
* Simple file transfer between two nodes
* Providing services

## Layers

### Transport

Different touch points:

* TCP
* Tor
* Bluetooth
* ...

### Connection management

* manage multiple ways of connections
* manage list of touch points

#### Device identity

A public key generated for a device

#### Handshake

chacha20

* Authentication
* Encrypted connection

#### API

* get touch points

### File transfer

#### API

* get file (optional range)
    * don't have
    * here you have

## Multiplexer

TODO

### Services

#### API

* get service list
* connect me to service using channel
* close connected channel
