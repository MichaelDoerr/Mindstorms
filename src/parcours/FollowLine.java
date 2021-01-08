package parcours;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class FollowLine extends Routine {

	public FollowLine(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		LCD.drawString("FollowLine here", 0, 0);
		LCD.drawString("soon", 0, 1);
		Delay.msDelay(2000);
		LCD.clear();
		return !Button.ESCAPE.isDown();
	}
}