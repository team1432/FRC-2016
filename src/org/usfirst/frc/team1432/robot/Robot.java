
package org.usfirst.frc.team1432.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.livewindow.*;
import edu.wpi.first.wpilibj.CameraServer;
import org.usfirst.frc.team1432.robot.Encoder;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	double Val;
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	CameraServer server;
	RobotDrive drive;
	public static OI oi;
    String autonomousCommand;
    SendableChooser chooser;
    Talon driveLeft;
    Talon driveRight;
    CANTalon talon = new CANTalon(2);
    Arm arm;
    double goal;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	print("Robot Init");
    	oi = new OI();
		setupdrive();
        chooser = new SendableChooser();
        chooser.addDefault("No Drive", "No Drive");
        chooser.addObject("Drive", "Drive");
        chooser.addObject("Short Drive", "Short Drive");
        SmartDashboard.putData("Auto mode", chooser);
        server = CameraServer.getInstance();
        server.setQuality(100);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
    	leftDriveEncoder = new Encoder(RobotMap.leftWheelEncoder);
    	rightDriveEncoder = new Encoder(RobotMap.rightWheelEncoder);
    	arm = new Arm();
    	arm.start();
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit() {
    	print("disabled start");
    	arm.continueReset = false;
    	if (leftDriveEncoder != null) {
    		leftDriveEncoder.stoprun();
    		rightDriveEncoder.stoprun();
    	}
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
	/**
	 * This autonomous (along with the chooser code above) shows 	how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {
		print("Autonomous Init");
		arm.continueReset = true;
		arm.reset();
    	
		autonomousCommand = (String) chooser.getSelected();
		print(autonomousCommand);
    	leftDriveEncoder.start();
    	rightDriveEncoder.start();
    	leftDriveEncoder.reset();
    	rightDriveEncoder.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if(autonomousCommand == "Drive") {
			Val = -(rightDriveEncoder.getRotations()+leftDriveEncoder.getRotations()/5);
			drive.arcadeDrive(1, Val);
    	} else if(autonomousCommand == "Short Drive") {
			Val = -(rightDriveEncoder.getRotations()+leftDriveEncoder.getRotations()/5);
			drive.arcadeDrive(0.5, Val);
			print(Double.toString(leftDriveEncoder.getRotations()) + "---" + Double.toString(rightDriveEncoder.getRotations()));
    	} else {
			drive.arcadeDrive(0,0);
			print("driving 0");
    	}
    }

    public void teleopInit() {
    	print("Teleop Init");
    	arm.continueReset = true;
    	arm.reset();
    	drive();
    	leftDriveEncoder.stoprun();
    	rightDriveEncoder.stoprun();
    	drive();
    	oi.controller.setRumble(RumbleType.kLeftRumble, 1);
    	oi.controller.setRumble(RumbleType.kRightRumble, 1);
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	oi.controller.setRumble(RumbleType.kLeftRumble, 0);
    	oi.controller.setRumble(RumbleType.kRightRumble, 0);

    	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //if (autonomousCommand != null) autonomousCommand.cancel();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
		drive();
		print(Double.toString(arm.YGoal) + "---" + Double.toString(arm.getUpperDegrees()) + "---" + Double.toString(arm.getDistance()) + "---" + Double.toString(arm.XGoal));
		if(oi.controller.getRawButton(8)) {
			print("reset");
			arm.reset();
		}
		if(oi.controller.getRawButton(6)) {
			while(oi.controller.getRawButton(6)) {
				drive();
			}
			arm.setupPortcullis();
		}
		if(oi.controller.getRawButton(1)) {
			print("A");
			//down
			while(oi.controller.getRawButton(1)) {
				drive();
				arm.YGoal += .5;
				Timer.delay(.02);
			}
		}
		if(oi.controller.getRawButton(4)) {
			print("Y");
			//up
			while(oi.controller.getRawButton(4)) {
				drive();
				arm.YGoal -= .5;
				Timer.delay(.02);
			}
		}
		if(oi.controller.getRawButton(2)) {
			print("B");
			//out
			while(oi.controller.getRawButton(2)) {
				drive();
				arm.XGoal += .25;
				Timer.delay(.02);
			}
		}
		if(oi.controller.getRawButton(3)) {
			print("X");
			//in
			while(oi.controller.getRawButton(3)) {
				drive();
				arm.XGoal -= .25;
				Timer.delay(.02);	
			}
		}
    }
    
    public double round(double value) {
		return Math.round((value) * 100d) / 100d;
	}
	public double roundlong(double value) {
		return Math.round((value) * 10000d) / 10000d;
	}
    
    public void setupdrive() {
    	driveLeft = new Talon (RobotMap.leftWheels);
		driveRight = new Talon (RobotMap.rightWheels);
		drive = new RobotDrive(driveLeft, driveRight);
    }
    
    public void drive() {
    	//drive.arcadeDrive(0, 0);
    	drive.arcadeDrive(-oi.controller.getRawAxis(1), -oi.controller.getRawAxis(0), true);
    	//drive.tankDrive(-oi.controller.getRawAxis(1), -oi.controller.getRawAxis(5), true);
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
    
    public void testInit() {
    	
    	//leftDriveEncoder.reset();
    	//rightDriveEncoder.reset();
    	//arm = new Arm();
    }
    
    /**
     * This function is called periodically during test mode
     */

	public void testPeriodic() {
        LiveWindow.run();
	}
}