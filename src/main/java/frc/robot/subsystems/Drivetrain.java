/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
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
}
