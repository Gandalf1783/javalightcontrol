# javalightcontrol

>JLC - Lighting Software build on Java

JavaLightControl (JLC) is a Lighting Control Software with 15 Universes. It mainly supports ArtNet. It is using the
ArtNet4J (ArtNetForJava) Library, aswell as the Kryonet Library for Session support.

**Latest Official Released Version**: BETA-1.3 (see the releases page!)

**Latest Pushed Version**: BETA-1.3.2 (MAIN); DEV-1.1 (NET)

It is compiled using the OpenJDK 13.0.2.

## Features:

1. Project Saving and Loading
2. ArtNet Output
3. Supports up to 15 Universes
4. Effect Engine (Fading from Value X to Y in Z seconds)

## Work In Progess:

1. Include the UI (default UI) into the jar itself.
2. Refactoring the Console - Adding Commands (Mostly done), Command Autocomplete via (TAB) Key (Current Focus)
3. Adding Auto-Save supports (to prevent data-loss in case of application crash)
4. User Interface
5. Commandline support (Commands are unstable and not safe to use yet)
6. Session Support (Multi-User)

## To Do:

1. Receiving ArtNet
2. Timecode
3. Fixture Support

#### Requested Features:
> May not be implemented due to bad API's or other issues.
1. sACN Support
2. USB-DMX Interfaces Support
