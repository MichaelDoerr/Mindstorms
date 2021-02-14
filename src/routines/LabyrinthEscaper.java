package routines;

import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Line;
import utils.Motors;
import utils.Sensors;

public class LabyrinthEscaper extends Routine {

	public LabyrinthEscaper(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		LCD.clear();
		LCD.drawString("Labyrinth here", 0, 0);
		boolean end = false;
		int tacho = 0;
		
		end = !Line.findBlack(super.sensors, super.motors, super.colors);
		
		while (!end) {
			tacho = Line.followTillEnd(super.sensors, super.motors, super.colors);
			if (0 < tacho) {
				motors.largeMotorA.resetTachoCount();
			} else {
				end = true;
			}
			
		}
		Delay.msDelay(2000);
		LCD.clear();
		return tacho != -1;
	}
}