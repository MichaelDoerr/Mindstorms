package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.Sensors;

public class FollowLine extends Routine {

	public FollowLine(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		LCD.drawString("FollowLine here", 0, 0);
		boolean end = false;
		int tacho = 0;
		while (!end) {
			tacho = Line.followTillEnd(super.sensors, super.motors, super.colors);
			if (0 < tacho) {
				motors.largeMotorA.resetTachoCount();
				while (-motors.largeMotorA.getTachoCount() < tacho) {
					Motors.motorSpeed(motors.largeMotorA, -Line.MAX_SPEED);
					Motors.motorSpeed(motors.largeMotorB, -Line.MIN_SPEED);
					end = Button.ESCAPE.isDown();
				}
				
				if (!end) {
					end = Line.findBlack(super.sensors, super.motors, super.colors);		
				}		
			} else {
				end = true;
			}
			
		}
		Delay.msDelay(2000);
		LCD.clear();
		return end;
	}
}