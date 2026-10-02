# Lab 2: white/red group monitor

This lab permits several threads of the same colour to be active together, while preventing white and red threads from being active at the same time during normal, non-interrupted execution.

`ColorSynchronizer` maintains two active-thread counters. Its entry and exit methods are `synchronized`. A thread waits in a `while` loop while the opposite colour is active. The last thread of a colour calls `notifyAll()` when leaving, allowing the waiting group to check its condition again.

This is group mutual exclusion, not an exclusive lock for every individual thread. The monitor methods protect counters and admission decisions; the simulated work occurs outside those methods.

## Build and run

From the repository root, with a JDK installed:

```sh
make java-lab2
java -cp build/lab2 ColorDemo
```

Without Make:

```sh
mkdir -p build/lab2
javac -encoding UTF-8 -d build/lab2 labs/02-color-monitor/ColorSynchronizer.java labs/02-color-monitor/ColorDemo.java
java -cp build/lab2 ColorDemo
```

There are no command-line parameters. The demonstration starts five white threads followed by five red threads. Starting the white threads first does not guarantee the order in which all threads execute.

## Demonstration and output

Each thread prints when it enters and when it finishes work in the critical section. `Baltoji gija` means white thread; `Raudonoji gija` means red thread.

In `ColorDemo.java`, white threads call `sleep(0)`, while red threads sleep for a random interval of approximately 500 to 1,500 ms. 

The main method does not explicitly join the workers. They are ordinary non-daemon threads, so the JVM remains alive until they finish.

## Synchronization limits

The admission rule does not guarantee fairness or prevent starvation under a continuing stream of one colour. In addition, a task unconditionally calls its exit method in `finally`, even if interrupted before successfully entering. The supplied demonstration does not deliberately interrupt workers; cancellation-safe bookkeeping is not implemented.

## Files

- `ColorSynchronizer.java`: the monitor, counters and `wait`/`notifyAll` protocol.
- `ColorDemo.java`: the ten-thread demonstration and console messages.

No external dependencies are needed.
