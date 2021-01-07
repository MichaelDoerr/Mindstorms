package parcours;

public enum SubRoutine {
	
	SETCOLORS("Set Colors", 0),
	FOLLOWLINE("Follow Line", 1),
	LABYRINTH("Labyrinth", 2),
	OBSTACLE("Obstacle", 3);
	//add more here
	
	public static final int COUNTSUBROUTINES = 4; //increase count
	private String name;
	private int order;
	
	private SubRoutine(String name, int order) {
		this.name = name;
		this.order = order;
	}
	
	public SubRoutine next() {		
		return SubRoutine.getAllSubRoutines()[(this.order + 1) % COUNTSUBROUTINES];
	}
	
	public static SubRoutine[] getAllSubRoutines() {
		SubRoutine[] routines = new SubRoutine[COUNTSUBROUTINES];
		routines[SETCOLORS.order] = SETCOLORS;
		routines[FOLLOWLINE.order] = FOLLOWLINE;
		routines[LABYRINTH.order] = LABYRINTH;
		routines[OBSTACLE.order] = OBSTACLE;
		//add more here
		
		return routines;
	}
	
	public int getorder() {
		return this.order;
	}
	
	public String toString() {
		return this.name;
	}

}
