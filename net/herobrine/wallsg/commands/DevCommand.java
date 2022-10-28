package net.herobrine.wallsg.commands;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.core.Ranks;
import net.herobrine.gamecore.*;
import net.herobrine.wallsg.WallsMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class DevCommand implements CommandExecutor {


    HashMap<UUID, Boolean> awaitingConfirmation = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;
            Ranks rank = HerobrinePVPCore.getFileManager().getRank(player);
            if (rank.getPermLevel() >= 9) {

                if(Manager.isPlaying(player)) {
                    Arena arena = Manager.getArena(player);

                    if(!arena.getGame(arena.getID()).equals(Games.WALLS_SG) || !arena.getState().equals(GameState.LIVE)) player.sendMessage(ChatColor.GREEN + "You must be in a live game of Walls SG to use this command!");
                    else {
                        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                            player.sendMessage(ChatColor.GREEN + "Walls SG Dev Tools");
                            player.sendMessage(ChatColor.GREEN + "/wsgdev freeze - Freeze/Unfreeze the ingame timer.");
                            player.sendMessage(ChatColor.GREEN + "/wsgdev settime <time> - Set the timer to a certain # of minutes or seconds. Can't be more than 30 minutes.");
                            player.sendMessage(ChatColor.GREEN + "/wsgdev changekit <class> - Change your class midgame. Only works in modifier games. Removes your potion effects and armor and gives you a starter setup.");
                        }
                        else if (args.length == 1 && args[0].equalsIgnoreCase("freeze")) {
                            if (arena.getwallsSGGame().isFrozen()) arena.getwallsSGGame().setFrozen(false);
                            else arena.getwallsSGGame().setFrozen(true);
                        }
                        else if (args.length == 1 && args[0].equalsIgnoreCase("settime") || args[0].equalsIgnoreCase("time")) player.sendMessage(ChatColor.RED + "Invalid Usage! Usage: /wsgdev settime <time>");
                        else if (args.length == 2 && args[0].equalsIgnoreCase("settime") || args[0].equalsIgnoreCase("time")) {
                            if (args[1] != null) {
                                try {
                                    if (args[1].endsWith("s") || args[1].endsWith("seconds")) {
                                        if (args[1].endsWith("s")) args[1] = args[1].replaceAll("s", "");

                                        else args[1] = args[1].replaceAll("seconds", "");


                                        int time = Integer.parseInt(args[1]);

                                        if (time / 60 > 30) player.sendMessage(ChatColor.RED + "You can't set the timer higher than 30 minutes!");

                                        else arena.getwallsSGGame().setTime(time, player);
                                    }

                                    else if (args[1].endsWith("m") || args[1].endsWith("minutes")) {
                                        if (args[1].endsWith("m")) args[1] = args[1].replaceAll("m", "");

                                        else args[1] = args[1].replaceAll("minutes", "");

                                        int time = Integer.parseInt(args[1]);
                                        if (time > 30) player.sendMessage(ChatColor.RED + "You can't set the timer higher than 30 minutes!");

                                        else arena.getwallsSGGame().setTime(time * 60, player);

                                    }

                                    else {
                                        player.sendMessage(ChatColor.RED + "Invalid time provided. Note that you can only put the time in units of " +
                                                "minutes(m/minutes) and seconds (s/seconds)");
                                    }

                                } catch (NumberFormatException e) {
                                    player.sendMessage(ChatColor.RED + "Invalid time provided. Did you put a number?");
                                }

                            }

                        }

                        else if (args.length == 2 && args[0].equalsIgnoreCase("changekit")) {

                            if(!arena.getType().equals(GameType.MODIFIER)) {
                                player.sendMessage(ChatColor.RED + "You can only do this in a modifier game!");
                                return false;
                            }

                            try {
                                if (ClassTypes.valueOf(args[1].toUpperCase()) != null) {

                                    ClassTypes newClass = ClassTypes.valueOf(args[1].toUpperCase());

                                    if (arena.getClass(player) == newClass) {
                                        player.sendMessage(ChatColor.RED + "You are already using this class!");
                                    }
                                    else if (!awaitingConfirmation.containsKey(player.getUniqueId())) {
                                        player.sendMessage(ChatColor.RED + "WARNING: This will remove ALL your equipped armor and active potion effects. You will also be given all starter items again. Run the command again within 6s if you are sure you want to do this.");
                                        awaitingConfirmation.put(player.getUniqueId(), true);
                                        confirmTimer(player.getUniqueId());
                                        return false;
                                    }

                                    else if(newClass.getGame().equals(Games.WALLS_SG)) {
                                        awaitingConfirmation.remove(player.getUniqueId());
                                        arena.setClass(player.getUniqueId(), newClass);
                                        for (PotionEffect effect : player.getActivePotionEffects()) {

                                            player.removePotionEffect(effect.getType());
                                        }
                                        player.getEquipment().setHelmet(null);
                                        player.getEquipment().setChestplate(null);
                                        player.getEquipment().setLeggings(null);
                                        player.getEquipment().setBoots(null);
                                        arena.getClasses().get(player.getUniqueId())
                                                .onStart(Bukkit.getPlayer(player.getUniqueId()));
                                        player.sendMessage(ChatColor.AQUA + "You switched to the "
                                                + arena.getClass(player).getDisplay() + ChatColor.AQUA + " class!");
                                    }
                                    else {
                                        awaitingConfirmation.remove(player.getUniqueId());
                                        player.sendMessage(ChatColor.RED + "You can only switch to classes from Walls SG!");
                                    }

                                } else {
                                    player.sendMessage(ChatColor.RED + "Invalid class type!");
                                }

                            }

                            catch (IllegalArgumentException e) {
                                player.sendMessage(ChatColor.RED + "Invalid class type!");
                            }
                        }
                    }
                }
                else player.sendMessage(ChatColor.GREEN + "You must be in a live game of Walls SG to use this command!");


            }
            else player.sendMessage(ChatColor.RED + "You do not have permission to use this!");

        }
        else sender.sendMessage(ChatColor.RED + "Only a player can use this command!");


        return false;
    }
    public void confirmTimer(UUID uuid) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if (awaitingConfirmation.containsKey(uuid)) {
                    awaitingConfirmation.remove(uuid);
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Alright, we won't change your class then.");
                }



            }

        }.runTaskLater(WallsMain.getInstance(), 120L);

    }
}
