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
		float x;
		float y;
		float stdSpeed = 70;
		boolean end = false;
		
		LCD.clear();
		LCD.drawString("W: " + String.valueOf(sampleWhite), 0, 1);
		LCD.drawString("B: " + String.valueOf(sampleBlack), 0, 2);
		LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
		
		motors.largeMotorA.resetTachoCount();
		
		while (!end) {
			
			colorSample = sensors.getSample(sensors.colorSensor);

			color = ColorPicker.getGreyMode(colorSample);
			
			
			x = color - avgLight;
			y = x * 300;
			
			end = Button.ESCAPE.isDown();
			
			Motors.motorSpeed(motors.largeMotorA, stdSpeed + y);
			Motors.motorSpeed(motors.largeMotorB, stdSpeed - y);
			LCD.clear();
			LCD.drawString("y: " + String.valueOf(y), 0, 1);
			LCD.drawString("tacho: " + String.valueOf(motors.largeMotorA.getTachoCount()), 0, 3);
			LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
			//Delay.msDelay(50);
			
		}
		Motors.motorSpeed(motors.largeMotorA, 0f);
		Motors.motorSpeed(motors.largeMotorB, 0f);
		
		return (end) ? -1 : 0;
	}
	
	
}