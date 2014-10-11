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
## Usage

### In Native Android Apps

Interact with the ODK SMS Bridge using an Intent. Arguments maybe be passed in
the extras `Bundle`.

The code below sends a message to number `3605551234` with the content `This is
a fancy message.`.

```java
Intent intent = new Intent();

ComponentName componentName = new ComponentName(
    "org.opendatakit.smsbridge",
    "org.opendatakit.smsbridge.activity.SMSDispatcherActivity");

intent.setComponent(componentName);

Bundle bundle = new Bundle();

// False to send an SMS immediately without the user stepping in.
// True to require user intervention.
bundle.putBoolean("require_confirmation", false);
bundle.putString("message_body", "This is a fancy message.");
bundle.putString("phone_number", "3605551234");

intent.putExtras(bundle);

context.startActivity(intent);
```

### In ODK Survey

The ODK SMS Bridge can also be called from [ODK Survey](
http://opendatakit.org/use/2_0_tools/odk-survey-2-0-rev122/).

## Architecture

SMS messages are sent by the [SMSDispatcherActivity](#). This activity has no
user interface. It interprets arguments passed via an Intent (see Usage) and
finishes immediately. This is the Activity callers should invoke.

The main screen of the app is the [WelcomeActivity](#). It exists to allow
users to see what interactions with the `SMSDispatcherActivity` are like. Two
buttons let you send a message with or without user confirmation to give you a
sense of the different user experiences.

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

## Not Currently Supported by Core ODK Team

This app is currently not supported by the core [Open Data Kit (ODK)](
http://opendatakit.org/) team.

However, this **is** a supported project. Feel free to open issues and use it,
but for now you can't necessarily count on support from the entire ODK core
team.



<p align="center">
    <img alt="BioTool at work in a context menu" src="images/contextMenuSample.png">
</p>

