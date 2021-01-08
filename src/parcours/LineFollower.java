package parcours;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;


public class LineFollower {
	
	
	
	
	
	public static void main(String[] args) {
		Sensors sensors = new Sensors();
		Motors motors = new Motors();
		

		
		float[] sampleWhite = new float[1];		
		float[] sampleBlack = new float[1];
		
		//Util.setColors(mode, sampleBlack, sampleWhite);
		sampleWhite[0] = 88.0f;
		sampleBlack[0] = 6.0f;

		float color;
		float avgLight = (sampleWhite[0] - sampleBlack[0]) / 2;
		float maxSpeed = 120.0f;
		float minSpeed = -maxSpeed / 2.0f;
		float tolerance = 0.05f;
		float aSpeed;
		float bSpeed;
		float m = 0;
		while (!Button.ESCAPE.isDown()) {

			color = sensors.getSample(sensors.colorSensor, "Red")[0] * 100.0f;
			
			if (sampleWhite[0] * (1.0 - tolerance) <= color) {
				aSpeed = maxSpeed;
				bSpeed = minSpeed;
				
			} else if (color <= sampleBlack[0] * (1.0 + tolerance)) {
				aSpeed = minSpeed;
				bSpeed = maxSpeed;
				
			} else if (color < avgLight) {
				m = (maxSpeed - minSpeed) / (avgLight - sampleBlack[0]);
				aSpeed = m * (color - sampleBlack[0]) + minSpeed;
				bSpeed = maxSpeed;
				
			} else if (avgLight < color) {
				m = (minSpeed - maxSpeed) / (sampleWhite[0] - avgLight);
				aSpeed = maxSpeed;
				bSpeed = m * (color - avgLight) + maxSpeed;
				
			} else {
				aSpeed = maxSpeed;
				bSpeed = maxSpeed;
			}
			
			Motors.motorSpeed(motors.largeMotorA, aSpeed);
			Motors.motorSpeed(motors.largeMotorB, bSpeed);
			LCD.clear();
			LCD.drawString("m: " + String.valueOf(m), 0, 0);
			LCD.drawString("a: " + String.valueOf(aSpeed), 0, 1);
			LCD.drawString("b: " + String.valueOf(bSpeed), 0, 2);
			LCD.drawString("avgl: " + String.valueOf(avgLight), 0, 5);
			//Delay.msDelay(200);
			
		}
		sensors.close();
		motors.close();

	}
}