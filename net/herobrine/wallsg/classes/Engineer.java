package net.herobrine.wallsg.classes;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.SkullMaker;
import net.herobrine.gamecore.*;
import net.herobrine.gamecore.Class;
import net.herobrine.wallsg.Config;
import net.herobrine.wallsg.WallsMain;
import net.herobrine.wallsg.game.Cannon;
import net.herobrine.wallsg.game.Upgrades;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftTask;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Engineer extends Class {

    private boolean hasCannonPlaced = false;
    private List<Location> cannonBlockLocations = new ArrayList<>();

    private List<Upgrades> upgrades = new ArrayList<>();
    private EditSession session;
    private long cooldown = 0;
    private long callbackCooldown = 0;

    private double baseHitSpeed = 1.3;

    private double baseRange = 10.0;

    private double range = baseRange;

    private int baseDamage = 3;

    private int damage = baseDamage;

    private double hitSpeed = baseHitSpeed;
    private ArmorStand hologram;
    private ArmorStand hitSpeedStatus;
    private ArmorStand damageStatus;
    private ArmorStand expiryStatus;
    private int expiresIn = 20;
    private Cannon cannon;
    private Arena arena;
    BukkitTask runnable;
    BukkitTask expiryRunnable;

    SkullMaker cannonItem = new SkullMaker(ChatColor.GOLD + "Placeable Cannon", Arrays.asList(ChatColor.GREEN + "Place down this cannon somewhere and it will",
            ChatColor.GREEN + "start shooting nearby enemies automatically!", " ", ChatColor.GREEN + "You pick up your cannon at any time by",
            ChatColor.GREEN + "using the "
                    + ChatColor.RED + "Callback Device", ChatColor.GREEN + "in your inventory.", "", ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.AQUA + "10s", ChatColor.DARK_GRAY + "Lifetime: " + ChatColor.AQUA + expiresIn + "s"),
            "http://textures.minecraft.net/texture/22523e15e9986355a1f851f43f750ee3f23c89ae123631da241f872ba7a781");
    SkullMaker callback = new SkullMaker(ChatColor.RED + "Callback Device", Arrays.asList(ChatColor.GREEN + "Right click with this in your hand to",
            ChatColor.GREEN + "call back your placed cannon!"),
            "http://textures.minecraft.net/texture/a4cc7efb63ee58a06e0ebbef7b7e9e858ffa178ca5c6271c8969e88e05ecf033");


    public Engineer(UUID uuid) {
        super(uuid, ClassTypes.ENGINEER);
    }

    @Override
    public void onStart(Player player) {
        ItemStack starterPickaxe = new ItemStack(Material.IRON_PICKAXE, 1);
        ItemMeta pickaxeMeta = starterPickaxe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, false);
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterPickaxe.setItemMeta(pickaxeMeta);

        ItemStack starterHelmet = new ItemStack(Material.GOLD_HELMET, 1);

        ItemMeta helmetMeta = starterHelmet.getItemMeta();

        helmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        helmetMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterHelmet.setItemMeta(helmetMeta);

        ItemStack starterChestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);

        ItemMeta chestplateMeta = starterChestplate.getItemMeta();

        chestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        chestplateMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterChestplate.setItemMeta(chestplateMeta);

        ItemStack starterLeggings = new ItemStack(Material.GOLD_LEGGINGS, 1);

        ItemMeta leggingsMeta = starterLeggings.getItemMeta();

        leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        leggingsMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterLeggings.setItemMeta(leggingsMeta);

        ItemStack starterBoots = new ItemStack(Material.GOLD_BOOTS, 1);

        ItemMeta bootsMeta = starterBoots.getItemMeta();

        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        bootsMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterBoots.setItemMeta(bootsMeta);
        player.getInventory().setHelmet(starterHelmet);
        player.getInventory().setChestplate(starterChestplate);
        player.getInventory().setLeggings(starterLeggings);
        player.getInventory().setBoots(starterBoots);
        player.getInventory().addItem(starterPickaxe);

        arena = Manager.getArena(player);




        player.getInventory().addItem(cannonItem.getSkull());


    }


    public EditSession getSession() {
        return session;
    }

    public void undoSession(Arena arena) {
        if (expiryRunnable != null) {
            expiryRunnable.cancel();
            expiresIn = -1;
            // this undoSession method only runs when the arena resets, so we want to ensure this runnable won't run anything else by accident by setting the time to -1,
            // even after cancelling it could still run 1 more time if the timing is perfect.
            expiryRunnable = null;
        }
        if(session != null) session.undo(session);
        if (hologram != null) {
            hologram.remove();
            hitSpeedStatus.remove();
            damageStatus.remove();
            expiryStatus.remove();

            hologram = null;
            hitSpeedStatus = null;
            damageStatus = null;
            expiryStatus = null;

        }
        if (cannon != null) {
            stopFiring();
            cannon.setActive(false);
            cannon = null;
        }

        if (arena.getState().equals(GameState.LIVE) && Bukkit.getPlayer(uuid) != null && hasCannonPlaced) {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Your cannon was removed because your class changed or the game was forcibly ended.");
            Bukkit.getPlayer(uuid).getInventory().remove(Material.SKULL_ITEM);
            Bukkit.getPlayer(uuid).getInventory().remove(Material.SKULL);
        }
    }


    // ACCEPTS WEST, NORTH, SOUTH, EAST. WEST IS DEFAULT -2,-2,-2 VECTOR.
    public Location getCenter(Location loc, String direction) {
        Vector vectorFromDespicableMe; //Yeah, I did that. Was it funny? No? Ok. He's pretty despicable you know...
        switch(direction){
            case "NORTH":
                vectorFromDespicableMe = new Vector(2,-2,-2);
                break;
            case "SOUTH":
                vectorFromDespicableMe = new Vector(-2,-2,2);
                break;
            case "EAST":
                vectorFromDespicableMe = new Vector(2,-2,2);
                break;
            default:
                vectorFromDespicableMe = new Vector(-2,-2,-2);
                break;
        }


    return loc.clone().add(vectorFromDespicableMe.getX(), vectorFromDespicableMe.getY(), vectorFromDespicableMe.getZ());
    }


    public List<Upgrades> getUpgrades() {return upgrades;}

    public Location getFiringLocation(Location loc, String direction) {
        Vector vectorFromDespicableMe;
        switch(direction){
            case "NORTH":
                vectorFromDespicableMe = new Vector(2.5,-1,-4.5);
                break;
            case "SOUTH":
                vectorFromDespicableMe = new Vector(-2.5,-1,4.5);
                break;
            case "EAST":
                vectorFromDespicableMe = new Vector(4.5,-1,2.5);
                break;
            default:
                vectorFromDespicableMe = new Vector(-4.5,-1,-2.5);
                break;
        }

        return loc.clone().add(vectorFromDespicableMe.getX(), vectorFromDespicableMe.getY(), vectorFromDespicableMe.getZ());
        }


    public void startFiring(long hitSpeed) {

       runnable = new BukkitRunnable() {

            @Override
            public void run() {
                if (cannon == null) {
                    cancel();
                    return;
                }

                if (!arena.getState().equals(GameState.LIVE)) {
                    cancel();
                        cannon.setTarget(null);
                        cannon.setActive(false);
                        cannon = null;
                        return;

                }


                    for (UUID uuid : arena.getPlayers()) {

                        Player player = Bukkit.getPlayer(uuid);

                        if (cannon.isActive() && !cannon.getFriendlyTeam().equals(arena.getTeam(player))
                                && !cannon.hasTarget() && !arena.getSpectators().contains(player.getUniqueId())) {
                            cannon.checkForTarget(player);
                        }
                    }

                    if (cannon.isActive() && cannon.hasTarget()) {
                        cannon.shootTarget();
                    }



            }
        }.runTaskTimer(WallsMain.getInstance(), 0L, hitSpeed);

    }

    public void startExpiryTimer(Player player) {
        expiresIn = 20;
        expiryRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!hasCannonPlaced){
                    cancel();
                    expiresIn = 20;
                    return;
                }
                if (!arena.getState().equals(GameState.LIVE)) {
                    cancel();
                    return;
                }

                if (expiresIn == 0) {
                    cancel();
                    expiresIn = 20;
                    session.undo(session);
                    stopFiring();
                    expiryStatus.remove();
                    hitSpeedStatus.remove();
                    damageStatus.remove();
                    hologram.remove();
                    expiryStatus = null;
                    hitSpeedStatus = null;
                    damageStatus = null;
                    hologram = null;
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1f, 1f);
                    cooldown = System.currentTimeMillis();
                    player.sendMessage(ChatColor.RED + "Your cannon has expired!");
                    GameCoreMain.getInstance().sendActionBar(player,ChatColor.RED + "Your cannon has expired!");

                    player.getInventory().remove(Material.SKULL_ITEM);
                    player.getInventory().remove(Material.SKULL_ITEM);
                    hasCannonPlaced = false;
                    player.getInventory().addItem(cannonItem.getSkull());

                    expiryRunnable = null;
                    return;
                }

                GameCoreMain.getInstance().sendActionBar(player, ChatColor.YELLOW + "Cannon Expires In: " + ChatColor.RED + expiresIn + "s");
                expiryStatus.setCustomName(ChatColor.YELLOW + "Expires In: " + ChatColor.RED + expiresIn + "s");
                expiresIn--;
            }
        }.runTaskTimer(WallsMain.getInstance(), 0L, 20L);
    }


    public void stopFiring() {
        if(runnable != null) {
            runnable.cancel();
            runnable = null;
        }
    }

    public void disableCannon() {
        cannon.setActive(false);
        cannon = null;
    }

    public void upgradeCannonHitSpeed(double speedSeconds, Player player) {
        long newTime = Math.round(speedSeconds * 20);
        hitSpeed = speedSeconds;

        player.sendMessage(ChatColor.GREEN + "You upgraded your cannon's hitspeed to " + ChatColor.AQUA + hitSpeed + "s" + ChatColor.GREEN + "!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1.25f);

        if (hasCannonPlaced) {
            stopFiring();
            hitSpeedStatus.setCustomName(ChatColor.GREEN + "Hit Speed: " + ChatColor.AQUA + hitSpeed + "s");
            startFiring(newTime);
        }
    }

    public void upgradeCannonRange(Player player, double range) {
        this.range = range;
        player.sendMessage(ChatColor.GREEN + "You upgraded your cannon's range to " + ChatColor.AQUA + range + ChatColor.GREEN + "!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1.25f);

        if (hasCannonPlaced) {
            cannon.setAttackRange(range);
            cannon.setProjectileRange(range);
        }

    }

    public void upgradeCannonDamage(Player player, int damage) {
        this.damage = damage;
        player.sendMessage(ChatColor.GREEN + "You upgraded your cannon's damage to " + ChatColor.AQUA + damage + ChatColor.GREEN + "!");
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1.25f);
        if (hasCannonPlaced) {
            cannon.setDamage(damage);
            damageStatus.setCustomName(ChatColor.translateAlternateColorCodes('&', "&aDamage: &b" + damage));
        }
    }

    public int getBaseDamage() {return baseDamage;}
    public double getBaseRange() {return baseRange;}
    public double getBaseHitSpeed() {return baseHitSpeed;}

    public long getCannonHitSpeed(double speedSeconds) {
        long time = Math.round(speedSeconds * 20);

        return time;
    }



    public int getAngle(String direction) {

        switch (direction) {
            case "NORTH": return 90;
            case "SOUTH": return 270;
            case "EAST": return 180;
            default: return 0;

        }

    }

    public String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "WEST";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            // It's actually Northeast. But I work with simple values here because that's all that's needed for this!
            return "NORTH";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "NORTH";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            // It's actually Southeast.
            return "EAST";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "EAST";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
          // It's actually southwest.
            return "SOUTH";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "SOUTH";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
           //It's actually Northwest.
            return "WEST";
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return "WEST";
        }
        return null;
    }


    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getPlayer().getUniqueId() != getUUID()) return;

        if (e.getBlockPlaced().getType().equals(Material.SKULL_ITEM) || e.getBlockPlaced().getType().equals(Material.SKULL)) {
            if (hasCannonPlaced) {
                e.setCancelled(true);
                return;
            }

            else {


                Player player = e.getPlayer();
                Block block = e.getBlockPlaced();
                Location loc = e.getBlockPlaced().getLocation();
                Block blockDown = block.getRelative(BlockFace.DOWN);

                if (!arena.getState().equals(GameState.LIVE)) {
                    player.sendMessage(ChatColor.RED + "You can't place the cannon now!");
                    return;
                }
                if (!arena.getwallsSGGame().getHasFallen()) {
                    player.sendMessage(ChatColor.RED + "You can only place your cannon after the Walls Fall! Get mining!");
                    return;
                }

                if (System.currentTimeMillis() - cooldown < 10000) {
                    double attackTime = (10000 - (System.currentTimeMillis() - cooldown)) / 1000.0;
                    player.sendMessage(HerobrinePVPCore.translateString("&cYou can place the cannon again in &c&l" + attackTime + "s" ));
                    e.setCancelled(true);
                    return;
                }

                if (blockDown.getType().equals(Material.WOOL) || blockDown.getType().equals(Material.CHEST) || blockDown.getType().equals(Material.CARPET)) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot place a cannon on this block!");
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, .7f, 0.5f);
                    return;
                }


                Location firstPos = new Location(loc.getWorld(), loc.getX() + 6, loc.getY() + 1, loc.getZ() - 6);
                Location secondPos = new Location(loc.getWorld(), loc.getX() - 6, loc.getY() + 7, loc.getZ() + 6);
                CuboidSelection selec = new CuboidSelection(loc.getWorld(), firstPos, secondPos);
                CuboidRegion region = new CuboidRegion(BukkitUtil.getLocalWorld(selec.getWorld()),
                        selec.getNativeMinimumPoint(), selec.getNativeMaximumPoint());

                int arenaID = Manager.getArena(player).getID();
                int maxId = net.herobrine.wallsg.Config.getMaxChestIds(arenaID);
                int i = 0;
                while(i < Config.getMaxChestIds(arenaID) + 1) {

                    if (net.herobrine.wallsg.Config.getChestLocation(arenaID, i).distance(loc) < 5) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot place a cannon here! Too close to the center!");
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, .7f, 0.5f);
                        return;
                    }
                    i++;
                }
                Iterator<BlockVector> blocks = region.iterator();
                while (blocks.hasNext()) {
                    BlockVector vector = blocks.next();
                    if (region.getWorld().getBlock(vector).getId() != 0) {
                        player.sendMessage(ChatColor.RED + "You can't place a cannon here! Not enough space.");
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, .7f, 0.5f);
                        return;
                    }
                }

                File file = new File(WallsMain.getInstance().getDataFolder(), "wsgcannon.schematic");
                session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 100);

                try {
                    CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(file).load(file);


                    String direction = getCardinalDirection(player);
                    clipboard.rotate2D(getAngle(direction));

                    clipboard.paste(session, new Vector(loc.getX(), loc.getY() + 3, loc.getZ()), false);
                    Location pasteLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 3, loc.getZ());
                    hasCannonPlaced = true;

                    if(arena.getTeam(player).equals(Teams.BLUE)) getFiringLocation(pasteLoc, direction).getBlock().setData((byte) 11);
                    if(arena.getTeam(player).equals(Teams.GREEN)) getFiringLocation(pasteLoc, direction).getBlock().setData((byte) 5);
                    if(arena.getTeam(player).equals(Teams.YELLOW)) getFiringLocation(pasteLoc, direction).getBlock().setData((byte) 4);

                    hologram = (ArmorStand) player.getWorld().spawnEntity(new Location(loc.getWorld(), getCenter(pasteLoc, direction).getX() + 0.5, getCenter(pasteLoc, direction).getY() + 3.5, getCenter(pasteLoc,direction).getZ() + 0.5), EntityType.ARMOR_STAND);

                    hologram.setVisible(false);
                    hologram.setCustomNameVisible(true);
                    hologram.setCustomName(arena.getTeam(player).getColor() + player.getName() + "'s Cannon");
                    hologram.setGravity(false);

                    hitSpeedStatus = (ArmorStand) player.getWorld().spawnEntity(new Location(loc.getWorld(), getCenter(pasteLoc, direction).getX() + 0.5, getCenter(pasteLoc, direction).getY() + 2.5, getCenter(pasteLoc,direction).getZ() + 0.5), EntityType.ARMOR_STAND);
                    hitSpeedStatus.setVisible(false);
                    hitSpeedStatus.setCustomNameVisible(true);
                    hitSpeedStatus.setCustomName(ChatColor.translateAlternateColorCodes('&', "&aHit Speed: &b" + hitSpeed + "&bs"));
                    hitSpeedStatus.setGravity(false);

                    damageStatus = (ArmorStand) player.getWorld().spawnEntity(new Location(loc.getWorld(), getCenter(pasteLoc, direction).getX() + 0.5, getCenter(pasteLoc, direction).getY() + 1.5, getCenter(pasteLoc,direction).getZ() + 0.5), EntityType.ARMOR_STAND);
                    damageStatus.setVisible(false);
                    damageStatus.setCustomNameVisible(true);
                    damageStatus.setCustomName(ChatColor.translateAlternateColorCodes('&', "&aDamage: &b" + damage));
                    damageStatus.setGravity(false);


                    expiryStatus = (ArmorStand) player.getWorld().spawnEntity(new Location(loc.getWorld(), getCenter(pasteLoc, direction).getX() + 0.5, getCenter(pasteLoc, direction).getY() + 1, getCenter(pasteLoc, direction).getZ() + 0.5), EntityType.ARMOR_STAND);
                    expiryStatus.setVisible(false);
                    expiryStatus.setCustomNameVisible(true);
                    expiryStatus.setGravity(false);

                    startExpiryTimer(player);

                    arena.freezeEntity(hologram);
                    arena.freezeEntity(hitSpeedStatus);
                    arena.freezeEntity(damageStatus);
                    arena.freezeEntity(expiryStatus);

                    player.sendMessage(ChatColor.GREEN + "You have placed your cannon!");
                    player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);

                    callbackCooldown = System.currentTimeMillis();

                    if(cannon == null) cannon = new Cannon(Cannon.CannonType.NORMAL,"cannon" + player.getName(), arena.getID(), getFiringLocation(pasteLoc, direction), player, arena.getTeam(player),  true);
                    cannon.setDamage(damage);
                    cannon.setAttackRange(range);
                    cannon.setProjectileRange(range);
                    cannon.setShootLocation(getFiringLocation(pasteLoc, direction));


                    if (e.getItemInHand().getType().equals(Material.AIR) || e.getItemInHand().getType().equals(Material.SKULL) ||
                            e.getItemInHand().getType().equals(Material.SKULL_ITEM)) player.setItemInHand(callback.getSkull());
                   else  player.getInventory().addItem(callback.getSkull());

                    startFiring(getCannonHitSpeed(hitSpeed));
                }
                catch(MaxChangedBlocksException | DataException | IOException exception) {
                    player.sendMessage(ChatColor.RED + "Error! " + exception.getCause());
                    player.sendMessage(ChatColor.RED + "Stack trace printed to console.");
                    exception.printStackTrace();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getUniqueId() != getUUID()) return;
        Iterator<ItemStack> items = e.getDrops().iterator();

        while (items.hasNext()) {

            ItemStack stack = items.next();
            if (stack.getType().equals(Material.SKULL_ITEM) || stack.getType().equals(Material.SKULL)) items.remove();
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getUniqueId() != getUUID()) return;
        if (e.getItem() == null || e.getItem().getType().equals(Material.AIR)) return;
        if (!e.getItem().hasItemMeta()) return;
        if (!e.getItem().getType().equals(Material.SKULL_ITEM) && !e.getItem().getType().equals(Material.SKULL)) return;

        else if (hasCannonPlaced) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                if (System.currentTimeMillis() - callbackCooldown < 1500) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Please wait a bit before doing this!");
                    return;
                }

                stopFiring();
                session.undo(session);
                hologram.remove();
                hitSpeedStatus.remove();
                damageStatus.remove();
                expiryStatus.remove();
                hologram = null;
                hitSpeedStatus = null;
                damageStatus = null;
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.NOTE_BASS_GUITAR, 1f, 1f);
                e.getPlayer().sendMessage(ChatColor.RED + "Your cannon has been removed!");
                cooldown = System.currentTimeMillis();
                hasCannonPlaced = false;

                if (e.getItem().getType().equals(Material.AIR) || e.getItem().getType().equals(Material.SKULL) ||
                        e.getItem().getType().equals(Material.SKULL_ITEM)) e.getPlayer().setItemInHand(cannonItem.getSkull());
                else  e.getPlayer().getInventory().addItem(cannonItem.getSkull());
        }

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().getUniqueId() != getUUID()) return;
        if (e.getItemDrop().getItemStack().getType().equals(Material.SKULL_ITEM) || e.getItemDrop().getItemStack().getType().equals(Material.SKULL)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You can't drop this item!");
        }
    }


}
