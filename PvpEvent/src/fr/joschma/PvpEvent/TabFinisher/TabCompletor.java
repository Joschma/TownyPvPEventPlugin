package fr.joschma.PvpEvent.TabFinisher;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompletor implements TabCompleter {
	List<String> arguments = new ArrayList<String>();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			arguments.clear();
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (arguments.isEmpty()) {
					if (p.hasPermission("PvpEvent.admin") || p.isOp()) {
						if (arguments.isEmpty()) {
							arguments.add("zone");
							arguments.add("reload");
							arguments.add("forceStart");
							arguments.add("finished");
							arguments.add("forceStop");
						}
					}
				}

				List<String> result = new ArrayList<String>();
				if (args.length == 1) {
					for (String a : arguments) {
						if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
							result.add(a);
						}
					}
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
