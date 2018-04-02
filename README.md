# Checking out OpenGL2 on Android 7.0 (Nougat)

Creating a GUI depends on design, tools and experience. OpenGL is a common library used, so I create a project and see what I can do with prototypes.

## My setup
- Android Studio 3.1 (pre-built on 2018-03-22)
- Java 8
- Windows 7 (duh), 8GB, 2.3 Mhz
- Samsung Galaxy J5, Android 7.0

## How to get most of the apps running

- Install Android Studio (2018-04-02 current stable build)
- Install driver for a compatible Android (2018-04-02 current stable build)
- Clone this repository https://github.com/minhkhoa92/DirtApp.git
- Open the repository in Android Studio
- Build \& run
- It crashed? Read the error messages, and figure out how to install the missing packages from SDK Manager (packages: SDK, NDK, CMake, AppCompat aka Compatibility)
- Sync your project
- Repeat Build \& run \& package install
- Check out the simple Apps and have fun producing more code

## Apps built

- Check OpenGLES 2 compatibility (branch!)

# Source
Learn OpenGL ES - For Mobile Game and Graphics Development
written by Prateek Mehta
published 2013

# Contact
In case anyone wants to contact me, use my email or open an issue (which will have to wait)

minhkhoa.ngo92@gmail.com
