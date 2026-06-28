package com.sp.cca_stuff;

import com.sp.init.BackroomsLevels;
import com.sp.world.events.AbstractEvent;
import com.sp.world.levels.BackroomsLevel;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Optional;

public class WorldEvents implements AutoSyncedComponent, ServerTickingComponent {
    private final World world;

    private AbstractEvent activeEvent;
    public int ticks;
    private int delay;

    public WorldEvents(World world) {
        this.world = world;
        this.ticks = 0;
        this.delay = 1800;
    }

    public void setActiveEvent(AbstractEvent activeEvent) {
        this.activeEvent = activeEvent;
    }
    public AbstractEvent getActiveEvent() {
        return this.activeEvent;
    }

    public void sync() {
        InitializeComponents.EVENTS.sync(this.world);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (BackroomsLevel level: BackroomsLevels.BACKROOMS_LEVELS) {
            if (this.world.getRegistryKey() == level.getWorldKey()) {
                level.readFromNbt(tag);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        for (BackroomsLevel level: BackroomsLevels.BACKROOMS_LEVELS) {
            if (this.world.getRegistryKey() == level.getWorldKey()) {
                level.writeToNbt(tag);
            }
        }
    }

    @Override
    public void serverTick() {
        if (world != null && !world.getPlayers().isEmpty() && BackroomsLevels.isInBackrooms(world.getRegistryKey())) {
            ticks++;

            tickWorldEvents();
        }

        shouldSync();
    }

    private void tickWorldEvents() {
        if (activeEvent == null) {
            this.delay--;
            if (this.delay > 0) {
                return;
            }

            this.delay = 0;

            Optional<BackroomsLevel> currentDimension = BackroomsLevels.getLevel(world);

            if (currentDimension.isEmpty()) {
                return;
            }

            this.activeEvent = currentDimension.get().getRandomEvent(world);
            activeEvent.init(this.world);
            ticks = 0;
            this.delay = currentDimension.get().nextEventDelay();

            return;
        }

        if (activeEvent.duration() <= ticks) {
            activeEvent.finish(this.world);
            if (activeEvent.isDone()) activeEvent = null;
        } else {
            activeEvent.ticks(ticks, this.world);
        }
    }

    private void shouldSync() {
        boolean sync = false;

        for (BackroomsLevel backroomsLevel : BackroomsLevels.BACKROOMS_LEVELS) {
            if (backroomsLevel.shouldSync()) {
                sync = true;
                break;
            }
        }

        if (sync){
            this.sync();
        }
    }
}
