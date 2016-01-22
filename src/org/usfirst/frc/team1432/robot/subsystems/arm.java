
package org.usfirst.frc.team1432.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class arm extends Subsystem {
    public Talon lowerarm = new Talon (RobotMap.joint1motor);
    public Talon upperarm = new Talon (RobotMap.joint2motor);
    double D;
    int arm1;
    int arm2;
    double angle1;
    int angle2;
    double portcullisAngle = 5;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setYVelocity(double velocity) {
    	lowerarm.set(velocity);
    	SafeDistance();
    }
    public void SafeDistance() {
		D = (arm1*Math.cos(angle1))+(arm2*Math.cos(180-(angle1+angle2)));
		while (D > 14){
			lowerarm.set(-.1);
		}
		while (D < 13) {
			lowerarm.set(.1);
		}
		lowerarm.set(0);    	
	}
    public void portcullis(){
    	while (angle1 < portcullisAngle){
    		setYVelocity(1);
    	}
    	setYVelocity(0);
	}
}