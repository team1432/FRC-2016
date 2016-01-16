
package org.usfirst.frc.team1432.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1432.robot.*;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team1432.robot.commands.*;

/**
 *
 */
public class arm extends Subsystem {
    Talon lowerarm = new Talon (RobotMap.joint1motor);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void up(){
    	lowerarm.set(1);
    }
    public void down() {
    	lowerarm.set(-1);
    }
    public void off(){
    	lowerarm.set(0);
    }
}

