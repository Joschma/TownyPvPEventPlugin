package fr.joschma.PvpEvent.Timer;

import org.bukkit.scheduler.BukkitRunnable;

import fr.joschma.PvpEvent.Arena.Arena;
import fr.joschma.PvpEvent.Utils.ScoreBoardUtils;

public class FightTimer extends BukkitRunnable {

	int timer;
	Arena a;

	public FightTimer(int timer, Arena a) {
		super();
		this.timer = timer;
		this.a = a;
	}

	@Override
	public void run() {
		if (timer == 0) {
			a.endEvent();
			cancel();
		} else if (timer > 0) {
			ScoreBoardUtils.updateScoreBoard(a, timer);
			timer--;
		}
	}
}
