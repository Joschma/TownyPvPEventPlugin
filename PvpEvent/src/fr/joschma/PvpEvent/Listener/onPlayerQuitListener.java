package fr.joschma.PvpEvent.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.joschma.PvpEvent.PvpEvent;

public class onPlayerQuitListener implements Listener {
	
	PvpEvent pl;

	public onPlayerQuitListener(PvpEvent pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(pl.getA().getPlayers().contains(p)) {
			pl.getA().removePlayer(p);
		}
	}
}
