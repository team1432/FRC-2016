
package org.usfirst.frc.team1432.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Encoder lowerArmEncoder;
	Encoder upperArmEncoder;
	CameraServer server;
	RobotDrive drive;
	public static OI oi;
    Command autonomousCommand;
    SendableChooser chooser;
    Talon driveLeft;
    Talon driveRight;
    Talon lowerJoint;
    Talon upperJoint;
    Arm arm;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {    	
    	oi = new OI();
		setupdrive();
        chooser = new SendableChooser();
        //chooser.addDefault("Default Auto", new arm());
        //chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        server = CameraServer.getInstance();
        server.setQuality(100);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
    	lowerArmEncoder = new Encoder(0, driveLeft);
    	upperArmEncoder = new Encoder(1, driveRight);
    	lowerArmEncoder.start();
    	upperArmEncoder.start();
    	lowerJoint = new Talon(RobotMap.lowerJointMotor);
    	upperJoint = new Talon(RobotMap.upperJointMotor);
    	arm = new Arm(lowerJoint, upperJoint, lowerArmEncoder, upperArmEncoder);
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
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
        autonomousCommand = (Command) chooser.getSelected();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command ()
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
    	print("Started Teleop");
    	upperArmEncoder.reset();
    	lowerArmEncoder.reset();
    	print(arm.getDistance());
    	//Arm.lowerarm.set(-.05);
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
        Scheduler.getInstance().run();
        LiveWindow.run();
		SmartDashboard.putNumber("Lower Arm", lowerArmEncoder.getDegrees());
		SmartDashboard.putNumber("Upper Arm", upperArmEncoder.getDegrees());
    }
    
    public double round(double value){
		return Math.round((value) * 100d) / 100d;
	}
	public double roundlong(double value){
		return Math.round((value) * 10000d) / 10000d;
	}
    
    public void setupdrive() {
    	driveLeft = new Talon (RobotMap.leftWheels);
		driveRight = new Talon (RobotMap.rightWheels);
		drive = new RobotDrive(driveLeft, driveRight);
    }
    
    public void drive() {
    	drive.arcadeDrive(oi.controller, true);
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

    /**
     * This function is called periodically during test mode
     */

	public void testPeriodic() {
        LiveWindow.run();
    }
}