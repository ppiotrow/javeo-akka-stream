Akka streams
========

To help me present Akka streams to workmates from javeo.eu

It is strongly related to http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
. Most of this source code was presented by **Roland Kuhn** and **Viktor Klang**.

## Manual
Artifact consist of 8 exercises. Each uses a big input file you will generate with generate3GBFile.scala.

Then you can run sbt with low memory (to get faster the "out of memory error") using command

*sbt -mem 64*

then just use command 

*run* 

to select available exercises

## Exercises
**Explains problem**
 * Ex1 - out of memory because whole file was loaded
 * Ex2 - no out of memory, but not all cores are used
 * Ex3 - push implementation, all cores used but mailboxes growing, OutOfMemoryError, no static typing

**Shows solution**
 * Ex4 - reactive streams FTW simple example
 * Ex5 - zip with ticks
 * Ex6 - mapFuture with WebService
 * Ex7 - every tick summarizes number of transactions between ticks
 * Ex8 - currency rates WS is asked only once per n seconds


