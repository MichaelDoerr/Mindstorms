import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
public class HelloEV3 {
	
	public static void main(String[] args) {
		boolean end = false;
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
		SampleProvider colorSample = colorSensor.getRGBMode();
		float[] color = new float[colorSample.sampleSize()];
		
		EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
		SampleProvider touchSample = touchSensor.getTouchMode();
		float[] touch = new float[touchSample.sampleSize()];
		
		EV3UltrasonicSensor ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S3);		 
		SampleProvider ultraSonicSample = ultraSonicSensor.getMode("Distance");		 
		float[] distance = new float[ultraSonicSample.sampleSize()];

		while (!end) {
			touchSample.fetchSample(touch, 0);
			colorSample.fetchSample(color, 0);
			ultraSonicSample.fetchSample(distance, 0);
			
			System.out.println("touch is: " + touch[0]);
			System.out.println("color: \n" 
					+ (String.valueOf(color[0]) + "    ").substring(0, 4) + "| " 
					+ (String.valueOf(color[1]) + "    ").substring(0, 4) + "| " 
					+ (String.valueOf(color[2]) + "    ").substring(0, 4));
			System.out.println("");
			System.out.println("distance is: \n" + (String.valueOf(distance[0]) + "        ").substring(0, 8));
			System.out.println("");
			
			
			
			if (Button.ESCAPE.isDown()) {
				end = true;
			}
			Delay.msDelay(200);
		}
		
		touchSensor.close();
		colorSensor.close();
		ultraSonicSensor.close();
	}
}