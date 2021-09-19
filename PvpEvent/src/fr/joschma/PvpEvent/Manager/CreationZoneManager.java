package fr.joschma.PvpEvent.Manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import fr.joschma.PvpEvent.Arena.Arena;

public class CreationZoneManager {
	
	static HashMap<Player, Arena> creationZone = new HashMap<Player, Arena>();

	public static HashMap<Player, Arena> getCreationZone() {
		return creationZone;
	}

	public static void setCreationZone(HashMap<Player, Arena> creationZone) {
		CreationZoneManager.creationZone = creationZone;
	}
	
	public static void addCreationZone(Player p, Arena a) {
		creationZone.put(p, a);
	}
	
	public static void rmvCreationZone(Player p) {
		creationZone.remove(p);
	}
}
