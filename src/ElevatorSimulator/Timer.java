package ElevatorSimulator;

import java.util.Date;

public class Timer {
	public static int INCREMENT = 1000;
	
	private Date currentTime;
	
	public void setTime(Date time) {
		this.currentTime = time;
	}
	
	public void tick() {
		currentTime.setTime(currentTime.getTime() + INCREMENT);
	}
	
	public Date getTime() {
		return currentTime;
	}
}
