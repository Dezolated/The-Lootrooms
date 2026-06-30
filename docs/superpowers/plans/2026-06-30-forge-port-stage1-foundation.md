# Forge 1.20.1 Port — Stage 1 (Foundation) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Stand up a fresh LexForge 1.20.1 mod (`spb-revamped`) that builds, launches, and registers every block/item/sound/block-entity-type with all assets in place — no dimensions, no rendering pipeline, no events yet.

**Architecture:** Fresh Forge MDK on a `forge-port` branch; the Fabric `master` tree is the reference implementation, read per-file via `git show master:<path>`. Loader-agnostic assets/data are copied 1:1 (namespace stays `spb-revamped:`); Java is ported package-for-package (`com.sp.x` → `com.sp.x`), each file rewritten Yarn→Mojmap + Forge idioms, keeping a compiling project at every task.

**Tech Stack:** Minecraft 1.20.1, **Forge 47.4.10**, **ForgeGradle 6**, official (Mojmap) mappings, Java 17, Mixin (via Forge), Veil-forge 1.0.0.228, GeckoLib-forge 4.8.4.

## Global Constraints

- **Platform:** LexForge 1.20.1 only. Mappings channel `official`, version `1.20.1`.
- **Identity unchanged:** mod id `spb-revamped`, package root `com.sp`, maven group `com.sp`, asset/data namespace `spb-revamped`. No rebrand in Stage 1.
- **Versions (pin exactly):** Forge `47.4.10`; `foundry.veil:Veil-forge-1.20.1:1.0.0.228` (maven `https://maven.blamejared.com`); `software.bernie.geckolib:geckolib-forge-1.20.1:4.8.4` (maven `https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/`); Java 17.
- **No test suite exists.** The verification gate for every task is `./gradlew build` (compile/AT/datagen-free) and, for milestone tasks, `./gradlew runClient`. There are no unit tests to write; "test" steps below mean "run the build/client and observe the stated result."
- **Do NOT run `./gradlew runDatagen`.** Generated models are hand-authored; the model provider emits broken `cube_all` stubs. Copy generated assets as source.
- **Resolve exact Mojmap signatures with mcmodding-mcp** (`search_mappings`, `get_class_details`, `get_method_signature`) per class — do not guess obfuscated/renamed members.
- **Block-entity tick/render logic is stubbed** (`// TODO(Stage 3/4)`) — only types register in Stage 1.
- **Scope ends at "mod loads + blocks exist."** No dimensions, rendering, events, capabilities, networking, config, or TaCZ.

---

## Yarn → Mojmap translation reference (used by all porting tasks)

Common renames (Yarn → Mojmap). Use `search_mappings` for anything not listed.

| Yarn | Mojmap |
|---|---|
| `net.minecraft.util.Identifier` | `net.minecraft.resources.ResourceLocation` |
| `net.minecraft.registry.Registries` | `net.minecraft.core.registries.BuiltInRegistries` |
| `net.minecraft.registry.Registry.register` | `net.minecraft.core.Registry.register` |
| `net.minecraft.block.Block` | `net.minecraft.world.level.block.Block` |
| `net.minecraft.block.AbstractBlock.Settings` | `net.minecraft.world.level.block.state.BlockBehaviour.Properties` |
| `FabricBlockSettings.create()` | `BlockBehaviour.Properties.of()` |
| `net.minecraft.item.Item` / `Item.Settings` | `net.minecraft.world.item.Item` / `Item.Properties` |
| `net.minecraft.item.BlockItem` | `net.minecraft.world.item.BlockItem` |
| `net.minecraft.sound.SoundEvent` | `net.minecraft.sounds.SoundEvent` |
| `net.minecraft.block.entity.BlockEntityType` | `net.minecraft.world.level.block.entity.BlockEntityType` |
| `World` | `net.minecraft.world.level.Level` |
| `new Identifier(MOD_ID, path)` | `ResourceLocation.fromNamespaceAndPath(MOD_ID, path)` |
| Fabric registration (`Registry.register(...)`) | Forge `DeferredRegister` (see Task pattern) |
| `FabricBlockEntityTypeBuilder.create(F::new, block).build()` | `BlockEntityType.Builder.of(F::new, block).build(null)` |

Forge registration replaces Fabric's eager `Registry.register` with `DeferredRegister` objects registered to the mod event bus in the `@Mod` constructor.

---

## File structure (Stage 1 deliverable on `forge-port` branch)

- `build.gradle`, `gradle.properties`, `settings.gradle` — ForgeGradle build (replace Fabric/Loom ones)
- `src/main/resources/META-INF/mods.toml` — mod metadata (replaces `fabric.mod.json`)
- `src/main/resources/META-INF/accesstransformer.cfg` — translated from `spb-revamped.accesswidener`
- `src/main/resources/pack.mcmeta`
- `src/main/resources/spb-revamped.mixins.json` — config present, **no mixin entries**
- `src/main/java/com/sp/SPBRevamped.java` — `@Mod` main, owns `DeferredRegister`s
- `src/main/java/com/sp/SPBRevampedClient.java` — client setup skeleton (no rendering)
- `src/main/java/com/sp/init/ModBlocks.java`, `ModItems.java`, `ModSounds.java`, `ModBlockEntities.java`
- `src/main/java/com/sp/item/ModCreativeTab.java` — replaces `ModItemGroups`
- `src/main/java/com/sp/block/custom/**`, `src/main/java/com/sp/block/entity/**` — ported block + block-entity classes (BE logic stubbed)
- `src/main/resources/assets/spb-revamped/**`, `src/main/resources/data/spb-revamped/**` — copied 1:1

---

### Task 1: Forge MDK skeleton + build

**Files:**
- Create branch `forge-port`; recommended: a git worktree so `master` stays checked out for reference (`git worktree add ../The-Lootrooms-forge -b forge-port master`).
- Create/replace: `build.gradle`, `gradle.properties`, `settings.gradle`
- Remove (on this branch): `src/main/java/**` (Fabric source — re-added per task from `master` reference), Loom-specific files
- Keep: `LICENSE`, `README.md`, `docs/`

**Interfaces:**
- Produces: a buildable Forge project with package root `com.sp`, mod id `spb-revamped`, and the version properties later tasks read (`mod_version`, `mod_id`, `maven_group`).

- [ ] **Step 1: Create the branch/worktree**

```bash
git worktree add ../The-Lootrooms-forge -b forge-port master
cd ../The-Lootrooms-forge
```

- [ ] **Step 2: Write `gradle.properties`**

```properties
org.gradle.jvmargs=-Xmx4G
org.gradle.daemon=false

minecraft_version=1.20.1
forge_version=47.4.10
mappings_channel=official
mappings_version=1.20.1

mod_id=spb-revamped
mod_version=1.1.8
maven_group=com.sp
archives_base_name=spb-revamped-1.20.1

veil_version=1.0.0.228
geckolib_version=4.8.4
```

- [ ] **Step 3: Write `settings.gradle`**

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = 'https://maven.minecraftforge.net/' }
    }
}
plugins { id 'org.gradle.toolchains.foojay-resolver-convention' version '0.8.0' }
```

- [ ] **Step 4: Write `build.gradle`** (assets-only resources still resolve; Java is added in later tasks)

```groovy
plugins {
    id 'net.minecraftforge.gradle' version '[6.0.16,6.2)'
    id 'org.spongepowered.mixin' version '0.7.+'
}

version = project.mod_version
group = project.maven_group
base { archivesName = project.archives_base_name }

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

minecraft {
    mappings channel: project.mappings_channel, version: project.mappings_version
    accessTransformer = file('src/main/resources/META-INF/accesstransformer.cfg')
    runs {
        client { workingDirectory project.file('run'); property 'forge.logging.console.level', 'debug'; mods { "${mod_id}" { source sourceSets.main } } }
        server { workingDirectory project.file('run'); property 'forge.logging.console.level', 'debug'; mods { "${mod_id}" { source sourceSets.main } } }
    }
}

mixin {
    add sourceSets.main, "${mod_id}.refmap.json"
    config "${mod_id}.mixins.json"
}

repositories {
    maven { url = 'https://maven.blamejared.com' }
    maven { url = 'https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/' }
}

dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
    annotationProcessor 'org.spongepowered:mixin:0.8.5:processor'
}

tasks.named('processResources') { inputs.property 'version', project.version
    filesMatching('META-INF/mods.toml') { expand version: project.version } }
tasks.withType(JavaCompile).configureEach { options.encoding = 'UTF-8'; options.release = 17 }
```

- [ ] **Step 5: Remove Fabric Java + Loom files on this branch**

```bash
git rm -r src/main/java
git rm -f src/main/resources/fabric.mod.json src/main/resources/spb-revamped.accesswidener 2>/dev/null || true
mkdir -p src/main/java/com/sp
```

- [ ] **Step 6: Build (compile gate — no sources yet, just resolves Forge)**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL` (Forge resolves; no Java to compile yet). If `accesstransformer.cfg` is missing, create an empty one first: `mkdir -p src/main/resources/META-INF && touch src/main/resources/META-INF/accesstransformer.cfg`.

- [ ] **Step 7: Commit**

```bash
git add -A && git commit -m "build: Forge 1.20.1 MDK skeleton (ForgeGradle 6, official mappings)"
```

---

### Task 2: Mod metadata + entrypoints

**Files:**
- Create: `src/main/resources/META-INF/mods.toml`, `src/main/resources/pack.mcmeta`
- Create: `src/main/java/com/sp/SPBRevamped.java`, `src/main/java/com/sp/SPBRevampedClient.java`

**Interfaces:**
- Produces: `SPBRevamped.MOD_ID` (String `"spb-revamped"`), `SPBRevamped.MOD_BUS` access for `DeferredRegister.register(...)`, and the `@Mod` lifecycle that later registration tasks hook into.

- [ ] **Step 1: Write `mods.toml`**

```toml
modLoader="javafml"
loaderVersion="[47,)"
license="GPL-3.0-only"
[[mods]]
modId="spb-revamped"
version="${version}"
displayName="SP-Backrooms Revamped"
authors="SpacePotato, Herr Chaos, MetaOne; Forge port by Dezolated"
description="Co-op PvE extraction looter on the backrooms levels."
[[dependencies.spb-revamped]]
    modId="forge"
    mandatory=true
    versionRange="[47,)"
    ordering="NONE"
    side="BOTH"
[[dependencies.spb-revamped]]
    modId="minecraft"
    mandatory=true
    versionRange="[1.20.1]"
    ordering="NONE"
    side="BOTH"
[[dependencies.spb-revamped]]
    modId="veil"
    mandatory=true
    versionRange="*"
    ordering="AFTER"
    side="BOTH"
[[dependencies.spb-revamped]]
    modId="geckolib"
    mandatory=true
    versionRange="*"
    ordering="AFTER"
    side="BOTH"
```

- [ ] **Step 2: Write `pack.mcmeta`**

```json
{ "pack": { "description": "spb-revamped", "pack_format": 15 } }
```

- [ ] **Step 3: Write `SPBRevamped.java`** (registration wiring added by later tasks; structure now)

```java
package com.sp;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(SPBRevamped.MOD_ID)
public class SPBRevamped {
    public static final String MOD_ID = "spb-revamped";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SPBRevamped() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // DeferredRegister registrations attached here in later tasks:
        // ModSounds.register(modBus); ModBlocks.register(modBus); ModItems.register(modBus);
        // ModBlockEntities.register(modBus); ModCreativeTab.register(modBus);
        LOGGER.info("SP-Backrooms Revamped (Forge) initializing");
    }
}
```

- [ ] **Step 4: Write `SPBRevampedClient.java`** (skeleton, no rendering)

```java
package com.sp;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SPBRevamped.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SPBRevampedClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // TODO(Stage 4): Veil hooks, renderers, key binds, block render layers
    }
}
```

- [ ] **Step 5: Build + run (runtime gate)**

Run: `./gradlew runClient`
Expected: client reaches main menu; log contains `SP-Backrooms Revamped (Forge) initializing` and lists mod `spb-revamped`. Close the client.

- [ ] **Step 6: Commit**

```bash
git add -A && git commit -m "feat: mods.toml + @Mod entrypoints (mod loads, empty)"
```

---

### Task 3: Veil + GeckoLib dependencies

**Files:**
- Modify: `build.gradle` (dependencies block)

**Interfaces:**
- Produces: `veil` and `geckolib` on the runtime classpath; Veil's deferred renderer available for Stage 4.

- [ ] **Step 1: Add dependencies to `build.gradle`**

```groovy
dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"
    annotationProcessor 'org.spongepowered:mixin:0.8.5:processor'
    implementation fg.deobf("foundry.veil:Veil-forge-${minecraft_version}:${veil_version}")
    implementation fg.deobf("software.bernie.geckolib:geckolib-forge-${minecraft_version}:${geckolib_version}")
}
```

- [ ] **Step 2: Build + run (runtime gate)**

Run: `./gradlew runClient`
Expected: log lists `veil` and `geckolib` as loaded mods; Veil logs `Veil is initializing.` and `Deferred Renderer Enabled`. No crash. Close the client.

- [ ] **Step 3: Commit**

```bash
git add -A && git commit -m "build: add Veil-forge and GeckoLib dependencies"
```

---

### Task 4: Mixin config + access transformer

**Files:**
- Create: `src/main/resources/spb-revamped.mixins.json`
- Modify: `src/main/resources/META-INF/accesstransformer.cfg`

**Interfaces:**
- Produces: a registered (empty) mixin config later stages append to, and an AT covering the members the rendering/mixin stages will need.

- [ ] **Step 1: Write `spb-revamped.mixins.json`** (no entries yet)

```json
{
  "required": true,
  "minVersion": "0.8.5",
  "package": "com.sp.mixin",
  "compatibilityLevel": "JAVA_17",
  "refmap": "spb-revamped.refmap.json",
  "mixins": [],
  "client": [],
  "injectors": { "defaultRequire": 1 }
}
```

- [ ] **Step 2: Reference mixin config in `mods.toml`** (append)

```toml
[[mixins]]
config="spb-revamped.mixins.json"
```

- [ ] **Step 3: Translate the access widener to `accesstransformer.cfg`**

Read the source: `git show master:src/main/resources/spb-revamped.accesswidener`. For each `accessible`/`mutable` line, emit an AT line in Mojmap names. AT syntax: `public <owner-class-binary-name> <member-descriptor>`; use `public-f` to also de-final. Resolve every member's Mojmap name with `search_mappings`/`get_class_details` — the AW uses Yarn and some intermediary (`field_4192`) names that must be looked up. Worked examples:

```
# AW: accessible method net/minecraft/client/render/GameRenderer getFov (...)D
public net.minecraft.client.renderer.GameRenderer m_109141_ # getFov(Camera,float,boolean)
# AW: accessible field net/minecraft/world/chunk/Chunk blockEntityNbts Ljava/util/Map;
public net.minecraft.world.level.chunk.LevelChunk f_62815_ # blockEntityNbts (verify name via mappings)
```

Translate all 41 entries this way. Entries whose owning class only appears in later stages are still valid now (the vanilla member exists).

- [ ] **Step 4: Build (AT + mixin validation gate)**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`. ForgeGradle applies the AT during setup; a bad AT line fails here with the offending entry named — fix that entry's Mojmap name.

- [ ] **Step 5: Commit**

```bash
git add -A && git commit -m "build: mixin config (empty) + access transformer from AW"
```

---

### Task 5: Copy assets and data wholesale

**Files:**
- Create: `src/main/resources/assets/spb-revamped/**`, `src/main/resources/data/spb-revamped/**`

**Interfaces:**
- Produces: all models/blockstates/textures/lang/sounds.json/shaders/structures/dimension JSON available under the unchanged `spb-revamped` namespace for registration tasks to resolve against.

- [ ] **Step 1: Copy from the master reference**

```bash
git archive master src/main/resources/assets src/main/resources/data | tar -x
```

(Includes the hand-authored generated models, the Veil `pinwheel/` tree, `gear_tiers/`, structures, dimension/dimension_type/worldgen JSON, lang, sounds.)

- [ ] **Step 2: Verify the floor PBR assets are present (regression guard)**

Run: `ls src/main/resources/assets/spb-revamped/models/block/floor_tiling.json src/main/resources/assets/spb-revamped/textures/block/pbr/floor_tiling/floor_tiling_color.png`
Expected: both exist.

- [ ] **Step 3: Build (resources gate)**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`; no datagen runs.

- [ ] **Step 4: Commit**

```bash
git add -A && git commit -m "assets: copy assets/ and data/ from Fabric reference (namespace unchanged)"
```

---

### Task 6: ModSounds (DeferredRegister)

**Files:**
- Create: `src/main/java/com/sp/init/ModSounds.java`
- Modify: `src/main/java/com/sp/SPBRevamped.java` (wire `ModSounds.register(modBus)`)

**Interfaces:**
- Consumes: `SPBRevamped.MOD_ID`.
- Produces: `RegistryObject<SoundEvent>` constants (e.g. `ModSounds.FALLING`) referenced by later code; `ModSounds.register(IEventBus)`.

- [ ] **Step 1: Port the class** — read `git show master:src/main/java/com/sp/init/ModSounds.java` for the full event name list (~50 names), and emit each as a `DeferredRegister` entry. Pattern + first entries:

```java
package com.sp.init;

import com.sp.SPBRevamped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SPBRevamped.MOD_ID);

    public static final RegistryObject<SoundEvent> SILENCE = reg("silence");
    public static final RegistryObject<SoundEvent> FLASHLIGHT_CLICK = reg("flashlight_click");
    public static final RegistryObject<SoundEvent> AMBIENCE = reg("ambience");
    public static final RegistryObject<SoundEvent> FALLING = reg("falling");
    // ... one reg(...) per event id from the master ModSounds list ...

    private static RegistryObject<SoundEvent> reg(String name) {
        return SOUNDS.register(name,
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SPBRevamped.MOD_ID, name)));
    }
    public static void register(IEventBus bus) { SOUNDS.register(bus); }
}
```

- [ ] **Step 2: Wire it** — in `SPBRevamped` constructor add `ModSounds.register(modBus);`.

- [ ] **Step 3: Build + run (runtime gate)**

Run: `./gradlew runClient`
Expected: no crash; no "Missing sound" errors for ported ids beyond the known dead `wind_tunnel_ambience` (do not port that one). Close the client.

- [ ] **Step 4: Commit**

```bash
git add -A && git commit -m "feat: port ModSounds to DeferredRegister"
```

---

### Task 7: Block classes + ModBlocks (DeferredRegister)

**Files:**
- Create: `src/main/java/com/sp/block/custom/**` (ported custom block classes)
- Create: `src/main/java/com/sp/init/ModBlocks.java`
- Modify: `src/main/java/com/sp/SPBRevamped.java` (wire `ModBlocks.register(modBus)`)

**Interfaces:**
- Consumes: `SPBRevamped.MOD_ID`.
- Produces: `RegistryObject<Block>` constants (names exactly as in master `ModBlocks`, e.g. `FLUORESCENT_LIGHT`, `FLOOR_TILING`, `CARPET_BLOCK`, `THIN_FLUORESCENT_LIGHT`, `TINY_FLUORESCENT_LIGHT`, `WOODEN_CRATE`, `CEILINGLIGHT`, `EMERGENCY_LIGHT`, `drawingMarker`, the `CONCRETE_BLOCK_*` set, `ROAD`, `POOL_TILES`, `PAVEMENT`, etc.) and `ModBlocks.BLOCKS`/`register(IEventBus)`.

- [ ] **Step 1: Port the custom block classes** — for each class under `git show master:src/main/java/com/sp/block/custom/` referenced by `ModBlocks`, port to Mojmap. Apply the translation reference; resolve block method overrides (`createBlockStateDefinition`, `getStateForPlacement`, `getShape`, `newBlockEntity`, etc.) with `get_class_details net.minecraft.world.level.block.Block`. Keep block-entity holders' `newBlockEntity`/`getTicker` present but have tickers reference the (stubbed) BE classes from Task 8. Worked example — a simple full-cube PBR block has no custom class (uses `Block`); a stateful one (e.g. `FluorescentLightBlock`) ports its `BooleanProperty`/`createBlockStateDefinition` to `net.minecraft.world.level.block.state.properties.BooleanProperty` + `BlockBehaviour`.

- [ ] **Step 2: Write `ModBlocks.java`** — read `git show master:src/main/java/com/sp/init/ModBlocks.java` for the complete block list and settings; emit each via `DeferredRegister`. Pattern + representative entries:

```java
package com.sp.init;

import com.sp.SPBRevamped;
import com.sp.block.custom.FluorescentLightBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, SPBRevamped.MOD_ID);

    public static final RegistryObject<Block> CONCRETE_BLOCK_1 = reg("concrete1",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5f).sound(SoundType.STONE)));
    public static final RegistryObject<Block> FLOOR_TILING = reg("floor_tiling",
        () -> new Block(BlockBehaviour.Properties.of().strength(-1f).noLootTable()));
    public static final RegistryObject<FluorescentLightBlock> FLUORESCENT_LIGHT = reg("fluorescent_light",
        () -> new FluorescentLightBlock(BlockBehaviour.Properties.of().lightLevel(s -> 15)));
    // ... one reg(...) per block in master ModBlocks, matching settings ...

    private static <T extends Block> RegistryObject<T> reg(String name, java.util.function.Supplier<T> sup) {
        return BLOCKS.register(name, sup);
    }
    public static void register(IEventBus bus) { BLOCKS.register(bus); }
}
```

- [ ] **Step 3: Wire it** — in `SPBRevamped` add `ModBlocks.register(modBus);` (before `ModBlockEntities`).

- [ ] **Step 4: Build (compile gate)**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`. Resolve any missing block-class members via mcmodding-mcp.

- [ ] **Step 5: Commit**

```bash
git add -A && git commit -m "feat: port block classes + ModBlocks to DeferredRegister"
```

---

### Task 8: ModBlockEntities (types, logic stubbed)

**Files:**
- Create: `src/main/java/com/sp/block/entity/**` (the 7 BE classes, stubbed)
- Create: `src/main/java/com/sp/init/ModBlockEntities.java`
- Modify: `src/main/java/com/sp/SPBRevamped.java`

**Interfaces:**
- Consumes: `ModBlocks` block RegistryObjects.
- Produces: `RegistryObject<BlockEntityType<…>>` for the 7 types; `ModBlockEntities.register(IEventBus)`.

- [ ] **Step 1: Port the 7 BE classes as stubs** — `FluorescentLightBlockEntity`, `ThinFluorescentLightBlockEntity`, `TinyFluorescentLightBlockEntity`, `DrawingMarkerBlockEntity`, `WoodenCrateBlockEntity`, `CeilingLightBlockEntity`, `EmergencyLightBlockEntity`. Each extends `net.minecraft.world.level.block.entity.BlockEntity` with the `(BlockEntityType, BlockPos, BlockState)` constructor. **Tick/render/light-state logic is replaced with `// TODO(Stage 3/4)` comments** — keep only what compiles and lets the type register. Worked stub:

```java
package com.sp.block.entity;

import com.sp.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FluorescentLightBlockEntity extends BlockEntity {
    public FluorescentLightBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUORESCENT_LIGHT_BLOCK_ENTITY.get(), pos, state);
    }
    // TODO(Stage 3/4): light-state tick + client render (incl. blackout-reset fix)
}
```

- [ ] **Step 2: Write `ModBlockEntities.java`** (mirrors master's 7 types)

```java
package com.sp.init;

import com.sp.SPBRevamped;
import com.sp.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SPBRevamped.MOD_ID);

    public static final RegistryObject<BlockEntityType<FluorescentLightBlockEntity>> FLUORESCENT_LIGHT_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("fluorescent_light_block_entity",
            () -> BlockEntityType.Builder.of(FluorescentLightBlockEntity::new, ModBlocks.FLUORESCENT_LIGHT.get()).build(null));
    // ... remaining 6 types, ids exactly as master (note DrawingMarker id is "poolrooms_window_block_entity") ...

    public static void register(IEventBus bus) { BLOCK_ENTITIES.register(bus); }
}
```

- [ ] **Step 3: Wire it** — `ModBlockEntities.register(modBus);` after `ModBlocks`.

- [ ] **Step 4: Build (compile gate)**

Run: `./gradlew build`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 5: Commit**

```bash
git add -A && git commit -m "feat: port block-entity types (logic stubbed for Stage 3/4)"
```

---

### Task 9: ModItems + creative tab (Stage 1 acceptance)

**Files:**
- Create: `src/main/java/com/sp/init/ModItems.java`
- Create: `src/main/java/com/sp/item/ModCreativeTab.java`
- Modify: `src/main/java/com/sp/SPBRevamped.java`

**Interfaces:**
- Consumes: `ModBlocks` RegistryObjects.
- Produces: `BlockItem`s for every block; one `CreativeModeTab` listing them; the Stage 1 acceptance surface.

- [ ] **Step 1: Write `ModItems.java`** — read `git show master:src/main/java/com/sp/init/ModItems.java` for any standalone items; register a `BlockItem` for each block in `ModBlocks`. Pattern:

```java
package com.sp.init;

import com.sp.SPBRevamped;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, SPBRevamped.MOD_ID);

    public static RegistryObject<Item> blockItem(String name, RegistryObject<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    // Register a blockItem(...) for each ModBlocks entry, name matching the block id.

    public static void register(IEventBus bus) { ITEMS.register(bus); }
}
```

- [ ] **Step 2: Write `ModCreativeTab.java`** (replaces `ModItemGroups`)

```java
package com.sp.item;

import com.sp.SPBRevamped;
import com.sp.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SPBRevamped.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("spb_revamped", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.spb-revamped"))
            .icon(() -> new ItemStack(ModItems.ITEMS.getEntries().iterator().next().get()))
            .displayItems((params, output) -> ModItems.ITEMS.getEntries().forEach(e -> output.accept(e.get())))
            .build());

    public static void register(IEventBus bus) { TABS.register(bus); }
}
```

- [ ] **Step 3: Wire it** — add `ModItems.register(modBus);` then `ModCreativeTab.register(modBus);` in `SPBRevamped`.

- [ ] **Step 4: Build + run (Stage 1 acceptance gate)**

Run: `./gradlew runClient`
Expected (verify all, per spec §4): client launches to menu and a vanilla world; the `spb-revamped` creative tab lists every block with correct models/textures — PBR blocks (`floor_tiling`, `carpet`, `concrete*`, `road`, `pavement`) show their color texture (not missing/black); blocks place and render under vanilla lighting; no dimension/render/event behavior. Close the client.

- [ ] **Step 5: Commit**

```bash
git add -A && git commit -m "feat: ModItems + creative tab — Stage 1 foundation complete"
```

---

## Self-Review

**Spec coverage** (spec §3 → tasks): build/ForgeGradle §3.1 → Task 1,3; mods.toml/@Mod/mixin/AT §3.2 → Tasks 2,4; registration+classes §3.3 → Tasks 6–9; assets copy §3.3 → Task 5; success criteria §4 → Task 9 Step 4 (+ per-task build gates). Out-of-scope §3.4 honored (no dimensions/rendering/CCA/events/config/TaCZ). No gaps.

**Placeholder scan:** The `// TODO(Stage 3/4)` markers are intentional, spec-mandated stubs (spec §3.3), not plan placeholders. Bulk-port steps ("one entry per id in master") point at the exact reference file to enumerate from plus a complete worked pattern — this is an API port where `master` is the authoritative source, not invented behavior.

**Type consistency:** `RegistryObject<T>` used throughout; `DeferredRegister.create(...)` + `register(IEventBus)` pattern identical across ModSounds/ModBlocks/ModBlockEntities/ModItems/ModCreativeTab; `SPBRevamped.MOD_ID` constant consistent; BE constructors take `(BlockPos, BlockState)` and reference their `ModBlockEntities` type — consistent with Task 8.
