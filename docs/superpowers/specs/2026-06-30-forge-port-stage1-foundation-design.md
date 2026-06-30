# Design: Fabric → Forge 1.20.1 Port — Stage 1 (Foundation)

**Date:** 2026-06-30
**Status:** Approved for implementation planning
**Scope of this spec:** Stage 1 (Foundation) only — stand up a fresh LexForge 1.20.1 mod that builds, launches, and registers all blocks/items/sounds with assets in place. Stages 2–6 are sketched as a roadmap to justify Stage 1's boundaries; each gets its own spec later.

---

## 1. Why this port

The mod is being repurposed into a co-op PvE extraction looter whose intended weapon base is **Timeless & Classics Zero (TaCZ)**. The official TaCZ is a **LexForge** mod (max version **1.20.1**); the unofficial Fabric port ("Refabricated") cannot be remapped into a Fabric/Yarn dev environment (intrinsic `render`/`method_3166` mapping conflict in its GeckoLib item renderers — confirmed both at Loom build time and via Fabric's `RuntimeModRemapper`). Moving to **LexForge 1.20.1** uses TaCZ natively and dissolves that wall.

### Verified platform facts (the constraints that decide the target)

- **`Veil-forge-1.20.1` exists** on the BlameJared maven, including **`1.0.0.228`** — the exact version this mod uses on Fabric — up to `1.0.0.296`. The entire rendering stack ports to LexForge at the same version. (Veil for 1.21+ moved to NeoForge; at 1.20.1 it is Fabric **or** LexForge.)
- **Official TaCZ** (`timeless-and-classics-zero`) is **LexForge-only**, max **1.20.1** — no NeoForge build, does not go past 1.20.1.
- **GeckoLib** ships for Forge 1.20.1.

**Therefore the target is LexForge 1.20.1** — the only platform where Veil's rendering *and* official TaCZ coexist. NeoForge would force a Minecraft jump to 1.21+, which loses official TaCZ entirely.

---

## 2. Approach & strategy (decided)

- **Fresh Forge MDK, port assets wholesale + logic incrementally.** Start from a Forge 1.20.1 skeleton that compiles/runs on day one. Loader-agnostic assets/data copy 1:1; Java logic is rewritten to Mojmap + Forge idioms file-by-file, keeping a runnable project at every step. The existing Fabric repo remains the reference implementation.
  - Rationale: Forge 1.20.1 uses **Mojmap (official)** names while the Fabric code is **Yarn**, so *every* Minecraft reference changes name regardless. In-place conversion therefore saves nothing and leaves the project non-compiling across all 223 files at once; a fresh mod never has a broken-limbo state.
- **Keep `spb-revamped` mod id + `com.sp` package through the port.** The entire asset/data layer is namespaced `spb-revamped:`; keeping the id means assets copy with zero path edits and Java ports package-for-package (one variable per file: MC/loader idioms). The Lootrooms rebrand is a separate later pass (Stage 6).
- **Location:** the Forge project lives on a **`forge-port` branch** of this repo (a git worktree may be used so the Fabric tree stays checked out for side-by-side reference).
- **Dead code does not come along** — only live code is ported, so the previously-deferred dead-code cleanup (wind-tunnel sound plumbing, `youCantEscape`, `shouldGlitch`, intercom remnants, `speakingBuffer`) resolves itself by omission.

### Port roadmap (later stages each get their own spec)

| Stage | Scope | Done when |
|---|---|---|
| **1 (this spec)** | Forge skeleton + build + registration + assets. | Mod builds + launches; all blocks/items registered; no dimensions/rendering. |
| 2 | Levels & worldgen: `BackroomsLevel`, dimensions, chunk + maze generators, structures, `/level`. | `/level` enters each level; generates + walkable (vanilla light). |
| 3 | State & networking: CCA → Forge capabilities, packets → Forge channel, event scheduler, commands. | Player/world state + events work (incl. blackout fix). |
| 4 | Rendering: `Veil-forge` wiring, `SPBRevampedClient` hooks, `render/` stack, per-level shaders, rendering mixins. | Backrooms render with the signature look. |
| 5 | Systems & polish: cutscenes, stamina, HUD, sounds/ambience, remaining mixins, Sound-Physics compat. | Parity with the current Fabric base. |
| 6 | TaCZ as a native Forge dependency; then the Lootrooms rebrand pass. | TaCZ loads alongside; mod rebranded. |

**Mixins travel with their subsystem** (rendering mixins → Stage 4, worldgen mixins → Stage 2, …), not one late mixin stage. The end state of Stages 1–5 is **parity with the current Fabric base, on LexForge 1.20.1**; the looter gameplay phases then resume from the Forge base.

---

## 3. Stage 1 design

### 3.1 Project & build
- Fresh Forge MDK on the `forge-port` branch. Retains GPL-3.0 `LICENSE` + fork attribution.
- **ForgeGradle 6**, Minecraft **1.20.1**, Forge **47.4.0**, mappings **`official` 1.20.1** (Parchment optional for parameter names), Java **17**.
- `gradle.properties` carries `mod_version` (current `1.1.8`), `maven_group=com.sp`, mod id `spb-revamped`, and pinned dependency versions.
- Dependencies wired now: **`foundry.veil:Veil-forge-1.20.1:1.0.0.228`** (BlameJared maven) and **GeckoLib** (Forge 1.20.1). Config lib (MidnightLib Forge build or Cloth Config) and **TaCZ** are deferred to the stages that consume them (4 and 6). **Mod Menu is dropped** (Forge has a built-in config screen).

### 3.2 Mod plumbing
- **`META-INF/mods.toml`** replaces `fabric.mod.json`: mod id `spb-revamped`, name, `${mod_version}`, license `GPL-3.0-only`, dependencies (`forge`, `minecraft ~1.20.1`, `veil`, `geckolib`).
- **`pack.mcmeta`** for resource/data pack versions.
- **`@Mod("spb-revamped")`** main class (`com.sp.SPBRevamped`) subscribing registration + common setup to the mod event bus. A thin client entry (`com.sp.SPBRevampedClient`) via `FMLClientSetupEvent` — **no rendering wiring yet** (Stage 4).
- **Mixin config** `spb-revamped.mixins.json` declared in `mods.toml` and wired via the mixin Gradle setup, but **empty of entries** — mixins arrive with their feature stages.
- **`META-INF/accesstransformer.cfg`**: a mechanical translation of the current `spb-revamped.accesswidener` entries into AT syntax in Mojmap names. Entries whose members do not yet exist in ported code are still valid as long as the vanilla member exists; translating the full set now avoids per-stage access churn.

### 3.3 Registration, classes & assets
- Port the registration layer to **`DeferredRegister`**: `ModBlocks`, `ModItems`, `ModBlockEntities` (types only), `ModSounds`, and creative tab(s) (`CreativeModeTab` via `DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ...)`).
- Port the **block/item classes** these reference (FluorescentLight family, concrete blocks, carpet, shelves, etc.) to Mojmap APIs (`Block`, `BlockBehaviour.Properties`, `BlockEntity`, …). Custom block *shape/property* behavior is ported; **block-entity tick/render logic that depends on later stages is stubbed** with explicit `// TODO(Stage 3/4)` no-ops (light state → Stage 3; block-entity renderers → Stage 4).
- **Copy `assets/spb-revamped/**` and `data/spb-revamped/**` wholesale**, namespace unchanged: PBR textures, the **hand-authored generated models** (block/item/blockstate JSON — copied as source; **datagen is NOT run**, per the known gotcha that the model provider emits broken `cube_all` stubs), the full Veil `pinwheel/` shader tree, models, blockstates, lang, sounds + `sounds.json`, structures, dimension/dimension_type/worldgen JSON, loot tables, and the existing `gear_tiers/`.

### 3.4 Out of scope for Stage 1
- Dimensions / level registration / worldgen (Stage 2).
- CCA → capabilities, networking, events, commands (Stage 3).
- Veil pipeline wiring and all rendering behavior (Stage 4).
- Cutscenes, stamina, HUD, ambience, remaining mixins (Stage 5).
- TaCZ dependency + Lootrooms rebrand (Stage 6).
- Config system (ported with its consumer, Stage 4).

---

## 4. Verification — Stage 1 "done"

Stage 1 is complete when **all** hold (observed, not assumed):

1. `./gradlew build` compiles with no errors; `./gradlew runClient` launches to the main menu and into a **vanilla** world without crashing.
2. Forge loads mod `spb-revamped`; **Veil** and **GeckoLib** load alongside, and Veil's deferred renderer initializes (log: "Deferred Renderer Enabled").
3. **All** blocks and items appear in the mod's creative tab with correct models/textures — PBR blocks (e.g. `floor_tiling`, `carpet`, `concrete`, `road`, `pavement`) show their color texture, not missing/black.
4. Blocks are placeable in a vanilla world and render correctly under **vanilla** lighting.
5. **No** dimensions, **no** custom render-pipeline behavior, **no** events — those are Stages 2–4.

---

## 5. Risks & notes

- **Mapping translation volume.** Yarn → Mojmap renaming is pervasive but mechanical; Stage 1 only covers the registration layer + block/item classes, bounding the first batch.
- **Access transformer fidelity.** The AW → AT translation must use correct Mojmap member names; mistranslations surface as compile/runtime access errors. Translate from the vanilla member identity, not the Yarn alias.
- **Do not run `runDatagen`.** The generated PBR models are hand-authored; the model provider would overwrite them with broken stubs. Copy the generated assets as source and leave datagen unwired in Stage 1.
- **Config/Mod Menu.** `ConfigStuff` (MidnightLib) and the Mod Menu entry are intentionally deferred; Mod Menu is replaced by Forge's native config screen later.
