package fr.joschma.PvpEvent.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.joschma.PvpEvent.Arena.Arena;

public class onPlayerMoveListener implements Listener {

	Arena a;

	public onPlayerMoveListener(Arena a) {
		super();
		this.a = a;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if (a.hasStarted()) {
			if (a.getCu().isIn(p)) {
				if (!a.getPlayers().contains(p)) {
					a.addPlayer(p);
				}
			} else {
				if (a.getPlayers().contains(p)) {
					a.removePlayer(p);
				}
			}
		}
	}
}