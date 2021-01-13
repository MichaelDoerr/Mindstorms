package routines;

import java.util.HashMap;

import utils.Motors;
import utils.Sensors;


public abstract class Routine {
	protected Sensors sensors;
	protected Motors motors;
	protected HashMap<String, Float[]> colors;
	
	/**
	 * Everything needed for a routine
	 * @param sensors
	 * @param motors
	 * @param colors
	 */
	public Routine(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		this.sensors = sensors;
		this.motors = motors;
		this.colors = colors;
	}

	/**
	 * Has to stop if Button.ESCAPE is pressed
	 * @return true if the routine stopped itself, false if Button.ESCAPE was pressed
	 */
	public boolean run() {return true;}

}
