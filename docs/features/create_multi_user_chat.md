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
:engine:api
:engine:domain:chat
:android:chat
:android:room
:net:smack
```

## Command
```kotlin
Chat.Service.CrateChat
```

## Requires
```kotlin
Session
```

## Returns
```kotlin
Chat.Service.ChatCreated
```

## Errors
```kotlin
todo
```
