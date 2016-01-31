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
	double ago1;
	double rotations;
	double resetValue;
	public static double degreesPerRotation = 360;
	public static double inchesPerRotation = 0.8853826956614173;
	public static double centemetersPerRotation = 2.24887204698;
	private ReentrantLock lock;
	private Thread thread; 
	private Boolean cont;
	
	
	public Encoder(int port) {
		cont = false;
		input = new AnalogInput(port);
		current = 0;
		ago1 = 0;
		rotations = 0;
		lock = new ReentrantLock();
	}
		
	public void reset(){
		lock.lock();
		resetValue = input.getAverageVoltage()/5;
		rotations = 0;
		current = 0;
		ago1 = 0;
		lock.unlock();
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
		double value = current;
		lock.unlock();
		return value;
	}
    
    public double getRotations(){
    	lock.lock();
    	double value =  /*round*/(rotations + current);
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
			lock.lock();
			ago1 = current;
			current = (input.getAverageVoltage()/5-resetValue);
			//clockwise
			if (current - ago1 < -.5) {
				rotations++;
			}
			if (current - ago1 > .5) {
				rotations--;
			}
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