package fr.joschma.PvpEvent.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.palmergames.bukkit.towny.object.Town;

import fr.joschma.PvpEvent.Arena.Arena;
import net.md_5.bungee.api.ChatColor;

public class ScoreBoardUtils {

	static Team team;
	static Objective obj;
	static Scoreboard board;

	public static void updateScoreBoard(Arena a, int timer) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		obj = board.registerNewObjective("dummy", "scoreboard", ChatColor.BOLD + "" + ChatColor.RED + "Towny PvP Event");
		team = board.registerNewTeam("Battler");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		obj.setDisplayName(ChatColor.YELLOW + "Towny PVP EVENT " + convertSeconds(timer));

		for (int i = 0; i <= a.getLenghtScoreBoard(); i++) {
			if (i < a.getPl().sortByValue(a.getTownKillTree()).keySet().size()) {
				for (Town town : a.getPl().sortByValue(a.getTownKillTree()).keySet()) {
					if (town != null) {
						Score playerAliveScore = obj
								.getScore(ChatColor.YELLOW + town.getName() + ChatColor.WHITE + " : " + a.getTownKillTree().get(town) + " kills");
						playerAliveScore.setScore(a.getTownKillTree().get(town));
						
					}
				}
			}
		}
		
		for(Player p : a.getPlayers()) {
			team.addEntry(p.getName());
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(board);
		}
	}

	public static String convertSeconds(int seconds) {
		int h = seconds / 3600;
		int m = (seconds % 3600) / 60;
		int s = seconds % 60;
		String hs;
		hs = h + "h";
		String ms;
		ms = m + "m";
		String ss;
		ss = s + "s";
		if(h == 0) {
			hs = "";
			if(m == 0) {
				ms = "";
			}
		}
		
		return hs + ms + ss + "";
	}

	public static void rmvScoreBoard(Player p) {
		if (team != null) {
			if(team.hasEntry(p.getName()))
				team.removeEntry(p.getName());
		}

		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
