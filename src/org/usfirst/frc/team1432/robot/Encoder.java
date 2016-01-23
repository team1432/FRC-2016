/**
 * 
 */
package org.usfirst.frc.team1432.robot;
import edu.wpi.first.wpilibj.*;

import java.util.concurrent.locks.*;

/**
 * @author team 1432
 *
 */
public class Encoder extends Thread {
	AnalogInput input;
	double current;
	double previous;
	double rotations;
	double currentAngle;
	public static double degreesPerRotation = 360;
	public static double inchesPerRotation = 0.8853826956614173;
	public static double centemetersPerRotation = 2.24887204698;
	Talon sensorMotor;
	private ReentrantLock lock;
	private Thread thread; 
	private Boolean cont;
	
	public Encoder(int port, Talon motor) {
		cont = false;
		input = new AnalogInput(port);
		current = 0;
		previous = 0;
		rotations = 0;
		sensorMotor = motor;
		lock = new ReentrantLock();
	}
	
	/*
	 * Gets double value from input 
	 */
	public double getValue(){
		lock.lock();
		double value = round(input.getVoltage()/5);
		lock.unlock();
		return value;
	}
	
	public void reset(){
		rotations = 0;
		current = 0;
		previous = 0;
	}
	
	public double round(double value){
		return Math.round((value) * 100d) / 100d;
	}
	public double roundlong(double value){
		return Math.round((value) * 10000d) / 10000d;
	}
	
	public void print(String string) {
    	System.out.println(string);
    }
    
    public void print(double Double){
    	System.out.println(round(Double));
    }
    
    public void print(int Int) {
    	System.out.println(round(Int));
    }
    
    public double getRotations(){
    	return round(rotations + current);
    }
	public double getDegrees(){
		return round((current)*degreesPerRotation);
	}
	public double getCM(){
		return round((current+rotations)*centemetersPerRotation);
	}
	public double getInches(){
		return round((current+rotations)*inchesPerRotation);
	}
    
	@Override
	public void run() {
		Boolean running = cont;
		while(running) {
			if (sensorMotor != null){
				previous = current;
				lock.lock();
				current = roundlong(input.getVoltage()/5);
				lock.unlock();
				if (sensorMotor.get() < 0 &&  current - previous < -0.5) {
					rotations ++;
				}
				else if (sensorMotor.get() > 0 && current - previous > 0.5){
					rotations --;
				}
			}
			lock.lock();
			running = cont;
			lock.unlock();
		}
	}
	
	public void start() {
		if(thread == null) {
			cont=true;
			thread = new Thread(this);
			thread.start();
		}
	}
	public void stoprun() {
		// TODO Auto-generated method stub
		lock.lock();
		cont = false;
		lock.unlock();
	}
}