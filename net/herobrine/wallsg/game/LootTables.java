package net.herobrine.wallsg.game;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum LootTables {

	LAPIS(Material.INK_SACK, 4, .25, 12, 13, 14, 15, .27, .26, .25, .22),
	IRON_INGOT(Material.IRON_INGOT, 0, .15, 7, 8, 9, 10, .27, .33, .21, .19),
	GOLD_INGOT(Material.GOLD_INGOT, 0, .25, 9, 10, 11, 12, .27, .33, .21, .19),
	DIAMOND(Material.DIAMOND, 0, .14, 6, 7, 8, 9, .40, .30, .20, .10),
	COAL(Material.COAL, 0, .15, 10, 11, 12, 13, .17, .18, .35, .30),
	EMERALD(Material.EMERALD, 0, .06, 2, 3, 4, 5, .65, .20, .10, .05);

	private Material material;
	private int durability;

	// base chance of an item appearing in a slot
	private double baseSpawnChance;

	// stack sizes that can appear
	private int stackSize1;
	private int stackSize2;
	private int stackSize3;
	private int stackSize4;

	// the chance for each stack size to appear in a slot
	private double stackSize1SpawnChance;
	private double stackSize2SpawnChance;
	private double stackSize3SpawnChance;
	private double stackSize4SpawnChance;

	private LootTables(Material material, int durability, double baseSpawnChance, int stackSize1, int stackSize2,
			int stackSize3, int stackSize4, double stackSize1SpawnChance, double stackSize2SpawnChance,
			double stackSize3SpawnChance, double stackSize4SpawnChance) {

		this.material = material;
		this.durability = durability;
		this.baseSpawnChance = baseSpawnChance;
		this.stackSize1 = stackSize1;
		this.stackSize2 = stackSize2;
		this.stackSize3 = stackSize3;
		this.stackSize4 = stackSize4;
		this.stackSize1SpawnChance = stackSize1SpawnChance;
		this.stackSize2SpawnChance = stackSize2SpawnChance;
		this.stackSize3SpawnChance = stackSize3SpawnChance;
		this.stackSize4SpawnChance = stackSize4SpawnChance;

	}

	public Material getMaterial() { return material; }

	public int getDurability() {
		return durability;
	}

	public double baseSpawnChance() {
		return baseSpawnChance;
	}

	public int getStackSize1() {
		return stackSize1;
	}

	public int getStackSize2() {
		return stackSize2;
	}

	public int getStackSize3() { return stackSize3; }

	public int getStackSize4() {
		return stackSize4;
	}

	public double getStackSize1SpawnChance() { return stackSize1SpawnChance; }

	public double getStackSize2SpawnChance() {
		return stackSize2SpawnChance;
	}

	public double getStackSize3SpawnChance() {
		return stackSize3SpawnChance;
	}

	public double getStackSize4SpawnChance() { return stackSize4SpawnChance; }

	public boolean shouldFill(Random random, double chance) { return random.nextDouble() < chance; }

	public ItemStack make(Random random, LootTables table) {

		if (random.nextDouble() < table.getStackSize1SpawnChance()) {

			return new ItemStack(table.getMaterial(), table.getStackSize1(), (short) table.getDurability());
		}

		else if (random.nextDouble() < table.getStackSize2SpawnChance()) {
			return new ItemStack(table.getMaterial(), table.getStackSize2(), (short) table.getDurability());
		} else if (random.nextDouble() < table.getStackSize2SpawnChance()) {
			return new ItemStack(table.getMaterial(), table.getStackSize2(), (short) table.getDurability());
		}

		else if (random.nextDouble() < table.getStackSize3SpawnChance()) {
			return new ItemStack(table.getMaterial(), table.getStackSize3(), (short) table.getDurability());
		} else if (random.nextDouble() < table.getStackSize4SpawnChance()) {
			return new ItemStack(table.getMaterial(), table.getStackSize4(), (short) table.getDurability());
		} else {
			return new ItemStack(table.getMaterial(), table.getStackSize1(), (short) table.getDurability());
		}

	}

}
