package fr.joschma.PvpEvent.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.joschma.PvpEvent.PvpEvent;
import fr.joschma.PvpEvent.Cuboid.Cuboid;
import fr.joschma.PvpEvent.Timer.DayTimer;
import fr.joschma.PvpEvent.Timer.FightTimer;
import fr.joschma.PvpEvent.Utils.ScoreBoardUtils;

public class Arena {

	Map<Town, Integer> townKillTree = new HashMap<Town, Integer>();
	List<Player> players = new ArrayList<Player>();
	Cuboid cu;
	List<String> beggingTime = new ArrayList<String>();
	List<String> rewards = new ArrayList<String>();
	boolean hasStarted;
	DayTimer dayTimer;
	FightTimer fightTimer;
	int lenghtScoreBoard;
	int eventTime;
	boolean finished;
	int numberOfTeamToGiveReward;
	int minPlayer;
	PvpEvent pl;

	public Arena(Cuboid cu, List<String> beggingTime, int lenghtScoreBoard, int eventTime, boolean finished,
			int numberOfTeamToGiveReward, int minPlayer, PvpEvent pl) {
		super();
		this.cu = cu;
		this.beggingTime = beggingTime;
		this.lenghtScoreBoard = lenghtScoreBoard;
		this.eventTime = eventTime;
		this.finished = finished;
		this.numberOfTeamToGiveReward = numberOfTeamToGiveReward;
		this.minPlayer = minPlayer;
		this.pl = pl;
	}

	public PvpEvent getPl() {
		return pl;
	}

	public void setPl(PvpEvent pl) {
		this.pl = pl;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int getLenghtScoreBoard() {
		return lenghtScoreBoard;
	}

	public void setLenghtScoreBoard(int lenghtScoreBoard) {
		this.lenghtScoreBoard = lenghtScoreBoard;
	}

	public DayTimer getDayTimer() {
		return dayTimer;
	}

	public void setDayTimer(DayTimer dayTimer) {
		this.dayTimer = dayTimer;
	}

	public Map<Town, Integer> getTownKillTree() {
		return townKillTree;
	}

	public void setTownKillTree(Map<Town, Integer> townKillTree) {
		this.townKillTree = townKillTree;
	}

	public List<String> getBeggingTime() {
		return beggingTime;
	}

	public void setBeggingTime(List<String> beggingTime) {
		this.beggingTime = beggingTime;
	}

	public boolean hasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Cuboid getCu() {
		return cu;
	}

	public void setCu(Cuboid cu) {
		this.cu = cu;
	}

	public void startEvent() {
		if (finished) {
			if (eventTime > 0) {
				if (lenghtScoreBoard > 0) {
					if (cu != null) {
						if (Bukkit.getOnlinePlayers().size() >= minPlayer) {
							hasStarted = true;
							for (Player p : Bukkit.getOnlinePlayers()) {
								addPlayer(p);
							}

							fightTimer = new FightTimer(eventTime, this);
							fightTimer.runTaskTimer(pl, 0, 20);
							for (String msg : pl.getConfig().getString("Msg.StartEvent").split("\n")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
							}
						} else {
							Bukkit.broadcastMessage(ChatColor.RED + "There are not enought player to start the event");
						}
					} else {
						Bukkit.broadcastMessage(ChatColor.RED + "The zone is null");
					}
				} else {
					Bukkit.broadcastMessage(ChatColor.RED + "The length of the number of team is negative");
				}
			} else {
				Bukkit.broadcastMessage(ChatColor.RED + "Event duration time is negative");
			}
		} else {
			Bukkit.broadcastMessage(ChatColor.RED + "The arena is not finished");
		}

	}

	public void endEvent() {
		hasStarted = false;
		for (Player p : Bukkit.getOnlinePlayers()) {
			ScoreBoardUtils.rmvScoreBoard(p);
		}

		List<Town> townKey = new ArrayList<Town>();
		townKey.addAll(pl.sortByValue(townKillTree).keySet());

		for (int townNum = 1; townNum <= numberOfTeamToGiveReward; townNum++) {
			if (townKey.size() > townNum - 1) {
				for (Resident r : townKey.get(townNum - 1).getResidents()) {
					Player p = Bukkit.getServer().getPlayer(r.getName());
					if (players.contains(p)) {
						for (String command : pl.getConfig().getStringList("Winner." + townNum + ".Reward")) {
							if (!command.isEmpty()) {
								command = command.replace("%player%", p.getName());
								command = command.replace("%place%", String.valueOf(townNum));

								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
							}
						}
					}
				}
			}
		}

		for (String msg : pl.getConfig().getString("Msg.EndEvent").split("\n")) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		}

		players = new ArrayList<Player>();

	}

	public void addPlayer(Player p) {
		Resident killerResident = TownyUniverse.getInstance().getResident(p.getName());
		if (killerResident.hasTown()) {
			players.add(p);
			Town town = null;
			try {
				town = killerResident.getTown();
			} catch (NotRegisteredException e) {
				e.printStackTrace();
			}
			if (!townKillTree.containsKey(town))
				townKillTree.put(town, 0);
		} else {
			p.sendMessage(ChatColor.RED + "You don't have a town you can't join");
		}
	}

	public void removePlayer(Player p) {
		players.remove(p);
	}
}
