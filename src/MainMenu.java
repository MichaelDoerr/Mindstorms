import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;
import routines.ColorPicker;
import routines.FollowLine;
import routines.LabyrinthEscaper;
import routines.ObstaclePusher;
import routines.Routine;
import routines.SubRoutine;
import utils.Motors;
import utils.Sensors;


public class MainMenu {
	
	Motors motors;
	Sensors sensors;
	HashMap<String, Float[]> colors;

	public MainMenu() {
		this.motors = new Motors();
		this.sensors = new Sensors();
		
		colors = new HashMap<String, Float[]>();
		Float[] black = {0.021f,0.032f,0.012f};
		Float[] white = {0.321f,0.502f,0.228f};
		Float[] green = {0.053f,0.257f,0.024f};
		Float[] red   = {0.232f,0.045f,0.014f};
		
		colors.put("black", black); //standard colors
		colors.put("white", white);
		colors.put("green", green);
		colors.put("red", red);
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

		sensors.close();
		motors.close();		
	}
}
