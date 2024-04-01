package net.herobrine.wallsg;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import net.herobrine.core.ItemTypes;
import net.herobrine.gamecore.*;
import net.herobrine.wallsg.game.CustomDeathCause;
import net.herobrine.wallsg.game.ShopItems;
import net.herobrine.wallsg.game.Shops;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.herobrine.core.BuildCommand;
import net.herobrine.core.HerobrinePVPCore;

public class GameListener implements Listener {

	public HashMap<UUID, Block> anvilMap = new HashMap<>();


	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (e.getView().getTitle()
				.contains(ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- &bClass Selector"))
				&& e.getCurrentItem() != null) {

			String classString = null;
			if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) classString = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase());

			if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) && !e.getCurrentItem().getType().equals(Material.BEDROCK)) {
				classString = classString.replaceAll("\\s", "");
				classString = classString.replaceAll("BATTLE", "");
			}
			else {return;}


			ClassTypes type;

			if (e.getCurrentItem().getType().equals(Material.EMERALD)) type = ClassTypes.ECONOMIST;
			else type = ClassTypes.valueOf(classString);



			if (Manager.hasKit(player) && Manager.getKit(player).equals(type)) player.sendMessage(ChatColor.RED + "You already have this class selected!");
			else {
				if (type.isUnlockable() && !HerobrinePVPCore.getFileManager().isItemUnlocked(ItemTypes.CLASS,
						type.toString(), player.getUniqueId())) {

					player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
					player.sendMessage(ChatColor.RED + "You do not have this class unlocked!");

				} else {
					player.sendMessage(ChatColor.GREEN + "You have selected the " + type.getDisplay() + ChatColor.GREEN
							+ " class!");

					Manager.getArena(player).setClass(player.getUniqueId(), type);
				}

			}

			e.setCancelled(true);
			player.closeInventory();
		} else {
			if (Manager.isPlaying(player)) {
				Arena arena = Manager.getArena(player);
				if (arena.getGame(arena.getID()).equals(Games.WALLS_SG)) e.setCancelled(false);
			}
		}
	}


	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)
				&& Manager.getArena(player).getGame(Manager.getArena(player).getID()).equals(Games.WALLS_SG)) {

			if (e.getBlock().getType().equals(Material.COBBLESTONE) || e.getBlock().getType().equals(Material.STONE) || e.getBlock().getType().equals(Material.DIRT)) {
				e.setCancelled(true);
				if (e.getBlock().getType().equals(Material.COBBLESTONE) || e.getBlock().getType().equals(Material.DIRT) || e.getBlock().getType().equals(Material.WOOD)) {
					if (Game.getPlacedBlockLocations().contains(e.getBlock().getLocation())) {

						e.setCancelled(false);
						Game.getPlacedBlockLocations().remove(e.getBlock().getLocation());
					}
					else {
						Game.getBlockLocations().put(e.getBlock().getLocation(), e.getBlock().getType());
						ItemStack blockDrop = new ItemStack(e.getBlock().getType(), 1);
						player.getInventory().addItem(blockDrop);
						e.getBlock().setType(Material.BEDROCK);

					}

				} else {
					Game.getBlockLocations().put(e.getBlock().getLocation(), Material.STONE);
					e.getBlock().setType(Material.BEDROCK);
					ItemStack blockDrop = new ItemStack(Material.COBBLESTONE, 1);
					player.getInventory().addItem(blockDrop);
				}

			} else if (e.getBlock().getType().equals(Material.IRON_ORE)
					|| e.getBlock().getType().equals(Material.GOLD_ORE)) {
				if (e.getBlock().getType().equals(Material.IRON_ORE)) {
					e.getBlock().getDrops().clear();
					e.setExpToDrop(3);
					e.getBlock().setType(Material.AIR);
					ItemStack smelted = new ItemStack(Material.IRON_INGOT, 1);
					player.getInventory().addItem(smelted);
				} else {
					e.getBlock().getDrops().clear();
					e.setExpToDrop(3);
					e.getBlock().setType(Material.AIR);
					ItemStack smelted = new ItemStack(Material.GOLD_INGOT, 1);
					player.getInventory().addItem(smelted);

				}
			} else if (e.getBlock().getType().equals(Material.DIAMOND_ORE)
					|| e.getBlock().getType().equals(Material.COAL_ORE)
					|| e.getBlock().getType().equals(Material.EMERALD_ORE)
					|| e.getBlock().getType().equals(Material.LAPIS_ORE)
					|| e.getBlock().getType().equals(Material.REDSTONE_ORE)
					|| e.getBlock().getType().equals(Material.GLOWING_REDSTONE_ORE)) {

				e.setCancelled(false);
			} else if (e.getBlock().getType().equals(Material.OBSIDIAN) || e.getBlock().getType().equals(Material.WEB) || e.getBlock().getType().equals(Material.WOOD)) {
				if (Game.getPlacedBlockLocations().contains(e.getBlock().getLocation())) {

					e.setCancelled(false);
					Game.getPlacedBlockLocations().remove(e.getBlock().getLocation());

				}

			}

			else {
				e.setCancelled(true);
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
				player.sendMessage(ChatColor.RED + "You cannot break that block!");
			}

			if (Game.getPlacedBlockLocations().contains(e.getBlock().getLocation())) {
				Game.getPlacedBlockLocations().remove(e.getBlock().getLocation());
			}

		} else {
			if (!BuildCommand.buildEnabledPlayers.contains(player)) {
				e.setCancelled(true);
			}
		}

	}


	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE)
				&& Manager.getArena(player).getGame(Manager.getArena(player).getID()).equals(Games.WALLS_SG)) {
			if (e.getBlock().getType().equals(Material.WORKBENCH)) {
				e.setCancelled(true);
				player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
				player.sendMessage(ChatColor.RED + "You cannot place crafting tables! Buy items from the shop NPC!");
			}
			else if (e.getBlock().getType().equals(Material.TRIPWIRE)) {
				player.getInventory().addItem(new ItemStack(Material.STRING, 1));
				player.getInventory().removeItem(new ItemStack(Material.STRING, 1));
				e.getBlock().setType(Material.AIR);
				e.setCancelled(true);
			}
			else if (e.getBlock().getType().equals(Material.REDSTONE_WIRE)) {
				player.getInventory().addItem(new ItemStack(Material.REDSTONE, 1));
				player.getInventory().removeItem(new ItemStack(Material.REDSTONE, 1));
				e.getBlock().setType(Material.AIR);
				e.setCancelled(true);
			}
			else Game.getPlacedBlockLocations().add(e.getBlock().getLocation());

		}
		else if (Manager.isPlaying(player) && Manager.getArena(player).getState().equals(GameState.LIVE_ENDING)
				&& Manager.getArena(player).getGame(Manager.getArena(player).getID()).equals(Games.WALLS_SG)) {
			e.setCancelled(true);
		}
		else {
			if (!BuildCommand.buildEnabledPlayers.contains(player)) e.setCancelled(true);
		}
	}

	@EventHandler
	public void onWaterPlace(PlayerBucketEmptyEvent e) {
		Block block = e.getBlockClicked().getRelative(e.getBlockFace());
		Game.getPlacedBlockLocations().add(block.getLocation());
	}

	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e) {
		Block block = e.getBlockClicked().getRelative(e.getBlockFace());
		if (Game.getPlacedBlockLocations().contains(block.getLocation())) Game.getPlacedBlockLocations().remove(block.getLocation());
	}

	@EventHandler
	public void flowingWater(BlockFromToEvent e) {
		if (e.getToBlock().getType().equals(Material.CARPET)) e.setCancelled(true);
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		if (e.getItem().getType().equals(Material.POTION)) Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(WallsMain.getInstance(), () -> e.getPlayer().setItemInHand(new ItemStack(Material.AIR)), 1L);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player)) if (Manager.getArena(player).getState().equals(GameState.LIVE)) return;
		else if (!BuildCommand.buildEnabledPlayers.contains(player)) e.setCancelled(true);

	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Villager) e.setCancelled(true);
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (Manager.isPlaying(player)) {
				if (!Manager.getArena(player).getState().equals(GameState.LIVE)) {
					if (e.getCause().equals(DamageCause.FALL)) e.setCancelled(true);
					if (e.getCause().equals(DamageCause.DROWNING)) e.setCancelled(true);
					if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) e.setCancelled(true);
					if (e.getCause().equals(DamageCause.FIRE)) e.setCancelled(true);
					if (e.getCause().equals(DamageCause.LAVA)) e.setCancelled(true);
				}
				else if (Manager.getArena(player).getSpectators().contains(player.getUniqueId())) e.setCancelled(true);
			} else {
				if (e.getCause().equals(DamageCause.FALL)) e.setCancelled(true);
				if (e.getCause().equals(DamageCause.DROWNING)) e.setCancelled(true);
				if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) e.setCancelled(true);
				if (e.getCause().equals(DamageCause.FIRE)) e.setCancelled(true);
				if (e.getCause().equals(DamageCause.LAVA)) e.setCancelled(true);
			}

		}

	}

	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent e) {
		Player player = (Player) e.getWhoClicked();

		if (Manager.isPlaying(player)) {
			if (!Manager.getArena(player).getState().equals(GameState.LIVE)) e.setCancelled(true);
		} else {
			if (!BuildCommand.buildEnabledPlayers.contains(player)) e.setCancelled(true);
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (Manager.isPlaying(player)) {
			if (Manager.getArena(player).getState().equals(GameState.LIVE)) {
				Arena arena = Manager.getArena(player);
				arena.sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor() + player.getName()
						+ ChatColor.YELLOW + " has left!");

				Game game = arena.getwallsSGGame();
				if (Game.getAlivePlayers().contains(player.getUniqueId())) {
					Game.getAlivePlayers().remove(player.getUniqueId());
					if (Game.alivePlayers1.containsKey(player.getUniqueId())) {

						Game.alivePlayers1.remove(player.getUniqueId());

						if (arena.getTeam(player).equals(Teams.RED)) game.aliveRedPlayers = game.aliveRedPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.BLUE)) game.aliveBluePlayers = game.aliveBluePlayers - 1;
						else if (arena.getTeam(player).equals(Teams.YELLOW)) game.aliveYellowPlayers = game.aliveYellowPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.GREEN)) game.aliveGreenPlayers = game.aliveGreenPlayers - 1;
					}
				}

				arena.removePlayer(player);

			} else {
				Manager.getArena(player).removePlayer(player);
			}
		} else {
			return;
		}
	}

	@EventHandler
	public void onOpenInventory(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			if (e.getInventory().getType().equals(InventoryType.MERCHANT)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onNPCClick(NPCRightClickEvent e) {
		Player player = e.getClicker();

		if (e.getNPC().getName().contains("WALLS SG")) GameCoreMain.getInstance().startQueue(e.getClicker(), Games.WALLS_SG, GameType.VANILLA);
		 else if (e.getNPC().getName().contains("SHOP")) {
			if (Manager.isPlaying(e.getClicker())) {

				Arena arena = Manager.getArena(player);

				if (arena.getGame(arena.getID()).equals(Games.WALLS_SG)) {
					if (arena.getState().equals(GameState.LIVE)) {
						if (Game.getAlivePlayers().contains(player.getUniqueId())) Menus.applyShopHome(player);
						else player.sendMessage(ChatColor.RED + "You must be alive to use the shop!");
					}
					else player.sendMessage(ChatColor.RED + "The game is not live!");
				}
			}
			else player.sendMessage(ChatColor.RED + "You are not in a game!");
		}

		else if (e.getNPC().getName().contains("BATTLE CLASH")) net.herobrine.core.Menus.applyGamemodeMenu(player);
		else if (e.getNPC().getName().contains("COMING SOON")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis game is coming soon!"));
	}
	@EventHandler
	public void onNPCLeftClick(NPCLeftClickEvent e) {
		Player player = e.getClicker();

		if (e.getNPC().getName().contains("WALLS SG")) GameCoreMain.getInstance().startQueue(e.getClicker(), Games.WALLS_SG, GameType.VANILLA);
		else if (e.getNPC().getName().contains("SHOP")) {
			if (Manager.isPlaying(e.getClicker())) {

				Arena arena = Manager.getArena(player);

				if (arena.getGame(arena.getID()).equals(Games.WALLS_SG)) {
					if (arena.getState().equals(GameState.LIVE)) {
						if (Game.getAlivePlayers().contains(player.getUniqueId())) Menus.applyShopHome(player);
						else player.sendMessage(ChatColor.RED + "You must be alive to use the shop!");
					}
					else player.sendMessage(ChatColor.RED + "The game is not live!");
				}
			}
			else player.sendMessage(ChatColor.RED + "You are not in a game!");
		}

		else if (e.getNPC().getName().contains("BATTLE CLASH")) net.herobrine.core.Menus.applyGamemodeMenu(player);
		else if (e.getNPC().getName().contains("COMING SOON")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis game is coming soon!"));

	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {

		Player player = e.getPlayer();

		if (Manager.isPlaying(player)) {
			Arena arena = Manager.getArena(player);

			if (arena.getGame(arena.getID()) == Games.WALLS_SG && arena.getState() == GameState.LIVE) {

				if (arena.getSpectators().contains(player.getUniqueId())) {
					ItemStack spectate = new ItemStack(Material.COMPASS, 1);
					ItemMeta spectateMeta = spectate.getItemMeta();
					spectateMeta.setDisplayName(ChatColor.GREEN + "Spectate");
					spectate.setItemMeta(spectateMeta);
					player.getInventory().setItem(0, spectate);

				}

				if (arena.getTeam(player).equals(Teams.RED)) e.setRespawnLocation(Config.getRedTeamSpawn(arena.getID()));
				else if (arena.getTeam(player).equals(Teams.BLUE)) e.setRespawnLocation(Config.getBlueTeamSpawn(arena.getID()));
				else if (arena.getTeam(player).equals(Teams.YELLOW)) e.setRespawnLocation(Config.getYellowTeamSpawn(arena.getID()));
				else e.setRespawnLocation(Config.getGreenTeamSpawn(arena.getID()));
			}
		}

	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() != null) {

			Player player = e.getEntity();
			DamageCause deathCause = player.getLastDamageCause().getCause();
			if (Manager.isPlaying(player)) {
				Arena arena = Manager.getArena(player);
				// DO NOT REMOVE THIS IF STATEMENT! It's essential even though it's blank. IDK why.
				if (!arena.getGame(arena.getID()).equals(Games.WALLS_SG)) {}

				else if (Game.getAlivePlayers().contains(player.getUniqueId())) {
					Game game = arena.getwallsSGGame();
					if (player.getKiller() != null) {
						Player killer = player.getKiller();

						if (deathCause.equals(DamageCause.ENTITY_ATTACK)) {
							arena.sendMessage(arena.getTeam(killer).getColor() + killer.getName() + ChatColor.GRAY
									+ " has killed " + arena.getTeam(player).getColor() + player.getName() + " "
									+ ChatColor.GRAY + "with their epic PVP skills.");

							killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
							player.playSound(player.getLocation(), Sound.BAT_DEATH, 1f, 1f);
							Game.getAlivePlayers().remove(player.getUniqueId());
							arena.setSpectator(player);
						} else if (deathCause.equals(DamageCause.FALL)) {
							arena.sendMessage(arena.getTeam(killer).getColor() + killer.getName() + ChatColor.GRAY
									+ " just made " + arena.getTeam(player).getColor() + player.getName()
									+ ChatColor.GRAY + " fall to their death.");
							killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
							player.playSound(player.getLocation(), Sound.BAT_DEATH, 1f, 1f);
							Game.getAlivePlayers().remove(player.getUniqueId());
							arena.setSpectator(player);
						} else if (deathCause.equals(DamageCause.FIRE)) {
							arena.sendMessage(arena.getTeam(killer).getColor() + killer.getName() + ChatColor.GRAY
									+ " burned " + arena.getTeam(player).getColor() + player.getName() + ChatColor.GRAY
									+ ".");
							killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
							player.playSound(player.getLocation(), Sound.BAT_DEATH, 1f, 1f);
							Game.getAlivePlayers().remove(player.getUniqueId());
							arena.setSpectator(player);
						} else if (deathCause.equals(DamageCause.PROJECTILE)) {
							arena.sendMessage(arena.getTeam(killer).getColor() + killer.getName() + ChatColor.GRAY
									+ " bowspammed " + arena.getTeam(player).getColor() + player.getName() + " "
									+ ChatColor.GRAY + "to death.\n" + ChatColor.GOLD + ChatColor.BOLD
									+ ChatColor.ITALIC + "EPIC SKILLS!");
							killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
							player.playSound(player.getLocation(), Sound.BAT_DEATH, 1f, 1f);
							Game.getAlivePlayers().remove(player.getUniqueId());
							arena.setSpectator(player);
						}

						if (arena.getTeam(player).equals(Teams.RED)) game.aliveRedPlayers = game.aliveRedPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.BLUE)) game.aliveBluePlayers = game.aliveBluePlayers - 1;
						else if (arena.getTeam(player).equals(Teams.YELLOW)) game.aliveYellowPlayers = game.aliveYellowPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.GREEN)) game.aliveGreenPlayers = game.aliveGreenPlayers - 1;

						arena.getwallsSGGame().updateKillCounts(killer);
					} else {
						player.playSound(player.getLocation(), Sound.BAT_DEATH, 1f, 1f);

						if(!game.hasCustomDeathCause(player) && !player.getLastDamageCause().getCause().equals(DamageCause.CUSTOM)) arena.sendMessage(arena.getTeam(player).getColor() + player.getName() + ChatColor.GRAY + " has died.");
						Game.getAlivePlayers().remove(player.getUniqueId());
						arena.setSpectator(player);
						if (arena.getTeam(player).equals(Teams.RED)) game.aliveRedPlayers = game.aliveRedPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.BLUE)) game.aliveBluePlayers = game.aliveBluePlayers - 1;
						else if (arena.getTeam(player).equals(Teams.YELLOW)) game.aliveYellowPlayers = game.aliveYellowPlayers - 1;
						else if (arena.getTeam(player).equals(Teams.GREEN)) game.aliveGreenPlayers = game.aliveGreenPlayers - 1;
					}
					Game.alivePlayers1.remove(player.getUniqueId());

					new BukkitRunnable() {
						@Override
						public void run() {
							player.spigot().respawn();

							cancel();
						}
					}.runTaskLater(WallsMain.getInstance(), 3L);

					new BukkitRunnable() {
						@Override
						public void run() {

							game.isGameOver();

							cancel();
						}
					}.runTaskLater(WallsMain.getInstance(), 4L);

				}

			}

		}

	}

	@EventHandler
	public void onNPCDamage(NPCDamageEvent e) {
		if (e.getNPC().isProtected()) e.setCancelled(true);
	}

	@EventHandler
	public void onDamageByEntityNPC(NPCDamageByEntityEvent e) {
		if (e.getNPC().isProtected()) e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {

		Player player = (Player) e.getPlayer();

		if (e.getInventory().getTitle().contains(ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- &bClass Selector"))) {

			if (!Manager.getArena(player).getClasses().containsKey(player.getUniqueId())) {
				new BukkitRunnable() {

					public void run() {
						Menus.applyClassSelector(player);
					}

				}.runTaskLater(WallsMain.getInstance(), 0L);


			}


		}


		if (e.getInventory().getType().equals(InventoryType.ANVIL)) {

			if (anvilMap.containsKey(e.getPlayer().getUniqueId())) {

				Block b = anvilMap.get(e.getPlayer().getUniqueId());

				if (b.getData() >= 4 && b.getData() < 12) {
					if (b.getData() == 4)
						b.setData((byte) 0);
					if (b.getData() == 5)
						b.setData((byte) 1);
					if (b.getData() == 6)
						b.setData((byte) 2);
					if (b.getData() == 7)
						b.setData((byte) 3);
					if (b.getData() == 8)
						b.setData((byte) 0);
					if (b.getData() == 9)
						b.setData((byte) 1);
					if (b.getData() == 10)
						b.setData((byte) 2);
					if (b.getData() == 11)
						b.setData((byte) 3);

				}

				anvilMap.remove(e.getPlayer().getUniqueId());

			}

		}

	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {

		if (e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();
			if (Manager.isPlaying(player)) {
				Arena arena = Manager.getArena(player);
				if (e.getEntity() instanceof Player) {
					Player victim = (Player) e.getEntity();
					if (Manager.isPlaying(victim)) {

						if (!arena.getState().equals(GameState.LIVE)) e.setCancelled(true);
						if (arena.getSpectators().contains(player.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
						if (arena.getTeam(player).equals(arena.getTeam(victim))
								&& arena.getGame(arena.getID()).isTeamGame() && arena.getState() == GameState.LIVE) {
							e.setCancelled(true);
							player.sendMessage(ChatColor.RED + "You cannot hurt your teammates!");
						}
					}
					else e.setCancelled(true);
				}
			}

		}

		else if (e.getDamager() instanceof Arrow) {

			Arrow arrow = (Arrow) e.getDamager();

			if (e.getEntity() instanceof Player) {
				Player victim = (Player) e.getEntity();
				if (Manager.isPlaying(victim)) {
					Arena arena = Manager.getArena(victim);
					if (arrow.getShooter() instanceof Player) {
						Player shooter = (Player) arrow.getShooter();
						if (arena.getTeam(shooter) == arena.getTeam(victim) || arena.getState() != GameState.LIVE || arena.getSpectators().contains(shooter.getUniqueId())) e.setCancelled(true);
						else {
							e.setCancelled(false);
							if (arrow.isCritical()) shooter.sendMessage(arena.getTeam(victim).getColor() + victim.getName() + ChatColor.GRAY + " is on " + ChatColor.RED + Math.round(victim.getHealth()) + "HP" + ChatColor.GRAY + "!");
						}
					}
				}
				else e.setCancelled(true);

			}

		}

		else if (e.getDamager() instanceof FishHook) {

			FishHook arrow = (FishHook) e.getDamager();

			if (e.getEntity() instanceof Player) {
				Player victim = (Player) e.getEntity();
				if (Manager.isPlaying(victim)) {
					Arena arena = Manager.getArena(victim);
					if (arrow.getShooter() instanceof Player) {
						Player shooter = (Player) arrow.getShooter();
						if (arena.getTeam(shooter) == arena.getTeam(victim) || arena.getState() != GameState.LIVE || arena.getSpectators().contains(shooter.getUniqueId())) e.setCancelled(true);
						else e.setCancelled(false);
					}
				}
				else e.setCancelled(true);

			}

		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.ANVIL
				&& e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if (b.getData() >= 4 && b.getData() < 12) {
				if (b.getData() == 4)
					b.setData((byte) 0);
				if (b.getData() == 5)
					b.setData((byte) 1);
				if (b.getData() == 6)
					b.setData((byte) 2);
				if (b.getData() == 7)
					b.setData((byte) 3);
				if (b.getData() == 8)
					b.setData((byte) 0);
				if (b.getData() == 9)
					b.setData((byte) 1);
				if (b.getData() == 10)
					b.setData((byte) 2);
				if (b.getData() == 11)
					b.setData((byte) 3);

			}
			anvilMap.put(player.getUniqueId(), b);
		}

		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
			if (Manager.isPlaying(player)) {
				Arena arena = Manager.getArena(player);
				if (arena.getSpectators().contains(player.getUniqueId())) e.setCancelled(true);
			}
		}


		if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
			if (player.getItemInHand().getType().equals(Material.REDSTONE) || player.getItemInHand().getType().equals(Material.STRING)) {
				e.setCancelled(true);
				player.updateInventory();
			}
			if (player.getItemInHand().getItemMeta() != null
					&& player.getItemInHand().getItemMeta().getDisplayName() != null) {
				if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Spectate")) {
					Random rand = new Random();
					int alivePlayersSize = Game.getAlivePlayers().size();
					int randomIndex = rand.nextInt(alivePlayersSize);
					Player randPlayer = Bukkit.getPlayer(Game.getAlivePlayers().get(randomIndex));
					player.teleport(randPlayer);
					player.sendMessage(
							ChatColor.GREEN + "You are now spectating " + ChatColor.GOLD + randPlayer.getName());

				} else if (player.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Leave")) {

					if (Manager.isPlaying(player)) {
						if (Manager.getArena(player).getState().equals(GameState.LIVE)) {
							player.sendMessage(ChatColor.RED
									+ "You can only use this item when the game is in the countdown or recruiting phase. Use /leave instead!");
						} else {
							Manager.getArena(player)
									.sendMessage(HerobrinePVPCore.getFileManager().getRank(player).getColor()
											+ player.getName() + ChatColor.YELLOW + " has left!");
							Manager.getArena(player).removePlayer(player);

						}
					}
					else player.sendMessage(ChatColor.RED + "You are not in a game!");
				}
				else return;

			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		e.setCancelled(true);
		if (entity.getType().equals(EntityType.VILLAGER) && entity.getCustomName()
				.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&e&lITEM SHOP"))) {
			if (Game.getAlivePlayers().contains(player.getUniqueId())) Menus.applyShopHome(player);
			 else player.sendMessage(ChatColor.RED + "You can only use the shop if you are alive in-game!");
		} else if (entity.getType().equals(EntityType.VILLAGER) && entity.getCustomName()
				.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&e&lWALLS SG"))) {
			e.setCancelled(true);
			if (Manager.isPlaying(player)) {
				player.sendMessage(ChatColor.RED + "You are in a game!");
			} else {
				if (Manager.getArena(0).getState().equals(GameState.LIVE)) {
					player.sendMessage(ChatColor.RED + "That game is currently live!");

				} else {
					if (Manager.getArena(0).canJoin()) {
						Manager.getArena(0).addPlayer(player);
						player.sendMessage(
								ChatColor.GRAY + "Sending you to a game of " + ChatColor.YELLOW + "Walls SG");
					} else {
						player.sendMessage(ChatColor.RED
								+ "You cannot join this game right now! If you believe this is in error, please contact staff.");
					}
				}
			}
		}

		Arena arena = null;
		if (Manager.isPlaying(player)) arena = Manager.getArena(player);
		if (arena == null) return;
		if (!arena.getState().equals(GameState.LIVE) || !arena.getGame().equals(Games.WALLS_SG)) return;
		if (!arena.getSpectators().contains(player.getUniqueId())) return;
		if (!(entity instanceof Player)) return;
		if (!Game.getAlivePlayers().contains(entity.getUniqueId())) return;
		CraftPlayer p = (CraftPlayer) player;
		player.setGameMode(GameMode.SPECTATOR);
		p.getHandle().setSpectatorTarget(((CraftEntity)entity).getHandle());
		GameCoreMain.getInstance().sendTitle(player, "&aSpectating " + entity.getName(), "&c&lSNEAK &7to stop spectating!", 0, 1, 0);
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();
		Arena arena = null;
		if (Manager.isPlaying(player)) arena = Manager.getArena(player);
		if (arena == null) return;
		if (!arena.getState().equals(GameState.LIVE) || !arena.getGame().equals(Games.WALLS_SG)) return;
		if (!arena.getSpectators().contains(player.getUniqueId())) return;
		CraftPlayer p = (CraftPlayer) player;
		player.teleport(player.getSpectatorTarget().getLocation());
		p.getHandle().setSpectatorTarget(((CraftEntity)player).getHandle());
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(true);
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		if (e.getCurrentItem() == null) {
			e.setCancelled(true);
			return;
		}


		if (e.getInventory().getTitle()
				.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- &aShop"))) {
			if (e.getCurrentItem() != null) {
				e.setCancelled(true);

				switch (e.getCurrentItem().getType()) {
				case EMERALD:
					Menus.applyShop(player, Shops.EMERALD);
					break;
				case DIAMOND:
					Menus.applyShop(player, Shops.DIAMOND);
					break;
				case GOLD_INGOT:
					Menus.applyShop(player, Shops.GOLD);
					break;
				case INK_SACK:
					Menus.applyShop(player, Shops.LAPIS);
					break;
				case IRON_INGOT:
					Menus.applyShop(player, Shops.IRON_INGOT);
					break;
				case COAL:
					Menus.applyShop(player, Shops.COAL);
					break;
				case WOOD:
					Menus.applyShop(player, Shops.BLOCK_TRADING);
					break;
					case REDSTONE:
						Menus.applyShop(player, Shops.ENGINEER_UPGRADES);
					break;
				default:
					return;
				}

			}
		}
		else if(e.getClickedInventory().getTitle().contains(HerobrinePVPCore.translateString("&6Walls SG &7- ")) && Manager.getArena(player).getState().equals(GameState.LIVE) && Shops.getShopFromMaterial(e.getClickedInventory().getItem(4)) != null && Manager.isPlaying(player) && Manager.getArena(player).getwallsSGGame() != null) {
			e.setCancelled(true);
			Arena arena = Manager.getArena(player);

			Shops clickedShop = Shops.getShopFromMaterial(e.getClickedInventory().getItem(4));
			ShopItems clickedItem = null;

			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
				if (e.getSlot() == 0) {
					Menus.applyShopHome(player);
					return;
				}
				ItemStack item = e.getCurrentItem();
				NBTReader reader = new NBTReader(item);

				try {
					clickedItem = ShopItems.valueOf(reader.getStringNBT("id").get());
				}
				catch(IllegalArgumentException ignored){}

			}

			if (clickedItem != null) {
				if(arena.getType() != GameType.MODIFIER) clickedItem.purchaseItem(clickedItem, clickedShop, player, false, e.getSlot());
				else if (arena.getClass(player) != null && arena.getClass(player).getShopDiscount() != 0) clickedItem.purchaseItem(clickedItem, clickedShop, player, true, e.getSlot());
				else clickedItem.purchaseItem(clickedItem, clickedShop, player, false, e.getSlot());
			}
		}

		else if (e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
			if (e.getRawSlot() == 2) {
				Block b = anvilMap.get(player.getUniqueId());
				// prevents anvil from taking damage
				if (b.getData() >= 4 && b.getData() < 12) {
					if (b.getData() == 4)
						b.setData((byte) 0);
					if (b.getData() == 5)
						b.setData((byte) 1);
					if (b.getData() == 6)
						b.setData((byte) 2);
					if (b.getData() == 7)
						b.setData((byte) 3);
					if (b.getData() == 8)
						b.setData((byte) 0);
					if (b.getData() == 9)
						b.setData((byte) 1);
					if (b.getData() == 10)
						b.setData((byte) 2);
					if (b.getData() == 11)
						b.setData((byte) 3);
				}
				if (e.getCurrentItem() != null) {

					String itemName;
					if (e.getClickedInventory().getItem(0).getItemMeta().getDisplayName() == null) itemName = Game.getFriendlyName(e.getClickedInventory().getItem(0).getType());
					else itemName = e.getClickedInventory().getItem(0).getItemMeta().getDisplayName();


					if (e.getCurrentItem().getItemMeta() != null
							&& e.getCurrentItem().getItemMeta().getDisplayName() != null) {
						ItemStack stack = e.getCurrentItem();

						if (stack.getType().equals(Material.ENCHANTED_BOOK)) {
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getCurrentItem().getItemMeta();
							if (meta.getDisplayName() != itemName) {
								meta.setDisplayName(itemName);
								stack.setItemMeta(meta);
							}

							for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {

								if (meta.getStoredEnchants().get(enchantment) == 1) {

									String name = e.getCurrentItem().getItemMeta().getDisplayName();

									name = name.replace("1", "1");
									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
									stack.setItemMeta(meta);

								} else if (meta.getStoredEnchants().get(enchantment) == 2) {
									String name = e.getCurrentItem().getItemMeta().getDisplayName();

									name = name.replace("1", "2");
									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
									stack.setItemMeta(meta);
								} else if (meta.getStoredEnchants().get(enchantment) == 3) {

									String name = e.getCurrentItem().getItemMeta().getDisplayName();

									name = name.replace("2", "3");
									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
									stack.setItemMeta(meta);

								} else if (meta.getStoredEnchants().get(enchantment) == 4) {

									String name = e.getCurrentItem().getItemMeta().getDisplayName();

									name = name.replace("3", "4");
									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

									stack.setItemMeta(meta);
								}

								else {
									String name = e.getCurrentItem().getItemMeta().getDisplayName();

									name = name.replace("4", "5");

									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
									stack.setItemMeta(meta);

								}

							}
						} else {
							ItemMeta meta = e.getCurrentItem().getItemMeta();

							if (meta.getDisplayName() != itemName) {
								meta.setDisplayName(itemName);
								stack.setItemMeta(meta);
							}
							meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
									e.getCurrentItem().getItemMeta().getDisplayName()));
							stack.setItemMeta(meta);
						}
					}

				}

			}

		}


	}

}
