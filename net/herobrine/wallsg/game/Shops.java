package net.herobrine.wallsg.game;

import net.herobrine.gamecore.Arena;
import net.herobrine.gamecore.GameType;
import net.herobrine.gamecore.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Shops {


    EMERALD(ChatColor.GREEN + "Special Items", ChatColor.GREEN + "Emerald", new ItemStack(Material.EMERALD, 1), new ItemStack(Material.EMERALD, 1), new ShopItems[]{ShopItems.EMERALD_BLADE, ShopItems.EMERALD_HELMET, ShopItems.EMERALD_CHESTPLATE, ShopItems.EMERALD_LEGGINGS, ShopItems.EMERALD_BOOTS, ShopItems.STRENGTH_POTION}, true, false),
    DIAMOND(ChatColor.AQUA + "Armor and Weapons", ChatColor.AQUA + "Diamond", new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND), new ShopItems[]{ShopItems.DIAMOND_SWORD, ShopItems.DIAMOND_HELMET, ShopItems.DIAMOND_CHESTPLATE, ShopItems.DIAMOND_LEGGINGS, ShopItems.DIAMOND_BOOTS, ShopItems.PICKAXE_UPGRADE}, false, false),
    LAPIS(ChatColor.BLUE + "Enchanting Shop", ChatColor.BLUE + "Lapis", new ItemStack(Material.INK_SACK, 1, (byte) 4),
            new ItemStack(Material.INK_SACK, 1, (byte) 4),
            new ShopItems[]{ShopItems.EXP_BOTTLES, ShopItems.PROTECTION_BOOK, ShopItems.SHARPNESS_BOOK}, false, false),
    GOLD(ChatColor.GOLD + "Healing", ChatColor.GOLD + "Gold", new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_INGOT), new ShopItems[]{ShopItems.GOLDEN_APPLE, ShopItems.HEALING_1, ShopItems.HEALING_2},false, false),
    IRON_INGOT(ChatColor.GRAY + "Armor and Weapons", ChatColor.GRAY + "Iron", new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ShopItems[] {ShopItems.IRON_SWORD, ShopItems.IRON_HELMET, ShopItems.IRON_CHESTPLATE, ShopItems.IRON_LEGGINGS, ShopItems.IRON_BOOTS, ShopItems.BOW, ShopItems.PACK_OF_ARROWS}, false, false),
    COAL(ChatColor.DARK_GRAY + "Utility Shop",  ChatColor.DARK_GRAY + "Coal", new ItemStack(Material.COAL), new ItemStack(Material.COAL), new ShopItems[] {ShopItems.WATER_BUCKET, ShopItems.FISHING_ROD, ShopItems.ENDER_PEARL}, false, false),
    BLOCK_TRADING(ChatColor.YELLOW + "Trades", ChatColor.YELLOW + "Trades", new ItemStack(Material.WOOD), null, new ShopItems[]{ShopItems.COBBLESTONE_TRADE, ShopItems.DIRT_TRADE, ShopItems.OBSIDIAN_TRADE, ShopItems.REDSTONE_TRADE}, false, true),
    ENGINEER_UPGRADES(ChatColor.RED + "Cannon Upgrades", ChatColor.RED + "Redstone", new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE), new ShopItems[] {ShopItems.CANNON_SPEED_UPGRADE, ShopItems.CANNON_DAMAGE_UPGRADE, ShopItems.CANNON_RANGE_UPGRADE}, true, false);


    private String name;
    private String currencyDisplayName;
    private ItemStack material;
    private  ItemStack currency;
    private ShopItems[] items;
    private boolean requiresCurrency;
    private boolean isBlockTrading;

    // Shops Contain:
    // String name: used to represent shop
    // Material material: used to represent shop
    // ItemStack currency: what is used to buy items here?
    // ShopItems[] items: items that can be purchased in the shop
    // boolean requiresCurrency;
    private Shops(String name, String currencyDisplayName, ItemStack material, ItemStack currency, ShopItems[] items, boolean requiresCurrency, boolean isBlockTrading) {

        this.name = name;
        this.currencyDisplayName = currencyDisplayName;
        this.material = material;
        this.currency = currency;
        this.items = items;
        this.requiresCurrency = requiresCurrency;
        this.isBlockTrading = isBlockTrading;


    }

    public String getName() {
        return name;
    }
    public String getCurrencyDisplayName() {
        return currencyDisplayName;
    }
    public ItemStack getMaterial() {
        return material;
    }
    public ItemStack getCurrency() {
        return currency;
    }
    public boolean requiresCurrency() {
        return requiresCurrency;
    }
    public boolean isBlockTrading() {
        return isBlockTrading;
    }
    public ShopItems[] getItems() {
        return items;
    }
    public static Shops getShopFromMaterial(ItemStack mat) {
        String shopName = ChatColor.stripColor(mat.getType().name());

       if(mat.getType().equals(Material.INK_SACK)) return Shops.LAPIS;
       else if (mat.getType().equals(Material.IRON_INGOT)) return Shops.IRON_INGOT;
       else if (mat.getType().equals(Material.REDSTONE)) return Shops.ENGINEER_UPGRADES;
       else if (mat.getType().equals(Material.WOOD)) return Shops.BLOCK_TRADING;
       else {
           String s = ChatColor.stripColor(mat.getItemMeta().getDisplayName());

           StringBuilder sb = new StringBuilder();
           sb.append(s.toUpperCase().replace(' ', '_'));

           return Shops.valueOf(sb.toString());
       }


    }

    public int getPurchasebleItems(Arena arena, Shops shop) {
        int purchaseableItems = 0;
        for (ShopItems item : shop.getItems()) {
            if (arena.getType().equals(GameType.MODIFIER) && item.vanillaOnly()) continue;
            if (arena.getType().equals(GameType.VANILLA) && item.modifierOnly()) continue;
            purchaseableItems++;
        }
        return purchaseableItems;
    }
}
