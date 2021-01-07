package parcours;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Motors {
	
	public EV3LargeRegulatedMotor largeMotorA;
	public EV3LargeRegulatedMotor largeMotorB;
	
	
	public Motors() {
		this.largeMotorA = new EV3LargeRegulatedMotor(MotorPort.A);
		this.largeMotorB = new EV3LargeRegulatedMotor(MotorPort.B);
	}
	
	public static void motorSpeed(EV3LargeRegulatedMotor motor, float speed) {		
		if (0 <= speed) {
			motor.setSpeed(new Float(speed).intValue());
			motor.forward();
		} else {
			motor.setSpeed(new Float(-speed).intValue());
			motor.backward();
		}
	}

}
