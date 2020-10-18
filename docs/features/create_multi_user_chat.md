# Create direct chat
This feature allows to create chat with multiple users.
Creating multi user chat automatically adds entity to database, 
registers chat on server and send invitation to specified users.
By default multi user chats are encrypted.
Invited users must be previously added to buddy list.

## User story
As a user I want to open chat with multiple users

## Business value
Creating multi user chat allows to share messages with many users in one chat scope.

## Modules
```
:core:api
:core:domain:chat
:android:chat
:platform:repo:room
:net:smack
```

## Command
```kotlin
Chat.CreateChat
```

## Requires
```kotlin
Session
```

## Returns
```kotlin
Account.ChatCreated
```

## Errors
```kotlin
todo
```
