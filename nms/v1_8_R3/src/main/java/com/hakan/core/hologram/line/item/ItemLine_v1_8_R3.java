package com.hakan.core.hologram.line.item;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.utils.Validate;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class ItemLine_v1_8_R3 implements ItemLine {

    private World world;
    private EntityItem nmsItem;
    private final HHologram hologram;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private ItemLine_v1_8_R3(@Nonnull HHologram hHologram, @Nonnull Location location) {
        this.world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hHologram, "hologram class cannot be null!");
        this.armorStand = new EntityArmorStand(this.world, location.getX(), location.getY(), location.getZ());

        this.armorStand.getDataWatcher().watch(10, (byte) 16);
        this.armorStand.b(new NBTTagCompound());
        this.armorStand.setArms(false);
        this.armorStand.setBasePlate(false);
        this.armorStand.setGravity(false);
        this.armorStand.setInvisible(true);
        this.armorStand.setSmall(true);
        this.armorStand.setCustomNameVisible(false);
        this.armorStand.setCustomName(" ");
        this.armorStand.setHealth(114.13f);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.nmsItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "item stack cannot be null!");

        Location loc = this.hologram.getLocation();
        this.nmsItem = new EntityItem(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityDestroy(this.nmsItem.getId()),
                new PacketPlayOutSpawnEntity(this.nmsItem, 2),
                new PacketPlayOutEntityTeleport(this.nmsItem),
                new PacketPlayOutEntityMetadata(this.nmsItem.getId(), this.nmsItem.getDataWatcher(), true),
                new PacketPlayOutAttachEntity(0, this.nmsItem, this.armorStand),

                new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return this.armorStand.getBukkitEntity().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");

        this.world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        if (!this.world.equals(this.armorStand.getWorld())) this.armorStand.spawnIn(this.world);
        this.armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownViewersAsPlayer(),
                new PacketPlayOutEntityTeleport(this.armorStand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        if (this.nmsItem != null) {
            HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                    new PacketPlayOutSpawnEntityLiving(this.armorStand),
                    new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true),
                    new PacketPlayOutEntityTeleport(this.armorStand),

                    new PacketPlayOutEntityDestroy(this.nmsItem.getId()),
                    new PacketPlayOutSpawnEntity(this.nmsItem, 2),
                    new PacketPlayOutEntityTeleport(this.nmsItem),
                    new PacketPlayOutEntityMetadata(this.nmsItem.getId(), this.nmsItem.getDataWatcher(), true),
                    new PacketPlayOutAttachEntity(0, this.nmsItem, this.armorStand));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.nmsItem.getId()),
                new PacketPlayOutEntityDestroy(this.armorStand.getId()));
    }
}