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
import com.nomythic2491.frc2019.ControlBoard;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.kClimber;

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
  }

  private static Climber mInstance = null;

  public static Climber getInstance() {
    if (mInstance == null) {
      mInstance = new Climber();
    }
    return mInstance;
  }

  ControlBoard mBoard = ControlBoard.getInstance();

  @Override
  public void periodic() {
    runClimberDemand(mBoard.getClimberDemand());
  }

  /**
   * left is master, right is slave
   */
  private TalonSRX mMasterClimber, mSlaveClimber;
  private Solenoid mRatchetSolenoid;
  DigitalInput limitSwitch;

  private Climber() {
    mMasterClimber = TalonSRXFactory.createDefaultTalon(kClimber.kPoleMasterId);
    configureMaster(mMasterClimber, true);

    mSlaveClimber = TalonSRXFactory.createPermanentSlaveTalon(kClimber.kPoleSlaveId, kClimber.kPoleMasterId);
    mSlaveClimber.setInverted(InvertType.FollowMaster);

    mRatchetSolenoid = new Solenoid(kClimber.kRatchetChannel);
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
    talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms, Constants.kLongCANTimeoutMs); // TODO:
                                                                                                        // configure V
                                                                                                        // and Ramp
    talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    talon.configClosedloopRamp(1, Constants.kLongCANTimeoutMs);
    talon.configNeutralDeadband(0.04, Constants.kLongCANTimeoutMs);
  }

  public void resetEncoders() {
    mMasterClimber.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
    mSlaveClimber.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
  }

  public void engageRatchet(boolean engaed) {
    mRatchetSolenoid.set(engaed);
  }

  public boolean getRatchet() {
    return mRatchetSolenoid.get();
  }
}
