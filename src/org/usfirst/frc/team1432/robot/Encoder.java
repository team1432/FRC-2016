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
	double fractionRotations;
	double currentAngle;
	public static double degreesPerRotation = 1;
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
	public double getvalue(){
		lock.lock();
		double value = input.getVoltage();
		lock.unlock();
		return value;
	}
	
	public void reset(){
		rotations = 0;
		current = 0;
		previous = 0;
	}
	
	private void updateAngle(){
		double decimal;
		decimal = current / 5;
		lock.lock();
		currentAngle = (rotations + decimal)*degreesPerRotation;
		lock.unlock();
	}
	
	public void print(String string) {
    	System.out.println(string);
    }
    
    public void print(double Double){
    	System.out.println(Double);
    }
    
    public void print(int Int) {
    	System.out.println(Int);
    }
    
    public double getRotations(){
    	return rotations;
    }
    
	@Override
	public void run() {
		Boolean running = cont;
		while(running) {
			if (sensorMotor != null){
				previous = current;
				lock.lock();
				current = input.getVoltage();
				lock.unlock();
				if (sensorMotor.get() < 0 &&  current - previous < 0) {
					rotations ++;
				}
				else if (sensorMotor.get() > 0 && current - previous > 0){
					rotations --;
				}
				updateAngle();
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
