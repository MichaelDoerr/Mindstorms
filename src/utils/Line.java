package utils;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import routines.ColorPicker;


public class Line {
		public static final float MAX_SPEED = 150.0f;
		public static final float MIN_SPEED = -MAX_SPEED / 2.0f;
	
	
	
	
	
	public static int followTillEnd(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		
		float sampleWhite = ColorPicker.getGreyMode(colors.get("white"));
		float sampleBlack = ColorPicker.getGreyMode(colors.get("black"));

		float color;
		float[] colorSample;
		float avgLight = (sampleWhite - sampleBlack) / 2;
		float tolerance = 0.1f;
		float aSpeed;
		float bSpeed;
		boolean end = false;
		float m = 0;
		int tacho = 0;
		
		motors.largeMotorA.resetTachoCount();
		
		while (!end && tacho < 400) {
			
			colorSample = sensors.getSample(sensors.colorSensor);

			color = ColorPicker.getGreyMode(colorSample);
			
			if (sampleWhite * (1.0 - tolerance) <= color) {
				aSpeed = MAX_SPEED;
				bSpeed = MIN_SPEED;
				
			} else if (color <= sampleBlack * (1.0 + tolerance) 
					|| ColorPicker.sameColor(colorSample, colors.get("red"))) {
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
			
			if (ColorPicker.sameColor(colorSample, colors.get("green"))) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return -3;
			}
			
			if (ObstacleFinder.obstacleAhead(sensors)) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return -2;
			}
			
			tacho = motors.largeMotorA.getTachoCount();
			end = Button.ESCAPE.isDown();
			
			Motors.motorSpeed(motors.largeMotorA, aSpeed);
			Motors.motorSpeed(motors.largeMotorB, bSpeed);
			
			/*
			LCD.clear();
			LCD.drawString("a: " + String.valueOf(aSpeed), 0, 1);
			LCD.drawString("b: " + String.valueOf(bSpeed), 0, 2);
			LCD.drawString("tacho: " + String.valueOf(motors.largeMotorA.getTachoCount()), 0, 3);
			LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
			*/
			//Delay.msDelay(50);
			
		}
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return (end) ? -1 : tacho;
	}
	
	
	public static boolean findBlack(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		
		float sampleBlack = ColorPicker.getGreyMode(colors.get("black"));

		float color;
		float tolerance = 0.15f;
		int tachoRadius = 200;
		int tachoStraight = 100;
		boolean pressedEscape = false;
		boolean foundBlack = false;
		
		Motors.motorSpeed(motors.largeMotorA, MAX_SPEED);
		Motors.motorSpeed(motors.largeMotorB, MAX_SPEED);
		motors.largeMotorA.resetTachoCount();
		
		while (motors.largeMotorA.getTachoCount() <= 10) {
			//busy waiting
		}
		
		while (!pressedEscape && !foundBlack) {
			
			motors.largeMotorA.resetTachoCount();			
			while(!pressedEscape && !foundBlack && motors.largeMotorA.getTachoCount() <= tachoStraight) {
				color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorA, 0f);
					Motors.motorSpeed(motors.largeMotorB, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorA, MAX_SPEED);
					Motors.motorSpeed(motors.largeMotorB, MAX_SPEED);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}	
			Motors.motorSpeed(motors.largeMotorA, 0f);
			Motors.motorSpeed(motors.largeMotorB, 0f);
			
			
			motors.largeMotorA.resetTachoCount();
			while(!pressedEscape && !foundBlack && motors.largeMotorA.getTachoCount() <= tachoRadius) {
				color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorA, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorA, MAX_SPEED);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}			
			while(!pressedEscape && !foundBlack && 0 <= motors.largeMotorA.getTachoCount()) {
				Motors.motorSpeed(motors.largeMotorA, -MAX_SPEED);
				pressedEscape = Button.ESCAPE.isDown();
			}
			Motors.motorSpeed(motors.largeMotorA, 0f);
			Motors.motorSpeed(motors.largeMotorB, 0f);
			
			
			motors.largeMotorB.resetTachoCount();
			while(!pressedEscape && !foundBlack && motors.largeMotorB.getTachoCount() < tachoRadius) {
				color = ColorPicker.getGreyMode(sensors.getSample(sensors.colorSensor));
				
				if (color <= sampleBlack * (1.0 + tolerance)) {
					foundBlack = true;
					Motors.motorSpeed(motors.largeMotorB, 0f);
				} else {
					Motors.motorSpeed(motors.largeMotorB, MAX_SPEED);
				}
				pressedEscape = Button.ESCAPE.isDown();
			}			
			while(!pressedEscape && !foundBlack && 0 <= motors.largeMotorB.getTachoCount()) {
				Motors.motorSpeed(motors.largeMotorB, -MAX_SPEED);
				pressedEscape = Button.ESCAPE.isDown();
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