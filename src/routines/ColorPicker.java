package routines;

import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import utils.Motors;
import utils.Sensors;

public class ColorPicker extends Routine {

	public ColorPicker(Sensors sensors, Motors motors, HashMap<String, Float[]> colors) {
		super(sensors, motors, colors);
	}

	public boolean run() {
		String[][] colorsToGet = { 
				{"white"}, // {colorName, sensorMode},
				{"black"}
				};
		int pressedButton;
		float[] gotColor;
		Float[] gotColorFormat;
		
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
			
			gotColor = super.sensors.getSample(super.sensors.colorSensor);
			gotColorFormat = new Float[gotColor.length];
			
			for (int i = 0; i < gotColor.length; i++) {
				gotColorFormat[i] = new Float(gotColor[i]);
			}
			
			super.colors.put(color[0], gotColorFormat);
			LCD.drawString(String.valueOf(gotColor[0]), 1, 2);
			LCD.drawString(String.valueOf(gotColor[1]), 1, 3);
			LCD.drawString(String.valueOf(gotColor[2]), 1, 4);
			Sound.twoBeeps();		
			Delay.msDelay(500);
		}
		
		return true;
	}
	
	public static float getGreyMode(Float[] colors) {
		return (colors[0] + colors[1] + colors[2]);
	}
	public static float getGreyMode(float[] colors) {
		return (colors[0] + colors[1] + colors[2]);
	}

	public static boolean sameColor(float[] colorSample, Float[] baseColor) {
		float tolerance = 0.1f;
		boolean isSame = true;
		
		for (int i = 0; i < colorSample.length; i++) {
			if (!(baseColor[i] * (1 - tolerance) <= colorSample[i] && colorSample[i] <= baseColor[i] * (1 + tolerance))) {
				isSame = false;
				break;
			}
		}
		
		return isSame;
	}
}
