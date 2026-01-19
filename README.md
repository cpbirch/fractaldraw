
# Mandlebrot Fractal Renderer in Kotlin with Coroutines

## TLDR;
`./gradlew run --args="-p 8 -w 1440"`

Your mileage may vary... see problems below.

Tested on OpenJDK 22.0.1 & 23.0.2, MacBook Pro with M1 Pro chip, MacOS 14.6, 15.2, 26.2

Draw a box with the mouse to zoom in.

# Why

Originally, I wanted to play with Kotlin Coroutines to understand them.
So I thought I'd use a computationally expensive algorithm, such as a 
Mandlebrot generator.

I decided that I'd like to see it draw, like I used to in the  1980's on my
8bit 3MHz Z80 processor where I could write values directly to video memory.
Things have moved on since then...

What I now know is that opening a Window (not using AWT / Swing) isn't easy.
It's still harder writing pixel values.  I don't understand all the OpenGL 
behaviours.  For example, if any kind of threading happens on the main thread, 
where OpenGL wants to render stuff, it generally results in a crash report.

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

# Program Architecture
The architecture is a bit naive but it boils down to layers from the point of
view of the user.

## Layer 1 - the Window
This has a size, number of pixels, capture mouse movements and keyboard inputs (has focus).
Where necessary, it calls functions in the next layer...  
This is where LWJGL / OpenGL functions live.

## Layer 2 - Graphics2D
The Skija 2D graphics library lives here.  This is where drawing to a Surface
happens which is bound to the OpenGL Window.  The coordinates here are
cartesian with the origin at the top left (0,0) and they increase to the right
and down.

This layer drives the rendering of a fractal by asking the fractal plane for
a colour value for each pixel row, top to bottom.

This is where coroutines happen.  For parallel run of 2, then two rows are done
at once: the top row and the middle row.  The rows below each of those next etc.

This creates a nicer visual render effect than doing the first 2 rows at the same time.

## Layer 3 - FractalPlane
This manages the cartesian coordinates of where the fractal lives, typically top left: -2.0, 1.1 to
bottom right: 0.66, -1.1.  Any mapping between Graphics2D coordinates is done here.
Also colour palette is managed here too.

A coordinate is given to the calc, the result is then assigned to a colour and
composed into a row of colour values.

## Layer 4 - MandlebrotPointCalculator
Each Graphics2D coordinate is converted into a FractalPlane coordinate.
Each of those is given to this calculator which iterates Mandlebrot's formula
and returns the calculated value.

