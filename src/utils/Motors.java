package utils;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Motors {
	
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
			motor.setSpeed(new Float(speed).intValue());
			motor.forward();
		} else {
			motor.setSpeed(new Float(-speed).intValue());
			motor.backward();
		}
	}
	
	public void close() {
		largeMotorA.close();
		largeMotorB.close();
	}

}
