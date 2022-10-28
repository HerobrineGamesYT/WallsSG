package net.herobrine.wallsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.herobrine.gamecore.*;
import net.herobrine.wallsg.game.ShopItems;
import net.herobrine.wallsg.game.Shops;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Menus {


	public static void applyClassSelector(Player player) {
		Inventory classSelector = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- &bClass Selector"));

		ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

		ItemMeta fillerMeta = filler.getItemMeta();
		fillerMeta.setDisplayName(" ");

		filler.setItemMeta(fillerMeta);


		int currentSlots = 0;

		while(currentSlots < classSelector.getSize()) {

			if (currentSlots < 10 || currentSlots > 43 || currentSlots == 36 || currentSlots == 18 || currentSlots == 27 || currentSlots == 35 || currentSlots == 26 || currentSlots == 17) {
				classSelector.setItem(currentSlots, filler);
			}

			currentSlots++;


		}

		int[] slots = new int[] {11, 13, 15, 29, 31, 33};


		int i = 0;

		for (ClassTypes classType : ClassTypes.values()) {

			if (classType.getGame().equals(Games.WALLS_SG)) {


				ItemBuilder item = new ItemBuilder(classType.getMaterial());
				item.setDisplayName(classType.getDisplay());
				item.setLore(Arrays.asList(classType.getDescription()));

				classSelector.setItem(slots[i], item.build());

				i++;

			}



		}

		for (int slot : slots) {
			if (classSelector.getItem(slot) == null) {
				ItemBuilder fillerItem = new ItemBuilder(Material.BEDROCK);

				fillerItem.setDisplayName(ChatColor.RED + "???");
				fillerItem.setLore(Arrays.asList(ChatColor.RED + "This class has not", ChatColor.RED + "been developed yet and will be", ChatColor.RED + "added in a future update."));
				classSelector.setItem(slot, fillerItem.build());
			}
		}



		player.openInventory(classSelector);
	}


	public static void applyShopHome(Player player) {
		Inventory shopHome = Bukkit.createInventory(null, 27,
				ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- &aShop"));
		ItemStack downGlass = new ItemStack(Material.THIN_GLASS, 1);
		ItemMeta downGlassMeta = downGlass.getItemMeta();
		downGlassMeta.setDisplayName(ChatColor.GRAY + "↓↓ Select a resource shop");
		downGlass.setItemMeta(downGlassMeta);
		ItemStack upGlass = new ItemStack(Material.THIN_GLASS, 1);
		ItemMeta upGlassMeta = upGlass.getItemMeta();
		upGlassMeta.setDisplayName(ChatColor.GRAY + "↑↑ Select a resource shop");
		upGlass.setItemMeta(upGlassMeta);
		ItemStack rightGlass = new ItemStack(Material.THIN_GLASS, 1);
		ItemMeta rightGlassMeta = rightGlass.getItemMeta();
		rightGlassMeta.setDisplayName(ChatColor.GRAY + "→→ Select a resource shop");
		rightGlass.setItemMeta(rightGlassMeta);
		ItemStack leftGlass = new ItemStack(Material.THIN_GLASS, 1);
		ItemMeta leftGlassMeta = leftGlass.getItemMeta();
		leftGlassMeta.setDisplayName(ChatColor.GRAY + "←← Select a resource shop");
		leftGlass.setItemMeta(leftGlassMeta);
		ItemStack emerald = new ItemStack(Material.EMERALD, 1);
		ItemMeta emeraldMeta = emerald.getItemMeta();
		emeraldMeta.setDisplayName(ChatColor.GREEN + "???");
		emeraldMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		emerald.setItemMeta(emeraldMeta);
		ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
		ItemMeta diamondMeta = diamond.getItemMeta();
		diamondMeta.setDisplayName(ChatColor.AQUA + "Armor and Weapon Shop");
		diamond.setItemMeta(diamondMeta);
		ItemStack lapis = new ItemStack(Material.INK_SACK, 1, (short) 4);
		ItemMeta lapisMeta = lapis.getItemMeta();
		lapisMeta.setDisplayName(ChatColor.BLUE + "Enchanting Shop");
		lapis.setItemMeta(lapisMeta);
		ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta goldMeta = gold.getItemMeta();
		goldMeta.setDisplayName(ChatColor.GOLD + "Healing Shop");
		gold.setItemMeta(goldMeta);
		ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
		ItemMeta ironMeta = iron.getItemMeta();
		ironMeta.setDisplayName(ChatColor.GRAY + "Armor and Weapon Shop");
		iron.setItemMeta(ironMeta);

		ItemStack coal = new ItemStack(Material.COAL, 1);
		ItemMeta coalMeta = coal.getItemMeta();
		coalMeta.setDisplayName(ChatColor.DARK_GRAY + "Utility Shop");
		coal.setItemMeta(coalMeta);
		ItemStack block = new ItemStack(Material.WOOD, 1);
		ItemMeta blockMeta = block.getItemMeta();
		blockMeta.setDisplayName(ChatColor.YELLOW + "Block Trading");
		block.setItemMeta(blockMeta);

		shopHome.setItem(0, downGlass);
		shopHome.setItem(1, downGlass);
		shopHome.setItem(2, downGlass);
		shopHome.setItem(3, downGlass);
		shopHome.setItem(4, downGlass);
		shopHome.setItem(5, downGlass);
		shopHome.setItem(6, downGlass);
		shopHome.setItem(7, downGlass);
		shopHome.setItem(8, downGlass);
		shopHome.setItem(9, rightGlass);
		shopHome.setItem(10, emerald);
		shopHome.setItem(11, diamond);
		shopHome.setItem(12, lapis);
		shopHome.setItem(13, gold);
		shopHome.setItem(14, iron);
		shopHome.setItem(15, coal);
		shopHome.setItem(16, block);
		shopHome.setItem(17, leftGlass);
		shopHome.setItem(18, upGlass);
		shopHome.setItem(19, upGlass);
		shopHome.setItem(20, upGlass);
		shopHome.setItem(21, upGlass);
		shopHome.setItem(22, upGlass);
		shopHome.setItem(23, upGlass);
		shopHome.setItem(24, upGlass);
		shopHome.setItem(25, upGlass);
		shopHome.setItem(26, upGlass);
		player.openInventory(shopHome);

	}


	public static void applyShop(Player player, Shops shop) {
		if (shop.requiresCurrency() && !player.getInventory().contains(shop.getCurrency().getType())) {
			player.sendMessage(ChatColor.RED + "You need at least 1 " + shop.getCurrencyDisplayName() + ChatColor.RED + " to use this shop!");
			player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
			player.closeInventory();
			return;
		}
		Inventory shopInv = Bukkit.createInventory(null, 54,
				ChatColor.translateAlternateColorCodes('&', "&6Walls SG &7- " + shop.getName()));

		ItemStack goBack = new ItemStack(Material.FEATHER, 1);
		ItemMeta goBackMeta = goBack.getItemMeta();
		goBackMeta.setDisplayName(ChatColor.RED + "Back");
		goBack.setItemMeta(goBackMeta);

		ItemStack title = shop.getMaterial().clone();
		ItemMeta titleMeta = title.getItemMeta();
		titleMeta.setDisplayName(shop.getCurrencyDisplayName());
		title.setItemMeta(titleMeta);

		shopInv.setItem(0, goBack);
		shopInv.setItem(4, title);
		boolean isEconomist = false;
		if(Manager.getArena(player).getType() == GameType.MODIFIER) {
			if(Manager.getArena(player).getClass(player).equals(ClassTypes.ECONOMIST)) isEconomist = true;
		}

			int i = 0;
			int startingSlot;
			int slot;
			if (shop.getItems().length > 3) {
				startingSlot = 21;
				slot = startingSlot - shop.getItems().length;

				if (slot < 19) slot = 19;

				for (ShopItems item : shop.getItems()) {
					if (i==7) {
						startingSlot = startingSlot + 7;
						slot = startingSlot;
						i = 0;
					}
					shopInv.setItem(slot, item.createItem(item, shop, isEconomist));

					slot = slot + 1;

					i++;
				}

			}

			else if (shop.getItems().length == 1) {
				shopInv.setItem(22, shop.getItems()[0].createItem(shop.getItems()[0], shop, isEconomist));
			}

			else {
				startingSlot = 21;

				for (ShopItems item : shop.getItems()) {
					shopInv.setItem(startingSlot, item.createItem(item, shop, isEconomist));
					startingSlot = startingSlot + 1;
				}

			}


	player.openInventory(shopInv);
	}

}
