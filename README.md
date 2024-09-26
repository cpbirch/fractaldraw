
# TLDR;
`./gradlew run -p 4`

Your mileage may vary... see problems below.

Zoom in: `./gradlew run -p 8 -w 1024 -fl neg0.06 -fr 0.1 -ft 0.74`
Zoom in more: `./gradlew run --args="-p 6 -w 1024 -fl 0.02 -fr 0.04 -ft 0.643"`

Tested on OpenJDK 22.0.1, MacBook Pro with M1 Pro chip, MacOS Sonoma 14.6

# Why

Originally, I wanted to play with Kotlin Coroutines to understand them.
So I thought I'd use a computationally expensive algorithm, such as a 
Mandlebrot generator.

I decided that I'd like to see it draw, like I used to in 80's on my
8bit 3MHz processor where I could write values directly to video memory.
Things have moved on since then...

What I now know is that opening a Window (not using AWT / Swing) isn't easy.
It's still harder writing pixel values.  Understand the weird behaviour if any
kind of threading happens on the main thread, where OpenGL wants to render stuff,
generally results in a crash report.

# Problems

### Multi-screen
On a multiscreen setup from my Macbook, if the dock isn't on my laptop screen
when the window opens on my non-laptop screen, 
then pixels are rendered in the bottom left quarter of the open window.
Move the dock, it renders in the full window.

### Rounding issues
I like the window-blind parallel draw.  Depending on how many threads you choose
or screen width, it may miss rendering the last few rows of pixels.  This is a
dodgy fix due to rounding errors causing some loop to never finish.

