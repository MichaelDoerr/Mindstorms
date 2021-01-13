package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.Sensors;

public class ObstaclePusher extends Routine {

	public ObstaclePusher(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		LCD.drawString("Obstacle here", 0, 0);
		LCD.drawString("soon", 0, 1);
		Delay.msDelay(2000);
		LCD.clear();
		return !Button.ESCAPE.isDown();
	}
}