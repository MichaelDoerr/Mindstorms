package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
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
			Motors.motorSpeed(motors.largeMotorA, 150);
			Motors.motorSpeed(motors.largeMotorB, 150);
			color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
			if (Button.ESCAPE.isDown()) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
		}
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorA.rotate(360, true);
		motors.largeMotorB.rotate(360);
		motors.largeMotorA.rotate(-180, true);
		motors.largeMotorB.rotate(-180);
		
		distance = ObstacleFinder.findObstacle(sensors, motors);
		if (distance == -1) {
			return false;
		}
		distance = distance - 0.09f;
		
		while (0 < distance && distance < 2.5f) {
			goTo = (int)(distance * Motors.RADIUS_TO_DEGREE);			

			if (!motors.goTo(goTo, goTo, 150, 150)) {
				return false;
			}
			distance = ObstacleFinder.findObstacle(sensors, motors) - 0.09f;
			if (distance == -1) {
				return false;
			}
		}
		color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
		while (!(color <= sampleBlack * (1.0 + tolerance))) {
			Motors.motorSpeed(motors.largeMotorA, 100);
			Motors.motorSpeed(motors.largeMotorB, 100);
			color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
			if (Button.ESCAPE.isDown()) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
		}
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorA.rotate(100, true);
		motors.largeMotorB.rotate(100);
		motors.largeMotorA.rotate(-120, true);
		motors.largeMotorB.rotate(-120);
		
		LCD.clear();
		return !Button.ESCAPE.isDown();
	}
}