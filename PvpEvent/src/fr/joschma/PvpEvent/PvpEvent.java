package fr.joschma.PvpEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import fr.joschma.PvpEvent.Arena.Arena;
import fr.joschma.PvpEvent.Commands.Commands;
import fr.joschma.PvpEvent.Cuboid.Cuboid;
import fr.joschma.PvpEvent.Listener.onPlayerInteract;
import fr.joschma.PvpEvent.Listener.onPlayerKillListener;
import fr.joschma.PvpEvent.Listener.onPlayerMoveListener;
import fr.joschma.PvpEvent.Listener.onPlayerQuitListener;
import fr.joschma.PvpEvent.TabFinisher.TabCompletor;
import fr.joschma.PvpEvent.Timer.DayTimer;
import fr.joschma.PvpEvent.Utils.UtilsLoc;

public class PvpEvent extends JavaPlugin {

	Arena a;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		Location loc1 = UtilsLoc.stringToLoc(getConfig().getString("Zone.Loc1"));
		Location loc2 = UtilsLoc.stringToLoc(getConfig().getString("Zone.Loc2"));
		
		if(getConfig().getString("Msg.StartEvent") == null) {
			getConfig().set("Msg.StartEvent", "The event has started");
		}

		if(getConfig().getString("Msg.EndEvent") == null) {
			getConfig().set("Msg.EndEvent", "The event has endded");
		}
		
		Cuboid cu = null;
		if (loc1 != null && loc2 != null) {
			cu = new Cuboid(loc1, loc2);
		}
		List<String> begstart = getConfig().getStringList("BegginTime");
		List<String> beg = new ArrayList<String>();

		if (getConfig().getStringList("BegginTime").isEmpty()) {
			beg.add("20:35");
			getConfig().set("BegginTime", beg);
		}
		if(!getConfig().contains("RequireOnline")) {
			getConfig().set("RequireOnline", 2);
		}
		if (getConfig().getInt("NumberOfTeamToShowOnTheScoreboard") == 0) {
			getConfig().set("NumberOfTeamToShowOnTheScoreboard", 5);
		}
		if (getConfig().getInt("EventTime") == 0) {
			getConfig().set("EventTime", 3600);
		}
		if (getConfig().getInt("NumberOfTeamToGiveReward") == 0) {
			getConfig().set("NumberOfTeamToGiveReward", 5);
		}
		List<String> defaultCommands = new ArrayList<String>();
		defaultCommands.add("say %player% you finished at place ");

		if (!getConfig().contains("Winner")) {
			getConfig().set("Winner.1.Reward", defaultCommands);
			getConfig().set("Winner.2.Reward", defaultCommands);
			getConfig().set("Winner.3.Reward", defaultCommands);
			getConfig().set("Winner.4.Reward", defaultCommands);
			getConfig().set("Winner.5.Reward", defaultCommands);
		}

		for (String time : getConfig().getStringList("BegginTime")) {
			String[] str = time.split(":");
			if (str.length != 2) {
				beg.add("20:35");
				getConfig().set("BegginTime", beg);
			}
		}

		getConfig().set("BegginTime", begstart);

		saveConfig();

		a = new Arena(cu, getConfig().getStringList("BegginTime"), getConfig().getInt("Lenghtscoreboard"),
				getConfig().getInt("EventTime"), getConfig().getBoolean("Finished"),
				getConfig().getInt("NumberOfTeamToGiveReward"), getConfig().getInt("RequireOnline"), this);

		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		a.setDayTimer(new DayTimer(calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.HOUR_OF_DAY), a));
		a.getDayTimer().runTaskTimer(this, 0, 20);

		getServer().getPluginManager().registerEvents(new onPlayerQuitListener(this), this);
		getServer().getPluginManager().registerEvents(new onPlayerKillListener(a), this);
		getServer().getPluginManager().registerEvents(new onPlayerMoveListener(a), this);
		getServer().getPluginManager().registerEvents(new onPlayerInteract(this), this);

		getCommand("pvpevent").setTabCompleter(new TabCompletor());
		getCommand("pvpevent").setExecutor(new Commands(this));

		super.onEnable();
	}

	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		Comparator<Map.Entry<K, V>> tes = Entry.comparingByValue();
		list.sort(tes.reversed());

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	@Override
	public void onDisable() {
		if (a.hasStarted()) {
			a.endEvent();
		}

		super.onDisable();
	}

	public Arena getA() {
		return a;
	}

	public void setNewDayTimer() {
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		a.setDayTimer(new DayTimer(calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.HOUR_OF_DAY), a));
	}
}