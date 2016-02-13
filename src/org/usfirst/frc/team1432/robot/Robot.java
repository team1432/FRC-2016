
package org.usfirst.frc.team1432.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        server.startAutomaticCapture("cam1");
    	arm = new Arm();
    	arm.start();
    	leftDriveEncoder = new Encoder(RobotMap.leftWheelEncoder);
    	rightDriveEncoder = new Encoder(RobotMap.rightWheelEncoder);
    	leftDriveEncoder.start();
    	rightDriveEncoder.start();
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
    	arm.reset();
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
		autonomousCommand = (String) chooser.getSelected();
		print(autonomousCommand);
    	leftDriveEncoder.reset();
    	rightDriveEncoder.reset();
		switch(autonomousCommand) {
		case "Drive":
			while(isAutonomous() && isEnabled()) {
				drive.arcadeDrive(0.5, (leftDriveEncoder.getRotations() + rightDriveEncoder.getRotations())/-2);
				print((leftDriveEncoder.getRotations() + rightDriveEncoder.getRotations())/10);
			}
			break;
		case "Short Drive":
			while(isAutonomous() && isEnabled()) {
				drive.arcadeDrive(0.5, 0);
			}
			break;
		case "No Drive":
		default:
			while(isAutonomous() && isEnabled()) {
				drive.arcadeDrive(0, 0);
			}
			break;
		}
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	leftDriveEncoder.stoprun();
    	rightDriveEncoder.stoprun();
    	print("Started Teleop");
    	drive();
    	oi.controller.setRumble(RumbleType.kLeftRumble, 1);
    	oi.controller.setRumble(RumbleType.kRightRumble, 1);
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	Timer.delay(0.05);
    	drive();
    	oi.controller.setRumble(RumbleType.kLeftRumble, 0);
    	oi.controller.setRumble(RumbleType.kRightRumble, 0);
    	while(isEnabled() && isOperatorControl()){
    		print(leftDriveEncoder.getRotations());
    		drive();
    		if(oi.controller.getRawButton(1)) {
    			while(oi.controller.getRawButton(1)) {
    				drive();
    				goal -=1;
    				Timer.delay(.02);
    				arm.setPosition(goal);
    			}
    		}
    		if(oi.controller.getRawButton(4)) {
    			while(oi.controller.getRawButton(4)) {
    				drive();
    				goal +=1;
    				Timer.delay(.02);
    				arm.setPosition(goal);
    			}
    		} 
    		//Scheduler.getInstance().run();
    		//LiveWindow.run();
		}

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
    	drive.arcadeDrive(-oi.controller.getRawAxis(1), -oi.controller.getRawAxis(0), true);
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
    
    public void testInit() {
    	leftDriveEncoder.reset();
    	rightDriveEncoder.reset();
    }
    
    /**
     * This function is called periodically during test mode
     */

	public void testPeriodic() {
        //LiveWindow.run();
    	print(leftDriveEncoder.getRotations());
	}
}