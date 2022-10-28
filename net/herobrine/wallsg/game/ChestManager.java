package net.herobrine.wallsg.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.herobrine.gamecore.Arena;
import net.herobrine.gamecore.GameState;
import net.herobrine.gamecore.Games;
import net.herobrine.gamecore.Manager;

public class ChestManager implements Listener {

	public static ChestManager instance;

	public ChestManager() {

		instance = this;
	}

	private final Set<Location> openedChests = new HashSet<>();

	public void fill(Inventory inventory) {

		inventory.clear();

		ThreadLocalRandom random = ThreadLocalRandom.current();
		int maxSlots = random.nextInt(4, 6);

		List<Integer> filledSlotsList = new ArrayList<>();

		int filledSlots = 0;
		while (filledSlots < maxSlots) {
			int slotIndex = random.nextInt(0, inventory.getSize() - 1);
			LootTables randomItem = LootTables.values()[random.nextInt(LootTables.values().length)];

			if (!filledSlotsList.contains(slotIndex)) {
				if (randomItem.shouldFill(random, randomItem.baseSpawnChance())) {

					ItemStack item = randomItem.make(random, randomItem);

					inventory.setItem(slotIndex, item);
					filledSlotsList.add(slotIndex);

					filledSlots++;
				}
			}

		}

	}

	public void markAsOpened(Location location) {
		openedChests.add(location);

	}

	public static ChestManager getInstance() {
		return instance;
	}

	public boolean hasBeenOpened(Location location) {
		return openedChests.contains(location);
	}

	public void reset() {

		for (Location location : openedChests) {
			location.getBlock().setType(Material.AIR);
		}

		openedChests.clear();

	}

	@EventHandler
	public void onOpen(InventoryOpenEvent e) {

		InventoryHolder holder = e.getInventory().getHolder();

		if (holder instanceof Chest) {
			Chest chest = (Chest) holder;

			if (Manager.getArena(chest.getLocation().getWorld()) != null) {
				Arena arena = Manager.getArena(chest.getLocation().getWorld());

				if (arena.getGame(arena.getID()).equals(Games.WALLS_SG) && arena.getState().equals(GameState.LIVE)) {
					if (!hasBeenOpened(chest.getLocation())) {
						markAsOpened(chest.getLocation());
						fill(chest.getInventory());

					}

				}

			}

		}

	}

}
