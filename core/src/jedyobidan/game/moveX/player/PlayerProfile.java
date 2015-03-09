package jedyobidan.game.moveX.player;

import java.util.HashMap;
import java.util.HashSet;

public class PlayerProfile {
	private HashMap<String, Float> maxStats;
	private HashMap<String, Float> currentStats;
	private HashSet<String> abilities;
	
	public PlayerProfile(){
		maxStats = new HashMap<String, Float>();
		currentStats = new HashMap<String, Float>();
		abilities = new HashSet<String>();
		
		initStats();
	}
	
	public void setStat(String stat, float value){
		maxStats.put(stat, value);
		currentStats.put(stat, value);
	}
	
	public float getStat(String stat){
		return currentStats.get(stat);
	}
	
	public void changeStat(String stat, int delta){
		currentStats.put(stat, currentStats.get(stat) + delta);
	}
	
	public void refreshStat(String stat){
		currentStats.put(stat, maxStats.get(stat));
	}
	
	public void addAbility(String ability){
		abilities.add(ability);
	}
	
	public boolean can(String ability){
		return abilities.contains(ability);
	}
	
	// Helper/Convenience Methods
	public boolean canAirJump(){
		return getStat("air_jumps") > 0;
	}
	
	public void useAirJump(){
		changeStat("air_jumps", -1);
	}
	
	public boolean canAirDash(){
		return getStat("air_dashes") > 0;
	}
	
	public void useAirDash(){
		changeStat("air_dashes", -1);
	}
	
	public void initStats(){

		setStat("walk_speed", 7f);
		setStat("walk_accel", 7f);
		setStat("jump_speed", 13.4f);
		setStat("air_speed", 5f);
		setStat("air_accel", 5f);
		setStat("skid_force", 15f);
		setStat("air_jumps", 1f);
		setStat("air_dashes", 1f);

		addAbility("jump");
		addAbility("dash");
		addAbility("direction_dash");
	}
}
