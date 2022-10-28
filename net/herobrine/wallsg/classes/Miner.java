package net.herobrine.wallsg.classes;

import net.herobrine.gamecore.Class;
import net.herobrine.gamecore.ClassTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Miner extends Class {

    public Miner (UUID uuid) {

        super(uuid, ClassTypes.MINER);
    }

    @Override
    public void onStart(Player player) {
        ItemStack starterPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta pickaxeMeta = starterPickaxe.getItemMeta();
        pickaxeMeta.setDisplayName(ChatColor.AQUA + "Miner Pickaxe");
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, false);
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



        player.updateInventory();

    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().getUniqueId().equals(getUUID())) return;

        if (e.getBlock().getType().toString().contains("ORE")) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            ArrayList<ItemStack> stack = new ArrayList<>();
            for (ItemStack stacks : e.getBlock().getDrops()) {
                stack.add(stacks);
            }
            if (random.nextDouble() < .15) {
                e.getPlayer().getInventory().addItem(new ItemStack(stack.get(0)));
                if(e.getBlock().getType().equals(Material.IRON_ORE) || e.getBlock().getType().equals(Material.GOLD_ORE)) e.setExpToDrop(3);
                e.setExpToDrop(e.getExpToDrop() * 2);
                e.getPlayer().sendMessage(ChatColor.GREEN + "Your ore drop was" + ChatColor.AQUA + " doubled" + ChatColor.GREEN + "!");
            }

        }

    }

}
