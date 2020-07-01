## `:engine:api`
A central part of application. 
Mostly pure structures, constants and interfaces.
It contains definition of: 
* entities
* commands
* queries
* scopes
* repository interfaces
* network api interfaces
* system interfaces
* internal communication protocol
* some crucial reusable code

Some of them are technically not important from client perspective, but it was early design decision to keep data 
with abstraction together. May be improved in the future.
 
## `:engine:domain`
Bundle of feature specific modules. 
The role for them is to cover crypton API with business logic implementation.
Should be implemented using functions as it defines interactions, not data.

## `:engine:backend`
Manage access to domain services through two related parts:
* Backend - Aggregates set of interactions for domain services management.
* BackendService - Is connectable wrapper-adapter for backend. Can be connected to clients.

## `:engine:util`
Bundle of non domain, non platform specific adapters and libs.

## `:engine:mock`
Should contain reusable mock implementations of repositories, network api and system interfaces specified in `:engine:api`

## `:jvm`
JVM specific implementations except smack adapter. which is specified in `:net:smack` and provided by `:net:smack:jvm` module

## `:android`
Contains all android related code except smack adapter. which is specified in `:net:smack` and provided by `:net:smack:android` module.

## `:net:smack`
Implementation of crypton network api adapters for smack. 
Contains reusable implementation which is provided through platform specific proxy modules [`:net:smack:jvm`, `:net:smack:android`].
