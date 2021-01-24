package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.ObstacleFinder;
import utils.Sensors;

public class ObstaclePusher extends Routine {

	public ObstaclePusher(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		float sampleBlack = ColorPicker.getGreyMode(colors.get("black"));
		float color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
		float tolerance = 0.1f;
		float distance;
		int goTo;
		
		LCD.drawString("Obstacle here", 0, 0);
		
		while (!(color <= sampleBlack * (1.0 + tolerance))) {
			Motors.motorSpeed(motors.largeMotorA, 100);
			Motors.motorSpeed(motors.largeMotorB, 100);
			color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
		}
		LCD.drawString("Obstacle here", 0, 1);
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorA.rotate(360, true);
		motors.largeMotorB.rotate(360);
		motors.largeMotorA.rotate(-180, true);
		motors.largeMotorB.rotate(-180);
		LCD.drawString("Obstacle here", 0, 2);
		distance = ObstacleFinder.findObstacle(sensors, motors);
		distance = distance - 0.09f;
		
		while (0 < distance && distance < 2.5f) {
			goTo = (int)(distance * Motors.RADIUS_TO_DEGREE);
			LCD.drawString(goTo + "      ", 0, 4);
			motors.goTo(goTo, goTo, 100, 100);			
			distance = ObstacleFinder.findObstacle(sensors, motors) - 0.09f;
		}
		LCD.drawString("Obstacle here", 0, 4);
		color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
		while (!(color <= sampleBlack * (1.0 + tolerance))) {
			Motors.motorSpeed(motors.largeMotorA, 100);
			Motors.motorSpeed(motors.largeMotorB, 100);
			color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
		}
		LCD.drawString("Obstacle here", 0, 5);
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorA.rotate(300, true);
		motors.largeMotorB.rotate(300);
		motors.largeMotorA.rotate(-360, true);
		motors.largeMotorB.rotate(-360);
		LCD.drawString("Obstacle here", 0, 6);
		
		LCD.clear();
		return !Button.ESCAPE.isDown();
	}
}