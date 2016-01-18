
package org.usfirst.frc.team1432.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class arm extends Subsystem {
    Talon lowerarm = new Talon (RobotMap.joint1motor);
    double D;
    int arm1;
    int arm2;
    double angle1;
    int angle2;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setYVelocity(){
    	D = (arm1*Math.cos(angle1))+(arm2*Math.cos(180-(angle1+angle2)));
    }
    
    public void setYPosition(){
    	
    }
}

