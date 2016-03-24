/**
 * 
 */
package org.usfirst.frc.team1432.robot;

import java.util.concurrent.locks.ReentrantLock;
import edu.wpi.first.wpilibj.IterativeRobot;
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
    double upperMultiplier = 111;
    DigitalInput lowerArmResetButton;
    DigitalInput upperArmResetButton;
	private Thread thread; 
	private Boolean cont;
	private ReentrantLock lock;
	public double YGoal;
	public double XGoal;
	public boolean continueReset = false;
	double encoderValue;
	public boolean canMove;
	double originalLowerArmSpeed;
	double lowerArmSpeed = 15;
	public static OI _oi;
    private long startTime;
    private static long resetLimitMillis = 4000;
	
    public Arm(OI oi) {
    	lowerEncoder = new Encoder(RobotMap.lowerArmEncoder);
    	upperEncoder = new Encoder(RobotMap.upperArmEncoder);
    	lowerArm = new CANTalon (RobotMap.lowerArmMotor);
    	upperArm = new CANTalon (RobotMap.upperArmMotor);
    	lowerArm.set(0);
    	upperArm.set(0);
    	lowerArmResetButton = new DigitalInput(RobotMap.lowerArmButton);
    	upperArmResetButton = new DigitalInput(RobotMap.upperArmButton);
    	lock = new ReentrantLock();
    	_oi = oi;
    }
	@Override
	public void run() {
		Boolean running = cont;
		//keep position
		while(running) {
			lock.lock();
			if(canMove) {
				if (XGoal > 14) {
					XGoal = 14;
				}
				/*
				if (XGoal < 6) {
					XGoal = 6;
				}*/
				if (!lowerArmResetButton.get() && XGoal - getDistance() < 0) {
					lowerArm.set(0);
				} else {
					upperArm.set((YGoal - getUpperDegrees())/60);
					lowerArm.set(-(XGoal - getDistance())/lowerArmSpeed);
				}
			}
			running = cont;
			lock.unlock();
		}
	}
	
	public void start() {
		print("Arm Starting");
		if(lowerEncoder != null) {
	    	lowerEncoder.start();
	    	upperEncoder.start();			
		}
		if(thread == null) {
			lock.lock();
			cont=true;
			thread = new Thread(this);
			thread.start();
			lock.unlock();
		} else {
			lock.lock();
			cont=false;
			Timer.delay(.01);
			thread = null;
			cont=true;
			thread = new Thread(this);
			thread.start();
			lock.unlock();
		}
		print("Finished starting arm.");
	}
	public void stoprun() {
		killEncoders();
		lock.lock();
		thread = null;
		cont = false;
		lock.unlock();
	}

    public double round(double value) {
		return Math.round((value) * 100d) / 100d;
	}
	public double roundlong(double value) {
		return Math.round((value) * 10000d) / 10000d;
	}
	
	public void print(String string) {
    	System.out.println(string);
    }
    
    public void print(double Double) {
    	System.out.println(round(Double));
    }
    
    public void print(int Int) {
    	System.out.println(round(Int));
    }
    public boolean isAtXGoal() {
    	if(getDistance() > XGoal - 1.5 && getDistance() < XGoal + 1.5) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    public boolean isAtYGoal() {
    	if(getUpperDegrees() > YGoal - 5 && getUpperDegrees() < YGoal + 5) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    public void setupPortcullis() {
    	reset();
    	originalLowerArmSpeed = lowerArmSpeed;
    	lowerArmSpeed = 5;
    	XGoal = 10;
    	while (!isAtXGoal() && continueReset) {
    		print("not at X goal 1 yet");
    		Timer.delay(.02);
    	}
    	print("Got to the position");
    	YGoal = 130;
    	while (!isAtYGoal() && continueReset) {
    		print("not at Y goal yet");
    		Timer.delay(.02);
    	}
    	print("Got to the position");
    	XGoal = 4.4;
    	while (!isAtXGoal() && continueReset) {
    		print("not at X goal 2 yet");
    		Timer.delay(.02);
    	}
    	print("Got to the position");
    	lowerArmSpeed = originalLowerArmSpeed;
    }
    public void set(double speed) {
    	if (speed < -1) {
    		speed = -1;
    	}
    	if (speed > 1) {
    		speed = 1;
    	}
    	upperArm.set(speed);
    	SafeDistance();
    }
    public double getLowerDegrees() {
    	return (lowerEncoder.getRotations())*lowerMultiplier-85;
    }

    public double getUpperDegrees() {
    	return (-upperEncoder.getRotations())*upperMultiplier+96;
    }
    public double getUpperRotations() {
    	return (upperEncoder.getRotations());
    }
        
    public double getLowerAngle() {
		return Math.toRadians((lowerEncoder.getRotations())*lowerMultiplier-85);
    }
    public double getUpperAngle() {
    	return Math.toRadians((-upperEncoder.getRotations())*upperMultiplier+96);
    }
    public boolean reset() {
    	canMove = false;
    	startTime = System.currentTimeMillis();
    	if(continueReset) {
       		print("Arm is going in reverse");
    		upperArm.set(-.5);
    		Timer.delay(.5);
    	}
    	upperArm.set(0);
    	while(lowerArmResetButton.get() && continueReset && !_oi.controller.getRawButton(7)) {
    		if(System.currentTimeMillis() - startTime < resetLimitMillis) lowerArm.set(.5);
    		else return false;
    	}
    	if(!lowerArmResetButton.get() && continueReset) {
    		lowerArm.set(0);
    		lowerEncoder.reset();
    	}
    	while(upperArmResetButton.get() && continueReset && !_oi.controller.getRawButton(7)) {
    		if(System.currentTimeMillis() - startTime < resetLimitMillis) upperArm.set(.2);
    		else return false;
    	}
    	if(!upperArmResetButton.get() && continueReset) {
    		upperArm.set(0);
    		upperEncoder.reset();
    	}
    	YGoal = getUpperDegrees();
    	XGoal = getDistance();
    	print("Finished Reset");
    	canMove = true;
    	return true;
    }
    public double getLowerDistance() {
		return round(lowerLength*Math.sin(getLowerAngle()));
    }
    public double getUpperDistance() {
    	return round(upperLength*Math.sin(getUpperAngle()));
    }
    public double getDistance() {
    	return round(lowerLength*Math.sin(getLowerAngle()))+(upperLength*Math.sin(getUpperAngle())) - 3;
    }
    public void SafeDistance() {
		distance = (lowerLength*Math.sin(getLowerAngle()))+(upperLength*Math.sin(getUpperAngle())) - 3;
		if (distance > 14) {
			lowerArm.set(.5);
		}
		if (distance < 13) {
			lowerArm.set(-.5);
		} else {
			lowerArm.set(0);
		}
		Timer.delay(0.1);
	}
    public void killEncoders() {
    	if (upperEncoder != null) {
    		upperEncoder.stoprun();
    		lowerEncoder.stoprun();
    	}
    }
}