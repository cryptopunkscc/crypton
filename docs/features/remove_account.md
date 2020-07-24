# Remove account from server
This feature allows to remove account from server.
It also covers feature specified in `delete_account.md`.

## User story
As a user I want to remove account from server and local application.

## Business value
User can remove account from server.

## Modules
```
:core:api
:core:domain:account
:smack:net
:android:room
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

## Notes
Crypton creates one database for accounts table and additional dedicated databases for each account.
Currently deleting account is performed only on accounts database, and dedicated database are left untouched. 
It's possible to retain dedicated database for deleted account by simply creating account with same address again.
This may be consider as issue in the future so this note is reminder. 
