import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;
import parcours.ColorPicker;
import parcours.FollowLine;
import parcours.LabyrinthEscaper;
import parcours.Motors;
import parcours.ObstaclePusher;
import parcours.Routine;
import parcours.Sensors;
import parcours.SubRoutine;


public class MainMenu {
	
	Motors motors;
	Sensors sensors;
	HashMap<String, Float> colors;

	public MainMenu() {
		this.motors = new Motors();
		this.sensors = new Sensors();
		
		colors = new HashMap<String, Float>();
		colors.put("black", 0.055f);
		colors.put("white", 0.855f);
	}
	
	/**
	 * The wrapper for all routines.
	 * Holds motors, sensors and colors for 
	 */
	public void openMenu() {
		Routine routine;
		int selected = 0;		
		SubRoutine[] routines = SubRoutine.getAllSubRoutines();
		SubRoutine selectedRoutine = SubRoutine.SETCOLORS;
		boolean completeRun = false;
		
		String[] routineNames = new String[SubRoutine.COUNTSUBROUTINES];
		for (int i = 0; i < SubRoutine.COUNTSUBROUTINES; i++) {
			routineNames[i] = routines[i].toString();
		}
		
		TextMenu menu = new TextMenu(routineNames);
		menu.setTitle("Select Routine");
		
		
		while(selected != -1) {
			if (!completeRun || selectedRoutine == SubRoutine.SETCOLORS) {
				selected = menu.select();
				if (selected == -1) { //stop program
					continue;         //skips to next iteration
				}
				selectedRoutine = routines[selected];
			}
			
			switch (selectedRoutine.getorder()) {
				case 0:
					routine = new ColorPicker(sensors, motors, colors);
					break;
				case 1:
					routine = new FollowLine(sensors, motors, colors);
					selectedRoutine = selectedRoutine.next();
					break;
				case 2:
					routine = new LabyrinthEscaper(sensors, motors, colors);
					selectedRoutine = selectedRoutine.next();
					break;
				case 3:
					routine = new ObstaclePusher(sensors, motors, colors);
					selectedRoutine = selectedRoutine.next();
					break;
				default:
					routine = new ColorPicker(sensors, motors, colors);
			}
			
			LCD.clear();
			completeRun = routine.run();

			Delay.msDelay(2000);
			
		}
	}
}