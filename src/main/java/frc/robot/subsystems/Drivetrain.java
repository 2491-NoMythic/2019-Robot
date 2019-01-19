/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.robot.settings.Constants;
import frc.robot.settings.Variables;
import sun.security.jca.GetInstance;




/**
 * Add your docs here.
 */
public class Drivetrain extends PIDSubsystem {
  private TalonSRX rightMaster, leftMaster;
  private VictorSPX rightFollower, leftFollower;
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
    super("Drive", Variables.ProportionalRotate, Variables.integralRotate, Variables.derivativeRotate);

    try {
      leftMaster = new TalonSRX(Constants.driveTalonLeftChannel);
      rightMaster = new TalonSRX(Constants.driveTalonRightChannel);
      leftFollower = new VictorSPX(Constants.driveVictorLeftChannel);
      rightFollower = new VictorSPX(Constants.driveVictorRightChannel);
    }
    catch (Exception e) {
      DriverStation.reportError("TalonSRX instantiation failure! Check CAN Bus, TalonSRX Decive ID's, and TalonSRX power", Variables.debugMode);

      if (Variables.debugMode) {System.out.println(e);}
    }
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
