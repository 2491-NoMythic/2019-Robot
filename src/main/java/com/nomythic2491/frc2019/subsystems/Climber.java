/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.lib.drivers.TalonSRXFactory;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.commands.Climber.ClimberLoop;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new ClimberLoop());
  }

  private static Climber mInstance = null;

  public static Climber getInstance() {
    if (mInstance == null) {
      mInstance = new Climber();
    }
    return mInstance;
  }

  /**
   * left is master, right is slave
   */
  private TalonSRX mMasterClimber, mSlaveClimber;
  private Solenoid mClimberSolenoid, mRatchetSolenoid;
  DigitalInput limitSwitch;

  private Climber() {
    mMasterClimber = TalonSRXFactory.createDefaultTalon(Constants.kPoleMasterId);
    configureMaster(mMasterClimber, true);
    
    mSlaveClimber = TalonSRXFactory.createPermanentSlaveTalon(Constants.kPoleSlaveId, Constants.kPoleMasterId);
    mSlaveClimber.setInverted(InvertType.FollowMaster);

    mClimberSolenoid = new Solenoid(Constants.kSkidsChannel);
    mRatchetSolenoid = new Solenoid(Constants.kRatchetChannel);
  }

  public void runClimberDemand(ClimberDemand demand) {
    engageRatchet(demand.getRatchet());
    mMasterClimber.setNeutralMode(demand.getBrake());
    mMasterClimber.set(ControlMode.PercentOutput, demand.getSpeed());
  }

  private void configureMaster(TalonSRX talon, boolean left) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, 100);
    final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
    if (sensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent, false);
    }
    talon.setInverted(!left);
    talon.setSensorPhase(true);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
    talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms, Constants.kLongCANTimeoutMs); //TODO: configure V and Ramp
    talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
    talon.configNeutralDeadband(0.04, 0);
  }

  public void resetEncoders() {
    resetRightEncoder();
    resetLeftEncoder();
  }

  private void resetRightEncoder() {
    mMasterClimber.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
  }

  private void resetLeftEncoder() {
    mSlaveClimber.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
  }

  /**
   * @return The value of the left drive encoder in inches
   */
  public double getLeftEncoderDistance() {
    try {
      return mSlaveClimber.getSelectedSensorPosition(0) * Constants.kClimberEncoderToInches;
    } catch (Exception e) {
      System.out.println("Failed to get left climber encoder distance.");
      return -1;
    }
  }

  /**
   * Gets the left encoder value in ticks (4096 per rotation)
   */
  public double getLeftEncoderDistanceRaw() {
    try {
      return mSlaveClimber.getSelectedSensorPosition(0);
    } catch (Exception e) {
      System.out.println("Failed to get left climber encoder distance raw.");
      return -1;
    }
  }

  /**
   * Gets the right encoder value in ticks (4096 per rotation)
   * 
   * @return
   */
  public double getRightEncoderDistanceRaw() {
    try {
      return mMasterClimber.getSelectedSensorPosition(0);
    } catch (Exception e) {
      System.out.println("Failed to get right climber encoder distance raw.");
      return -1;
    }
  }

  /**
   * @return The value of the right drive encoder in inches. If return negative 1
   *         it has failed.
   */
  public double getRightEncoderDistance() {
    try {
      return mMasterClimber.getSelectedSensorPosition(0) * Constants.kClimberEncoderToInches;
    } catch (Exception e) {
      System.out.println("Failed to get right climber encoder distance.");
      return -1;
    }
  }

  /**
   * @return The average value of the two encoders in inches
   */
  public double getDistance() {
    return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2;

  }

  public void engageSkid() {
    mClimberSolenoid.set(true);
  }

  public void disengageSkid() {
    mClimberSolenoid.set(false);
  }

  public void engageRatchet(boolean engaed) {
    mRatchetSolenoid.set(engaed);
  }

  public boolean isSkidUp() {
    return mClimberSolenoid.get();
  }
}
