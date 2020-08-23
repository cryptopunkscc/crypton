# Create Account
Using this feature user can create new account on server. 
This action also covers `Add account` feature,
May return error if any occurs.

## User story
As a user I can create new account on server and initialize session.

## Business value
This is key access feature to other features in application. 

## Modules
```
:core:api
:core:domain:account
:net:smack
:android:room
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
