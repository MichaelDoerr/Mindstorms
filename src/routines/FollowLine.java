package routines;

import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Line;
import utils.Motors;
import utils.Sensors;

public class FollowLine extends Routine {

	public FollowLine(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
	}
	
	public boolean run() {
		LCD.drawString("FollowLine here", 0, 0);
		boolean end = false;
		int tacho = 0;
		
		while (!end) {
			Line.followTillEnd(super.sensors, super.motors, super.colors);			
			end = true;
		}
		
		Delay.msDelay(2000);
		LCD.clear();
		return tacho != -1;
	}
}