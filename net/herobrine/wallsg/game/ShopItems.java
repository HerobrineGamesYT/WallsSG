package net.herobrine.wallsg.game;

import javafx.util.Pair;
import net.herobrine.gamecore.Arena;
import net.herobrine.gamecore.ClassTypes;
import net.herobrine.gamecore.ItemBuilder;
import net.herobrine.gamecore.Manager;
import net.herobrine.wallsg.Game;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ShopItems {


    EMERALD_BLADE(ChatColor.GREEN + "Emerald Blade", 6, 1, Material.EMERALD, null, new String[] {}, new Enchantment[]{Enchantment.DAMAGE_ALL},
            10, 0, 0, null, 0, 0, false,  false,
            false, false, false, false, false, null, 0, Shops.EMERALD, false, null),
    EMERALD_HELMET(ChatColor.GREEN + "Emerald Helmet", 3, 1, Material.LEATHER_HELMET, Color.fromRGB(56, 217, 56),
            new String[] {ChatColor.GREEN + "Defense is slightly better than Protection 3 Diamond Armor", "", ChatColor.GREEN +
                    "A piece of armor so legendary,", ChatColor.GREEN + "Emerus once wore it in battle.",
                    "", ChatColor.GOLD + "Full Set Bonus: Emerus's Wind", ChatColor.GRAY + "Gain speed 2 while wearing."}, new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL},
            9, 0, 0, null, 0, 0, false, true, false,
            true, true, false, false, null, 0, Shops.EMERALD, false, null),
    EMERALD_CHESTPLATE(ChatColor.GREEN + "Emerald Chestplate", 5, 1, Material.LEATHER_CHESTPLATE, Color.fromRGB(56, 217, 56),
            new String[] {ChatColor.GREEN + "Defense is slightly better than Protection 3 Diamond Armor", "", ChatColor.GREEN +
                    "A piece of armor so legendary,", ChatColor.GREEN + "Emerus once wore it in battle.",
                    "", ChatColor.GOLD + "Full Set Bonus: Emerus's Wind", ChatColor.GRAY + "Gain speed 2 while wearing."},
            new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL},
            9,0,0,null, 0, 0, false, true, false, true,
            true, false, false, null, 0, Shops.EMERALD, false, null),
    EMERALD_LEGGINGS(ChatColor.GREEN + "Emerald Leggings", 4, 1, Material.LEATHER_LEGGINGS, Color.fromRGB(56, 217, 56),
            new String[] {ChatColor.GREEN + "Defense is slightly better than Protection 3 Diamond Armor", "", ChatColor.GREEN +
                    "A piece of armor so legendary,", ChatColor.GREEN + "Emerus once wore it in battle.",
                    "", ChatColor.GOLD + "Full Set Bonus: Emerus's Wind", ChatColor.GRAY + "Gain speed 2 while wearing."},
            new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL},
            9,0,0,null, 0, 0, false, true, false, true,
            true, false, false, null, 0, Shops.EMERALD, false, null),
    EMERALD_BOOTS(ChatColor.GREEN + "Emerald Boots", 2, 1, Material.LEATHER_BOOTS, Color.fromRGB(56, 217, 56),
            new String[] {ChatColor.GREEN + "Defense is slightly better than Protection 3 Diamond Armor", "", ChatColor.GREEN +
                    "A piece of armor so legendary,", ChatColor.GREEN + "Emerus once wore it in battle.",
                    "", ChatColor.GOLD + "Full Set Bonus: Emerus's Wind", ChatColor.GRAY + "Gain speed 2 while wearing."},
            new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL},
            9,0,0,null, 0, 0, false, true, false, true,
            true, false, false, null, 0,Shops.EMERALD, false, null),
    STRENGTH_POTION(ChatColor.GREEN + "Strength Potion", 4, 1, Material.POTION, null, new String[] {ChatColor.GRAY + "Strength (0:07)", "",
    ChatColor.GREEN + "Give a powerful strength effect to yourself."}, null,  0,0 ,0, PotionEffectType.INCREASE_DAMAGE,
            7, 1, false, false, false, false, false, true, false, null, 0, Shops.EMERALD, false, null),
    DIAMOND_SWORD(ChatColor.AQUA + "Diamond Sword", 2, 1, Material.DIAMOND_SWORD, null, new String[] {}, new Enchantment[] {},
            0 ,0 ,0, null, 0,0, false, false, false, false, false, false, false, null, 0, Shops.DIAMOND, false, null),
    DIAMOND_HELMET(ChatColor.AQUA + "Diamond Helmet", 4, 1, Material.DIAMOND_HELMET, null, new String[] {}, new Enchantment[] {}, 0,0 ,0,
            null, 0,0, false, false, false, false, false, false, false, null, 0, Shops.DIAMOND,false, null),
    DIAMOND_CHESTPLATE(ChatColor.AQUA + "Diamond Chestplate", 7, 1, Material.DIAMOND_CHESTPLATE, null, new String[] {}, new Enchantment[] {}, 0,0 ,0,
            null, 0,0, false, false, false, false, false, false, false, null, 0,Shops.DIAMOND, false, null),
    DIAMOND_LEGGINGS(ChatColor.AQUA + "Diamond Leggings", 6, 1, Material.DIAMOND_LEGGINGS, null, new String[] {}, new Enchantment[] {}, 0,0 ,0,
            null, 0,0, false, false, false, false, false, false, false, null, 0,Shops.DIAMOND, false, null),
    DIAMOND_BOOTS(ChatColor.AQUA + "Diamond Boots", 2, 1, Material.DIAMOND_BOOTS, null, new String[] {}, new Enchantment[] {}, 0,0 ,0,
            null, 0,0, false, false, false, false, false, false, false, null, 0,Shops.DIAMOND, false, null),
    PICKAXE_UPGRADE(ChatColor.AQUA + "Diamond Pickaxe", 5, 1, Material.DIAMOND_PICKAXE, null, new String[]
            {ChatColor.AQUA + "The best pickaxe for", ChatColor.AQUA + "all your mining needs!"}, new Enchantment[] {Enchantment.DIG_SPEED, Enchantment.DURABILITY},
            4, 3, 0, null, 0,0, false, false, false, false,
            false, false, false, null, 0, Shops.DIAMOND, false, null),
    EXP_BOTTLES(ChatColor.BLUE + "XP Bottle", 3, 32, Material.EXP_BOTTLE, null, new String[] {ChatColor.BLUE + "Required for enchants."},
            new Enchantment[] {}, 0 ,0 ,0, null, 0,0,
            false,false,false,false,false,false, false, null, 0, Shops.LAPIS, false, null),
    PROTECTION_BOOK(ChatColor.BLUE + "Protection 1", 8, 1, Material.ENCHANTED_BOOK, null,
            new String[] {ChatColor.BLUE + "Apply to your item using the anvil", ChatColor.BLUE + "next to the shopkeeper!"},
            new Enchantment[] {Enchantment.PROTECTION_ENVIRONMENTAL}, 1 ,0 ,0, null, 0,0,
            true,false,false,false,false,false, false, null, 0, Shops.LAPIS, false, null),
    SHARPNESS_BOOK(ChatColor.BLUE + "Sharpness 1", 7, 1, Material.ENCHANTED_BOOK, null,
            new String[] {ChatColor.BLUE + "Apply to your item using the anvil", ChatColor.BLUE + "next to the shopkeeper!"},
            new Enchantment[] {Enchantment.DAMAGE_ALL}, 1 ,0 ,0, null, 0,0,
            true,false,false,false,false,false,  false, null, 0, Shops.LAPIS, false, null),
    POWER_BOOK(ChatColor.BLUE + "Power 1", 6, 1, Material.ENCHANTED_BOOK, null,
            new String[] {ChatColor.BLUE + "Apply to your item using the anvil", ChatColor.BLUE + "next to the shopkeeper!"},
            new Enchantment[] {Enchantment.ARROW_DAMAGE}, 1 ,0 ,0, null, 0,0,
            true,false,false,false,false,false,  false, null, 0, Shops.LAPIS, false, null),
    GOLDEN_APPLE(ChatColor.GOLD + "Golden Apple", 3, 1,Material.GOLDEN_APPLE, null, new String[] {}, new Enchantment[] {},
            0,0,0,null, 0,0, false, false, false, false,
            false, false,  false, null, 0, Shops.GOLD, false, null),
    HEALING_1(ChatColor.translateAlternateColorCodes('&', "&6Instant Health (&f2.0 &c❤&6)"), 2, 1, Material.POTION, null, new String[] {},
            new Enchantment[] {}, 0,0,0, PotionEffectType.HEAL, 0,0, false,
            false, false, false, false, true,  false, null, 0, Shops.GOLD, false, null),
    HEALING_2(ChatColor.translateAlternateColorCodes('&', "&6Instant Health (&f4.0 &c❤&6)"), 5, 1, Material.POTION, null, new String[] {},
            new Enchantment[] {}, 0,0,0, PotionEffectType.HEAL, 0,1, false,
            false, false, false, false, true, false, null, 0, Shops.GOLD, false, null),
    IRON_SWORD(ChatColor.GRAY + "Iron Sword", 2, 1, Material.IRON_SWORD, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false, false,
            false, false, false,  false, null, 0, Shops.IRON_INGOT, false, null),
    IRON_HELMET(ChatColor.GRAY + "Iron Helmet", 4, 1, Material.IRON_HELMET, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false, false,
            false, false, false,  false, null, 0, Shops.IRON_INGOT, false, null),
    IRON_CHESTPLATE(ChatColor.GRAY + "Iron Chestplate", 7, 1, Material.IRON_CHESTPLATE, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false, false,
            false, false, false,  false, null, 0, Shops.IRON_INGOT, false, null),
    IRON_LEGGINGS(ChatColor.GRAY + "Iron Leggings", 6, 1, Material.IRON_LEGGINGS, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false, false,
            false, false, false,  false, null, 0, Shops.IRON_INGOT, false, null),
    IRON_BOOTS(ChatColor.GRAY + "Iron Boots", 2, 1, Material.IRON_BOOTS, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false, false,
            false, false, false,  false, null, 0, Shops.IRON_INGOT, false, null),
    BOW(ChatColor.GRAY + "Simple Bow", 1, 1, Material.BOW, null, new String[] {}, new Enchantment[] {},
            0,0,0, null, 0,0,false, false, false,
            false ,false, false, false, Material.STRING, 2, Shops.IRON_INGOT, false, null),
    PACK_OF_ARROWS(ChatColor.GRAY + "Arrows", 2, 16, Material.ARROW, null, new String[] {}, new Enchantment[] {},
            0,0,0,null,0,0, false, false, false, false,
            false, false, false, null, 0, Shops.IRON_INGOT, false, null),
    FISHING_ROD(ChatColor.DARK_GRAY + "Fishing Rod", 5, 1, Material.FISHING_ROD, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false ,
            false, false, false, false, false, null, 0, Shops.COAL, false, null),
    WATER_BUCKET(ChatColor.DARK_GRAY + "Water Bucket", 6, 1, Material.WATER_BUCKET, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false ,
            false, false, false, false, false, null, 0, Shops.COAL, false, null),
    ENDER_PEARL(ChatColor.DARK_GRAY + "Ender Pearl", 10, 1, Material.ENDER_PEARL, null, new String[] {ChatColor.GRAY + "A secret lies within" + ChatColor.GREEN + " emeralds...", ChatColor.DARK_GRAY + "A worthy investment, I tell you!"},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false ,
            false, false, false, false, false, null, 0, Shops.COAL,false, null),
    DIRT_TRADE(ChatColor.DARK_GRAY + "Dirt Trade", 5, 5, Material.DIRT, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false,
            false, false, false, false, false, null, 0, Shops.BLOCK_TRADING, true, Trades.DIRT_TRADE),
    COBBLESTONE_TRADE(ChatColor.GRAY + "Cobblestone Trade", 8, 8, Material.COBBLESTONE, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false,
            false, false, false, false, false, null, 0, Shops.BLOCK_TRADING, true, Trades.COBBLESTONE_TRADE),
    OBSIDIAN_TRADE(ChatColor.DARK_PURPLE + "Obsidian Trade", 5, 5, Material.OBSIDIAN, null, new String[] {},
            new Enchantment[] {}, 0,0,0, null, 0,0, false, false,
            false, false, false, false, false, null, 0,
            Shops.BLOCK_TRADING,true, Trades.OBSIDIAN_TRADE);


// displayname, price, material, lore, enchantments[],

    private String displayName;
    private int price;
    private int count;
    private Material material;
    private Color color;
    private String[] lore;
    private Enchantment[] enchantments;
    private int enchLvl;
    private int enchLvl1;
    private int enchLvl2;
    private PotionEffectType effectType;
    private int duration;
    private int amplifier;
    private boolean isEnchantmentBook;
    private boolean hideEnchantments;
    private boolean hideAttributes;
    private boolean isUnbreakable;
    private boolean isLeatherArmor;
    private boolean isPotion;
    private boolean hasCraftFee;
    private Material craftMaterial;
    private int craftFee;
    private Shops shop;
    private boolean isTrade;
    private Trades trade;

    private ShopItems(String displayName, int price, int count, Material material, Color color, String[] lore, Enchantment[] enchantments, int enchLvl,
                      int enchLvl1, int enchLvl2, PotionEffectType effectType, int duration, int amplifier, boolean isEnchantmentBook, boolean hideEnchantments,
                      boolean hideAttributes, boolean isUnbreakable, boolean isLeatherArmor, boolean isPotion,boolean hasCraftFee, Material craftMaterial,
                      int craftFee, Shops shop, boolean isTrade, Trades trade) {
        this.displayName = displayName;
        this.price = price;
        this.count = count;
        this.material = material;
        this.color = color;
        this.lore = lore;
        this.enchantments = enchantments;
        this.enchLvl = enchLvl;
        this.enchLvl1 = enchLvl1;
        this.enchLvl2 = enchLvl2;
        this.effectType = effectType;
        this.duration = duration;
        this.amplifier = amplifier;
        this.isEnchantmentBook = isEnchantmentBook;
        this.hideEnchantments = hideEnchantments;
        this.hideAttributes = hideAttributes;
        this.isUnbreakable = isUnbreakable;
        this.isLeatherArmor = isLeatherArmor;
        this.isPotion = isPotion;
        this.hasCraftFee = hasCraftFee;
        this.craftMaterial = craftMaterial;
        this.craftFee = craftFee;
        this.shop = shop;
        this.isTrade = isTrade;
        this.trade = trade;

    }

// creates item for GUI. if there is an economist, it will create within the GUI a special version of the item that has a reduced price.
    public ItemStack createItem(ShopItems item, Shops shopType, boolean isEconomist) {
        ItemBuilder i = new ItemBuilder(item.material);
        if (!item.isEnchantmentBook && !item.isPotion && !item.isTrade) {
            int iterator = 0;
            for (Enchantment enchantment : item.enchantments) {
                if (iterator == 0)
                    i.addEnchant(enchantment, item.enchLvl);
                else if (iterator == 1)
                    i.addEnchant(enchantment, item.enchLvl1);
                else if (iterator == 2)
                    i.addEnchant(enchantment, item.enchLvl2);

                else {
                    break;
                }
               iterator++;
            }



            i.setDisplayName(item.displayName);
            i.setLore(addPriceToLore(item, shopType, item.lore, isEconomist));
            i.setAmount(item.count);

            if(item.hideEnchantments) i.addItemFlag(ItemFlag.HIDE_ENCHANTS);
            if(item.isLeatherArmor) i.setColor(item.color);
            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if(item.isUnbreakable) {
                i.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
                i.setUnbreakable(true);
            }


            return i.build();
        }
        else if (item.isPotion) {
            PotionMeta potion = (PotionMeta) i.getItemMeta();

            potion.addCustomEffect(item.effectType.createEffect(item.duration*20, item.amplifier), true);

            i.setItemMeta(potion);

            i.setDisplayName(item.displayName);
            i.setLore(addPriceToLore(item, shopType, item.lore, isEconomist));
            i.setAmount(item.count);

            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);

            return i.build();

        }
        else if (isEnchantmentBook) {
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) i.getItemMeta();
            int iterator = 0;
            for (Enchantment enchantment : item.enchantments) {
                if(iterator == 0)
                enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl, true);
                else if(iterator == 1)
                    enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl1, true);
                else if (iterator == 2)
                    enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl2, true);

                else {
                    break;
                }

                iterator++;
            }

            i.setItemMeta(enchantmentStorageMeta);
            i.setDisplayName(item.displayName);
            i.setLore(addPriceToLore(item, shopType, item.lore, isEconomist));
            i.setAmount(item.count);
            if(item.hideEnchantments) i.addItemFlag(ItemFlag.HIDE_ENCHANTS);
            if(item.isLeatherArmor) i.setColor(item.color);
            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if(item.isUnbreakable) {
                i.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
                i.setUnbreakable(true);
            }
            return i.build();


        }
        else {

            i.setDisplayName(item.displayName);
            i.setAmount(item.count);
            i.setLore(item.getTrade(item).addPriceToLore(item, item.trade));

            return i.build();
        }
    }


    // When you successfully purchase an item in the GUI, the ItemStack will be created and given to the player.
    public void purchaseItem(ShopItems item, Shops shop, Player player, boolean isEconomist) {
        // checking if player is able to buy item
        if (item.isTrade) {
            Trades.valueOf(item.name()).conductTrade(Trades.valueOf(item.name()), player);
            return;
        }

        if (!player.getInventory().containsAtLeast(shop.getCurrency(), item.getPrice(item, player, isEconomist))) {
            player.sendMessage(ChatColor.RED + "You do not have enough resources to buy this item!");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }
        if (item.hasCraftFee) {
            if (!player.getInventory().containsAtLeast(new ItemStack(getCraftFee(item).getKey()), getCraftFee(item).getValue())) {
                player.sendMessage(ChatColor.RED + "You don't have all the required items!");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                return;
            }
            else player.getInventory().removeItem(new ItemStack(getCraftFee(item).getKey(), getCraftFee(item).getValue()));
        }

        ItemStack removeAmt = shop.getCurrency().clone();
        removeAmt.setAmount(item.getPrice(item, player, isEconomist));

        player.getInventory().removeItem(removeAmt);
     //   player.sendMessage(ChatColor.GOLD + "[DEBUG] Removed " + removeAmt.getAmount() + " " + removeAmt.getType());

        // purchase successful, building item and adding to inventory.
        ItemBuilder i = new ItemBuilder(item.material);
        if (!item.isEnchantmentBook && !item.isPotion) {
            int iterator = 0;
            for (Enchantment enchantment : item.enchantments) {
                if (iterator == 0)
                    i.addEnchant(enchantment, item.enchLvl);
                else if (iterator == 1)
                    i.addEnchant(enchantment, item.enchLvl1);
                else if (iterator == 2)
                    i.addEnchant(enchantment, item.enchLvl2);

                else {
                    System.out.println(ChatColor.AQUA + "[WSG-INFO] " + ChatColor.RESET + "There are more than 3 enchants on " + item + " - this is not supported.");
                    break;
                }
                iterator++;
            }


            i.setDisplayName(item.displayName);
            i.setLore(Arrays.asList(item.lore));
            i.setAmount(item.count);
            if(item.hideEnchantments) i.addItemFlag(ItemFlag.HIDE_ENCHANTS);
            if(item.isLeatherArmor) i.setColor(item.color);
            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if(item.isUnbreakable) {
                i.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
                i.setUnbreakable(true);
            }
            player.getInventory().addItem(i.build());
            player.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + ChatColor.stripColor(item.displayName));
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);

        }
        else if (item.isPotion) {
            PotionMeta potion = (PotionMeta) i.getItemMeta();
            potion.addCustomEffect(item.effectType.createEffect(item.duration*20, item.amplifier), true);
            i.setItemMeta(potion);

            i.setDisplayName(item.displayName);
            i.setLore(Arrays.asList(item.lore));
            i.setAmount(item.count);


            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);

            player.getInventory().addItem(i.build());
            player.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + ChatColor.stripColor(item.displayName));
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
        }

        else {
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) i.getItemMeta();
            int iterator = 0;
            for (Enchantment enchantment : item.enchantments) {
                if(iterator == 0)
                    enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl, true);
                else if(iterator == 1)
                    enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl1, true);
                else if (iterator == 2)
                    enchantmentStorageMeta.addStoredEnchant(enchantment, item.enchLvl2, true);

                else {
                    break;
                }

                iterator++;
            }

            i.setItemMeta(enchantmentStorageMeta);
            i.setDisplayName(item.displayName);
            i.setLore(Arrays.asList(item.lore));
            i.setAmount(item.count);
            if(item.hideEnchantments) i.addItemFlag(ItemFlag.HIDE_ENCHANTS);
            if(item.isLeatherArmor) i.setColor(item.color);
            if(item.hideAttributes) i.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
            if(item.isUnbreakable) {
                i.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
                i.setUnbreakable(true);
            }
            player.getInventory().addItem(i.build());
            player.sendMessage(ChatColor.GREEN + "You purchased " + ChatColor.GOLD + "Enchantment Book" + ChatColor.GOLD +  " (" + ChatColor.stripColor(item.displayName) + ")");
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);


        }
    }

    public List<String> addPriceToLore(ShopItems item, Shops shopType, String[] lore, boolean isEconomist) {

        List<String> newLore = new ArrayList<>();


        for (String str : lore) {
            newLore.add(str);
        }

        if(isEconomist) {

            int newPrice = getPrice(item) - ((int)Math.round(getPrice(item)*ClassTypes.ECONOMIST.getShopDiscount()));
            newLore.addAll(Arrays.asList(" ", ChatColor.WHITE + "Cost: " + ChatColor.STRIKETHROUGH + getPrice(item)+ ChatColor.RESET + " " + ChatColor.WHITE + newPrice + " " + shopType.getCurrencyDisplayName()));
            if (item.hasCraftFee) newLore.add(ChatColor.WHITE + "Craft Fee: " + getCraftFee(item).getValue() + " " + ChatColor.YELLOW + Game.getFriendlyName(getCraftFee(item).getKey()));
            return newLore;
        }

        newLore.addAll(Arrays.asList(" ", ChatColor.WHITE + "Cost: " + getPrice(item) + " " + shopType.getCurrencyDisplayName()));

        if (item.hasCraftFee) newLore.add(ChatColor.WHITE + "Craft Fee: " + getCraftFee(item).getValue() + " " + ChatColor.YELLOW + Game.getFriendlyName(getCraftFee(item).getKey()));
        return newLore;
    }

    public String[] getLore(ShopItems item) {
        return item.lore;
    }

    public int getPrice(ShopItems item, ClassTypes c) {
        if (c.getShopDiscount() != 0)
            return (int)Math.round(item.price - item.price * c.getShopDiscount());

        else
            return item.price;

    }
    public int getPrice(ShopItems item, Player player, boolean isEconomist) {
        if (isEconomist) return item.price - (int)Math.round((item.price *  Manager.getArena(player).getClass(player).getShopDiscount()));

        else return item.price;
    }

    public int getPrice(ShopItems item) {
        return item.price;
    }
    public Trades getTrade(ShopItems item) {
        return trade;
    }

    public Pair<Material, Integer> getCraftFee(ShopItems item) {
        if (item.hasCraftFee) {
            Pair itemCraftFee = new Pair<Material, Integer>(item.craftMaterial, item.craftFee);

            return itemCraftFee;
        }
        return null;

    }
}
