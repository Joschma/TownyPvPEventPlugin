package fr.joschma.PvpEvent.Timer;

import org.bukkit.scheduler.BukkitRunnable;

import fr.joschma.PvpEvent.Arena.Arena;

public class DayTimer extends BukkitRunnable {

	int minutes;
	int hours;
	int seconds;
	Arena a;

	public DayTimer(int seconds, int minutes, int hours, Arena a) {
		super();
		this.seconds = seconds;
		this.minutes = minutes;
		this.hours = hours;
		this.a = a;
	}

	@Override
	public void run() {
		seconds++;
		if (seconds == 60) {
			seconds = 0;
			minutes++;
			if (minutes == 60) {
				minutes = 0;
				hours++;
				if (hours == 24) {
					hours = 0;
				}
			}

			for (String beginTime : a.getBeggingTime()) {
				String[] parsed = beginTime.split(":");
				
				String hour = String.valueOf(hours);
				String minute = String.valueOf(minutes);

				if (parsed[0].equals(hour)) {
					if (parsed[1].equals(minute)) {
						a.startEvent();
					}
				}
			}
		}
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}
}
