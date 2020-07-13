# Create direct chat
This feature allows to create chat with single user.
User can have only one direct chat with each buddy.
Creating direct chat adds chat entity to data base, but not send invitation.
To send invitation queue first message in chat scope.
This message will be send when interlocutor accepts subscription.
By default single user chats are encrypted

## User story
As a user I want to open chat with one user

## Business value
Creating chat is required for sending direct messages

## Modules
```
:core:api
:core:domain:chat
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

```
