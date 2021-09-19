package fr.joschma.PvpEvent.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class UtilsLoc {

	public static Location stringToLoc(String loc) {
		if (loc != null) {
			if (loc.contains("/")) {
				String[] locs = loc.split("/");
				Location signLoc = new Location(Bukkit.getServer().getWorld(locs[0]), Double.parseDouble(locs[1]),
						Double.parseDouble(locs[2]), Double.parseDouble(locs[3]), Float.parseFloat(locs[4]),
						Float.parseFloat(locs[5]));
				return signLoc;
			}
		}
		return null;
	}

	public static String locToString(Location loc) {
		String str = loc.getWorld().getName() + "/" + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ()
				+ "/" + loc.getYaw() + "/" + loc.getPitch();
		return str;
	}
}
