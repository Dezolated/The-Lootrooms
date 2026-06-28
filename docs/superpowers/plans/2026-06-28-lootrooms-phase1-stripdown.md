# Lootrooms Phase 1 — Strip-Down Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Strip SP-Backrooms Revamped down to a clean, launchable base for a co-op PvE extraction looter — keeping the levels, all rendering (VHS off by default), cinematics, and frameworks, while removing all enemies, the skinwalker mechanic, Level 324, and horror-specific content.

**Architecture:** Surgical deletion in place on the existing `com.sp` / `spb-revamped` codebase. There is **no test suite**; the safety net is the Java compiler (`./gradlew build`) plus a manual `runClient` smoke test. Each task removes one coherent system end-to-end, finishes with a green build and a commit, and uses `grep` to prove no dangling references remain. The compiler is the source of truth for completeness — if a task's listed reference sites miss something, the build error names it; fix and re-run before committing.

**Tech Stack:** Fabric 1.20.1, Java 17, Gradle (Fabric Loom), Yarn mappings, Veil, Cardinal Components, GeckoLib, Simple Voice Chat, MidnightLib.

## Global Constraints

- **Branch:** all work on `phase1-stripdown` (already created off `master`).
- **Build command (the "test"):** `./gradlew build` must succeed after every task. Use `./gradlew runClient` only in the final verification task.
- **Keep these dependencies** (do not remove from `build.gradle`/`fabric.mod.json`): Fabric API, Veil, Cardinal Components, MidnightLib, Mod Menu, **GeckoLib** (future enemies), **Simple Voice Chat** (future party-talk), Sound Physics Remastered / Essential `modCompileOnly` compat (leave untouched).
- **Do NOT touch the rendering pipeline:** no edits to `assets/spb-revamped/pinwheel/**` (shaders, `post/*.json`, framebuffers). The VHS look is disabled via config default only.
- **Keep all sounds** except Level 324 sound files (physically deleted in Task 3). Unused `SoundEvent` registrations are harmless; do not prune `ModSounds.java` or `sounds.json` beyond Level 324 entries.
- **Mod id / package stay** `spb-revamped` / `com.sp`. Rebrand is out of scope (deferred).
- After deleting any class, run `grep -rn "<ClassName>" src/main` and confirm only the deletion remains; fix any straggler before building.
- Commit messages end with the project's required trailer:
  `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>`

---

### Task 1: Disable the VHS effect by default

**Files:**
- Modify: `src/main/java/com/sp/compat/modmenu/ConfigStuff.java` (the two VHS boolean defaults, ~lines 56 and 59)

**Interfaces:**
- Consumes: nothing.
- Produces: `ConfigStuff.enableVhsEffect == false`, `ConfigStuff.enableVhsEffectInTheBackrooms == false` as shipped defaults. Read by `SPBRevampedClient.shouldRenderCameraEffect()` (unchanged).

- [ ] **Step 1: Locate the flags.** Run `grep -n "enableVhsEffect" src/main/java/com/sp/compat/modmenu/ConfigStuff.java`. Confirm two fields: `enableVhsEffect` and `enableVhsEffectInTheBackrooms`, both `= true`.

- [ ] **Step 2: Flip both defaults to `false`.** Change `public static boolean enableVhsEffect = true;` → `= false;` and `public static boolean enableVhsEffectInTheBackrooms = true;` → `= false;`. Leave the field names, annotations, and everything else intact (the knobs remain, just off by default).

- [ ] **Step 3: Build.** Run `./gradlew build`. Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Commit.**
```bash
git add src/main/java/com/sp/compat/modmenu/ConfigStuff.java
git commit -m "Disable VHS post-processing by default

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 2: Remove the respawn "YOU CAN'T ESCAPE" sequence

**Files:**
- Modify: `src/main/java/com/sp/SPBRevamped.java` (the `ServerPlayerEvents.AFTER_RESPAWN.register(...)` block, ~lines 95–129)

**Interfaces:**
- Consumes: nothing.
- Produces: no behavioral export. The helper methods `sendBlackScreenPacket`, `sendPersonalPlaySoundPacket`, `sendLevelTransitionLightsOutPacket`, `sendCameraShakePacket` **remain** (used by kept level transitions and the entry cinematic). `ModSounds.NO_ESCAPE` **remains** (used by `CastToTheBackroomsCommand`).

- [ ] **Step 1: Locate the block.** Run `grep -n "AFTER_RESPAWN" src/main/java/com/sp/SPBRevamped.java`. Confirm one `ServerPlayerEvents.AFTER_RESPAWN.register((...) -> { ... });` lambda (the one that calls `sendBlackScreenPacket(..., true)` and plays `ModSounds.NO_ESCAPE`, using `ScheduledExecutorService`).

- [ ] **Step 2: Delete the entire `AFTER_RESPAWN.register(...)` statement** (from `ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {` through its closing `}));`). Do **not** remove the helper method definitions lower in the file. Remove now-unused imports only if the compiler flags them (e.g. `ServerPlayerEvents`, `Executors`, `ScheduledExecutorService`, `TimeUnit`, `PlaySoundS2CPacket`, `RegistryEntry`, `SoundCategory`) — check each is unused elsewhere in the file first.

- [ ] **Step 3: Build.** Run `./gradlew build`. Expected: `BUILD SUCCESSFUL`. Fix any unused-import errors the compiler reports.

- [ ] **Step 4: Commit.**
```bash
git add src/main/java/com/sp/SPBRevamped.java
git commit -m "Remove respawn horror sequence (YOU CAN'T ESCAPE)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 3: Remove Level 324 entirely

**Files:**
- Delete (Java): `world/levels/custom/Level324Backroomslevel.java`, `world/generation/chunk_generator/Level324ChunkGenerator.java`, `world/events/level324/ScreechSoundEvent.java` (and the now-empty `world/events/level324/` dir)
- Delete (resources): `data/spb-revamped/dimension/level324.json`, `data/spb-revamped/dimension_type/level324.json`, `data/spb-revamped/save/dimension/level324.json`, `data/spb-revamped/worldgen/biome/level324.json`, `data/spb-revamped/structures/level324/` (whole dir), `assets/spb-revamped/sounds/level324/` (whole dir)
- Modify: `init/BackroomsLevels.java`, `SPBRevampedClient.java`, `command/EventCommand.java`, `world/levels/custom/Level1BackroomsLevel.java`, `cca_stuff/PlayerComponent.java`, `render/grass/GrassRenderer.java`, `mixin/rain/AlwaysRainLevelPropertiesMixin.java`, `mixin/rain/NoRainParticlesWorldRendererMixin.java`, `mixin/rain/ServerWorldNoHorseTrapMixin.java`, `mixin/rain/LightningSkyLightingUpLightningEntity.java`, `sounds/InfiniteGrassAmbienceSoundInstance.java`, `clientWrapper/ClientWrapper.java`, `render/bird/FlockManager.java`, `assets/spb-revamped/sounds.json`

**Interfaces:**
- Consumes: nothing.
- Produces: `BackroomsLevels` no longer declares `LEVEL324_WORLD_KEY` or `LEVEL324_BACKROOMS_LEVEL`; the `definitions` map has no `LEVEL324` key.

- [ ] **Step 1: Survey.** Run `grep -rn "324" src/main/java src/main/resources` and skim. This is the master checklist; every hit is removed or in a deletable file by the end of this task.

- [ ] **Step 2: Delete the Level 324 Java files** listed above. Then `grep -rn "Level324\|level324\|LEVEL324" src/main/java` to see remaining reference sites in surviving files.

- [ ] **Step 3: Clean `init/BackroomsLevels.java`.** Remove: the `LEVEL324_WORLD_KEY` constant, the `LEVEL324_BACKROOMS_LEVEL` constant, the `BACKROOMS_LEVELS.add(LEVEL324_BACKROOMS_LEVEL);` line in `init()`, the `"LEVEL324", LEVEL324_WORLD_KEY,` entry in the `definitions` map, and the `Level324Backroomslevel` import.

- [ ] **Step 4: Clean `SPBRevampedClient.java`.** Remove the `else if (clientWorld.getRegistryKey() == BackroomsLevels.LEVEL324_WORLD_KEY) { ... grassRenderer.render(); }` branch (~lines 255–262) and the commented `Level324Backroomslevel` reference in the bird-tick `ifPresent` (~line 567).

- [ ] **Step 5: Clean the remaining reference sites** (use the grep from Step 2 as the worklist):
  - `command/EventCommand.java` — remove the `else if (registryKey == BackroomsLevels.LEVEL324_WORLD_KEY) {...}` flicker branch.
  - `world/levels/custom/Level1BackroomsLevel.java` — remove the commented Level 324 transition block and the `getLevel324Transition()` method.
  - `cca_stuff/PlayerComponent.java` — remove the `Level324Backroomslevel` import; drop `Level324Backroomslevel` from the `isTeleportingToPoolrooms()` condition; remove the two `LEVEL324_BACKROOMS_LEVEL` wool-teleport blocks; remove the `LEVEL324` `summonSmilers()` trigger block. (Leave the `summonSmilers()` method itself — it is deleted in Task 5.)
  - `render/grass/GrassRenderer.java` — remove the `isInLevel(LEVEL324_BACKROOMS_LEVEL)` grass-height branch.
  - `mixin/rain/AlwaysRainLevelPropertiesMixin.java`, `mixin/rain/NoRainParticlesWorldRendererMixin.java`, `mixin/rain/ServerWorldNoHorseTrapMixin.java`, `mixin/rain/LightningSkyLightingUpLightningEntity.java` — remove every `LEVEL324`/`isInBackroomsLevel(... LEVEL324 ...)` check (keep the surrounding behavior for the remaining levels).
  - `sounds/InfiniteGrassAmbienceSoundInstance.java` — drop `&& level != BackroomsLevels.LEVEL324_WORLD_KEY` from the early-return condition.
  - `clientWrapper/ClientWrapper.java` — remove the four Level 324 blocks (the glitch/green-wool condition, the green-wool glitch-tick block, and the two `LEVEL324_WORLD_KEY` wind-ambience blocks).
  - `render/bird/FlockManager.java` — remove the commented Level 324 blocks.

- [ ] **Step 6: Delete Level 324 resources** (all files/dirs listed under "Delete (resources)" above), and remove the `"wind_tunnel_ambience"` entry from `assets/spb-revamped/sounds.json`.

- [ ] **Step 7: Prove it's gone.** Run `grep -rn "324" src/main/java src/main/resources`. Expected: no matches (or only unrelated numeric coincidences — verify each). Resolve anything real.

- [ ] **Step 8: Build.** Run `./gradlew build`. Expected: `BUILD SUCCESSFUL`. Fix any straggler the compiler names.

- [ ] **Step 9: Commit.**
```bash
git add -A
git commit -m "Remove Level 324 entirely

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 4: Remove horror-specific events (intercom, music, Smiler blackout)

**Files:**
- Delete: `world/events/level0/Level0IntercomBasic.java`, `world/events/level0/Level0Music.java`, `world/events/level1/Level1Blackout.java`
- Modify: `world/levels/custom/Level0BackroomsLevel.java`, `world/levels/custom/Level1BackroomsLevel.java`

**Interfaces:**
- Consumes: nothing.
- Produces: Level 0 registers only `blackout` (`LightLevelBlackout`) and `flicker` (`LightLevelFlicker`); Level 1 registers only `flicker` (`LightLevelFlicker`) and `ambience` (`Level1Ambience`). Generic light/ambience events under `world/events/generic/**` and the other levels' ambience/warp/sunset events are untouched.

- [ ] **Step 1: Remove registrations in `Level0BackroomsLevel.java`.** In `register()`, delete `this.registerEvent("intercom", Level0IntercomBasic::new);` and `this.registerEvent("music", Level0Music::new);`. Remove their imports.

- [ ] **Step 2: Remove registration in `Level1BackroomsLevel.java`.** In `register()`, delete `this.registerEvent("blackout", Level1Blackout::new);` (it spawns a Smiler). Keep the `flicker` and `ambience` registrations. Remove the `Level1Blackout` import.

- [ ] **Step 3: Delete the three event files** listed above.

- [ ] **Step 4: Prove it's gone.** Run `grep -rn "Level0IntercomBasic\|Level0Music\|Level1Blackout" src/main`. Expected: no matches.

- [ ] **Step 5: Build.** Run `./gradlew build`. Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: Commit.**
```bash
git add -A
git commit -m "Remove horror-specific events (intercom, music, Smiler blackout)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 5: Remove all enemies and the skinwalker mechanic (atomic)

This is the large, atomic removal: enemies, AI, IK, entity renderers/models, CCA entity components, the skinwalker capture state machine, jumpscare, skinwalker networking/command/mixins, and the voicechat plugin's skinwalker logic. Because AI goals read `PlayerComponent` capture fields and entities hold their components, these cannot be split into separately-compiling sub-tasks — do it as one sweep and let `./gradlew build` find any straggler.

**Files:**
- Delete (whole trees/files): `entity/` (entire package — `custom/`, `ai/`, `ik/`, `client/`), `cca_stuff/SkinWalkerComponent.java`, `cca_stuff/SmilerComponent.java`, `render/SkinwalkerJumpscare.java`, `command/SkinwalkerCommand.java`, `networking/C2S/SeeActiveSkinwalkerSync.java`, `networking/C2S/TargetEntitySync.java`, `sounds/entity/SkinWalkerChaseSoundInstance.java`, `mixin/skinstolen/` (entire dir: `ChatScreenMixin`, `GameMenuScreenMixin`, `MinecraftClientMixin`, `ServerPlayerEntityMixin`), `init/ModEntities.java`, `init/ModModelLayers.java`
- Modify: `SPBRevamped.java`, `SPBRevampedClient.java`, `cca_stuff/InitializeComponents.java`, `cca_stuff/PlayerComponent.java`, `cca_stuff/WorldEvents.java`, `networking/InitializePackets.java`, `sounds/voicechat/BackroomsVoicechatPlugin.java`, `clientWrapper/ClientWrapper.java`, `mixin/BodyControlMixin.java`, `mixin/LivingEntityRendererMixin.java`, `resources/spb-revamped.mixins.json`, `resources/fabric.mod.json`

**Interfaces:**
- Consumes: nothing.
- Produces: no entities registered; `InitializeComponents` exposes only `PLAYER` and `EVENTS`; `BackroomsVoicechatPlugin` is a minimal stub (registration only, no skinwalker logic); GeckoLib dependency retained but `MowzieModelFactory` (custom-model factory) no longer registered.

- [ ] **Step 1: Remove wiring in `SPBRevamped.java`.** Delete the two `FabricDefaultAttributeRegistry.register(ModEntities...)` lines, the `GeckoLibUtil.addCustomBakedModelFactory(MOD_ID, new MowzieModelFactory());` line (keep `GeckoLib.initialize();` and the GeckoLib dependency), the `CommandRegistrationCallback.EVENT.register(SkinwalkerCommand::register);` line, and the related imports (`SkinWalkerEntity`, `SmilerEntity`, `MowzieModelFactory`, `SkinwalkerCommand`). Also remove the `PrAnCommonClass` dev-flag block in `onInitialize()` and its imports if `PrAnCommonClass` lives under the deleted `entity/ik/util` (it does) — the IK debug toggles go with the IK system. Keep `GeckoLib.initialize()`.

- [ ] **Step 2: Remove wiring in `SPBRevampedClient.java`.** Delete: the three `EntityRendererRegistry.register(...)` lines, the `EntityModelLayerRegistry.registerModelLayer(ModModelLayers.SMILER, ...)` line, the active-skinwalker frustum/visibility block (the `Entity activeSkinwalker = events.getActiveSkinwalkerTarget(); ...` block), and the jumpscare `if (playerComponent.isBeingCaptured()) { SkinwalkerJumpscare.doJumpscare(...); } else { ... }` — **keep the `else` body** (the four `setInt/setVector` zeroing lines) as the unconditional path. Remove the now-unused imports (`SmilerModel`, `SkinWalkerRenderer`, `SmilerRenderer`, `WalkerRenderer`, `SkinwalkerJumpscare`).

- [ ] **Step 3: Trim `InitializeComponents.java`.** Remove the `SKIN_WALKER` and `SMILER` `ComponentKey` fields, their `registry.registerFor(...)` lines in `registerEntityComponentFactories`, and the `SkinWalkerEntity`/`SmilerEntity` imports. Keep `PLAYER` and `EVENTS`.

- [ ] **Step 4: Trim `PlayerComponent.java`.** Remove the capture/skinwalker fields and their accessors: `isBeingCaptured`, `hasBeenCaptured`, `isBeingReleased`, `targetEntity`, `skinWalkerLookDelay`, `canSeeActiveSkinWalker(Target)`, `prevGameMode`. Remove the corresponding NBT keys in `readFromNbt`/`writeToNbt`. Delete the `summonSmilers()` method and the `SmilerEntity` import. Keep flashlight, stamina, noclip, `isDoingCutscene`, `teleportingTimer`/`isTeleporting`, `suffocationTimer`, `shouldRender`/`shouldDoStatic`, speaking/`shouldBeMuted`, glitch-timer, and `currentTransition` fields.

- [ ] **Step 5: Trim `WorldEvents.java`.** Remove the skinwalker state machine: fields `activeSkinwalkerTarget`, `activeSkinWalkerEntity`, `done`, `tick`; methods `getActiveSkinwalkerTarget()`, `setActiveSkinwalkerTarget()`, `shouldReleasePlayer()`, `tickSkinWalkerCapturing()`; the two calls to those from `serverTick()`; the skinwalker NBT keys in `readFromNbt`/`writeToNbt`; and the `SkinWalkerEntity` import. Keep `tickWorldEvents()` (event scheduler), `shouldSync()`, `sync()`, and the per-level NBT delegation.

- [ ] **Step 6: Trim `networking/InitializePackets.java`.** Remove the `TARGET_ENTITY_SYNC` and `SEE_SKINWALKER_SYNC` identifiers and their `registerGlobalReceiver(...)` registrations, plus imports of the two deleted C2S packet classes.

- [ ] **Step 7: Replace `BackroomsVoicechatPlugin.java` with a minimal stub.** Keep the dependency and entrypoint working but remove all skinwalker/speaking-time/capture/pitch logic and the `SkinWalkerEntity` import. Resulting file:
```java
package com.sp.sounds.voicechat;

import com.sp.SPBRevamped;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;

/**
 * Minimal Simple Voice Chat plugin. Skinwalker logic removed in the Phase 1
 * strip-down; this is the base for a future party-talk feature.
 */
public class BackroomsVoicechatPlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return SPBRevamped.MOD_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
    }
}
```
If any kept code references removed statics like `BackroomsVoicechatPlugin.speakingTime` (the build will say so), remove those reference sites — they belong to the skinwalker mechanic.

- [ ] **Step 8: Clean `clientWrapper/ClientWrapper.java`.** Remove all remaining references to `SkinWalkerEntity`, `SmilerEntity`, `SkinWalkerCapturedFlavorText`, `SkinWalkerChaseSoundInstance`, and skinwalker capture state (imports, the Smiler visibility methods, chase-sound handling, captured-flavor-text handling). Use `grep -n "SkinWalker\|Smiler\|Walker\|CapturedFlavor" src/main/java/com/sp/clientWrapper/ClientWrapper.java` to enumerate, and remove each block. Keep all flashlight/cutscene/level-ambience/sound logic for kept levels.

- [ ] **Step 9: Handle the two entity-referencing mixins.**
  - `mixin/BodyControlMixin.java` — its purpose is special-casing `SkinWalkerEntity` body control. Open it; if it does nothing for non-skinwalker entities, delete the file and remove `"BodyControlMixin"` from `spb-revamped.mixins.json`. If it has other behavior, remove only the `instanceof SkinWalkerEntity` branch.
  - `mixin/LivingEntityRendererMixin.java` — special-cases `SmilerEntity` opacity. Same decision: if Smiler-only, delete the file + its `mixins.json` entry; otherwise remove just the Smiler branch and the `SmilerEntity`/`SmilerComponent` imports.

- [ ] **Step 10: Delete the class trees/files** listed under "Delete" above (entity package, components, jumpscare, command, skinwalker packets, chase sound, skinstolen mixins, `ModEntities`, `ModModelLayers`). Then remove the `skinstolen.*` entries (and any deleted-mixin entries from Step 9) from `spb-revamped.mixins.json`.

- [ ] **Step 11: Clean `fabric.mod.json`.** Remove the `"spb-revamped:skw"` and `"spb-revamped:smi"` entries from the `custom.cardinal-components` list. Keep the `cardinal-components` and `voicechat` entrypoints.

- [ ] **Step 12: Prove it's gone.** Run `grep -rn "com.sp.entity\|SkinWalker\|Skinwalker\|SmilerEntity\|WalkerEntity\|ModEntities\|ModModelLayers\|MowzieModelFactory\|PrAnCommonClass" src/main`. Expected: no matches.

- [ ] **Step 13: Build.** Run `./gradlew build`. Expected: `BUILD SUCCESSFUL`. The compiler will name any reference the steps missed — remove it (it belongs to a cut system) and rebuild until green.

- [ ] **Step 14: Commit.**
```bash
git add -A
git commit -m "Remove all enemies and the skinwalker mechanic

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 6: Full verification, launch smoke test, and docs update

**Files:**
- Modify: `CLAUDE.md` (reflect removed systems so future sessions aren't misled)

**Interfaces:**
- Consumes: a green build from Task 5.
- Produces: a verified, launchable Phase 1 base.

- [ ] **Step 1: Clean build.** Run `./gradlew clean build`. Expected: `BUILD SUCCESSFUL` with no warnings about missing mixin targets.

- [ ] **Step 2: Launch.** Run `./gradlew runClient`. Expected: reaches the main menu without crashing; the log shows the Simple Voice Chat plugin loading and **no** errors about missing dimensions, entities, components, or mixin targets.

- [ ] **Step 3: In-game checks.** Create/open a world. For **each** retained level, run `/level` (the `LevelCommand`) to teleport in and confirm it is lit and walkable:
  - `level0`, `level1`, `level2`, `poolrooms`, `infinite_field`.
  - For each: flashlight toggles and lights the area; fluorescent lights glow; shadows render; Level 1 shows its fog. No console errors on entry.

- [ ] **Step 4: Negative checks.** Confirm: no enemies spawn anywhere; no skinwalker behavior; Level 324 is absent (`/level level324` fails / the dimension does not exist); the VHS grain/glitch is **off** by default. Then set `enableVhsEffectInTheBackrooms = true` in the in-game Mod Menu config (or `ConfigStuff`) and confirm the VHS look returns — proving the rendering path is intact, not deleted. Restore it to off.

- [ ] **Step 5: Entry cinematic.** Trigger the existing backrooms-entry path and confirm the fall-into-the-backrooms cinematic still plays (it was not modified). If the entry trigger is not wired to a command in this build, note that and confirm `CutsceneManager` is intact and uncompiled-away.

- [ ] **Step 6: Update `CLAUDE.md`.** Edit the architecture notes to reflect Phase 1: remove/curtail the "Entities" section (no enemies now; note the roster is to be rebuilt), note the skinwalker mechanic and Level 324 are gone, note the VHS effect ships disabled, and note the project's new direction (extraction looter — point to `docs/superpowers/specs/2026-06-28-lootrooms-extraction-base-design.md`).

- [ ] **Step 7: Commit.**
```bash
git add -A
git commit -m "Verify Phase 1 strip-down and update CLAUDE.md

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

## Self-Review notes

- **Spec coverage:** §4 CUT items map to tasks — enemies/IK/skinwalker → Task 5; Level 324 → Task 3; horror events → Task 4; respawn sequence → Task 2; VHS-off → Task 1. §5 EDIT files (`PlayerComponent`, `WorldEvents`, `SPBRevamped*`, voicechat stub, `ConfigStuff`, `mixins.json`, `fabric.mod.json`) are all covered. §6 verification → Task 6.
- **Deviations from spec (intentional, lower risk):** (1) `NO_ESCAPE` and the skinwalker/Smiler/Walker `SoundEvent` registrations are **kept** — `NO_ESCAPE` is used by the kept entry cinematic, and unused sound events are harmless; only Level 324 sound files are deleted. (2) Sound Physics / Essential compat left untouched (per user). (3) `ModSounds`/`sounds.json` pruning beyond Level 324 is deferred as cosmetic cleanup.
- **Completeness oracle:** every task ends with a `grep` proof + `./gradlew build`; line numbers from the reference map may drift, so each task greps by symbol rather than trusting line numbers, and relies on the compiler to surface anything missed.
