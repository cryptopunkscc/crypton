# Auto reconnection

## User story
As a user I want to have account sessions automatically reconnected after connection lost or application restart

## Business value
User is not forced for interactions to keep connection with external server persistent.

## Modules
```
:engine:api
:engine:domain:app
:net:smack
:android:room
```

## Service
```kotlin
AppScope.startAppService()
```

## Methods
```kotlin
AppScope.loadSessions()
AppScope.networkStatusFlow()
AppScope.handleNetworkStatus()
```

## Errors
```kotlin
// TODO: 01.07.20  
```
