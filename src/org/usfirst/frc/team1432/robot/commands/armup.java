
package org.usfirst.frc.team1432.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1432.robot.*;

/**
 *
 */
public class armup extends Command {

    public armup() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.Arm);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.Arm.up();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.Arm.down();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//Timer.delay(2);
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.Arm.off();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
