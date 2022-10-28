package net.herobrine.wallsg.classes;

import net.herobrine.gamecore.Class;
import net.herobrine.gamecore.ClassTypes;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Economist extends Class {

    public Economist (UUID uuid) {

        super(uuid, ClassTypes.ECONOMIST);
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

        player.getInventory().addItem(new ItemStack(Material.COAL, 10));
        player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 6));
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 4));
        player.getInventory().addItem(new ItemStack(Material.INK_SACK, 10, (byte) 4));
        player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));

    }
}
