package ElevatorSimulator;

import java.util.Date;

/**
 * Class for the timer.
 * 
 * @author Kyra Lothrop
 * @author Bardia Parmoun
 *
 */
public class Timer {
	public static int INCREMENT = 1000;
	
	private Date currentTime;
	
	/**
	 * Setter for the time.
	 * @param time value to be set
	 */
	public void setTime(Date time) {
		this.currentTime = time;
	}
	
	/**
	 * Increment the timer.
	 */
	public void tick() {
		currentTime.setTime(currentTime.getTime() + INCREMENT);
	}
	
	/**
	 * Getter for the time.
	 * @return the current time
	 */
	public Date getTime() {
		return currentTime;
	}
}