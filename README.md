Akka streams
========

To help me present Akka streams to workmates from javeo.eu

It is strongly related to http://www.parleys.com/play/53a7d2c3e4b0543940d9e53a
. Most of this source code was presented by **Roland Kuhn** and **Viktor Klang**.


## Preparation
Repo consist of 9 exercises. Each uses a big input file you will generate with generate3GBFile.scala.

Then you may run sbt with low memory. Its because you may want to get faster the "out of memory error" in Ex[1-3] and to simulate scale between large amount of data and your program.
There are different ways to achieve that depends on sbt version. You can try
*sbt -mem 64* command or to modify -Xmx param somewhere in your downloaded sbt launcher.

Then just use command *run* to select available exercises.

During most of exercises is worth to watch at *htop* console to compare cores usage. It is better to switch off some CPU cores if you have more than 4 cores. In my Ubuntu I use 

*sudo sh -c "echo 0 > /sys/devices/system/cpu/cpu5/online"*

example command switches off 5th core (you need to restart *htop* to see less cores number). With more than 4 cores stream solutions don't use 100% power of all cores,
  because problems in exercises are too simple (need less number of actors).


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
 * Ex9 - two independent streams back-pressured by common producer

