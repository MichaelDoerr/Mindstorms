package utils;

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
		return (lastDistances[0] * 0.15f + lastDistances[1] * 0.25f + lastDistances[2] * 0.6f);
	}

	public static float currentDistance(Sensors sensors) {
		float distance = sensors.getSample(sensors.ultraSonicSensor)[0];
		distance = distance == Float.POSITIVE_INFINITY ? 3 : distance;
		pushDistance(distance);
		
		return averageDistance();
	}
	
	public static boolean obstacleAhead(Sensors sensors) {
		return currentDistance(sensors) <= 0.07f;
	}
	
	public static float findObstacle(Sensors sensors, Motors motors) {
		
		float circleScopeInDegree = Motors.WHEEL_DISTANCE * (float)Math.PI * Motors.RADIUS_TO_DEGREE;
		int speed = 100;
		int degreeOfObstacle = 5000;
		float distanceMin = 3f;
		float currentDistance = 3f;
		int tachoCount;
		
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorB.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, speed);
		Motors.motorSpeed(motors.largeMotorB, -speed);
		
		tachoCount = motors.largeMotorA.getTachoCount();
		while (tachoCount < circleScopeInDegree) {			
			currentDistance = currentDistance(sensors);			
			if (currentDistance < distanceMin) {
				distanceMin = currentDistance;
				degreeOfObstacle = motors.largeMotorA.getTachoCount();
			}			
			tachoCount = motors.largeMotorA.getTachoCount();
		}
		motors.largeMotorA.resetTachoCount();
		motors.largeMotorB.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, 0);
		Motors.motorSpeed(motors.largeMotorB, 0);

		
		tachoCount = motors.largeMotorA.getTachoCount();			
		degreeOfObstacle =  -((int)circleScopeInDegree - degreeOfObstacle);
		
		Motors.motorSpeed(motors.largeMotorA, -speed);
		Motors.motorSpeed(motors.largeMotorB, speed);
		while (tachoCount > degreeOfObstacle) {			
			tachoCount = motors.largeMotorA.getTachoCount();
		}
		
		
		
		
		Motors.motorSpeed(motors.largeMotorA, 0);
		Motors.motorSpeed(motors.largeMotorB, 0);
		Delay.msDelay(2000);
		return distanceMin;
	}
	
	public static boolean getBehind(Motors motors) {
		
		int speed = 100;
		float radius = 0.06f; 
		boolean pressedEscape = false;
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, -speed);
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && -(int)((Motors.WHEEL_DISTANCE * 3.14f / 4.0f) * Motors.RADIUS_TO_DEGREE) <= motors.largeMotorA.getTachoCount() ) {
			pressedEscape = Button.ESCAPE.isDown();
		}
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, speed * (radius * 2 + Motors.WHEEL_DISTANCE) / (radius * 2));
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && motors.largeMotorA.getTachoCount() <= (int)(((radius * 2 + Motors.WHEEL_DISTANCE) * 3.14) * Motors.RADIUS_TO_DEGREE) * 0.95) {
			pressedEscape = Button.ESCAPE.isDown();
		}
		
		motors.largeMotorA.resetTachoCount();
		Motors.motorSpeed(motors.largeMotorA, -speed);
		Motors.motorSpeed(motors.largeMotorB, speed);
		while(!pressedEscape && -(int)((Motors.WHEEL_DISTANCE * 3.14f / 4.0f) * Motors.RADIUS_TO_DEGREE) * 0.3 <= motors.largeMotorA.getTachoCount()) {
			pressedEscape = Button.ESCAPE.isDown();
		}	
		
		
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return !pressedEscape;
	}
}