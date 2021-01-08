package parcours;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class Util {
	private Util() {
	}
	
	public static void setColors(SensorMode mode, float[] sampleBlack, float[] sampleWhite) {
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

}
