package utils;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;


public class ObstacleFinder {
	
	private static float[] lastDistances = {3, 3, 3};
	
	private ObstacleFinder() {}
	
	private static void pushDistance(float distance) {
		lastDistances[0] = lastDistances[1];
		lastDistances[1] = lastDistances[2];
		lastDistances[2] = distance;
	}
	
	private static float averageDistance()  {
		return (lastDistances[0] + lastDistances[1] + lastDistances[2]) / 3.0f;
	}

	public static boolean obstacleAhead(Sensors sensors) {
		float distance = sensors.getSample(sensors.ultraSonicSensor)[0];
		distance = distance == Float.POSITIVE_INFINITY ? 3 : distance;
		pushDistance(distance);
		
		return averageDistance() <= 0.07f;
	}
	
	public static boolean getBehind(Motors motors) {
		
		int speed = 100;
		float radius = 0.06f;
		float wheelDistance = 0.1075f;
		float radiusToDegree = 360.0f / (0.056f * 3.14f); 
		boolean pressedEscape = false;
		
	
		
		LCD.clear();
		LCD.drawString("a: " + String.valueOf(-(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree)), 0, 1);
		LCD.drawString("b: " + String.valueOf((int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree)), 0, 2);
		
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, -speed);
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && -(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree) <= motors.largeMotorA.getTachoCount() ) {
			pressedEscape = Button.ESCAPE.isDown();
		}
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, speed * (radius * 2 + wheelDistance) / (radius * 2));
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && motors.largeMotorA.getTachoCount() <= (int)(((radius * 2 + wheelDistance) * 3.14) * radiusToDegree) * 0.95) {
			pressedEscape = Button.ESCAPE.isDown();
		}
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, -speed);
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && -(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree) * 0.3 <= motors.largeMotorA.getTachoCount()) {
			pressedEscape = Button.ESCAPE.isDown();
		}	
		
		
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return !pressedEscape;
	}
}