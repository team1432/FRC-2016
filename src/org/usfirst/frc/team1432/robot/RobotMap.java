package org.usfirst.frc.team1432.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    
	//////////////// PWM ////////////////
    public static int leftWheels = 0;
    public static int rightWheels = 1;
    
    ////////////// CAN Bus //////////////
    public static int lowerArmMotor = 1;
    public static int upperArmMotor = 2;
    
    ///////////// Analog IN /////////////
    public static int lowerArmEncoder = 0;
    public static int upperArmEncoder = 1;
    public static int leftWheelEncoder = 2;
    public static int rightWheelEncoder = 3;
    
    ///////////// Digital IN /////////////
    public static int lowerArmButton = 0;
    public static int upperArmButton = 1;
    
    ////////////// Lengths ///////////////
    public static double upperArmLength = 25.5;
    public static double lowerArmLength = 14.25;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
}
