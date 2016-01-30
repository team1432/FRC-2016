/**
 * 
 */
package org.usfirst.frc.team1432.robot;

import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team1432.robot.Encoder;

/**
 * @author team 1432
 *
 */
public class Arm {
/*    CANTalon lowerArm = new CANTalon (RobotMap.lowerArmMotor);
    CANTalon upperArm = new CANTalon (RobotMap.upperArmMotor);
    Encoder lowerEncoder;
    Encoder upperEncoder;
    double D;
    double lowerLength = 1;
    double upperLength = 2;
    
    public Arm() {
    	lowerEncoder = new Encoder(RobotMap.lowerArmEncoder, lowerArm);
    	upperEncoder = new Encoder(RobotMap.upperArmEncoder, upperArm);
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
    
    public void setPosition(double position){
    	while (true) {
    		if (upperEncoder.getValue()> position){
    			System.out.println("positive");
    			set(.3);
    		}
    		if (upperEncoder.getValue()< position){
    			System.out.println("negative");
    			set(-.3);
    		}
			SafeDistance();
			System.out.println("upper:");
	    	System.out.println(upperEncoder.getValue());
	    	System.out.println("lower:");
	    	System.out.println(lowerEncoder.getValue());
    	}
    	
    }
    
    public double getDistance(){
    	return round(lowerLength*Math.cos(lowerEncoder.getValue()))+(upperLength*Math.cos(180-(lowerEncoder.getValue()+upperEncoder.getValue())));
    }
    public void SafeDistance() {
		D = (lowerLength*Math.cos(lowerEncoder.getValue()))+(upperLength*Math.cos(180-(lowerEncoder.getValue()+upperEncoder.getValue())));
		while (D > 14){
			lowerArm.set(-.1);
		}
		while (D < 13) {
			lowerArm.set(.1);
		}
		lowerArm.set(0);    	
	}*/
}