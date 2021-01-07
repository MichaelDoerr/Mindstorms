package parcours;

public abstract class Routine {
	private SubRoutine routine;
	private Sensors sensors;
	private Motors motors;

	public Routine(SubRoutine routine, Sensors sensors, Motors motors) {
		this.routine = routine;
		this.sensors = sensors;
		this.motors = motors;
	}

	public SubRoutine getroutine() {
		return routine;
	}
	
	public void run() {}
	
	public void setColors() {}

}
