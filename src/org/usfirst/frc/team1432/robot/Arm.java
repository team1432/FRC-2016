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
    Talon lowerarm = new Talon (RobotMap.lowerJointMotor);
    Talon upperarm = new Talon (RobotMap.upperJointMotor);
    Encoder lowerEncoder;
    Encoder upperEncoder;
    double D;
    double lowerLength;
    double upperLength;
    
    public Arm(Talon joint1, Talon joint2, Encoder encoder1, Encoder encoder2) {
    	lowerEncoder = encoder1;
    	upperEncoder = encoder2;
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

    
    public double getDistance(){
    	return round(lowerLength*Math.cos(lowerEncoder.getDegrees()))+(upperLength*Math.cos(180-(lowerEncoder.getDegrees()+upperEncoder.getDegrees())));
    }
    public void SafeDistance() {
		D = (lowerLength*Math.cos(lowerEncoder.getDegrees()))+(upperLength*Math.cos(180-(lowerEncoder.getDegrees()+upperEncoder.getDegrees())));
		while (D > 14){
			lowerarm.set(-.1);
		}
		while (D < 13) {
			lowerarm.set(.1);
		}
		lowerarm.set(0);    	
	}
}
