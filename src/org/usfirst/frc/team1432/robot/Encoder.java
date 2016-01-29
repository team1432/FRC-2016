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
	private double[] numbers;
	public static double degreesPerRotation = 360;
	public static double inchesPerRotation = 0.8853826956614173;
	public static double centemetersPerRotation = 2.24887204698;
	CANTalon sensorMotor;
	private ReentrantLock lock;
	private Thread thread; 
	private Boolean cont;
	
	
	public Encoder(int port, CANTalon motor) {
		cont = false;
		input = new AnalogInput(port);
		current = 0;
		previous = 0;
		rotations = 0;
		sensorMotor = motor;
		lock = new ReentrantLock();
		numbers = new double[10];
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
    
	/*
	 * Gets double value from input 
	 */
	public double getValue(){
		lock.lock();
		double value = round(input.getVoltage()/5);
		lock.unlock();
		return value;
	}
    
    public double getRotations(){
    	lock.lock();
    	double value =  round(rotations + current);
    	lock.unlock();
    	return value;
    }
	public double getDegrees(){
		lock.lock();
		double value = round((current)*degreesPerRotation);
		lock.unlock();
		return value;
	}
	public double getCM(){
		lock.lock();
		double value = round((current+rotations)*centemetersPerRotation);
		lock.unlock();
		return value;
	}
	public double getInches(){
		lock.lock();
		double value = round((current+rotations)*inchesPerRotation);
		lock.unlock();
		return value;
	}
    
	@Override
	public void run() {
		Boolean running = cont;
		while(running) {
			if (sensorMotor != null){
				lock.lock();
				previous = current;
				for (int x = 9; x > 0; x--){
					numbers[x]= numbers[x-1];
				}
				numbers[0] = roundlong(input.getVoltage()/5);
				current = (numbers[0] + numbers[1] + numbers[2] + numbers[3] + numbers[4] + numbers[5] + numbers[6] + numbers[7] + numbers[8] + numbers[9])/10;
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