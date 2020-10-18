# Add Account
User can insert existing account credentials to local data base,
so it can be used to session authorization, which is also performed as next step.
May return error if any occurs.     

## User story
As a user I want add existing account to application and initialize session.

## Business value
This is key access feature to other features in application. 

## Modules
```
:core:api
:core:domain:account
:net:smack
:platform:repo:room
:android:account
```

## Command
```kotlin
Exec.Add(account)
```

## Requires
```kotlin
InternetConnection
```

## Returns
```kotlin
Connecting(address)
Connected(address)
Error(address, message)
```

## Errors
- todo
