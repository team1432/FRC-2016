/**
 * 
 */
package org.usfirst.frc.team1432.robot;

import java.util.concurrent.locks.ReentrantLock;

import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team1432.robot.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 * @author team 1432
 *
 */
public class Arm extends Thread {
    CANTalon lowerArm;
    CANTalon upperArm;
    Encoder lowerEncoder;
    Encoder upperEncoder;
    double distance;
    double lowerLength = RobotMap.lowerArmLength;
    double upperLength = RobotMap.upperArmLength;
    double lowerAngle;
    double upperAngle;
    double lowerMultiplier = 90;
    double upperMultiplier = 112.5;
    DigitalInput lowerArmResetButton;
    DigitalInput upperArmResetButton;
	private Thread thread; 
	private Boolean cont;
	private ReentrantLock lock;
	private double goal;
	
    public Arm() {
    	lowerEncoder = new Encoder(RobotMap.lowerArmEncoder);
    	upperEncoder = new Encoder(RobotMap.upperArmEncoder);
    	lowerArm = new CANTalon (RobotMap.lowerArmMotor);
    	upperArm = new CANTalon (RobotMap.upperArmMotor);
    	lowerArmResetButton = new DigitalInput(RobotMap.lowerArmButton);
    	upperArmResetButton = new DigitalInput(RobotMap.upperArmButton);
    	lowerEncoder.start();
    	upperEncoder.start();
    	lock = new ReentrantLock();
    	reset();
    }
    
	@Override
	public void run() {
		Boolean running = cont;
		//keep position
		while(running) {
			lock.lock();
			upperArm.set(-(goal - getUpperDegrees())/100);
			SafeDistance();
			lock.unlock();
		}
	}
	
	public void start() {
		if(thread == null) {
			lock.lock();
			cont=true;
			thread = new Thread(this);
			thread.start();
			lock.unlock();
		}
	}
	public void stoprun() {
		lock.lock();
		cont = false;
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
    
    public void set(double speed){
    	if (speed < -1){
    		speed = -1;
    	}
    	if (speed > 1){
    		speed = 1;
    	}
    	upperArm.set(speed);
    	SafeDistance();
    }
    public double getLowerDegrees(){
    	return lowerEncoder.getRotations()/**lowerMultiplier*/;
    }

    public double getUpperDegrees(){
    	return upperEncoder.getRotations()/**upperMultiplier*/;
    }
    
    public double getLowerAngle() {
		return Math.toRadians(lowerEncoder.getRotations()*lowerMultiplier);
    }
    public double getUpperAngle() {
    	return Math.toRadians(upperEncoder.getRotations()*upperMultiplier);
    }
    public void setPosition(double position){
    	goal = position;
	}
    public void reset() {
    	upperEncoder.reset();
    	lowerEncoder.reset();
    	goal = getUpperDegrees();
    	/*
    	while(!lowerArmResetButton.get()) {
    		lowerArm.set(-1);
    	}
    	if(lowerArmResetButton.get()) {
    		lowerArm.set(0);
    		lowerEncoder.reset();
    	}
    	while(!upperArmResetButton.get()) {
    		upperArm.set(1);
    	}
    	if(upperArmResetButton.get()) {
    		upperArm.set(0);
    		upperEncoder.reset();
    	}*/
    }
    public double getDistance(){
    	return round(lowerLength*Math.cos(getLowerAngle()))+(upperLength*Math.cos(180-(getLowerAngle()+getUpperAngle())));
    }
    public void SafeDistance() {
		distance = (lowerLength*Math.cos(getLowerAngle()))+(upperLength*Math.cos(180-(getLowerAngle()+getUpperAngle())));
		if (distance > 14){
			lowerArm.set(-.1);
		}
		if (distance < 13) {
			lowerArm.set(.1);
		}
		lowerArm.set(0);    	
	}
}