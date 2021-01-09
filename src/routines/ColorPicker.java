package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.Sensors;

public class ColorPicker extends Routine {

	public ColorPicker(Sensors sensors, Motors motors, HashMap<String, Float> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		String[][] colorsToGet = { 
				{"white", "Red"}, // {colorName, sensorMode},
				{"black", "Red"}
				};
		int pressedButton;
		float gotColor;
		
		for (String[] color : colorsToGet) {
			LCD.clear();
			LCD.drawString(color[0], 1, 1);
			pressedButton = -1;
			
			while (pressedButton != Button.ID_ENTER && pressedButton != Button.ID_ESCAPE) {
				pressedButton = Button.waitForAnyEvent();
			}
			if (pressedButton == Button.ID_ESCAPE) {
				continue;
			}
			
			gotColor = super.sensors.getSample(super.sensors.colorSensor, color[1])[0];
			super.colors.put(color[0], gotColor);
			LCD.drawString(String.valueOf(gotColor), 1, 2);
			Sound.twoBeeps();		
			Delay.msDelay(500);
		}
		
		return true;
	}
}
