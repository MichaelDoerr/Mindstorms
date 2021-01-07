package parcours;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Sensors {
	
	public EV3ColorSensor colorSensor;
	private SampleProvider colorSample;
	private float[] color;
	
	public EV3UltrasonicSensor ultraSonicSensor;		 
	private SampleProvider ultraSonicSample;		 
	private float[] distance;
	
	public EV3TouchSensor touchSensor;
	private SampleProvider touchSample;
	private float[] touch;
	
	public Sensors() {
		this.colorSensor = new EV3ColorSensor(SensorPort.S1);
		this.colorSample = this.colorSensor.getRGBMode();
		this.color = new float[this.colorSample.sampleSize()];
		
		this.ultraSonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		this.ultraSonicSample = this.ultraSonicSensor.getMode("Distance");
		this.distance = new float[this.ultraSonicSample.sampleSize()];
		
		this.touchSensor = new EV3TouchSensor(SensorPort.S3);
		this.touchSample = this.touchSensor.getTouchMode();
		this.touch = new float[this.touchSample.sampleSize()];
	}
	
	public float[] getSample(BaseSensor sensor, String mode) {
		float[] sample = new float[sensor.getMode(mode).sampleSize()];
		sensor.getMode(mode).fetchSample(sample, 0);
		return sample;
	}
	
	
	public float[] getSample(BaseSensor sensor) {
		if (sensor == this.colorSensor) {
			this.colorSample.fetchSample(color, 0);
			return color;
			
		} else if (sensor == this.ultraSonicSensor) {
			this.ultraSonicSample.fetchSample(distance, 0);
			return distance;
			
		} else if (sensor == this.touchSensor) {
			this.touchSample.fetchSample(touch, 0);
			return touch;
			
		} else {
			return null;
		}
	}
	
	public void close() {
		this.colorSensor.close();
		this.touchSensor.close();
		this.ultraSonicSensor.close();
	}

}
