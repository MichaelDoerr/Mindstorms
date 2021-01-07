package parcours;

import java.util.HashMap;

public abstract class Routine {
	private SubRoutine routine;
	private Sensors sensors;
	private Motors motors;
	HashMap<String, Float> colors;

	public Routine(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		this.sensors = sensors;
		this.motors = motors;
		this.colors = colors;
	}

	public SubRoutine getroutine() {
		return routine;
	}
	
	public boolean run() {return true;}

}
