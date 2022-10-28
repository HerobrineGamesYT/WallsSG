package net.herobrine.wallsg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Config {
	private static WallsMain main;

	public Config(WallsMain main) {
		Config.main = main;
		main.getConfig().options().copyDefaults();
		main.saveDefaultConfig();
	}

	public static int getRequiredPlayers() {
		return main.getConfig().getInt("required-players");
	}

	public static int getMaxPlayers() {
		return main.getConfig().getInt("max-players");
	}

	public static int getCountdownSeconds() {
		return main.getConfig().getInt("countdown-seconds");
	}

	public static Location getLobbySpawn() {
		return new Location(Bukkit.getWorld(main.getConfig().getString("lobby-spawn.world")),
				main.getConfig().getDouble("lobby-spawn.x"), main.getConfig().getDouble("lobby-spawn.y"),
				main.getConfig().getDouble("lobby-spawn.z"), main.getConfig().getInt("lobby-spawn.yaw"),
				main.getConfig().getInt("lobby-spawn.pitch"));

	}

	public static Location getLobbyNPCSpawn() {
		return new Location(Bukkit.getWorld(main.getConfig().getString("lobby-npc-spawn.world")),
				main.getConfig().getDouble("lobby-npc-spawn.x"), main.getConfig().getDouble("lobby-npc-spawn.y"),
				main.getConfig().getDouble("lobby-npc-spawn.z"), main.getConfig().getInt("lobby-npc-spawn.yaw"),
				main.getConfig().getInt("lobby-npc-spawn.pitch"));
	}

	public static int getMaxChestIds(int id) {
		return main.getConfig().getInt("arenas." + id + ".center-chest-spawning.maxId");
	}

	public static Location getChestLocation(int id, int chestID) {

		return new Location(getArenaWorld(id),
				main.getConfig().getDouble("arenas." + id + ".center-chest-spawning." + chestID + ".x"),
				main.getConfig().getDouble("arenas." + id + ".center-chest-spawning." + chestID + ".y"),
				main.getConfig().getDouble("arenas." + id + ".center-chest-spawning." + chestID + ".z"));
	}

	public static World getArenaWorld(int id) {
		return (World) Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".world"));
	}

	public static int getArenaAmount() {
		return main.getConfig().getConfigurationSection("arenas.").getKeys(false).size();
	}

	public static Location getRedTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".red-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".red-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".red-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".red-spawn.yaw"));

	}

	public static Location getBlueTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".blue-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".blue-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".blue-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".blue-spawn.yaw"));
	}

	public static Location getYellowTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".yellow-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".yellow-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".yellow-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".yellow-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".yellow-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".yellow-spawn.yaw"));
	}

	public static double getBorderCenterX(int id) {
		return main.getConfig().getDouble("arenas." + id + ".border.x");
	}

	public static double getBorderCenterZ(int id) {
		return main.getConfig().getDouble("arenas." + id + ".border.z");
	}

	public static Location getGreenTeamSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".green-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".green-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".green-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".green-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".green-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".green-spawn.yaw"));
	}

	public static Location getFirstPosition(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".world")),
				main.getConfig().getDouble("arenas." + id + ".first-position.x"),
				main.getConfig().getDouble("arenas." + id + ".first-position.y"),
				main.getConfig().getDouble("arenas." + id + ".first-position.z"));
	}

	public static Location getSecondPosition(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".world")),
				main.getConfig().getDouble("arenas." + id + ".second-position.x"),
				main.getConfig().getDouble("arenas." + id + ".second-position.y"),
				main.getConfig().getDouble("arenas." + id + ".second-position.z"));
	}

	public static Location getRedNPCSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".red-npc-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".red-npc-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".red-npc-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".red-npc-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".red-npc-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".red-npc-spawn.yaw"));
	}

	public static Location getBlueNPCSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".blue-npc-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".blue-npc-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".blue-npc-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".blue-npc-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".blue-npc-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".blue-npc-spawn.yaw"));
	}

	public static Location getYellowNPCSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".yellow-npc-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".yellow-npc-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".yellow-npc-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".yellow-npc-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".yellow-npc-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".yellow-npc-spawn.yaw"));
	}

	public static Location getGreenNPCSpawn(int id) {
		return new Location(Bukkit.getWorld(main.getConfig().getString("arenas." + id + ".green-npc-spawn.world")),
				main.getConfig().getDouble("arenas." + id + ".green-npc-spawn.x"),
				main.getConfig().getDouble("arenas." + id + ".green-npc-spawn.y"),
				main.getConfig().getDouble("arenas." + id + ".green-npc-spawn.z"),
				main.getConfig().getInt("arenas." + id + ".green-npc-spawn.pitch"),
				main.getConfig().getInt("arenas." + id + ".green-npc-spawn.yaw"));
	}
}