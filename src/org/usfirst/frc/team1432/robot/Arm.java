/**
 * 
 */
package org.usfirst.frc.team1432.robot;

import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team1432.robot.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * @author team 1432
 *
 */
public class Arm {
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
    
    public Arm() {
    	lowerEncoder = new Encoder(RobotMap.lowerArmEncoder);
    	upperEncoder = new Encoder(RobotMap.upperArmEncoder);
    	lowerArm = new CANTalon (RobotMap.lowerArmMotor);
    	upperArm = new CANTalon (RobotMap.upperArmMotor);
    	lowerArmResetButton = new DigitalInput(RobotMap.lowerArmButton);
    	upperArmResetButton = new DigitalInput(RobotMap.upperArmButton);
    	lowerEncoder.start();
    	upperEncoder.start();
    }
    
	public double round(double value){
		return Math.round((value) * 100d) / 100d;
	}
	public double roundlongs(double value){
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
    private double getLowerAngle() {
		return Math.toRadians(lowerEncoder.getRotations()*lowerMultiplier);
    }
    private double getUpperAngle() {
    	return Math.toRadians(upperEncoder.getRotations()*upperMultiplier);
    }
    public void setPosition(double position){
    	while (true) {
    		if (getUpperAngle()> position){
    			System.out.println("positive");
    			set(.3);
    		}
    		if (getUpperAngle()< position){
    			System.out.println("negative");
    			set(-.3);
    		}
			SafeDistance();
    	}
    	
    }
    public void reset() {
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
    	}
    }
    public double getDistance(){
    	return round(lowerLength*Math.cos(getLowerAngle()))+(upperLength*Math.cos(180-(getLowerAngle()+getUpperAngle())));
    }
    public void SafeDistance() {
		distance = (lowerLength*Math.cos(getLowerAngle()))+(upperLength*Math.cos(180-(getLowerAngle()+getUpperAngle())));
		while (distance > 14){
			lowerArm.set(-.1);
		}
		while (distance < 13) {
			lowerArm.set(.1);
		}
		lowerArm.set(0);    	
	}
}