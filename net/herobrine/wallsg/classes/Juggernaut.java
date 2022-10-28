package net.herobrine.wallsg.classes;

import net.herobrine.gamecore.Class;
import net.herobrine.gamecore.ClassTypes;
import net.herobrine.gamecore.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class Juggernaut extends Class {

    public Juggernaut (UUID uuid) {

        super(uuid, ClassTypes.JUGGERNAUT);
    }

    @Override
    public void onStart(Player player) {
        ItemStack starterPickaxe = new ItemStack(Material.IRON_PICKAXE, 1);
        ItemMeta pickaxeMeta = starterPickaxe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, false);
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        starterPickaxe.setItemMeta(pickaxeMeta);


        ItemBuilder juggernautAxe = new ItemBuilder(Material.DIAMOND_AXE);

        juggernautAxe.setDisplayName(ChatColor.RED + "Juggernaut Axe");

        juggernautAxe.addEnchant(Enchantment.DAMAGE_ALL, 2);

        juggernautAxe.setLore(Arrays.asList(ChatColor.GREEN + "Gains another level of sharpness for every kill.", " ", ChatColor.RED + "Will only work if you are a Juggernaut."));




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
        player.getInventory().addItem(juggernautAxe.build());
        player.getInventory().addItem(starterPickaxe);



    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        if (e.getEntity().getKiller().equals(Bukkit.getPlayer(uuid)) && e.getEntity().getUniqueId() != uuid) {
            Player player = e.getEntity().getKiller();

           for (ItemStack item : player.getInventory().getContents()) {
               if (item.getType().equals(Material.DIAMOND_AXE)) {
                   item.addEnchantment(Enchantment.DAMAGE_ALL, item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1);
                   break;
               }
            }

        }


    }



}
