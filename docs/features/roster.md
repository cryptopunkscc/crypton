# Roster
This feature provides list of users conversations.
It depends only on data from local storage. 
Other features are need to provide required feed for local storage.

## User story
As a user I can see list of all conversations with details such invitations, buddy status, last message.

## Business value
This feature provides info about user conversations status, including:
* buddy list subscriptions
* conversation invitations
* buddy status updated
* Last message in chat 

## Requires
only for producing output
```kotlin
Session
Chat
Message
```

## Modules
```
:engine:api
:engine:domain:roster
:android:room
:android:roster
```

## Commands
```kotlin
Roster.Service.GetItems
Roster.Service.SubscribeItems
```

## Returns
```kotlin
Roster.Service.Items
```

## Errors
todo
