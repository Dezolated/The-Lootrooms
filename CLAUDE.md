# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A Fabric mod for **Minecraft 1.20.1 / Java 17** — "SP-Backrooms Revamped" (a Backrooms / "Found Footage" horror mod). Note the discrepancy: the repo directory is `The-Lootrooms`, but the mod id is `spb-revamped`, the Maven group is `com.sp`, and all packages live under `com.sp`. Current version is in `gradle.properties` (`mod_version`).

## Commands

Use the Gradle wrapper (`./gradlew` on Unix, `gradlew.bat` on Windows).

- `./gradlew build` — compile + produce the remapped mod jar in `build/libs/`
- `./gradlew runClient` — launch a dev Minecraft client with the mod
- `./gradlew runServer` — launch a dev dedicated server
- `./gradlew runDatagen` — run data generation (entry point `com.sp.SPBRevampedDataGenerator`; outputs to `src/main/generated/`)
- `./gradlew genSources` — decompile/generate Minecraft sources for navigation

There is no test suite. Verification is done by running the client (`runClient`) and exercising behavior in-game. Running in a development environment auto-enables IK debug renderers (see `SPBRevamped.onInitialize`).

### Hard compatibility constraints
- **macOS is unsupported** — the mod logs an error and shows a toast on Mac.
- **Incompatible with Sodium, Iris, OptiFine, Lithium, Phosphor, Indium, Create, Modern Industrialization** (declared in `breaks` in `fabric.mod.json`). The rendering stack depends on Veil's deferred renderer, which these mods conflict with.

## Key dependencies (and why they matter)

Versions are pinned in `gradle.properties`; repositories and `include`/`modImplementation` wiring are in `build.gradle`.

- **Veil** — the entire custom rendering pipeline (deferred rendering, post-processing, custom shaders, dynamic lights, shadow maps). Most of `SPBRevampedClient` and the `render/` package is built on Veil APIs.
- **Cardinal Components API (CCA)** — the primary mechanism for persistent + auto-synced game state (per-player, per-world, per-entity). See `cca_stuff/`.
- **GeckoLib** — entity model animation; integrated with a custom IK system.
- **Simple Voice Chat** — proximity voice; the skinwalker mechanic depends on who is speaking (`BackroomsVoicechatPlugin`).
- **MidnightLib** — config (`compat/modmenu/ConfigStuff`), surfaced through **Mod Menu**.

## Architecture

### Entry points (`fabric.mod.json`)
- `com.sp.SPBRevamped` (main) — server/common init: registers blocks, items, sounds, entities, packets, commands, gamerules, levels, and server-side world-change / respawn event hooks.
- `com.sp.SPBRevampedClient` (client) — all rendering: registers renderers, key binds, block render layers, and (crucially) hooks into Veil's render-stage and post-processing events to drive the VHS look, shadow maps, grass/bird compute shaders, flashlight, and per-level shader definitions.
- `com.sp.cca_stuff.InitializeComponents` — registers CCA components.
- `com.sp.SPBRevampedDataGenerator` — datagen.

Registration helper classes live in `com.sp.init` (`ModBlocks`, `ModItems`, `ModEntities`, `ModSounds`, `ModBlockEntities`, `BackroomsLevels`, etc.) and are called from `onInitialize`.

### The Levels system (the core abstraction)
The mod's "Backrooms levels" are custom dimensions. The central abstraction is the abstract class `com.sp.world.levels.BackroomsLevel`:
- Each level is a **singleton subclass** under `world/levels/custom/` (e.g. `Level0BackroomsLevel`, `PoolroomsBackroomsLevel`, `Level324Backroomslevel`).
- Every level is instantiated and registered in `com.sp.init.BackroomsLevels` (`init()` adds it to `BACKROOMS_LEVELS` and calls `register()`, which registers its `ChunkGenerator` codec).
- A level binds together: a `levelId`, a `ChunkGenerator` codec, a spawn position, and a `RegistryKey<World>`. The dimension itself is **data-driven JSON** in `data/spb-revamped/dimension/` and `dimension_type/`.
- A level controls: lighting behavior (`hasVanillaLighting`), sky/cloud rendering, whether the flashlight is allowed, NBT save/load (`writeToNbt`/`readFromNbt`), registered **events** (random ambient horror occurrences) and **transitions** (criteria-based teleports to other levels), plus `transitionIn`/`transitionOut` hooks.
- `BackroomsLevelWithLights` extends this with a `LightState` system (ON/OFF/FLICKER) used by light blocks and events.
- `WorldRepresentingBackroomsLevel` / `vanilla_representing/OverworldRepresentingBackroomsLevel` represent non-backrooms vanilla worlds so the same APIs work everywhere; `BackroomsLevels.isInBackrooms(...)` excludes these.

Lookups go through static helpers on `BackroomsLevels` (`getLevel(World)`, `getById`, `isInBackroomsLevel`, `getCurrentLevelsOrigin`). The `BackroomsLevels.definitions` map ties a level's world key to a Veil shader pre-definition string toggled client-side.

**To add a level:** create a `BackroomsLevel` subclass, register it in `BackroomsLevels.init()`, add `dimension/` + `dimension_type/` JSON, add a chunk generator + codec under `world/generation/chunk_generator/`, and (if it needs level-specific shaders or rendering) wire it into the `definitions` map and the per-level branches in `SPBRevampedClient`.

### World generation
`world/generation/chunk_generator/` holds one `ChunkGenerator` per level. The procedural Backrooms layouts come from `world/generation/maze_generator/` (`MazeGenerator` + per-level subclasses operating on `MazeCell`s). Levels are largely assembled from NBT structures in `data/spb-revamped/structures/<level>/`.

### State & sync (CCA components, `cca_stuff/`)
- `PlayerComponent` (per-player, `ALWAYS_COPY` across respawn) — flashlight state, skinwalker capture/release state, cutscene flags, render/static flags, teleport state, etc.
- `WorldEvents` (per-world, **server-ticking**) — the per-dimension heartbeat. Drives the random-event scheduler (`tickWorldEvents`), the Level 0 skinwalker capture/release state machine, and per-level NBT save + client sync (it delegates `readFromNbt`/`writeToNbt`/`shouldSync` to the current `BackroomsLevel`).
- `SkinWalkerComponent`, `SmilerComponent` (per-entity).

Components auto-sync via CCA; one-shot client effects go through packets instead.

### Networking (`networking/`)
`InitializePackets` registers all C2S (`C2S/`) and S2C (`S2C/`) packets. `SPBRevamped` exposes static senders for common S2C effects (`sendBlackScreenPacket`, `sendCameraShakePacket`, `sendPersonalPlaySoundPacket`, `sendLevelTransitionLightsOutPacket`). `networking/callbacks/` holds client connection lifecycle hooks.

### Rendering (`render/` + `SPBRevampedClient`)
Built on Veil. The signature "found footage" look is a stack of post-processing pipelines (`vhs`, `ssao`, `glitch`, motion blur via `everything`/`mixed`/`vhs_post` shaders) toggled and fed uniforms in `SPBRevampedClient`'s `onVeilRenderTypeStageRender` / `preVeilPostProcessing` callbacks. Also here: custom shadow maps (`ShadowMapRenderer`), compute-shader grass (`render/grass`) and bird flocking (`render/bird`), flashlight (`FlashlightRenderer`), a PBR material/id registry (`render/pbr`, registered on resource reload), camera shake & cutscenes (`render/camera`), and HUD (`render/gui`). Per-level shader behavior is selected with Veil `ShaderPreDefinitions` driven by `BackroomsLevels.definitions` and `ConfigDefinitions`.

### Mixins & access widener
Mixins are declared in `spb-revamped.mixins.json` and organized into feature sub-packages under `com.sp.mixin` (e.g. `collision`, `cutscene`, `pbr`, `lightshadows`, `rain`, `respawnsystem`, `stamina`, `skinstolen`, `soundphysicsremasteredcompat`). `common` mixins are top-level; client-only mixins are under the `client` list. `spb-revamped.accesswidener` (referenced from `build.gradle` `loom {}` and `fabric.mod.json`) widens many vanilla internals the renderer and mixins need.

### Entities (`entity/`)
Custom mobs: `SkinWalkerEntity`, `SmilerEntity`, `WalkerEntity` (`entity/custom/`). AI in `entity/ai/` (goals + a node-based pathing helper). A custom inverse-kinematics rig lives under `entity/ik/` (chains, limbs, debug renderers) and integrates with GeckoLib via `MowzieModelFactory`.

### Commands (`command/`)
`/event` (`EventCommand`), `/level` (`LevelCommand`, level teleport/transition), `GimmeMyInventoryBack`, and a skinwalker command — registered via `CommandRegistrationCallback` in `onInitialize`.
