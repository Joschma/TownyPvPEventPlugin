package fr.joschma.PvpEvent.Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.joschma.PvpEvent.PvpEvent;
import fr.joschma.PvpEvent.Arena.Arena;
import fr.joschma.PvpEvent.Cuboid.Cuboid;
import fr.joschma.PvpEvent.Manager.CreationZoneManager;
import fr.joschma.PvpEvent.Utils.UtilsLoc;
import net.md_5.bungee.api.ChatColor;

public class onPlayerInteract implements Listener {

	Location loc1;
	Location loc2;
	PvpEvent pl;

	public onPlayerInteract(PvpEvent pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Arena a = pl.getA();

		if (e.getItem() != null) {
			if (e.getItem().getType() == Material.STICK) {
				if (CreationZoneManager.getCreationZone().containsKey(p)) {
					a = CreationZoneManager.getCreationZone().get(p);

					if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
						loc1 = e.getClickedBlock().getLocation();
						p.sendMessage(ChatColor.GRAY + "You have succesfully put the first location");
					} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						loc2 = e.getClickedBlock().getLocation();
						p.sendMessage(ChatColor.GRAY + "You have succesfully put the second location");
					}

					if (loc1 != null && loc2 != null) {
						Cuboid cu = new Cuboid(loc1, loc2);
						a.setCu(cu);

						e.getPlayer().getInventory().setItem(0, new ItemStack(Material.AIR));

						p.sendMessage(ChatColor.GRAY + "You have created the zone");

						CreationZoneManager.rmvCreationZone(p);
						FileConfiguration fc = pl.getConfig();
						fc.set("Zone.Loc1", UtilsLoc.locToString(loc1));
						fc.set("Zone.Loc2", UtilsLoc.locToString(loc2));

						pl.saveConfig();

						loc1 = null;
						loc2 = null;
					}
					e.setCancelled(true);
				}
			}
		}
	}
}
