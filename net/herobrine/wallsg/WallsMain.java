package net.herobrine.wallsg;

import net.herobrine.wallsg.game.ChestManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.herobrine.core.HerobrinePVPCore;
import net.herobrine.gamecore.GameCoreMain;
import net.herobrine.wallsg.commands.DevCommand;

public class WallsMain extends JavaPlugin {
	private static WallsMain instance;
	private Game game;

	@Override
	public void onEnable() {
		WallsMain.instance = this;
		if (getCustomAPI() == null) {
			System.out.println(
					"[WALLS SG] The Herobrine PVP Core was not found. HBPVP-Core is required for the plugin to function, disabling.");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			System.out.println("[WALLS SG] Successfully hooked into Herobrine PVP Core Plugin!");
		}

		new Config(this);

		new ChestManager();

		if (getAPI() == null) {
			System.out.println(
					"[WALLS SG] WorldEdit was not found. WorldEdit is required for the plugin to function, disabling.");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			System.out.println("[WALLS SG] Successfully hooked into worldedit!");
		}

		Bukkit.getPluginManager().registerEvents(new GameListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChestManager(), this);
		getCommand("wsgdev").setExecutor(new DevCommand());
	}




	public void repair() {return;}



	public static WallsMain getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		System.out.println("[WALLS SG] Disabling...");

		System.out.println("[WALLS SG] Successfully disabled!");
	}

	public WorldEditPlugin getAPI() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin instanceof WorldEditPlugin) {
			return (WorldEditPlugin) plugin;
		} else {
			return null;
		}
	}

	public GameCoreMain getGameCoreAPI() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GameCore");
		if (plugin instanceof GameCoreMain) {
			return (GameCoreMain) plugin;
		} else {
			return null;
		}
	}

	public HerobrinePVPCore getCustomAPI() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("HBPVP-Core");
		if (plugin instanceof HerobrinePVPCore) {
			return (HerobrinePVPCore) plugin;
		} else {
			return null;
		}
	}
}
