package com.hakan.core.hologram.line.entity;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import net.minecraft.server.v1_9_R2.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public final class HHologramArmorStand_v1_9_R2 extends EntityArmorStand implements HHologramArmorStand {

    private final HHologram hologram;

    /**
     * {@inheritDoc}
     */
    private HHologramArmorStand_v1_9_R2(@Nonnull HHologram hHologram, @Nonnull Location location) {
        super(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ());
        super.setMarker(true);
        super.setArms(false);
        super.setBasePlate(false);
        super.setGravity(false);
        super.setInvisible(true);
        super.setSmall(true);
        super.setCustomNameVisible(true);
        super.setHealth(114.13f);
        this.hologram = hHologram;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return super.getCustomName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        super.setCustomName(Objects.requireNonNull(text, "text cannot be null!"));
        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityMetadata(super.getId(), super.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return super.getBukkitEntity().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null");

        World world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        if (!world.equals(super.getWorld())) super.spawnIn(world);
        super.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        HCore.sendPacket(Objects.requireNonNull(players, "players cannot be null"),
                new PacketPlayOutSpawnEntityLiving(this),
                new PacketPlayOutEntityMetadata(super.getId(), super.getDataWatcher(), true),
                new PacketPlayOutEntityTeleport(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Objects.requireNonNull(players, "players cannot be null"),
                new PacketPlayOutEntityDestroy(super.getId()));
    }
}