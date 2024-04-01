package net.herobrine.wallsg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import net.herobrine.core.LevelRewards;
import net.herobrine.gamecore.*;
import net.herobrine.wallsg.game.ChestManager;
import net.herobrine.wallsg.game.CustomDeathCause;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.regions.CuboidRegion;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.SongPlayer;
import net.herobrine.core.Songs;

public class Game {
	private Arena arena;
	private WallsMain main;
	private int seconds;
	private int endSeconds;
	private int timer;
	private Teams winningTeam;
	public static final List<UUID> alivePlayers = new ArrayList<>();
	public static HashMap<UUID, Teams> alivePlayers1 = new HashMap<>();

	private static HashMap<Location, Material> blockLocations = new HashMap<>();
	private static ArrayList<Location> placedBlockLocations = new ArrayList<>();
	public static HashMap<UUID, Integer> kills = new HashMap<>();
	private HashMap<UUID, CustomDeathCause> customDeathCause = new HashMap<UUID, CustomDeathCause>();
	private boolean hasFallen;
	private boolean suddenDeath;
	private boolean isFrozen;
	public int aliveRedPlayers;
	public int aliveBluePlayers;
	public int aliveGreenPlayers;
	public int aliveYellowPlayers;

	public Game(Arena arena) {
		this.arena = arena;

		this.seconds = 300;
	}

	public Game(WallsMain main) {
		this.main = main;
	}

	public static List<UUID> getAlivePlayers() {
		return alivePlayers;
	}

	public void isGameOver() {
		if (arena.getState().equals(GameState.LIVE_ENDING)) return;
		WorldBorder border = net.herobrine.wallsg.Config.getArenaWorld(arena.getID()).getWorldBorder();

		if (aliveBluePlayers == 0 && aliveGreenPlayers == 0 && aliveYellowPlayers == 0 && aliveRedPlayers >= 1) {
// red wins
			arena.setState(GameState.LIVE_ENDING);
			winningTeam = Teams.RED;

			for (UUID uuid : arena.getPlayers()) {

				Player player = (Player) Bukkit.getPlayer(uuid);

				if (arena.getTeam(player) == winningTeam) SongPlayer.playSong(player, Songs.WSGWIN);
				else SongPlayer.playSong(player, Songs.WSGLOSE);


			}


			startEnding(winningTeam.getDisplay());

		}

		else if (aliveRedPlayers == 0 && aliveYellowPlayers == 0 && aliveGreenPlayers == 0 && aliveBluePlayers >= 1) {
			arena.setState(GameState.LIVE_ENDING);
			winningTeam = Teams.BLUE;

			for (UUID uuid : arena.getPlayers()) {
				Player player = (Player) Bukkit.getPlayer(uuid);

				if (arena.getTeam(player) == winningTeam) SongPlayer.playSong(player, Songs.WSGWIN);
				else SongPlayer.playSong(player, Songs.WSGLOSE);
			}


			startEnding(winningTeam.getDisplay());

		}

		else if (aliveRedPlayers == 0 && aliveBluePlayers == 0 && aliveGreenPlayers == 0 && aliveYellowPlayers >= 1) {
			arena.setState(GameState.LIVE_ENDING);
			winningTeam = Teams.YELLOW;
			for (UUID uuid : arena.getPlayers()) {
				Player player = (Player) Bukkit.getPlayer(uuid);
				if (arena.getTeam(player) == winningTeam) SongPlayer.playSong(player, Songs.WSGWIN);
				else SongPlayer.playSong(player, Songs.WSGLOSE);
			}
			arena.setState(GameState.LIVE_ENDING);

			startEnding(winningTeam.getDisplay());

			// yellow wins

		} else if (aliveRedPlayers == 0 && aliveBluePlayers == 0 && aliveYellowPlayers == 0 && aliveGreenPlayers >= 1) {
			arena.setState(GameState.LIVE_ENDING);
			winningTeam = Teams.GREEN;
			for (UUID uuid : arena.getPlayers()) {
				Player player = (Player) Bukkit.getPlayer(uuid);

				if (arena.getTeam(player) == winningTeam) SongPlayer.playSong(player, Songs.WSGWIN);
				else SongPlayer.playSong(player, Songs.WSGLOSE);
			}
			arena.setState(GameState.LIVE_ENDING);

			startEnding(winningTeam.getDisplay());

			// green wins
		}

		else {

			if (seconds == 0 && hasFallen && !suddenDeath) {
				seconds = 120;
				suddenDeath = true;
				border.setSize(100);
				for (UUID uuid : arena.getPlayers()) {
					Player player = Bukkit.getPlayer(uuid);
					GameCoreMain.getInstance().sendTitle(player, "&c&lWATCH OUT!", "&eThe world border is shrinking!",
							1, 2, 0);
					if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().contains("Walls SG")) player.getScoreboard().getTeam("wsgtimer").setPrefix(ChatColor.GREEN + "Game End ");
					player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lThe World Border begins to shrink.."));
				}

				border.setDamageBuffer(0.0);
				border.setSize(10, 100);


			}

			else if (seconds == 0 && hasFallen && suddenDeath) {
				arena.setState(GameState.LIVE_ENDING);
				arena.sendMessage(ChatColor.YELLOW + "DRAW!");
				startEnding(ChatColor.YELLOW + "DRAW!");
			}
		}

	}

	public static HashMap<Location, Material> getBlockLocations() {return blockLocations;}

	public HashMap<UUID, CustomDeathCause> getCustomDeathCause() {
		return customDeathCause;
	}

	public CustomDeathCause getCustomDeathCause(Player player) {return customDeathCause.get(player.getUniqueId());}

	public boolean hasCustomDeathCause(Player player) {
		if(customDeathCause.containsKey(player.getUniqueId()) && player.getLastDamageCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) return true;
		return false;
	}


	public static ArrayList<Location> getPlacedBlockLocations() {
		return placedBlockLocations;
	}

	public boolean getHasFallen() {
		return hasFallen;
	}

	@SuppressWarnings("deprecation")
	public void start() {
		seconds = 300;
		WorldBorder border = net.herobrine.wallsg.Config.getArenaWorld(arena.getID()).getWorldBorder();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		aliveRedPlayers = 0;
		aliveBluePlayers = 0;
		aliveYellowPlayers = 0;
		aliveGreenPlayers = 0;

		if (arena.getType() != GameType.MODIFIER) arena.setState(GameState.LIVE);


		hasFallen = false;
		suddenDeath = false;
		isFrozen = false;
		border.setCenter(net.herobrine.wallsg.Config.getBorderCenterX(arena.getID()), net.herobrine.wallsg.Config.getBorderCenterZ(arena.getID()));
		border.setSize(1000);

		CuboidSelection selec = new CuboidSelection(net.herobrine.wallsg.Config.getArenaWorld(arena.getID()),
				net.herobrine.wallsg.Config.getFirstPosition(arena.getID()), net.herobrine.wallsg.Config.getSecondPosition(arena.getID()));
		CuboidRegion region = new CuboidRegion(BukkitUtil.getLocalWorld(selec.getWorld()),
				selec.getNativeMinimumPoint(), selec.getNativeMaximumPoint());

		Iterator<BlockVector> i = region.iterator();
		World world = Bukkit.getWorld(region.getWorld().getName());
		while (i.hasNext()) {
			BlockVector vector = i.next();

			if (region.getWorld().getBlock(vector).getId() == 16) {

				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.COAL_ORE);
			}

			else if (region.getWorld().getBlock(vector).getId() == 15) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.IRON_ORE);
			}

			else if (region.getWorld().getBlock(vector).getId() == 14) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.GOLD_ORE);
			} else if (region.getWorld().getBlock(vector).getId() == 56) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.DIAMOND_ORE);
			}

			else if (region.getWorld().getBlock(vector).getId() == 30) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.WEB);
			}

			else if (region.getWorld().getBlock(vector).getId() == 41) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.GOLD_BLOCK);
			}

			else if (region.getWorld().getBlock(vector).getId() == 22) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.LAPIS_BLOCK);
			}

			else if (region.getWorld().getBlock(vector).getId() == 152) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.REDSTONE_BLOCK);
			}

			else if (region.getWorld().getBlock(vector).getId() == 133) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(),
						vector.getY(), vector.getZ()), Material.EMERALD_BLOCK);
			}

			if (region.getWorld().getBlock(vector).getType() == 19) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(), vector.getY(), vector.getZ()), Material.SPONGE);
				double dub = random.nextDouble();
				// Iron/Coal 50/50;
				if (dub <= .5)world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.COAL_ORE);
				else world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.IRON_ORE);
			} else if (region.getWorld().getBlock(vector).getType() == 103) {
				// CHANCES:
				// Diamond: 75%
				// Gold: 25%
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(), vector.getY(), vector.getZ()), Material.MELON_BLOCK);
				double dub = random.nextDouble();
				if (dub <= .25) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.GOLD_ORE);
				else if (dub <= .75) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.DIAMOND_ORE);
				else world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.DIAMOND_ORE);

			} else if (region.getWorld().getBlock(vector).getType() == 47) {
				blockLocations.put(new Location(Bukkit.getWorld(region.getWorld().getName()), vector.getX(), vector.getY(), vector.getZ()), Material.BOOKSHELF);
				double dub = random.nextDouble();
				//VANILLA CHANCES
				// LAPIS: 75%
				// OBSIDIAN: 10%
				// EMERALD: 15%
				//MODIFIER CHANCES
				// LAPIS: 65%
				// EMERALD: 10%
				// REDSTONE: 25%

				if (arena.getType().equals(GameType.VANILLA)) {
					if (dub <=.10) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.OBSIDIAN);
					else if (dub <=.15) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.EMERALD_ORE);
					else if (dub <= .75) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.LAPIS_ORE);
					else world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.LAPIS_ORE);
				}

				if (arena.getType().equals(GameType.MODIFIER)) {
					if (dub <= .10) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.EMERALD_ORE);
					else if (dub <=.25) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.REDSTONE_ORE);
					else if (dub<= .65) world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.LAPIS_ORE);
					else world.getBlockAt(new Location(world, vector.getX(), vector.getY(), vector.getZ())).setType(Material.LAPIS_ORE);
				}
			}

		}

		System.out.println("First Position: " + net.herobrine.wallsg.Config.getFirstPosition(arena.getID()));
		System.out.println("Second Position: " + net.herobrine.wallsg.Config.getSecondPosition(arena.getID()));
		System.out.println("Region World: " + BukkitUtil.getLocalWorld(selec.getWorld()));
		System.out.println("Region First Position: " + selec.getNativeMinimumPoint());
		System.out.println("Region Second Position: " + selec.getNativeMaximumPoint());

		// Walls SG
		// 6/29/21 wsg2
		// Team: RED
		//
		// Next Event:
		// Walls Fall 5:00
		//
		// Players Left: 5
		// Kills: 0
		//
		// herobrinepvp.beastmc.com
		DateFormat df = new SimpleDateFormat("MM/dd/yy");
		Date dateobj = new Date();
		System.out.println(df.format(dateobj));
		System.out.println("Walls SG Game Starting In Arena " + arena.getID() + "!");
//
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

		if (arena.getType().equals(GameType.VANILLA)) {


			for (UUID uuid : arena.getPlayers()) {
				System.out.println("WORKING ON UUID:" + uuid);
				Player player = Bukkit.getPlayer(uuid);
				alivePlayers.add(uuid);
				player.setDisplayName(arena.getTeam(player).getColor() + player.getName());
				player.getInventory().clear();
				player.getInventory().setHelmet(starterHelmet);
				player.getInventory().setChestplate(starterChestplate);
				player.getInventory().setLeggings(starterLeggings);
				player.getInventory().setBoots(starterBoots);
				player.getInventory().addItem(starterPickaxe);

				HerobrinePVPCore.getFileManager().setGameStats(player.getUniqueId(), Games.WALLS_SG, "roundsPlayed",
						HerobrinePVPCore.getFileManager().getGameStats(player.getUniqueId(), Games.WALLS_SG,
								"roundsPlayed") + 1);

				alivePlayers1.put(player.getUniqueId(), arena.getTeam(player));
				Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
				Objective obj = board.registerNewObjective("game", "dummy");
				Objective healthObj = board.registerNewObjective("wsgHP", "health");
				healthObj.setDisplayName(ChatColor.RED + "❤");
				healthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);

				obj.setDisplayName(ChatColor.YELLOW + "Walls SG");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);

				Team dateAndID = board.registerNewTeam("dateandid");
				dateAndID.addEntry(ChatColor.DARK_RED.toString());
				dateAndID.setPrefix(ChatColor.GRAY + df.format(dateobj) + ChatColor.DARK_GRAY + " sg" + arena.getID());
				obj.getScore(ChatColor.DARK_RED.toString()).setScore(12);

				Team rank = board.registerNewTeam("wsgteam");

				rank.addEntry(ChatColor.BLUE.toString());
				rank.setPrefix(ChatColor.AQUA + "Team: ");
				rank.setSuffix(arena.getTeam(player).getDisplay());
				obj.getScore(ChatColor.BLUE.toString()).setScore(11);

				Score blank1 = obj.getScore(" ");
				blank1.setScore(10);

				Score nextEvent = obj.getScore("Event:");
				nextEvent.setScore(9);

				Team timer = board.registerNewTeam("wsgtimer");
				timer.addEntry(ChatColor.LIGHT_PURPLE.toString());
				timer.setPrefix(ChatColor.GREEN + "Walls Fall ");
				String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
				timer.setSuffix(ChatColor.GREEN + time);
				obj.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(8);

				Score blank2 = obj.getScore("  ");
				blank2.setScore(7);

				Team playersLeft = board.registerNewTeam("playersLeft");
				playersLeft.addEntry(ChatColor.RED.toString());
				playersLeft.setPrefix(ChatColor.AQUA + "Players: ");
				playersLeft.setSuffix(ChatColor.GREEN + "" + getAlivePlayers().size());

				obj.getScore(ChatColor.RED.toString()).setScore(6);

				Team yourKills = board.registerNewTeam("kills");
				yourKills.addEntry(ChatColor.GOLD.toString());
				yourKills.setPrefix(ChatColor.AQUA + "Kills: ");
				yourKills.setSuffix(ChatColor.GREEN + "0");
				obj.getScore(ChatColor.GOLD.toString()).setScore(5);

				Score blank3 = obj.getScore("   ");
				blank3.setScore(4);

				Score ip;
				if (HerobrinePVPCore.getFileManager().getEnvironment().equalsIgnoreCase("DEV")) ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cDevelopment Server"));
				else ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cherobrinepvp.beastmc.com"));
				ip.setScore(3);
				int nameCount = 0;
				Team redTeam = board.registerNewTeam("redTeam");
				redTeam.setDisplayName(ChatColor.RED + "RED");
				redTeam.setPrefix(ChatColor.RED + "RED ");

				Team blueTeam = board.registerNewTeam("blueTeam");

				blueTeam.setDisplayName(ChatColor.BLUE + "BLUE");
				blueTeam.setPrefix(ChatColor.BLUE + "BLUE ");

				Team yellowTeam = board.registerNewTeam("yellowTeam");

				yellowTeam.setDisplayName(ChatColor.YELLOW + "YELLOW");
				yellowTeam.setPrefix(ChatColor.YELLOW + "YELLOW ");

				Team greenTeam = board.registerNewTeam("greenTeam");

				greenTeam.setDisplayName(ChatColor.GREEN + "GREEN");
				greenTeam.setPrefix(ChatColor.GREEN + "GREEN ");

				if (arena.getTeam(player).equals(Teams.RED)) {

					aliveRedPlayers = aliveRedPlayers + 1;
				} else if (arena.getTeam(player).equals(Teams.BLUE)) {

					aliveBluePlayers = aliveBluePlayers + 1;
				} else if (arena.getTeam(player).equals(Teams.YELLOW)) {

					aliveYellowPlayers = aliveYellowPlayers + 1;
				} else if (arena.getTeam(player).equals(Teams.GREEN)) {

					aliveGreenPlayers = aliveGreenPlayers + 1;

				}

				for (UUID uuid1 : arena.getPlayers()) {

					Player player1 = Bukkit.getPlayer(uuid1);

					if (arena.getTeam(player1).equals(Teams.RED)) {
						redTeam.addPlayer(player1);

					} else if (arena.getTeam(player1).equals(Teams.BLUE)) {
						blueTeam.addPlayer(player1);

					} else if (arena.getTeam(player1).equals(Teams.YELLOW)) {
						yellowTeam.addPlayer(player1);

					} else if (arena.getTeam(player1).equals(Teams.GREEN)) {
						greenTeam.addPlayer(player1);

					}

					nameCount++;
				}

				player.setScoreboard(board);

				kills.put(player.getUniqueId(), 0);
				if (arena.getTeam(player).equals(Teams.RED)) {
					player.teleport(net.herobrine.wallsg.Config.getRedTeamSpawn(arena.getID()));
				} else if (arena.getTeam(player).equals(Teams.BLUE)) {
					player.teleport(net.herobrine.wallsg.Config.getBlueTeamSpawn(arena.getID()));
				} else if (arena.getTeam(player).equals(Teams.YELLOW)) {
					player.teleport(net.herobrine.wallsg.Config.getYellowTeamSpawn(arena.getID()));
				} else if (arena.getTeam(player).equals(Teams.GREEN)) {
					player.teleport(net.herobrine.wallsg.Config.getGreenTeamSpawn(arena.getID()));
				} else {
					player.sendMessage(ChatColor.RED
							+ "Couldn't send you to your teams spawn point! Reason: You are not on a team. Please report this to staff, as you shouldn't be getting this error.");

				}

			}


			for (UUID uuid : arena.getPlayers()) {

				Player player = Bukkit.getPlayer(uuid);

				player.getScoreboard().getTeam("playersLeft").setSuffix(ChatColor.GREEN + "" + getAlivePlayers().size());

			}


			arena.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));
			arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "                   &f&lWalls SG"));
			arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&e&lMine within your space to upgrade your gear\n&e&lbefore the walls fall. The NPC outside your cave will have\n&e&lspecial items for you to purchase!\n&e&lLast team standing wins."));
			arena.sendMessage(
					ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));

			startWallTimer();
		}

		else {

			arena.setJoinState(false);

			for (UUID uuid: arena.getPlayers()) {

				Player player = Bukkit.getPlayer(uuid);

				player.sendMessage(ChatColor.GREEN + "Pick a class before the game starts! You have 15 seconds!");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
				Menus.applyClassSelector(player);

			}


			timer = 15;
			new BukkitRunnable() {


				@Override
				public void run() {

					if (arena.getPlayers().size() < 1) {
						cancel();
						arena.reset();
					}


					if (timer == 0) {
						cancel();
						startModGame();
					}


					if (timer > 1 && timer <= 5) {
						arena.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + timer + ChatColor.YELLOW + " seconds!");
						arena.playSound(Sound.CLICK);

					}

					if (timer == 1) {
						arena.sendMessage(ChatColor.YELLOW + "The game starts in " + ChatColor.RED + timer + ChatColor.YELLOW + " second!");
						arena.playSound(Sound.CLICK);
					}

					timer--;
				}
			}.runTaskTimer(WallsMain.getInstance(), 0L, 20L);


		}
	}

	public void resetBlocks() {
		for (Location loc : placedBlockLocations) {
			loc.getBlock().setType(Material.AIR);
		}
		for (Location loc : blockLocations.keySet()) {
			loc.getBlock().setType(blockLocations.get(loc));
		}
		blockLocations.clear();
		placedBlockLocations.clear();
	}
	public ClassTypes randomClass() {
		int i = 0;
		do {
			int pick2 = new Random().nextInt(ClassTypes.values().length);
			if (ClassTypes.values()[pick2].getGame().equals(Games.WALLS_SG)
					&& !ClassTypes.values()[pick2].isUnlockable()) {
				i = 1;
				return ClassTypes.values()[pick2];
			}
		} while (i != 1);
		return null;

	}
	public void startModGame() {
		int i = 0;
		for (Teams team : Teams.values()) {if (arena.getTeamCount(team) == 0 && team != Teams.PLACEHOLDER) i++;}

		if (i >= 5) {
			arena.sendMessage(ChatColor.RED + "The other teams left, so this game was cancelled.");
			for (UUID uuid: arena.getPlayers()) {
				arena.setClass(uuid, randomClass());
				Bukkit.getPlayer(uuid).closeInventory();
			}
			arena.reset();
		}
       else{
		for (UUID uuid : arena.getPlayers()) {
			System.out.println("WORKING ON UUID:" + uuid);
			Player player = Bukkit.getPlayer(uuid);
			alivePlayers.add(uuid);
			player.setDisplayName(arena.getTeam(player).getColor() + player.getName());
			player.getInventory().clear();
			DateFormat df = new SimpleDateFormat("MM/dd/yy");
			Date dateobj = new Date();
			if (!arena.getClasses().containsKey(player.getUniqueId())) {
				arena.setClass(player.getUniqueId(), randomClass());
				player.sendMessage(ChatColor.GREEN + "You didn't select a class, so we picked out the "
						+ arena.getClass(player).getDisplay() + ChatColor.GREEN + " class for you!");
			}


			player.closeInventory();

			HerobrinePVPCore.getFileManager().setGameStats(player.getUniqueId(), Games.WALLS_SG, "roundsPlayed",
					HerobrinePVPCore.getFileManager().getGameStats(player.getUniqueId(), Games.WALLS_SG,
							"roundsPlayed") + 1);

			alivePlayers1.put(player.getUniqueId(), arena.getTeam(player));
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = board.registerNewObjective("game", "dummy");
			Objective healthObj = board.registerNewObjective("wsgHP", "health");

			healthObj.setDisplayName(ChatColor.RED + "❤");
			healthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);

			obj.setDisplayName(ChatColor.YELLOW + "Walls SG");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);

			Team dateAndID = board.registerNewTeam("dateandid");
			dateAndID.addEntry(ChatColor.DARK_RED.toString());
			dateAndID.setPrefix(ChatColor.GRAY + df.format(dateobj) + ChatColor.DARK_GRAY + " sg" + arena.getID());
			obj.getScore(ChatColor.DARK_RED.toString()).setScore(12);

			Team rank = board.registerNewTeam("wsgteam");

			rank.addEntry(ChatColor.BLUE.toString());
			rank.setPrefix(ChatColor.AQUA + "Team: ");
			rank.setSuffix(arena.getTeam(player).getDisplay());
			obj.getScore(ChatColor.BLUE.toString()).setScore(11);

			Score blank1 = obj.getScore(" ");
			blank1.setScore(10);

			Score nextEvent = obj.getScore("Event:");
			nextEvent.setScore(9);

			Team timer = board.registerNewTeam("wsgtimer");
			timer.addEntry(ChatColor.LIGHT_PURPLE.toString());
			timer.setPrefix(ChatColor.GREEN + "Walls Fall ");
			String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
			timer.setSuffix(ChatColor.GREEN + time);
			obj.getScore(ChatColor.LIGHT_PURPLE.toString()).setScore(8);

			Score blank2 = obj.getScore("  ");
			blank2.setScore(7);

			Team yourTeamKills = board.registerNewTeam("playersLeft");
			yourTeamKills.addEntry(ChatColor.RED.toString());
			yourTeamKills.setPrefix(ChatColor.AQUA + "Players: ");
			yourTeamKills.setSuffix(ChatColor.GREEN + "" + getAlivePlayers().size());

			obj.getScore(ChatColor.RED.toString()).setScore(6);

			Team block = board.registerNewTeam("kills");
			block.addEntry(ChatColor.GOLD.toString());
			block.setPrefix(ChatColor.AQUA + "Kills: ");
			block.setSuffix(ChatColor.GREEN + "0");
			obj.getScore(ChatColor.GOLD.toString()).setScore(5);

			Score blank3 = obj.getScore("   ");
			blank3.setScore(4);

			Score ip;
			if (HerobrinePVPCore.getFileManager().getEnvironment().equalsIgnoreCase("DEV")) ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cDevelopment Server"));
			else ip = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&cherobrinepvp.beastmc.com"));
			ip.setScore(3);
			int nameCount = 0;
			Team redTeam = board.registerNewTeam("redTeam");
			redTeam.setDisplayName(ChatColor.RED + "RED");
			redTeam.setPrefix(ChatColor.RED + "RED ");

			Team blueTeam = board.registerNewTeam("blueTeam");

			blueTeam.setDisplayName(ChatColor.BLUE + "BLUE");
			blueTeam.setPrefix(ChatColor.BLUE + "BLUE ");

			Team yellowTeam = board.registerNewTeam("yellowTeam");

			yellowTeam.setDisplayName(ChatColor.YELLOW + "YELLOW");
			yellowTeam.setPrefix(ChatColor.YELLOW + "YELLOW ");

			Team greenTeam = board.registerNewTeam("greenTeam");

			greenTeam.setDisplayName(ChatColor.GREEN + "GREEN");
			greenTeam.setPrefix(ChatColor.GREEN + "GREEN ");

			if (arena.getTeam(player).equals(Teams.RED)) aliveRedPlayers = aliveRedPlayers + 1;
			else if (arena.getTeam(player).equals(Teams.BLUE)) aliveBluePlayers = aliveBluePlayers + 1;
			else if (arena.getTeam(player).equals(Teams.YELLOW)) aliveYellowPlayers = aliveYellowPlayers + 1;
			else if (arena.getTeam(player).equals(Teams.GREEN)) aliveGreenPlayers = aliveGreenPlayers + 1;


			for (UUID uuid1 : arena.getPlayers()) {

				Player player1 = Bukkit.getPlayer(uuid1);

				if (arena.getTeam(player1).equals(Teams.RED)) redTeam.addPlayer(player1);
				else if (arena.getTeam(player1).equals(Teams.BLUE)) blueTeam.addPlayer(player1);
				else if (arena.getTeam(player1).equals(Teams.YELLOW)) yellowTeam.addPlayer(player1);
				else if (arena.getTeam(player1).equals(Teams.GREEN)) greenTeam.addPlayer(player1);

				nameCount++;
			}

			player.setScoreboard(board);
			kills.put(player.getUniqueId(), 0);

			if (arena.getTeam(player).equals(Teams.RED)) player.teleport(net.herobrine.wallsg.Config.getRedTeamSpawn(arena.getID()));
			else if (arena.getTeam(player).equals(Teams.BLUE)) player.teleport(net.herobrine.wallsg.Config.getBlueTeamSpawn(arena.getID()));
			else if (arena.getTeam(player).equals(Teams.YELLOW)) player.teleport(net.herobrine.wallsg.Config.getYellowTeamSpawn(arena.getID()));
			else if (arena.getTeam(player).equals(Teams.GREEN)) player.teleport(net.herobrine.wallsg.Config.getGreenTeamSpawn(arena.getID()));
			else player.sendMessage(ChatColor.RED + "Couldn't send you to your teams spawn point! Reason: You are not on a team. Please report this to staff, as you shouldn't be getting this error.");
		}

		for (UUID uuid : arena.getClasses().keySet()) {arena.getClasses().get(uuid).onStart(Bukkit.getPlayer(uuid));}
		for (UUID uuid : arena.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			player.getScoreboard().getTeam("playersLeft").setSuffix(ChatColor.GREEN + "" + getAlivePlayers().size());
		}

		arena.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));
		arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "                   &f&lWalls SG"));
		arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&lMine within your space to upgrade your gear\n&e&lbefore the walls fall. The NPC outside your cave will have\n&e&lspecial items for you to purchase!\n&e&lLast team standing wins."));
		arena.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));

		arena.setState(GameState.LIVE);
		startWallTimer();

	}
	}

	public void updateKillCounts(Player killer) {

		kills.put(killer.getUniqueId(), kills.get(killer.getUniqueId()) + 1);
		killer.getScoreboard().getTeam("kills").setSuffix(ChatColor.GREEN + "" + kills.get(killer.getUniqueId()));

		HerobrinePVPCore.getFileManager().setGameStats(killer.getUniqueId(), Games.CLASH_ROYALE, "kills",
				HerobrinePVPCore.getFileManager().getGameStats(killer.getUniqueId(), Games.WALLS_SG,
						"kills") + 1);
		LevelRewards prestige = HerobrinePVPCore.getFileManager().getPrestige(HerobrinePVPCore.getFileManager().getPlayerLevel(killer.getUniqueId()));
		int baseKillCoins = 15;
		int earnedCoins = (int)Math.round(baseKillCoins * prestige.getGameCoinMultiplier());

		HerobrinePVPCore.getFileManager().addCoins(killer, earnedCoins);
		killer.sendMessage(ChatColor.YELLOW + "+" + earnedCoins + " coins! (Kill)");
	}

	public static ItemStack getSpecialItem(Player player, Material material, int durability) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType().equals(material) && item.getDurability() == durability) return item;
		}

		return null;
	}

	public static boolean containsAtLeast(Player player, Material material, int durability, int amount) {
		int count = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() == material && item.getDurability() == durability) count += item.getAmount();
		}

		if (count >= amount) return true;
		else return false;


	}

	public static void resetChests() {ChestManager.getInstance().reset();}

	public void setTime(int time, Player player) {
		seconds = time;
		String newTime = String.format("%02d:%02d",
				seconds / 60,
				seconds % 60);
		arena.sendMessage(HerobrinePVPCore.translateString(
				"&a&lTIME! &eThe timer has been set to " + newTime + " by &c")
				+ player.getName());
	}

	public void setFrozen(boolean frozen) {
		isFrozen = frozen;
		if(isFrozen) {
			arena.sendMessage(ChatColor.YELLOW + "The timer has been " + ChatColor.AQUA + "frozen!");
			if (suddenDeath) {
				WorldBorder border = net.herobrine.wallsg.Config.getArenaWorld(arena.getID()).getWorldBorder();
				border.setSize(border.getSize());
			}
		}
		else {
			arena.sendMessage(ChatColor.YELLOW + "The timer has been " + ChatColor.RED + "unfrozen!");
			if (suddenDeath) {
				WorldBorder border = net.herobrine.wallsg.Config.getArenaWorld(arena.getID()).getWorldBorder();
				border.setSize(10, seconds - 20);
			}
		}
	}

	public static void removeItem(Player player, Material material, int durability, int amount) {

		for (ItemStack invItem : player.getInventory().getContents()) {
			if (invItem != null) {
				if (invItem.getType().equals(material) && invItem.getDurability() == durability) {
					int preAmount = invItem.getAmount();
					int newAmount = Math.max(0, preAmount - amount);
					amount = Math.max(0, amount - preAmount);
					invItem.setAmount(newAmount);
					if (amount == 0) break;
				}
			}
		}

	}

	public void startEnding(String winningTeam) {

		WorldBorder border = net.herobrine.wallsg.Config.getArenaWorld(arena.getID()).getWorldBorder();

		border.setSize(1000);
		for (UUID uuid : arena.getPlayers()) {

			Player player = Bukkit.getPlayer(uuid);

			try {
				if (arena.getTeam(player).getDisplay() == winningTeam
						&& !winningTeam.equalsIgnoreCase(ChatColor.YELLOW + "DRAW!")) {

					GameCoreMain.getInstance().sendTitle(player, "&6&lVICTORY", "&7Your team is the last one standing! ", 0,
							3, 0);

					HerobrinePVPCore.getFileManager().setGameStats(player.getUniqueId(), Games.WALLS_SG, "wins",
							HerobrinePVPCore.getFileManager().getGameStats(player.getUniqueId(), Games.WALLS_SG,
									"wins") + 1);

				} else if (arena.getTeam(player).getDisplay() != winningTeam
						&& !winningTeam.equalsIgnoreCase(ChatColor.YELLOW + "DRAW!")) {
					GameCoreMain.getInstance().sendTitle(player, "&c&lGAME OVER", "&7Your team didn't win this time.", 0, 3,
							0);
				} else {
					GameCoreMain.getInstance().sendTitle(player, "&e&lDRAW", "The game ended in a draw!", 0, 3, 0);
					SongPlayer.playSong(player, Songs.WSGDRAW);
				}
			}
			catch(NullPointerException e) {
				GameCoreMain.getInstance().sendTitle(player, "&e&lDRAW", "The game ended in a draw!", 0, 3, 0);
				SongPlayer.playSong(player, Songs.WSGDRAW);
			}


		}

		arena.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));

		arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "                   &f&lWalls SG"));
		arena.sendMessage("");
		arena.sendMessage(ChatColor.YELLOW + "Winner " + ChatColor.GRAY + "- " + winningTeam);
		arena.sendMessage("");
		List<UUID> keys = kills.entrySet().stream().sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
				.limit(3).map(Map.Entry::getKey).collect(Collectors.toList());

		arena.sendMessage(ChatColor.GOLD + "                   Most Kills");

		if (keys.size() >= 1) {
			Player player1 = Bukkit.getPlayer(keys.get(0));
			if (player1 != null) {
				if (Manager.isPlaying(player1)) {
					if (Manager.getArena(player1).getID() == arena.getID()) arena.sendMessage(arena.getTeam(player1).getColor() + player1.getName() + ChatColor.GRAY + " - " + kills.get(player1.getUniqueId()));
					else arena.sendMessage(ChatColor.GRAY + player1.getName() + ChatColor.GRAY + " - " + kills.get(player1.getUniqueId()));
				}
				else arena.sendMessage(ChatColor.GRAY + player1.getName() + ChatColor.GRAY + " - " + kills.get(player1.getUniqueId()));
			}

		} else {
			arena.sendMessage(ChatColor.RED + "No players found. Hello? Is Anyone here?");
			System.out.println("No players.");
		}

		if (keys.size() >= 2) {
			Player player2 = Bukkit.getPlayer(keys.get(1));
			if (player2 != null) {
				if (Manager.isPlaying(player2)) {
					if (Manager.getArena(player2).getID() == arena.getID()) arena.sendMessage(arena.getTeam(player2).getColor() + player2.getName() + ChatColor.GRAY + " - " + kills.get(player2.getUniqueId()));
					else arena.sendMessage(ChatColor.GRAY + player2.getName() + ChatColor.GRAY + " - " + kills.get(player2.getUniqueId()));
				}
				else arena.sendMessage(ChatColor.GRAY + player2.getName() + ChatColor.GRAY + " - " + kills.get(player2.getUniqueId()));
			}
		}

		if (keys.size() == 3) {
			Player player3 = Bukkit.getPlayer(keys.get(2));
			if (player3 != null) {
				if (Manager.isPlaying(player3)) {

					if (Manager.getArena(player3).getID() == arena.getID()) arena.sendMessage(arena.getTeam(player3).getColor() + player3.getName() + ChatColor.GRAY + " - " + kills.get(player3.getUniqueId()));
					else arena.sendMessage(ChatColor.GRAY + player3.getName() + ChatColor.GRAY + " - " + kills.get(player3.getUniqueId()));

				} else arena.sendMessage(ChatColor.GRAY + player3.getName() + ChatColor.GRAY + " - " + kills.get(player3.getUniqueId()));
			}
		}

		arena.sendMessage("");
		arena.sendMessage(ChatColor.GREEN + "Rewards: ");

		if (winningTeam.equalsIgnoreCase(ChatColor.YELLOW + "DRAW!")) arena.distributeRewards(Teams.PLACEHOLDER);
		else arena.distributeRewards(this.winningTeam);


		arena.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&a&m&l----------------------------------------"));

		kills.clear();
		endSeconds = 5;


		new BukkitRunnable() {
			@Override
			public void run() {

				if (endSeconds == 0) {
					cancel();
					arena.reset();
					ChestManager.getInstance().reset();
					getCustomDeathCause().clear();
				}

				endSeconds--;
			}
		}.runTaskTimer(WallsMain.getInstance(), 0L, 20L);
	}

	public boolean isWearingEmeraldArmor(Player player) {
		for (ItemStack stack : player.getEquipment().getArmorContents()) {
			if (stack == null) return false;
			if (!stack.hasItemMeta()) return false;
			if (stack.getItemMeta().getDisplayName() == null) return false;
			if (!stack.getItemMeta().getDisplayName().contains("Emerald")) return false;
		}
		return true;
	}
	public boolean isFrozen() {
		return isFrozen;
	}

	public static String getFriendlyName(Material mat) {
		StringBuilder sb = new StringBuilder();
		for (String str : mat.name().split("_"))
			sb.append(" ").append(Character.toUpperCase(str.charAt(0))).append(str.substring(1).toLowerCase());
		return sb.toString().trim().replace("Diode", "Redstone Repeater").replace("Thin Glass", "Glass Pane")
				.replace("Wood ", "Wooden ");
	}

	public void startWallTimer() {
		CuboidSelection selec = new CuboidSelection(net.herobrine.wallsg.Config.getArenaWorld(arena.getID()),
				net.herobrine.wallsg.Config.getFirstPosition(arena.getID()), net.herobrine.wallsg.Config.getSecondPosition(arena.getID()));
		@SuppressWarnings("deprecation")
		CuboidRegion region = new CuboidRegion(BukkitUtil.getLocalWorld(selec.getWorld()),
				selec.getNativeMinimumPoint(), selec.getNativeMaximumPoint());
		@SuppressWarnings("deprecation")
		EditSession session = new EditSession(BukkitUtil.getLocalWorld(selec.getWorld()), region.getArea());

		new BukkitRunnable() {
			@Override
			public void run() {
				if (arena.getState().equals(GameState.RECRUITING) || arena.getState().equals(GameState.COUNTDOWN) || arena.getState().equals(GameState.LIVE_ENDING)) cancel();

				if (seconds < 0) {
					cancel();
					return;
				}

				for (UUID uuid : arena.getPlayers()) {
					Player player = Bukkit.getPlayer(uuid);
					String time = String.format("%02d:%02d", seconds / 60, seconds % 60);

					if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().contains("Walls SG")) {
						player.getScoreboard().getTeam("wsgtimer").setSuffix(ChatColor.GREEN + time);
						player.getScoreboard().getTeam("playersLeft").setSuffix(ChatColor.GREEN + "" + getAlivePlayers().size());
					}

					if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()
							.equals(Material.BARRIER) && !arena.getSpectators().contains(player.getUniqueId())) {
						player.setHealth(0.0);
						player.sendMessage(ChatColor.RED + "Don't try to leave the playing area!");
					}

					if (Game.getAlivePlayers().size() == 0) {
						startEnding(ChatColor.YELLOW + "DRAW!");
						cancel();
					}

					if (player.getGameMode().equals(GameMode.SURVIVAL)) {
						if (isWearingEmeraldArmor(player)) {
							PotionEffect playerEffectForArmor = PotionEffectType.SPEED.createEffect(9999999, 1);
							player.addPotionEffect(playerEffectForArmor);
						}
						else player.removePotionEffect(PotionEffectType.SPEED);

					}

				}

				if (seconds == 0 && hasFallen && !suddenDeath) isGameOver();
				if (seconds == 0 && hasFallen && suddenDeath) {
					startEnding(ChatColor.YELLOW + "DRAW!");
					cancel();
				}

				if (seconds == 175 && hasFallen && !suddenDeath) {

					net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 0).getBlock().setType(Material.CHEST);
					net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 1).getBlock().setType(Material.CHEST);
					net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 2).getBlock().setType(Material.CHEST);
					net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 3).getBlock().setType(Material.CHEST);

					blockLocations.put(net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 0), Material.AIR);
					blockLocations.put(net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 1), Material.AIR);
					blockLocations.put(net.herobrine.wallsg.Config.getChestLocation(arena.getID(), 2), Material.AIR);
					blockLocations.put(Config.getChestLocation(arena.getID(), 3), Material.AIR);
					arena.playSound(Sound.NOTE_PLING);
					arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&a&lLOOT! &fSome chests have appeared near the center of the map!"));

				}
				if (seconds == 0 && !hasFallen) {

					try {

						Set<BaseBlock> filter = new HashSet<BaseBlock>();
						filter.add(new BaseBlock(152)); // redstone block
						filter.add(new BaseBlock(22)); // lapis block
						filter.add(new BaseBlock(41)); // gold block
						filter.add(new BaseBlock(133)); // emerald block
						filter.add(new BaseBlock(42)); // iron block
						session.replaceBlocks(region, filter, new BaseBlock(0));

						arena.playSound(Sound.ENDERDRAGON_GROWL);
						arena.sendMessage(ChatColor.GOLD + "The Walls" + ChatColor.GREEN
								+ " have fallen. May the best team win!");

					} catch (MaxChangedBlocksException e) {
						arena.sendMessage(ChatColor.RED
								+ "Unfortunatley, there was an error in making the walls fall down. This is a bug, and will be fixed now that it's known to actually happen.\nAn admin in your testing session will remove the walls manually. This may take a minute.");
						e.printStackTrace();
					}

					for (UUID uuid : arena.getPlayers()) {
						Player player = Bukkit.getPlayer(uuid);
						if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().contains("Walls SG")) player.getScoreboard().getTeam("wsgtimer").setPrefix(ChatColor.GREEN + "Sudden Death ");
					}

					seconds = 180;
					hasFallen = true;

				}
				if (seconds == 180 && !hasFallen && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The Walls" + " will fall in " + ChatColor.GREEN + "3"
							+ ChatColor.YELLOW + " minutes!");
				}

				else if (seconds == 180 && hasFallen && !suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "Sudden Death will begin in " + ChatColor.GREEN + "3"
							+ ChatColor.YELLOW + " minutes!");
				}

				else if (seconds == 180 && hasFallen && suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The game will end in " + ChatColor.GREEN + "3"
							+ ChatColor.YELLOW + " minutes!");
				}

				if (seconds == 120 && !hasFallen && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The Walls" + " will fall in " + ChatColor.GREEN + "2"
							+ ChatColor.YELLOW + " minutes!");
				}

				else if (seconds == 120 && hasFallen && !suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "Sudden Death" + " will begin in " + ChatColor.GREEN + "2"
							+ ChatColor.YELLOW + " minutes!");
				} else if (seconds == 120 && hasFallen && suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The game will end in " + ChatColor.GREEN + "2"
							+ ChatColor.YELLOW + " minutes!");
				}

				if (seconds == 30 && !hasFallen && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The Walls" + " will fall in " + ChatColor.GOLD + "30"
							+ ChatColor.YELLOW + " seconds!");
				}

				else if (seconds == 30 && hasFallen && !suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "Sudden Death" + " will begin in " + ChatColor.GOLD + "30"
							+ ChatColor.YELLOW + " seconds!");
				}

				else if (seconds == 30 && hasFallen && suddenDeath && !isFrozen) {
					arena.playSound(Sound.CLICK);
					arena.sendMessage(ChatColor.YELLOW + "The game will end in " + ChatColor.GOLD + "30"
							+ ChatColor.YELLOW + " seconds!");
				}

				if (seconds <= 10 && !hasFallen && !isFrozen) {

					if (seconds > 1) {
						arena.playSound(Sound.CLICK);
						arena.sendMessage(ChatColor.YELLOW + "The Walls" + " will fall in " + ChatColor.RED + seconds
								+ ChatColor.YELLOW + " seconds!");
					}
					if (seconds == 1) {
						arena.playSound(Sound.CLICK);
						arena.sendMessage(ChatColor.YELLOW + "The Walls" + " will fall in " + ChatColor.RED + "1"
								+ ChatColor.YELLOW + " second!");
					}

				}

				else if (seconds <= 10 && hasFallen && !suddenDeath && !isFrozen) {
					if (seconds > 1) {
						arena.playSound(Sound.CLICK);
						arena.sendMessage(ChatColor.YELLOW + "Sudden Death" + " will begin in " + ChatColor.RED
								+ seconds + ChatColor.YELLOW + " seconds!");
					}
					if (seconds == 1) {
						arena.playSound(Sound.CLICK);
						arena.sendMessage(ChatColor.YELLOW + "Sudden Death" + " will begin in " + ChatColor.RED + "1"
								+ ChatColor.YELLOW + " second!");
					}
				}


        if(!isFrozen) seconds--;
			}
		}.runTaskTimer(WallsMain.getInstance(), 0L, 20L);

	}
}
