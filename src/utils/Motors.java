package utils;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Motors {
	public static final float WHEEL_DISTANCE = 0.1075f;
	public static final float WHEEL_DIAMETER = 0.056f;
	public static final float RADIUS_TO_DEGREE = 360.0f / (WHEEL_DIAMETER * (float)Math.PI);
	public EV3LargeRegulatedMotor largeMotorA;
	public EV3LargeRegulatedMotor largeMotorB;
	
	
	public Motors() {
		this.largeMotorA = new EV3LargeRegulatedMotor(MotorPort.A);
		this.largeMotorB = new EV3LargeRegulatedMotor(MotorPort.B);
	}
	
	/**
	 * Set the speed of a motor.
	 * If speed is negative, motor will turn backwards.
	 * 
	 * @param motor this.largeMotorABCD
	 * @param speed the speed for motor.setSpeed(speed)
	 */
	public static void motorSpeed(EV3LargeRegulatedMotor motor, float speed) {		
		if (0 <= speed) {
			motor.setSpeed(speed);
			motor.forward();
		} else {
			motor.setSpeed(-speed);
			motor.backward();
		}
	}
	
	public boolean goTo(int degreeA, int degreeB, float speedA, float speedB) {		
		speedA = Math.abs(speedA) * Math.signum(degreeA);
		speedB = Math.abs(speedB) * Math.signum(degreeB);
		degreeA = Math.abs(degreeA);
		degreeB = Math.abs(degreeB);
		
		this.largeMotorA.resetTachoCount();
		this.largeMotorB.resetTachoCount();
		motorSpeed(this.largeMotorA, speedA);
		motorSpeed(this.largeMotorB, speedB);
		while( Math.abs(this.largeMotorA.getTachoCount()) < degreeA 
			|| Math.abs(this.largeMotorB.getTachoCount()) < degreeB) {
			if (!(Math.abs(this.largeMotorA.getTachoCount()) < degreeA)) {
				motorSpeed(this.largeMotorA, 0);
			}
			if (!(Math.abs(this.largeMotorB.getTachoCount()) < degreeB)) {
				motorSpeed(this.largeMotorB, 0);
			}

			if (Button.ESCAPE.isDown()) {
				Motors.motorSpeed(this.largeMotorA, 0);
				Motors.motorSpeed(this.largeMotorB, 0);
				return false;
			}
		}
		return true;
	}
	
	public void close() {
		largeMotorA.close();
		largeMotorB.close();
	}

}
