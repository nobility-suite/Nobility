package net.civex4.nobility.siege;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.civex4.nobility.estate.Estate;
import net.md_5.bungee.api.ChatColor;
import vg.civcraft.mc.civmodcore.scoreboard.side.CivScoreBoard;
import vg.civcraft.mc.civmodcore.scoreboard.side.ScoreBoardAPI;

public class Siege {
	Estate defender;
	int duration; //time in millis;
	long start; //time started;
	boolean active;
	private ArrayList<CivScoreBoard> scoreBoards;
	
	public Siege(Estate e) {
		this.defender = e;
		this.scoreBoards = new ArrayList<CivScoreBoard>();
		this.buildScoreboard();
		this.start = System.currentTimeMillis();
		this.duration = 120*60*1000;
	}
	
	public int getHealth() {
		return defender.getCurrentHealth();
	}
	
	public int getMaxHealth() {
		return defender.getMaxHealth();
	}

	public Estate getDefender() {
		return defender;
	}

	public void setDefender(Estate defender) {
		this.defender = defender;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int mins) {
		this.duration = mins;
	}
	
	public void buildScoreboard() {
		int lines = 6;
		
		for (int i = 0; i < lines; i++) {
			scoreBoards.add(ScoreBoardAPI.createBoard("siege" + this.getDefender().getGroup().getName() + i));	
		}
		
		
	}
	
	public ArrayList<CivScoreBoard> getScoreboard() {
		return this.scoreBoards;
	}
	
	public int getOnlineDefenders() {
		return 0;
	}

	public String formatTime() {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		long millis = duration - (time - start);
		String ret = String.format(ChatColor.WHITE + "[%02d:%02d:%02d]", 
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));   
		return ret;
	}
	

}
