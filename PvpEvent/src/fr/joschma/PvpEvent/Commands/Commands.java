package fr.joschma.PvpEvent.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.joschma.PvpEvent.PvpEvent;
import fr.joschma.PvpEvent.Manager.CreationZoneManager;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	PvpEvent pl;

	public Commands(PvpEvent pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("pvpevent")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("forceStart")) {
						pl.getA().startEvent();
					} else if (args[0].equalsIgnoreCase("reload")) {
						pl.reloadConfig();
						if(pl.getA().hasStarted())
							pl.getA().endEvent();
						
						pl.getA().getDayTimer().cancel();
						pl.setNewDayTimer();
						pl.getA().getDayTimer().runTaskTimer(pl, 0, 20);
						p.sendMessage(ChatColor.GRAY + "You have reloaded the plugin !");
					} else if (args[0].equalsIgnoreCase("zone")) {
						CreationZoneManager.addCreationZone(p, pl.getA());
						p.getInventory().setItem(0, new ItemStack(Material.STICK));
					} else if (args[0].equalsIgnoreCase("finished")) {
						pl.getA().setFinished(!pl.getA().isFinished());
						pl.getConfig().set("Finished", pl.getA().isFinished());
						p.sendMessage(ChatColor.GRAY + "You have set finished at " + ChatColor.GOLD + pl.getA().isFinished());
					} else if (args[0].equalsIgnoreCase("forceStop")) {
						pl.getA().endEvent();
					}
					
					pl.saveConfig();
				}
			}
		}
		return false;
	}
}
