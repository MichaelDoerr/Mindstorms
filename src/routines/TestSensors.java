package routines;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Sensors;
public class TestSensors {
	
	public static void main(String[] args) {
		Sensors sensors = new Sensors();

		while (!Button.ESCAPE.isDown()) {
			float [] touchR = sensors.getSample(sensors.touchSensor);
			float [] color = sensors.getSample(sensors.colorSensor);
			float [] distance = sensors.getSample(sensors.ultraSonicSensor);			
			
			LCD.clear();
			LCD.drawString("touch is: " + touchR[0], 0, 0);
			LCD.drawString("color:", 0, 1);
			LCD.drawString((String.valueOf(color[0]) + "    ").substring(0, 4) + "| " 
						 + (String.valueOf(color[1]) + "    ").substring(0, 4) + "| " 
						 + (String.valueOf(color[2]) + "    ").substring(0, 4)
					, 0, 2);
			LCD.drawString("distance is: \n" + (String.valueOf(distance[0]) + "        ").substring(0, 8), 0, 4);
			
			
			Delay.msDelay(200);
		}
		
		sensors.close();
	}
}