package routines;

import java.util.HashMap;

import com.sun.jndi.url.dns.dnsURLContext;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import sun.security.util.DisabledAlgorithmConstraints;
import utils.Line;
import utils.Motors;
import utils.ObstacleFinder;
import utils.Sensors;

public class FollowLine extends Routine {

	public FollowLine(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		super(sensors, motors, colors);
	}
	
	public boolean run() {
		LCD.drawString("FollowLine here", 0, 0);
		boolean end = false;
		int tacho = 0;
		
		
		while (!end) {
			tacho = Line.followTillEnd(super.sensors, super.motors, super.colors);
			if (0 < tacho) {
				motors.largeMotorA.resetTachoCount();
				while (-motors.largeMotorA.getTachoCount() < tacho && !end) {
					Motors.motorSpeed(motors.largeMotorA, -Line.MAX_SPEED);
					Motors.motorSpeed(motors.largeMotorB, -Line.MIN_SPEED);
					end = Button.ESCAPE.isDown();
				}
				
				if (!end) {
					end = !Line.findBlack(super.sensors, super.motors, super.colors);		
				}
				
			} else if (tacho == -2) {
				/*
				LCD.clear();
				LCD.drawString("a: " + String.valueOf(-(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree)), 0, 1);
				LCD.drawString("b: " + String.valueOf((int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree)), 0, 2);
				
				Motors.motorSpeed(motors.largeMotorA, -60);
				Motors.motorSpeed(motors.largeMotorB, 60);	
				
				motors.largeMotorA.resetTachoCount();
				motors.largeMotorB.resetTachoCount();
				
				motors.largeMotorA.rotate(-(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree));
				
				Motors.motorSpeed(motors.largeMotorA, 60 * (radius * 2 + wheelDistance) / (radius * 2));
				Motors.motorSpeed(motors.largeMotorB, 60);
				
				motors.largeMotorA.resetTachoCount();
				
				motors.largeMotorA.rotate((int)(((radius * 2 + wheelDistance) * 3.14) * radiusToDegree));
				
				Motors.motorSpeed(motors.largeMotorA, -60);
				Motors.motorSpeed(motors.largeMotorB, 60);
				
				motors.largeMotorA.resetTachoCount();
				
				motors.largeMotorA.rotate(-(int)((wheelDistance * 3.14f / 4.0f) * radiusToDegree)); */
				
				if (! (ObstacleFinder.getBehind(motors) && !Line.findBlack(super.sensors, super.motors, super.colors))) {
					tacho = -1;
				}
				
				
			} else {
				end = true;
			}
			
		}
		Delay.msDelay(2000);
		LCD.clear();
		return tacho != -1;
	}
}