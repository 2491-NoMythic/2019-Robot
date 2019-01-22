/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.settings.Constants;
import frc.robot.settings.Variables;



public class Drivetrain extends PIDSubsystem {
  private TalonSRX rightMaster, leftMaster;
  private VictorSPX rightSlave, leftSlave;
  private NetworkTable limelight;
  private NetworkTableEntry tx, ty, ta, tv;
  private double currentPIDOutput;

  private static Drivetrain instance;

  public static Drivetrain GetInstance() {
    if (instance == null) {
      instance = new Drivetrain();
    }
    return instance;
  }

  private Drivetrain() {
    super("Drive", Variables.proportionalRotate, Variables.integralRotate, Variables.derivativeRotate);

    try {
      leftMaster = new TalonSRX(Constants.driveTalonLeftChannel);
      rightMaster = new TalonSRX(Constants.driveTalonRightChannel);
      leftSlave = new VictorSPX(Constants.driveVictorLeftChannel);
      rightSlave = new VictorSPX(Constants.driveVictorRightChannel);
    }
    catch (Exception e) {
      DriverStation.reportError("TalonSRX instantiation failure! Check CAN Bus, TalonSRX Decive ID's, and TalonSRX power", Variables.debugMode);

      if (Variables.debugMode) {System.out.println(e);}
    }

    //Binds Victor slaves to Talon masters
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    //Configures Talon feedback sensors
    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kVelocitySlotId, Constants.kTimeoutMs);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kVelocitySlotId, Constants.kTimeoutMs);

    //Corrects sensor direction to match throttle direction
    leftMaster.setSensorPhase(true);
    rightMaster.setSensorPhase(true);
  }

  @Override
  protected void initDefaultCommand() {
  }

  @Override
  //Add in gyro stuff later
  public double returnPIDInput() {
    return 0;
  }

  @Override
  //Add gyro stuff later
  protected void usePIDOutput(double output) {
    
  }

  /**
   * Drives both sides of the robot at the same speed
   * @param speed Speed of all drivetrain wheels in inches per second
   */
  public void driveVelocity(double speed) {
    driveLeftVelocity(speed);
    driveRightVelocity(speed);
  }

  /**
   * Drives each side of the robot at different speeds
   * @param leftSpeed Speed of left wheels in inches per second
   * @param rightSpeed Speed of right wheels in inches per second
   */
  public void driveVelocity(double leftSpeed, double rightSpeed) {
    driveLeftVelocity(leftSpeed);
    driveRightVelocity(rightSpeed);
  }
  
  /**
   * Drives the left side of the robot
   * @param speed Speed of left wheels in inches per second
   */
  public void driveLeftVelocity(double speed) {
    leftMaster.set(ControlMode.Velocity, speed);
  }

  /**
   * Drives the right side of the robot
   * @param speed Speed of the right wheels in inches per second
   */
  public void driveRightVelocity(double speed) {
    rightMaster.set(ControlMode.Velocity, speed);
  }

  /**
   * Drives both sides of the robot at the same speed, ranging from -1 to 1
   * @param speed Speed of all drivetrain wheels, ranging from -1 to 1
   */
  public void drivePercentOutput(double speed) {
    drivePercentOutput(speed, speed);
  }
  
  /**
   * Drives each side of the robot at a given speed, ranging from -1 to 1
   * @param leftSpeed Speed of the left wheels from -1 to 1
   * @param rightSpeed Speed of the right wheels from -1 to 1
   */
  public void drivePercentOutput(double leftSpeed, double rightSpeed) {
    driveLeftPercentOutput(leftSpeed);
    driveRightPercentOutput(rightSpeed);
  }

  /**
   * Drives the left side of the robot at a given speed, ranging from -1 to 1
   * @param speed Speed of the left wheels from -1 to 1
   */
  public void driveLeftPercentOutput(double speed) {
    leftMaster.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Drives the right side of the robot at a given speed, ranging from -1 to 1
   * @param speed Speed of the right wheels from -1 to 1
   */
  public void driveRightPercentOutput(double speed) {
    rightMaster.set(ControlMode.PercentOutput, speed);
  }
}
