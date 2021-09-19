package fr.joschma.PvpEvent.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.joschma.PvpEvent.Arena.Arena;

public class onPlayerKillListener implements Listener {

	Arena a;

	public onPlayerKillListener(Arena a) {
		super();
		this.a = a;
	}

	@EventHandler
	public void onEntityDamagedByEntity(PlayerDeathEvent e) {
		Player dead = e.getEntity();
		Player killer = dead.getKiller();

		if (a.hasStarted()) {
			if (a.getPlayers().contains(killer) && a.getPlayers().contains(dead)) {
				Resident killerResident = TownyUniverse.getInstance().getResident(killer.getName());
				if (killerResident.hasTown()) {
					try {
						Town town = killerResident.getTown();
						if (a.getTownKillTree().containsKey(town)) {
							a.getTownKillTree().put(town, a.getTownKillTree().get(town) + 1);
						}
					} catch (NotRegisteredException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
}
