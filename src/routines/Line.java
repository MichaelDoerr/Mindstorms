package routines;
import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.Sensors;


public class Line {
		public static final float MAX_SPEED = 120.0f;
		public static final float MIN_SPEED = -MAX_SPEED / 2.0f;
	
	
	
	
	
	public static int followTillEnd(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		
		float sampleWhite = colors.get("white");		
		float sampleBlack = colors.get("black");

		float color;
		float avgLight = (sampleWhite - sampleBlack) / 2;
		float tolerance = 0.05f;
		float aSpeed;
		float bSpeed;
		float m = 0;
		int tacho = 0;
		motors.largeMotorA.resetTachoCount();
		
		while (!Button.ESCAPE.isDown() && tacho < 400) {

			color = sensors.getSample(sensors.colorSensor, "Red")[0];
			
			if (sampleWhite * (1.0 - tolerance) <= color) {
				aSpeed = MAX_SPEED;
				bSpeed = MIN_SPEED;
				
			} else if (color <= sampleBlack * (1.0 + tolerance)) {
				motors.largeMotorA.resetTachoCount();
				aSpeed = MIN_SPEED;
				bSpeed = MAX_SPEED;
				
			} else if (color < avgLight) {
				motors.largeMotorA.resetTachoCount();
				m = (MAX_SPEED - MIN_SPEED) / (avgLight - sampleBlack);
				aSpeed = m * (color - sampleBlack) + MIN_SPEED;
				bSpeed = MAX_SPEED;
				
			} else if (avgLight < color) {
				motors.largeMotorA.resetTachoCount();
				m = (MIN_SPEED - MAX_SPEED) / (sampleWhite - avgLight);
				aSpeed = MAX_SPEED;
				bSpeed = m * (color - avgLight) + MAX_SPEED;
				
			} else {
				motors.largeMotorA.resetTachoCount();
				aSpeed = MAX_SPEED;
				bSpeed = MAX_SPEED;
			}
			
			tacho = motors.largeMotorA.getTachoCount();
			
			Motors.motorSpeed(motors.largeMotorA, aSpeed);
			Motors.motorSpeed(motors.largeMotorB, bSpeed);
			LCD.clear();
			LCD.drawString("a: " + String.valueOf(aSpeed), 0, 1);
			LCD.drawString("b: " + String.valueOf(bSpeed), 0, 2);
			LCD.drawString("tacho: " + String.valueOf(motors.largeMotorA.getTachoCount()), 0, 3);
			LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
			//Delay.msDelay(50);
			
		}
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return (tacho < 400) ? 0 : tacho;
	}
	
	
	public static boolean findBlack(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		
		float sampleBlack = colors.get("black");

		float color;
		float maxSpeed = 120.0f;
		float minSpeed = 0f;
		float tolerance = 0.05f;
		int tachoRadius = 200;
		int tachoStraight = 100;
		float m = 0;
		int tacho = 0;
		boolean pressedEscape = false;
		boolean foundBlack = false;
		
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		while (!pressedEscape && !foundBlack) {
			
			motors.largeMotorA.resetTachoCount();			
			while(!pressedEscape && !foundBlack && motors.largeMotorA.getTachoCount() <= tachoStraight) {
				color = sensors.getSample(sensors.colorSensor, "Red")[0];
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorA, 0f);
					Motors.motorSpeed(motors.largeMotorB, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorA, maxSpeed);
					Motors.motorSpeed(motors.largeMotorB, maxSpeed);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}	
			Motors.motorSpeed(motors.largeMotorA, 0f);
			Motors.motorSpeed(motors.largeMotorB, 0f);
			
			
			motors.largeMotorA.resetTachoCount();
			while(!pressedEscape && !foundBlack && motors.largeMotorA.getTachoCount() <= tachoRadius) {
				color = sensors.getSample(sensors.colorSensor, "Red")[0];
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorA, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorA, maxSpeed);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}			
			while(!pressedEscape && !foundBlack && 0 <= motors.largeMotorA.getTachoCount()) {
				Motors.motorSpeed(motors.largeMotorA, -maxSpeed);
			}
			Motors.motorSpeed(motors.largeMotorA, 0f);
			Motors.motorSpeed(motors.largeMotorB, 0f);
			
			
			motors.largeMotorB.resetTachoCount();
			while(!pressedEscape && !foundBlack && motors.largeMotorB.getTachoCount() < tachoRadius) {
				color = sensors.getSample(sensors.colorSensor, "Red")[0];
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorB, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorB, maxSpeed);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}			
			while(!pressedEscape && !foundBlack && 0 <= motors.largeMotorB.getTachoCount()) {
				Motors.motorSpeed(motors.largeMotorB, -maxSpeed);
			}

			Motors.motorSpeed(motors.largeMotorA, 0f);
			Motors.motorSpeed(motors.largeMotorB, 0f);
			
			
			//Delay.msDelay(50);
			
		}
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return foundBlack;
	}
	
}