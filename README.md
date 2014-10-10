# ODK SMS Bridge

> An app for sending SMS messages with and without user confirmation.

## Overview

The ODK SMS Bridge lets you send SMS messages with an
[Intent](http://developer.android.com/reference/android/content/Intent.html).
Sending SMS messages via Intents is supported by default in Android. However!
Doing so always launches a messaging app and requires user confirmation.

Android 4.4 introduced the [SmsManager](
http://developer.android.com/reference/android/telephony/SmsManager.html),
which lets you send messages without an external app. However! There is no way
to trigger it using an Intent.

This app is a small wrapper around both methods that lets you use either
technique via an Intent. A `require_confirmation` boolean variable selects
which method is used.

## Not Currently Supported by Core ODK Team

This app is currently not supported by the core [Open Data Kit (ODK)](
http://opendatakit.org/) team.

However, this **is** a supported project. Feel free to open issues and use it,
but for now you can't necessarily count on support from the entire ODK core
team.



<p align="center">
    <img alt="BioTool at work in a context menu" src="images/contextMenuSample.png">
</p>

## Usage

### In Native Android Apps

Interact with the ODK SMS Bridge using an Intent. Arguments maybe be passed in
the extras `Bundle`.

The code below sends a message to number `3605551234` with the content `This is
a fancy message.`.

```java

```



## Architecture


## Building and Contributing

The original structure of the project came from [deckard-gradle](
https://github.com/robolectric/deckard-gradle). Follow their setup instructions
to build the project.

It uses the [Android Gradle build system](
http://tools.android.com/tech-docs/new-build-system).


### Tests

Tests can be run from the root directory with the command
`./gradlew clean test`.

They can also be run within IntelliJ, provided you
follow the setup instructions from deckard-gradle.
