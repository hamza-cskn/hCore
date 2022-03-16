package com.hakan.core;

import com.hakan.core.command.HCommand;
import com.hakan.core.command.HCommandExecutor;
import com.hakan.core.hooks.Metrics;
import com.hakan.core.message.MessageAPI;
import com.hakan.core.message.bossbar.HBarColor;
import com.hakan.core.message.bossbar.HBarFlag;
import com.hakan.core.message.bossbar.HBarStyle;
import com.hakan.core.message.bossbar.HBossBar;
import com.hakan.core.message.title.HTitle;
import com.hakan.core.packet.HPacketHandler;
import com.hakan.core.particle.HParticle;
import com.hakan.core.particle.HParticleAPI;
import com.hakan.core.scheduler.HScheduler;
import com.hakan.core.ui.inventory.HInventory;
import com.hakan.core.ui.inventory.HInventoryHandler;
import com.hakan.core.ui.inventory.builder.HInventoryBuilder;
import com.hakan.core.ui.sign.HSign;
import com.hakan.core.ui.sign.HSignHandler;
import com.hakan.core.worldborder.HWorldBorderHandler;
import com.hakan.core.worldborder.border.HBorderColor;
import com.hakan.core.worldborder.border.HWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Main class of this core.
 * You can reach all APIs from this
 * class as static.
 */
@SuppressWarnings({"unused"})
public class HCore {

    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return HCore.instance;
    }

    /**
     * Initializes all APIs.
     *
     * @param plugin Instance of main class.
     */
    public static void initialize(@Nonnull JavaPlugin plugin) {
        HCore.instance = Objects.requireNonNull(plugin, "plugin cannot be null!");

        Metrics.initialize(plugin);
        HPacketHandler.initialize(plugin);
        HWorldBorderHandler.initialize(plugin);
        HInventoryHandler.initialize(plugin);
        HSignHandler.initialize(plugin);
        MessageAPI.initialize();
        HParticleAPI.initialize();
    }


    /*
    OTHERS
     */
    public static String getVersionString() {
        return Bukkit.getServer().getClass().getName().split("\\.")[3];
    }


    /*
    SCHEDULER
     */
    public static HScheduler scheduler(boolean async) {
        return new HScheduler(HCore.instance, async);
    }

    public static HScheduler asyncScheduler() {
        return new HScheduler(HCore.instance, true);
    }

    public static HScheduler syncScheduler() {
        return new HScheduler(HCore.instance, false);
    }


    /*
    INVENTORY API
     */
    public static Map<UUID, HInventory> getInventoryContentSafe() {
        return HInventoryHandler.getContentSafe();
    }

    public static Map<UUID, HInventory> getInventoryContent() {
        return HInventoryHandler.getContent();
    }

    public static Collection<HInventory> getInventoryValuesSafe() {
        return HInventoryHandler.getValuesSafe();
    }

    public static Collection<HInventory> getInventoryValues() {
        return HInventoryHandler.getValues();
    }

    public static Optional<HInventory> findInventoryByPlayer(Player player) {
        return HInventoryHandler.findByPlayer(player);
    }

    public static HInventory getInventoryByPlayer(Player player) {
        return HInventoryHandler.getByPlayer(player);
    }

    public static Optional<HInventory> findInventoryByUID(UUID uid) {
        return HInventoryHandler.findByUID(uid);
    }

    public static HInventory getInventoryByUID(UUID uid) {
        return HInventoryHandler.getByUID(uid);
    }

    public static HInventoryBuilder buildInventory(String id) {
        return new HInventoryBuilder(id);
    }

    public static HInventory createInventory(String id, String title, InventoryType type, int size) {
        return new HInventoryBuilder(id).title(title).type(type).size(size).build();
    }


    /*
    SIGN
     */
    public static Map<UUID, HSign> getSignContentSafe() {
        return HSignHandler.getContentSafe();
    }

    public static Map<UUID, HSign> getSignContent() {
        return HSignHandler.getContent();
    }

    public static Collection<HSign> getSignValuesSafe() {
        return HSignHandler.getValuesSafe();
    }

    public static Collection<HSign> getSignValues() {
        return HSignHandler.getValues();
    }

    public static Optional<HSign> findSignByPlayer(Player player) {
        return HSignHandler.findByPlayer(player);
    }

    public static HSign getSignByPlayer(Player player) {
        return HSignHandler.getByPlayer(player);
    }

    public static Optional<HSign> findSignByUID(UUID uid) {
        return HSignHandler.findByUID(uid);
    }

    public static HSign getSignByUID(UUID uid) {
        return HSignHandler.getByUID(uid);
    }

    public static HSign createSign(Material type, String... lines) {
        return HSignHandler.create(type, lines);
    }


    /*
    COMMAND
     */
    public static HCommand registerCommand(String command, String... aliases) {
        return new HCommand(command, aliases).register();
    }

    public static void registerCommand(HCommandExecutor... executors) {
        Arrays.asList(executors).forEach(HCommandExecutor::register);
    }


    /*
    PACKET
     */
    public static void sendPacket(Player player, Object packet) {
        HPacketHandler.getPacketPlayerMap().get(player).send(packet);
    }

    public static void sendPacket(Player player, Object... packets) {
        HPacketHandler.getPacketPlayerMap().get(player).send(packets);
    }


    /*
    PARTICLE
     */
    public static void playParticle(Player player, Location location, HParticle hParticle) {
        HParticleAPI.play(player, location, hParticle);
    }

    public static void playParticle(Collection<Player> players, Location location, HParticle hParticle) {
        HParticleAPI.play(players, location, hParticle);
    }


    /*
    MESSAGE
     */
    public static void sendTitle(Player player, HTitle hTitle) {
        MessageAPI.sendTitle(player, hTitle);
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        MessageAPI.sendTitle(player, title, subTitle);
    }

    public static void sendTitle(Player player, String title, String subTitle, int stay, int fadein, int fadeout) {
        MessageAPI.sendTitle(player, title, subTitle, stay, fadein, fadeout);
    }

    public static void sendTitle(Collection<Player> players, HTitle hTitle) {
        MessageAPI.sendTitle(players, hTitle);
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle) {
        MessageAPI.sendTitle(players, title, subTitle);
    }

    public static void sendTitle(Collection<Player> players, String title, String subTitle, int stay, int fadein, int fadeout) {
        MessageAPI.sendTitle(players, title, subTitle, stay, fadein, fadeout);
    }

    public static void sendActionBar(Player player, String text) {
        MessageAPI.sendActionBar(player, text);
    }

    public static void sendActionBar(Collection<Player> players, String text) {
        MessageAPI.sendActionBar(players, text);
    }

    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style, HBarFlag... flags) {
        return MessageAPI.createBossBar(title, color, style, flags);
    }

    public static HBossBar createBossBar(String title, HBarColor color, HBarStyle style) {
        return MessageAPI.createBossBar(title, color, style);
    }


    /*
    WORLD BORDER
     */
    public static Collection<HWorldBorder> getBorderContentSafe() {
        return HWorldBorderHandler.getContentSafe();
    }

    public static Collection<HWorldBorder> getBorderContent() {
        return HWorldBorderHandler.getContent();
    }

    public static Optional<HWorldBorder> findBorderByPlayer(Player player) {
        return HWorldBorderHandler.findByPlayer(player);
    }

    public static HWorldBorder getBorderByPlayer(Player player) {
        return HWorldBorderHandler.getByPlayer(player);
    }

    public static HWorldBorder createBorder(Location location, double size, double damageAmount, double damageBuffer, int warningDistance, int warningTime, HBorderColor color) {
        return HWorldBorderHandler.create(location, size, damageAmount, damageBuffer, warningDistance, warningTime, color);
    }
}