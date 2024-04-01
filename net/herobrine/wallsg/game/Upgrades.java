package net.herobrine.wallsg.game;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.gamecore.Arena;
import net.herobrine.gamecore.ClassTypes;
import net.herobrine.wallsg.Game;
import net.herobrine.wallsg.classes.Engineer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Upgrades {

    CANNON_DAMAGE_UPGRADE(ChatColor.GRAY + "Upgrades" + ChatColor.GOLD + " Cannon Damage ",.35, .70, 1.0, 5, 10, 20),
    CANNON_SPEED_UPGRADE(ChatColor.GRAY + "Upgrades" + ChatColor.GOLD + " Cannon Hitspeed ", .10, .20, .30, 5, 10, 20),
    CANNON_RANGE_UPGRADE(ChatColor.GRAY + "Upgrades" + ChatColor.GOLD + " Cannon Range ",.15, .25, .35, 5, 10, 20);

    String description;
    double tier1, tier2, tier3;
    int cost1, cost2, cost3;

    private Upgrades(String description, double tier1, double tier2, double tier3, int cost1, int cost2, int cost3) {
        this.description = description;
        this.tier1 = tier1;
        this.tier2 = tier2;
        this.tier3 = tier3;
        this.cost1 = cost1;
        this.cost2 = cost2;
        this.cost3 = cost3;
    }

    public String getDescription() {return description;}
    public double getTier1() {return tier1;}
    public double getTier2() {return tier2;}
    public double getTier3() {return tier3;}

    public int getCost1() {return cost1;}
    public int getCost2() {return cost2;}
    public int getCost3() {return cost3;}

    public int getCostForLevel(int upgradeLevel) {
        if (upgradeLevel == 1) return cost1;
        if (upgradeLevel == 2) return cost2;
        if (upgradeLevel == 3) return cost3;

        return 0;
    }

    public double getLastTierBoost(int upgradeLevel) {
        if (upgradeLevel == 1) return 0;
        if (upgradeLevel == 2) return tier1;
        if (upgradeLevel == 3) return tier2;

      else  return 0;
    }

    public double getTierBoost(int upgradeLevel) {
        if (upgradeLevel == 1) return tier1;
        if (upgradeLevel == 2) return tier2;
        if (upgradeLevel == 3) return tier3;

       else return 0;
    }


    public List<String> createUpgradeLore(ShopItems item, Upgrades upgrade, Player player, Arena arena) {
        Engineer engineer = (Engineer) arena.getClasses().get(player.getUniqueId());
        int upgradeTier = Collections.frequency(engineer.getUpgrades(), upgrade) + 1;
        List<String> newLore = new ArrayList<>();

        for (String lore : item.getLore(item)) {
            newLore.add(lore);
        }

        if (!newLore.isEmpty()) newLore.add(" ");

        if (upgradeTier == 4) {
            newLore.add(upgrade.getDescription() + HerobrinePVPCore.translateString("&c&l") + upgrade.getTier3()*100 + "%");
            newLore.add("");
            newLore.add(HerobrinePVPCore.translateString("&b&lMAXED"));
        }
        else if (upgradeTier == 1) {
            newLore.add(HerobrinePVPCore.translateString("&7Upgrade Tier: &b" + upgradeTier));
            newLore.add(upgrade.getDescription() + HerobrinePVPCore.translateString("&7by &c&l" + upgrade.getTier1()*100 + "%"));
            newLore.add("");
            newLore.add(HerobrinePVPCore.translateString("&fCost: &f" + upgrade.getCost1() + " &cRedstone"));
        }

        else {
            newLore.add(HerobrinePVPCore.translateString("&7Upgrade Tier: &b" + upgradeTier));
            newLore.add(upgrade.getDescription() + HerobrinePVPCore.translateString("&c&l" + getLastTierBoost(upgradeTier)*100+"%⟹" + getTierBoost(upgradeTier)*100 + "%"));
            newLore.add("");
            newLore.add(HerobrinePVPCore.translateString("&fCost: &f" + getCostForLevel(upgradeTier) + " &cRedstone"));
        }
        return newLore;
    }

    public void doUpgrade(Upgrades upgrade, Player player, Arena arena, int slot) {
    if (arena.getClass(player.getUniqueId()) != ClassTypes.ENGINEER) {
        player.sendMessage(ChatColor.RED + "You can only get upgrades as an Engineer!");
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f,1f);
        player.closeInventory();
        return;
    }

    Engineer engineer = (Engineer) arena.getClasses().get(player.getUniqueId());
    int upgradeTier = Collections.frequency(engineer.getUpgrades(), upgrade) + 1;

    if (upgradeTier > 3) {
        player.sendMessage(ChatColor.RED + "This upgrade is max level!");
        return;
    }

    if (player.getInventory().containsAtLeast(Shops.ENGINEER_UPGRADES.getCurrency(), upgrade.getCostForLevel(upgradeTier))) {
        player.getInventory().removeItem(new ItemStack(Shops.ENGINEER_UPGRADES.getCurrency().getType(), upgrade.getCostForLevel(upgradeTier)));

        engineer.getUpgrades().add(upgrade);
        player.getOpenInventory().setItem(slot, ShopItems.valueOf(upgrade.name()).createItem(ShopItems.valueOf(upgrade.name()), Shops.ENGINEER_UPGRADES, false, player));
        if (upgrade.equals(Upgrades.CANNON_SPEED_UPGRADE)) engineer.upgradeCannonHitSpeed(Math.round((engineer.getBaseHitSpeed() - engineer.getBaseHitSpeed() * upgrade.getTierBoost(upgradeTier)) * 10)/10.0, player);
        if (upgrade.equals(Upgrades.CANNON_RANGE_UPGRADE)) engineer.upgradeCannonRange(player, Math.round(engineer.getBaseRange() + engineer.getBaseRange() * upgrade.getTierBoost(upgradeTier)));
        if (upgrade.equals(Upgrades.CANNON_DAMAGE_UPGRADE)) engineer.upgradeCannonDamage(player,(int)Math.round(engineer.getBaseDamage() + engineer.getBaseDamage() * upgrade.getTierBoost(upgradeTier)));
    }
    else {
        player.sendMessage(ChatColor.RED + "You don't have enough resources to upgrade!");
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
    }

    }


}
