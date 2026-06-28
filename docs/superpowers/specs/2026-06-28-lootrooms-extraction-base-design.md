# Design: The Lootrooms — Extraction Looter Base (Phase 1 Strip-Down)

**Date:** 2026-06-28
**Status:** Approved for implementation planning
**Scope of this spec:** Phase 1 only — strip the existing SP-Backrooms Revamped mod down to a clean, launchable base for a co-op PvE extraction looter. Phases 2–5 are sketched as a roadmap to justify Phase 1 keep/cut decisions; each gets its own spec later.

---

## 1. Vision (the target game)

A **co-op PvE extraction looter** built on the backrooms levels of this mod.

- Small group plays on a server with **shared run state**.
- A persistent **hub** holds each player's **stash** and their **highest-unlocked key level**.
- At the hub the group picks a **key level** (integer `1 → ∞`, must be `≤ unlocked`). This single scalar scales **enemy stats** and **loot quality** — Mythic+ style. The gameplay loop is identical at every key level; only the numbers change.
- A **run** = enter the backrooms and **descend** through levels (`0 → 1 → 2 → …`), looting chests and killing enemies. Deeper is the run's natural progression.
- Reach an **extraction point** → run succeeds → **carried loot saved to stash** → **key N+1 unlocks**.
- **Die** before extracting → carried loot is **lost** (stash is always safe).
- **Aesthetic:** keep the dark, atmospheric, fully-lit backrooms look. The VHS/found-footage post-processing stays in the code but is **off by default** (it is already a runtime config toggle). Start with **zero enemies**; a fresh roster is built later.

### Phased roadmap (later phases get their own specs)

| Phase | Sub-project | Depends on |
|---|---|---|
| **1 (this spec)** | Strip to a clean, launchable base. | — |
| 2 | Run lifecycle: hub, enter-run, descent, extraction points, success/death resolution. | 1 |
| 3 | Key-level difficulty: selection + gating, persisted highest key, scalar threaded into spawns + loot. | 2, 5 |
| 4 | Loot + stash: difficulty-scaled loot tables, chests in structures, persistent stash, run-inventory vs stash split. | 2 |
| 5 | Enemy roster: new GeckoLib mobs, AI, spawning hooked to key level. | 1 |

---

## 2. Phase 1 goal & approach

**Goal:** A version of the mod that compiles, launches, and lets you walk through every retained backrooms level — lit, atmospheric, with the entry cinematic — and contains **no enemies and no horror-specific content**. This is the foundation Phases 2–5 build on.

**Approach (chosen): surgical strip in place.** Delete cut systems directly in the existing `com.sp` / `spb-revamped` codebase, verifying the build launches after each chunk of removal. Package/mod-id stay `com.sp` / `spb-revamped` for now; a rebrand to a Lootrooms identity is a deferred, separate mechanical pass once the base is stable. Rationale: the lighting ↔ levels ↔ CCA ↔ transition systems are deeply entangled; deletion preserves those couplings far more safely than porting to a fresh mod, and git history is retained.

---

## 3. KEEP — the base

- **Levels & generation:** `world/levels/` (`BackroomsLevel`, `BackroomsLevelWithLights`, `WorldRepresentingBackroomsLevel`, and the retained `custom/*` levels), `world/generation/` (chunk + maze generators), the `data/` dimension / dimension_type / worldgen / structures JSON, and `BackroomsLevels` registration. Retained levels: **Level 0, Level 1, Level 2, Poolrooms, Infinite Field** (Overworld-representing level kept as the non-backrooms base).
- **Rendering — 100% intact.** No shader, pipeline, or `pinwheel/*` changes. This includes Veil deferred lighting, `FlashlightRenderer`, fluorescent light block entities + renderers, `ShadowMapRenderer`, PBR (`render/pbr`), bloom, SSAO, light cones, water/puddles, Level 1 fog, Poolrooms sky/day-cycle, infinite-grass grass + birds, and the full `vhs.json` post pipeline. The VHS *look* is disabled via config default (see §5), not deleted.
- **Cinematics:** `CutsceneManager` (incl. the fall-into-the-backrooms cinematic — reused as the Phase 2 "enter a run" mechanic), `CameraShake`, `render/camera/*`, the `cutscene/*` mixins, and the noclip/suffocation → Level 0 entry flow (`CastToTheBackroomsCommand` and supporting `PlayerEntityMixin` paths). The black-screen fade lives in the `everything` shader (kept), so cinematics need **no** reimplementation.
- **Blocks / items / block entities / textures / models / sounds / lang** — the full material palette (`ModBlocks`, `ModItems`, `ModBlockEntities`, `assets/*`), minus entries that belong solely to cut content (Level 324, skinwalker).
- **Transition & event *frameworks*** — `BackroomsLevel` transition/event machinery + `WorldEvents` tick loop (repurposed for run progression and hazards in later phases). Generic light events (`world/events/generic/lights/*`) kept.
- **CCA scaffolding** — `InitializeComponents`, `PlayerComponent` (trimmed, §6), `WorldEvents` (trimmed, §6).
- **Dev / util / networking** — `LevelCommand` (teleport between levels; essential for testing), `GimmeMyInventoryBack`, `util/`, the networking framework (`InitializePackets` + retained packets).
- **Dependencies kept:** Fabric API, Veil, Cardinal Components, MidnightLib, Mod Menu, **GeckoLib** (for future enemies), **Simple Voice Chat** (for future party-talk), stamina system, and the **Sound Physics Remastered / Essential** `modCompileOnly` compat (left untouched — harmless and low-churn).

---

## 4. CUT — removed systems

- **All enemies and their support:** `entity/custom/*` (SkinWalkerEntity, SmilerEntity, WalkerEntity), `entity/ai/*`, the custom IK system `entity/ik/*` + `MowzieModelFactory` usage, `entity/client/*` (renderers, models, debug), `ModEntities`, `ModModelLayers`, entity attribute registration, entity renderer/model-layer registration in `SPBRevampedClient`.
- **SkinWalker capture mechanic (end-to-end):** the skinwalker capture/release state machine in `WorldEvents`, `SkinWalkerComponent`, `SmilerComponent`, `SkinwalkerCommand`, the skinwalker-related C2S packets (`SeeActiveSkinwalkerSync`, `TargetEntitySync`), the `skinstolen/*` mixins, and `SkinwalkerJumpscare` (the jumpscare `if` branch in `SPBRevampedClient` is removed; the existing `else` already sets `Jumpscare = 0`).
- **Level 324 — fully removed:** `Level324Backroomslevel`, `Level324ChunkGenerator`, `world/events/level324/*`, `data/spb-revamped/dimension/level324.json`, `dimension_type/level324.json`, `save/dimension/level324.json`, structures under `structures/level324/`, its entry in `BackroomsLevels` (constant, `init()` list, `definitions` map, world key), all client render branches referencing `LEVEL324_WORLD_KEY`, and its sound events.
- **Horror-specific events:** intercom and level-specific music/flavor events (`world/events/level0/*`, `level1/*`, `level2/*`, `poolrooms/*` that are narrative horror). **Keep** `world/events/generic/*` (incl. `generic/lights/*`). Exact per-file classification confirmed during implementation; any event referencing removed entities/sounds is cut.
- **Respawn "YOU CAN'T ESCAPE" sequence** and black-screen-on-respawn in `SPBRevamped.onInitialize` (`AFTER_RESPAWN`) — Phase 2 defines death/extraction handling.

---

## 5. EDIT — shared files trimmed (de-entanglement)

- **`PlayerComponent`** — keep: flashlight, noclip, cutscene (`isDoingCutscene`), stamina, `suffocationTimer`, speaking/mute hooks (retained for future party-talk). Remove: capture state (`isBeingCaptured`, `hasBeenCaptured`, `setBeingReleased`, prev-gamemode), `canSeeActiveSkinWalkerTarget`, glitch timer, `youCantEscape`-related and other skinwalker/jumpscare fields.
- **`WorldEvents`** — keep the event-tick scheduler + per-level NBT save/sync delegation. Remove `tickSkinWalkerCapturing`, `shouldReleasePlayer`, `activeSkinwalkerTarget`/`activeSkinWalkerEntity` and related fields/NBT keys.
- **`SPBRevamped`** — remove enemy registration (attributes), the `AFTER_RESPAWN` horror sequence, and skinwalker references. Keep `BackroomsLevels.init()`, block/item/sound/packet/command/gamerule registration, GeckoLib init (kept for future mobs), the world-change `RELOAD_LIGHTS` hook, and the S2C sender helpers (minus respawn-horror-only ones).
- **`SPBRevampedClient`** — remove enemy renderer/model registration, the skinwalker frustum/visibility check block, and the `SkinwalkerJumpscare` `if` branch. Keep all lighting/shadow/cutscene/flashlight/grass/bird wiring and the full post pipeline.
- **`BackroomsVoicechatPlugin`** — reduce to a clean stub: keep the plugin registration + entrypoint, remove all skinwalker speaking-time/capture/pitch-down logic and the `SkinWalkerEntity` import (it would not compile once enemies are gone). Leaves a minimal working VC integration for Phase-2+ party-talk.
- **`ConfigStuff`** — set `enableVhsEffect = false` and `enableVhsEffectInTheBackrooms = false` as defaults (VHS off out of the box, still toggleable). Keep all VHS/motion-blur/distortion knobs.
- **`ModSounds`** + `assets/spb-revamped/sounds.json` + sound files — prune skinwalker-, Level-324-, and respawn-horror-specific sounds. Keep `FALLING` (cinematic) and general ambience/light sounds.
- **`spb-revamped.mixins.json`** — remove the `skinstolen.*` entries. Cutscene mixins stay.
- **`fabric.mod.json`** — remove the `skw` / `smi` cardinal-components custom entries. Keep the `voicechat` entrypoint.
- **`BackroomsLevels`** — remove all Level 324 members (see §4).

---

## 6. Verification — Phase 1 "done"

Phase 1 is complete when **all** of the following hold (observed, not assumed):

1. `./gradlew build` compiles with no missing-reference errors.
2. `./gradlew runClient` launches to the main menu and into a world without crashing; the Simple Voice Chat stub plugin loads.
3. `/level` teleports into **each** retained level — Level 0, Level 1, Level 2, Poolrooms, Infinite Field — and each is **lit and walkable**: flashlight works, fluorescent lights glow, shadows render, Level 1 fog is present.
4. The **fall-into-the-backrooms cinematic** still plays on entry (`casttothebackrooms` flow).
5. **No** enemies spawn or are registered; **no** skinwalker behavior occurs; **Level 324** is absent (no dimension, no registry entry, no dangling references).
6. The **VHS/glitch look is off** by default; enabling `enableVhsEffectInTheBackrooms` in config restores it (proving the rendering path is intact, not deleted).

---

## 7. Out of scope (deferred)

- Rebranding mod id / package / assets to a Lootrooms identity (deferred mechanical pass after the base is stable).
- All gameplay systems: run lifecycle, key-level difficulty, loot/stash, new enemies (Phases 2–5).
- Any shader/pipeline cleanup beyond leaving the VHS passes dormant.
- Pruning of unused blocks/items beyond those uniquely tied to cut content.
