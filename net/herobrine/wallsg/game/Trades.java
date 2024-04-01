package net.herobrine.wallsg.game;

import net.herobrine.wallsg.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum Trades {
    // Make sure to keep the price here and in ShopItems the same. Can't get it directly due to a cross-reference error.
    DIRT_TRADE("Dirt Trade", Material.DIRT, 5, Material.COAL, 1, ChatColor.DARK_GRAY, ChatColor.DARK_GRAY),
    COBBLESTONE_TRADE("Cobblestone Trade", Material.COBBLESTONE, 8, Material.WOOD,16, ChatColor.GRAY, ChatColor.YELLOW),
    OBSIDIAN_TRADE("Obsidian Trade", Material.OBSIDIAN, 5, Material.EMERALD, 2, ChatColor.DARK_PURPLE, ChatColor.GREEN),
    REDSTONE_TRADE("Redstone Trade", Material.REDSTONE, 5, Material.EMERALD, 2, ChatColor.RED, ChatColor.GREEN);



    private String tradeName;
    private Material input;
    private int amtIn;
    private Material output;
    private int amtOut;
    private ChatColor color1;
    private ChatColor color2;



    private Trades(String tradeName, Material input, int amtIn, Material output, int amtOut, ChatColor color1, ChatColor color2) {
        this.tradeName = tradeName;
        this.input = input;
        this.amtIn = amtIn;
        this.output = output;
        this.amtOut = amtOut;
        this.color1 = color1;
        this.color2 = color2;

    }

    public List<String> addPriceToLore(ShopItems item, Trades trade) {

        List<String> newLore = new ArrayList<>();

        for (String lore : item.getLore(item)) {
            newLore.add(lore);
        }

        if (!newLore.isEmpty()) newLore.add(" ");

        newLore.add(ChatColor.WHITE + "You Give: " + trade.amtIn + trade.color1 + " " + Game.getFriendlyName(trade.input));
        newLore.add(ChatColor.WHITE + "You Get: " + trade.amtOut + trade.color2 + " " + Game.getFriendlyName(trade.output));



        return newLore;
    }




    public void conductTrade(Trades trade, Player player) {
        if(player.getInventory().containsAtLeast(new ItemStack(trade.getInput()), trade.getAmtIn())) {

            player.getInventory().removeItem(new ItemStack(trade.getInput(), trade.getAmtIn()));
            player.getInventory().addItem(new ItemStack(trade.getOutput(), trade.getAmtOut()));

            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
            player.sendMessage(ChatColor.GREEN + "You have completed a " + ChatColor.GOLD + "Block Trade (" + trade.getTradeName() + ")");

        }

        else {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            player.sendMessage(ChatColor.RED + "You don't have enough to complete this trade!");
        }

    }

    public String getTradeName() {
        return tradeName;
    }
    public Material getInput() {
        return input;
    }
    public int getAmtIn() {
        return amtIn;
    }

    public Material getOutput() {
        return output;
    }

    public int getAmtOut() {
        return amtOut;
    }
}
