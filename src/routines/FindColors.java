package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import utils.Line;
import utils.Motors;
import utils.Sensors;

public class FindColors extends Routine {

	private boolean turn;
	boolean red;
	boolean green;

	public FindColors(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
		this.turn = true;
		this.red = false;
		this.green = false;
	}

	public boolean run() {
		LCD.drawString("Searching here", 0, 0);
		
		Line.findBlack(sensors, motors, colors);
		
		if (!this.setAlignment()) {
			return false;
		}
		while (true) {
			if (!searchColors()) {
				return true;
			}
		}
	}

	public boolean searchColors() {
		float sampleBlack = ColorPicker.getGreyMode(colors.get("black"));
		float sampleWhite = ColorPicker.getGreyMode(colors.get("white"));
		float tolerance = 0.1f;
		float[] colorSample = sensors.getSample(sensors.colorSensor);
		float gray = ColorPicker.getGreyMode(colorSample);
		float avgLight = (sampleWhite - sampleBlack) / 2;
		int speed = 200;

		if (turn) {
			Motors.motorSpeed(motors.largeMotorA, -speed);
			Motors.motorSpeed(motors.largeMotorB, -speed);
		} else {
			Motors.motorSpeed(motors.largeMotorA, speed);
			Motors.motorSpeed(motors.largeMotorB, speed);
		}

		while (gray <= avgLight) {
			colorSample = sensors.getSample(sensors.colorSensor);
			gray = ColorPicker.getGreyMode(colorSample);
			if (Button.ESCAPE.isDown()) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
		}
		while (true) {
			colorSample = sensors.getSample(sensors.colorSensor);
			gray = ColorPicker.getGreyMode(colorSample);
			if (ColorPicker.sameColor(colorSample, colors.get("red")) && !red && !green) {
				Sound.beepSequenceUp();
				red = true;
			}
			if (ColorPicker.sameColor(colorSample, colors.get("red")) && !red && green) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
			if (ColorPicker.sameColor(colorSample, colors.get("green")) && !green && !red) {
				Sound.beepSequence();
				green = true;
			}
			if (ColorPicker.sameColor(colorSample, colors.get("green")) && !green && red) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
			if (gray <= sampleBlack * (1.0 + tolerance)) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				break;
			}
			if (Button.ESCAPE.isDown()) {
				Motors.motorSpeed(motors.largeMotorA, 0);
				Motors.motorSpeed(motors.largeMotorB, 0);
				return false;
			}
		}

		this.turn = !this.turn;
		Motors.motorSpeed(motors.largeMotorA, 30);
		Motors.motorSpeed(motors.largeMotorB, 30);
		if (this.turn) {
			motors.largeMotorB.rotate(-10);
		} else {
			motors.largeMotorA.rotate(-10);
		}

		return true;
	}

	public boolean setAlignment() {
		float sampleWhite = ColorPicker.getGreyMode(colors.get("white"));
		float sampleBlack = ColorPicker.getGreyMode(colors.get("black"));

		float color;
		float[] colorSample;
		float avgLight = (sampleWhite - sampleBlack) / 2;
		float tolerance = 0.1f;
		float aSpeed;
		float bSpeed;
		float m = 0;
		int tacho = 0;
		motors.largeMotorA.resetTachoCount();

		while (tacho < 360) {

			colorSample = sensors.getSample(sensors.colorSensor);

			color = ColorPicker.getGreyMode(colorSample);

			if (sampleWhite * (1.0 - tolerance) <= color) {
				aSpeed = Line.MAX_SPEED;
				bSpeed = Line.MIN_SPEED;

			} else if (color <= sampleBlack * (1.0 + tolerance)) {
				aSpeed = Line.MIN_SPEED;
				bSpeed = -Line.MIN_SPEED;

			} else if (color < avgLight) {
				m = (Line.MAX_SPEED - Line.MIN_SPEED) / (avgLight - sampleBlack);
				aSpeed = m * (color - sampleBlack) + Line.MIN_SPEED;
				bSpeed = Line.MAX_SPEED;

			} else if (avgLight < color) {
				m = (Line.MIN_SPEED - Line.MAX_SPEED) / (sampleWhite - avgLight);
				aSpeed = Line.MAX_SPEED;
				bSpeed = m * (color - avgLight) + Line.MAX_SPEED;

			} else {
				aSpeed = Line.MAX_SPEED;
				bSpeed = Line.MAX_SPEED;
			}
			
			tacho = motors.largeMotorA.getTachoCount();
			if (Button.ESCAPE.isDown()) {
				return false;
			}

			Motors.motorSpeed(motors.largeMotorA, aSpeed);
			Motors.motorSpeed(motors.largeMotorB, bSpeed);
		}
		return true;

	}
}