# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A Fabric mod for **Minecraft 1.20.1 / Java 17** — "SP-Backrooms Revamped" (originally a Backrooms / "Found Footage" horror mod, now being repurposed into a **co-op PvE extraction looter**). See `docs/superpowers/specs/2026-06-28-lootrooms-extraction-base-design.md` for the new direction. **Phase 1** (branch `phase1-stripdown`) stripped the mod to a clean base: all custom enemies, the IK system, the skinwalker mechanic, Level 324, and the horror-specific events (intercom/music) have been removed (the generic event scheduler and light/ambience events remain). Note the discrepancy: the repo directory is `The-Lootrooms`, but the mod id is `spb-revamped`, the Maven group is `com.sp`, and all packages live under `com.sp`. Current version is in `gradle.properties` (`mod_version`).

## Commands

Use the Gradle wrapper (`./gradlew` on Unix, `gradlew.bat` on Windows).

- `./gradlew build` — compile + produce the remapped mod jar in `build/libs/`
- `./gradlew runClient` — launch a dev Minecraft client with the mod
- `./gradlew runServer` — launch a dev dedicated server
- `./gradlew runDatagen` — run data generation (entry point `com.sp.SPBRevampedDataGenerator`; outputs to `src/main/generated/`)
- `./gradlew genSources` — decompile/generate Minecraft sources for navigation

There is no test suite. Verification is done by running the client (`runClient`) and exercising behavior in-game. (The IK debug renderers and `PrAnCommonClass` dev helpers were removed in Phase 1.)

### Hard compatibility constraints

- **macOS is unsupported** — the mod logs an error and shows a toast on Mac.
- **Incompatible with Sodium, Iris, OptiFine, Lithium, Phosphor, Indium, Create, Modern Industrialization** (declared in `breaks` in `fabric.mod.json`). The rendering stack depends on Veil's deferred renderer, which these mods conflict with.

## Key dependencies (and why they matter)

Versions are pinned in `gradle.properties`; repositories and `include`/`modImplementation` wiring are in `build.gradle`.

- **Veil** — the entire custom rendering pipeline (deferred rendering, post-processing, custom shaders, dynamic lights, shadow maps). Most of `SPBRevampedClient` and the `render/` package is built on Veil APIs.
- **Cardinal Components API (CCA)** — the primary mechanism for persistent + auto-synced game state (per-player, per-world, per-entity). See `cca_stuff/`.
- **GeckoLib** — entity model animation. The custom IK system that previously integrated with it was removed in Phase 1; GeckoLib is kept for future enemy models.
- **Simple Voice Chat** — proximity voice. The skinwalker dependency on speaker identity is gone; `BackroomsVoicechatPlugin` is now a minimal stub kept as a placeholder for a future party-talk feature.
- **MidnightLib** — config (`compat/modmenu/ConfigStuff`), surfaced through **Mod Menu**.

## Architecture

### Entry points (`fabric.mod.json`)

- `com.sp.SPBRevamped` (main) — server/common init: registers blocks, items, sounds, packets, commands, gamerules, levels, and a server-side world-change hook. (Entity registration and the respawn horror hook were removed in Phase 1.)
- `com.sp.SPBRevampedClient` (client) — all rendering: registers renderers, key binds, block render layers, and (crucially) hooks into Veil's render-stage and post-processing events to drive the VHS look, shadow maps, grass/bird compute shaders, flashlight, and per-level shader definitions.
- `com.sp.cca_stuff.InitializeComponents` — registers CCA components.
- `com.sp.SPBRevampedDataGenerator` — datagen.

Registration helper classes live in `com.sp.init` (`ModBlocks`, `ModItems`, `ModSounds`, `ModBlockEntities`, `BackroomsLevels`, etc.) and are called from `onInitialize`. (`ModEntities` and `ModModelLayers` were removed in Phase 1 — there are no registered entity types at present.)

### The Levels system (the core abstraction)

The mod's "Backrooms levels" are custom dimensions. The central abstraction is the abstract class `com.sp.world.levels.BackroomsLevel`:

- Each level is a **singleton subclass** under `world/levels/custom/` (e.g. `Level0BackroomsLevel`, `PoolroomsBackroomsLevel`). **Level 324 was fully removed in Phase 1** (class, dimension JSON, structures, and sound files deleted).
- Every level is instantiated and registered in `com.sp.init.BackroomsLevels` (`init()` adds it to `BACKROOMS_LEVELS` and calls `register()`, which registers its `ChunkGenerator` codec).
- A level binds together: a `levelId`, a `ChunkGenerator` codec, a spawn position, and a `RegistryKey<World>`. The dimension itself is **data-driven JSON** in `data/spb-revamped/dimension/` and `dimension_type/`.
- A level controls: lighting behavior (`hasVanillaLighting`), sky/cloud rendering, whether the flashlight is allowed, NBT save/load (`writeToNbt`/`readFromNbt`), registered **events** (random ambient occurrences — e.g. light blackout/flicker, ambience) and **transitions** (criteria-based teleports to other levels), plus `transitionIn`/`transitionOut` hooks. (The event _framework_ is intact; Phase 1 removed only the horror-specific events — intercom, music, and the Smiler-spawning blackout.)
- `BackroomsLevelWithLights` extends this with a `LightState` system (ON/OFF/FLICKER) used by light blocks and events.
- `WorldRepresentingBackroomsLevel` / `vanilla_representing/OverworldRepresentingBackroomsLevel` represent non-backrooms vanilla worlds so the same APIs work everywhere; `BackroomsLevels.isInBackrooms(...)` excludes these.

Lookups go through static helpers on `BackroomsLevels` (`getLevel(World)`, `getById`, `isInBackroomsLevel`, `getCurrentLevelsOrigin`). The `BackroomsLevels.definitions` map ties a level's world key to a Veil shader pre-definition string toggled client-side.

**To add a level:** create a `BackroomsLevel` subclass, register it in `BackroomsLevels.init()`, add `dimension/` + `dimension_type/` JSON, add a chunk generator + codec under `world/generation/chunk_generator/`, and (if it needs level-specific shaders or rendering) wire it into the `definitions` map and the per-level branches in `SPBRevampedClient`.

### World generation

`world/generation/chunk_generator/` holds one `ChunkGenerator` per level. The procedural Backrooms layouts come from `world/generation/maze_generator/` (`MazeGenerator` + per-level subclasses operating on `MazeCell`s). Levels are largely assembled from NBT structures in `data/spb-revamped/structures/<level>/`.

### State & sync (CCA components, `cca_stuff/`)

- `PlayerComponent` (per-player, `ALWAYS_COPY` across respawn) — flashlight state, cutscene flags, render/static flags, teleport state, etc. Skinwalker capture/release fields were removed in Phase 1.
- `WorldEvents` (per-world, **server-ticking**) — the per-dimension heartbeat. It still runs the random-event scheduler (`tickWorldEvents`) and per-level NBT save + client sync (delegates `readFromNbt`/`writeToNbt`/`shouldSync` to the current `BackroomsLevel`). The skinwalker capture/release state machine was removed in Phase 1.
- `SkinWalkerComponent`, `SmilerComponent` (per-entity) — **removed in Phase 1** along with the entities themselves.

Components auto-sync via CCA; one-shot client effects go through packets instead.

### Networking (`networking/`)

`InitializePackets` registers all C2S (`C2S/`) and S2C (`S2C/`) packets. `SPBRevamped` exposes static senders for common S2C effects (`sendBlackScreenPacket`, `sendCameraShakePacket`, `sendPersonalPlaySoundPacket`, `sendLevelTransitionLightsOutPacket`). `networking/callbacks/` holds client connection lifecycle hooks.

### Rendering (`render/` + `SPBRevampedClient`)

Built on Veil. The signature "found footage" look is a stack of post-processing pipelines (`vhs`, `ssao`, `glitch`, motion blur via `everything`/`mixed`/`vhs_post` shaders) toggled and fed uniforms in `SPBRevampedClient`'s `onVeilRenderTypeStageRender` / `preVeilPostProcessing` callbacks. **The VHS post-processing renders only inside the backrooms** — `shouldRenderCameraEffect()` gates on `isInBackrooms() && ConfigStuff.enableVhsEffectInTheBackrooms` (the overworld `enableVhsEffect` option was removed in Phase 1.x). Note this toggle is overloaded: it also drives the backrooms deferred lighting (vanilla-light disable) at the `isInBackrooms()` branch in the END_CLIENT_TICK handler, so disabling it also disables the dark-level lighting (the upstream mod flags it "broken" to turn off). Also here: custom shadow maps (`ShadowMapRenderer`), compute-shader grass (`render/grass`) and bird flocking (`render/bird`), flashlight (`FlashlightRenderer`), a PBR material/id registry (`render/pbr`, registered on resource reload), camera shake & cutscenes (`render/camera`), and HUD (`render/gui`). Per-level shader behavior is selected with Veil `ShaderPreDefinitions` driven by `BackroomsLevels.definitions` and `ConfigDefinitions`.

### Mixins & access widener

Mixins are declared in `spb-revamped.mixins.json` and organized into feature sub-packages under `com.sp.mixin` (e.g. `collision`, `cutscene`, `pbr`, `lightshadows`, `rain`, `respawnsystem`, `stamina`, `soundphysicsremasteredcompat`). The `skinstolen` mixin package was removed in Phase 1 along with the skinwalker mechanic. `common` mixins are top-level; client-only mixins are under the `client` list. `spb-revamped.accesswidener` (referenced from `build.gradle` `loom {}` and `fabric.mod.json`) widens many vanilla internals the renderer and mixins need.

### Entities (`entity/`)

**Phase 1: No custom entities.** `SkinWalkerEntity`, `SmilerEntity`, and `WalkerEntity` were all removed, along with their AI goals, the IK rig (`entity/ik/`), the skinwalker capture mechanic, and `MowzieModelFactory` integration. The enemy roster is to be rebuilt in a later phase as part of the extraction-looter redesign.

### Commands (`command/`)

`/event` (`EventCommand`), `/level` (`LevelCommand`, level teleport/transition), `GimmeMyInventoryBack` — registered via `CommandRegistrationCallback` in `onInitialize`. The skinwalker command was removed in Phase 1. Active levels accessible via `/level`: `level0`, `level1`, `level2`, `poolrooms`, `infinite_field` (Level 324 is gone).

### Modding capabilities

You are an expert Minecraft Modding Assistant connected to mcmodding-mcp. DO NOT pnly rely on your internal knowledge for modding APIs (Fabric/NeoForge) as they change frequently. ALWAYS use the available tools:

search_fabric_docs and get_example for documentation and code patterns
search_mappings and get_class_details for Minecraft internals and method signatures
search_mod_examples for battle-tested implementations from popular mods
Prioritize working code examples over theoretical explanations. When dealing with Minecraft internals, use the mappings tools to get accurate parameter names and Javadocs. If the user specifies a Minecraft version, ensure all retrieved information matches that version.
