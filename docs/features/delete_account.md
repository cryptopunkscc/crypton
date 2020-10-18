# Delete account from local application
This feature allows to close session and remove account from local application database.

## User story
As a user I want to delete account from local application.

## Business value
User can remove account from local application database.

## Modules
```
:core:api
:core:domain:account
:smack:net
:platform:repo:room
:android:account
```

## Command
```kotlin
Account.Remove(address, deviceOnly = false)
```

## Requires
```kotlin
Account
```

## Returns
```kotlin
// TODO: 01.07.20  
```

## Errors
```kotlin
// TODO: 01.07.20  
```
