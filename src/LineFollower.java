import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;


public class LineFollower {
	
	public static void motorSpeed(EV3LargeRegulatedMotor motor, float speed) {		
		if (0 <= speed) {
			motor.setSpeed(new Float(speed).intValue());
			motor.forward();
		} else {
			motor.setSpeed(new Float(-speed).intValue());
			motor.backward();
		}
	}
	
	public static void setBlackWhite(SensorMode mode, float[] sampleBlack, float[] sampleWhite) {
		Sound.twoBeeps();
		LCD.drawString("white", 0, 0);
		Button.ESCAPE.waitForPressAndRelease();
		mode.fetchSample(sampleWhite, 0);
		LCD.drawInt(new Float(sampleWhite[0] * 100).intValue(), 1, 1);
		sampleWhite[0] *= 100.0f;
		
		Delay.msDelay(500);
		
		Sound.twoBeeps();
		LCD.drawString("Black", 2, 2);
		Button.ESCAPE.waitForPressAndRelease();
		mode.fetchSample(sampleBlack, 0);
		LCD.drawInt(new Float(sampleBlack[0] * 100).intValue(), 3, 3);
		sampleBlack[0] *= 100.0f;
		Delay.msDelay(1000);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		SensorMode mode = colorSensor.getRedMode();

		
		float[] sampleWhite = new float[1];		
		float[] sampleBlack = new float[1];
		
		//setBlackWhite(mode, sampleBlack, sampleWhite);
		sampleWhite[0] = 88.0f;
		sampleBlack[0] = 6.0f;
		
		int defaultPower = 20;
		int multiplyingFactor = 50;

		EV3LargeRegulatedMotor largeMotorB = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor largeMotorC = new EV3LargeRegulatedMotor(MotorPort.C);
		float[] colorGet = new float[1];
		float color;
		float avgLight = (sampleWhite[0] - sampleBlack[0]) / 2;
		float maxSpeed = 120.0f;
		float minSpeed = -maxSpeed / 2.0f;
		float tolerance = 0.05f;
		float cSpeed;
		float bSpeed;
		float m = 0;
		while (!Button.ESCAPE.isDown()) {

			mode.fetchSample(colorGet, 0);
			color = colorGet[0] * 100.0f;
			
			if (sampleWhite[0] * (1.0 - tolerance) <= color) {
				cSpeed = minSpeed;
				bSpeed = maxSpeed;
				
			} else if (color <= sampleBlack[0] * (1.0 + tolerance)) {
				cSpeed = maxSpeed;
				bSpeed = minSpeed;
				
			} else if (color < avgLight) {
				m = (maxSpeed - minSpeed) / (avgLight - sampleBlack[0]);
				cSpeed = maxSpeed;
				bSpeed = m * (color - sampleBlack[0]) + minSpeed;
				
			} else if (avgLight < color) {
				m = (minSpeed - maxSpeed) / (sampleWhite[0] - avgLight);
				bSpeed = maxSpeed;
				cSpeed = m * (color - avgLight) + maxSpeed;
				
			} else {
				bSpeed = maxSpeed;
				cSpeed = maxSpeed;
			}
			
			motorSpeed(largeMotorC, cSpeed);
			motorSpeed(largeMotorB, bSpeed);
			LCD.clear();
			LCD.drawString("m: " + String.valueOf(m), 0, 0);
			LCD.drawString("c: " + String.valueOf(bSpeed), 0, 1);
			LCD.drawString("b: " + String.valueOf(cSpeed), 0, 2);
			LCD.drawString("minb: " + String.valueOf(m * (sampleBlack[0] - avgLight) + maxSpeed), 0, 3);
			LCD.drawString("maxb: " + String.valueOf(m * (0 - avgLight) + maxSpeed), 0, 4);
			LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
			LCD.drawString("mxspeed: " + String.valueOf(sampleWhite[0]), 0, 6);
			LCD.drawString("mxspeed: " + String.valueOf(sampleBlack[0]), 0, 7);
			//Delay.msDelay(200);
			
		}

	}
}