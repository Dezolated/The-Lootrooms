# The Lootrooms

A Fabric mod for **Minecraft 1.20.1** (Java 17). A co-op PvE **extraction looter**
built on the Backrooms levels: enter the backrooms at an escalating difficulty,
loot and fight your way down, and extract — or lose what you carried.

> **Status:** in active development. The mod id is still `spb-revamped` and the
> package is still `com.sp`; a rebrand to a Lootrooms identity is planned for
> later. See `CLAUDE.md` for architecture notes.

## Attribution — this is a fork

The Lootrooms is a **derivative work** of
[**SP-Backrooms Revamped**](https://github.com/SpacePotatoee/SPBackrooms-Revamped)
("Minecraft Found Footage") by **SpacePotato**, **Herr Chaos**, and
**MetaOne** (Russian translation). Full credit for the original mod — its
Backrooms levels, world generation, and Veil-based rendering — goes to them.

**Modifications by Dezolated, beginning June 2026:** the mod is being
repurposed from a Backrooms horror experience into a co-op PvE
extraction looter. Notably, the custom enemies, the IK system, the skinwalker
mechanic, Level 324, and the horror-specific events have been removed to
establish a clean base, and the project is being rebuilt around a run /
extraction / loot gameplay loop. See the git history for the complete record
of changes.

## License

The Lootrooms is licensed under the **GNU General Public License v3.0**
(`GPL-3.0-only`), the same license as the upstream project. See [`LICENSE`](LICENSE).

As a GPL-3.0 work: the complete source is available in this repository, you are
free to use, study, modify, and redistribute it under the same license, and any
distributed derivative must also remain under GPL-3.0.

## Building

Use the Gradle wrapper:

```
./gradlew build       # produce the remapped mod jar in build/libs/
./gradlew runClient   # launch a dev client
```

This mod's rendering depends on **Veil** and is **incompatible** with Sodium,
Iris, OptiFine, Lithium, Phosphor, Indium, Create, and Modern Industrialization
(see `breaks` in `fabric.mod.json`). **macOS is unsupported.**
