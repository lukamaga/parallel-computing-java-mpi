# Lab 1: shared warehouse state

Two Java threads update a shared warehouse. Each thread performs 1,000 iterations of adding one item and then removing one item. An item is valued at 10 units. After joining both threads, the program prints the final item count and total value.

The active version intentionally has no synchronization. Updating an `int` field with `+=` or `-=` is a read/modify/write operation, and the count and value are stored separately. Interleaving those operations can lose updates or leave inconsistent totals.

## Build and run

Run these commands from the repository root. A JDK with `javac` and `java` is required; no external Java libraries are used.

```sh
make java-lab1
java -cp build/lab1 Main
```

Without Make:

```sh
mkdir -p build/lab1
javac -encoding UTF-8 -d build/lab1 labs/01-warehouse-race/Main.java
java -cp build/lab1 Main
```

There are no command-line parameters. The two threads, 1,000 iterations and item price are fixed in `Main.java`.

## Reading the output

The Lithuanian labels mean:

| Console label | Meaning |
| --- | --- |
| `Galutinis prekių kiekis` | Final item count |
| `Galutine prekiu verte` | Final total value |

With correctly serialized operations, both final values would be zero. An unsafe run can also print zero, so one successful-looking run does not prove synchronization. Results are scheduling-dependent; no particular nonzero result is promised.

The source includes a commented `synchronized (wh)` alternative around the add/remove pair. It remains commented in `Main.java`. Enabling it is a separate manual exercise; the published program has no `safe` flag or alternate mode.

## Files

- `Main.java`: thread startup/join, warehouse model and the intentional race.
