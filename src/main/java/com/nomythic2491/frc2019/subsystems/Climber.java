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
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.lib.drivers.TalonSRXFactory;
import com.nomythic2491.frc2019.Settings.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class Climber extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static Climber instance;
  private TalonSRX mRightClimber, mLeftClimber;
  private DoubleSolenoid mClimberSolenoid;

  public static Climber getInstance() {
    if (instance == null) {
      instance = new Climber();
    }
    return instance;
  }

  private Climber() {
    mRightClimber = TalonSRXFactory.createDefaultTalon(Constants.kPoleMasterId);
    configureMaster(mRightClimber, true);

    mLeftClimber = TalonSRXFactory.createPermanentSlaveTalon(Constants.kPoleSlaveId, Constants.kPoleMasterId);
    mLeftClimber.setInverted(false);

    mClimberSolenoid = new DoubleSolenoid(Constants.kClimberForward, Constants.kClimberOff);

  }

  public void driveVelocity(double speed) {
    mRightClimber.set(ControlMode.Velocity, speed);
  }

  public void drivePercentOutput(double speed) {
    mRightClimber.set(ControlMode.PercentOutput, speed);
  }

  // Elias --- this stuff is all copied from Drivetrain.java and we might not need
  // it
  // but its here for now i guess
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
    // talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms,
    // Constants.kLongCANTimeoutMs);
    talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
    talon.configNeutralDeadband(0.04, 0);
  }

  public void resetEncoders() {
    resetRightEncoder();
    resetLeftEncoder();
  }

  private void resetRightEncoder() {
    mRightClimber.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
  }

  private void resetLeftEncoder() {
    mLeftClimber.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
  }

  /**
   * @return The value of the left drive encoder in inches
   */
  public double getLeftEncoderDistance() {
    return mLeftClimber.getSelectedSensorPosition(0) * Constants.kClimberEncoderToInches;
  }

  /**
   * Gets the left encoder value in ticks (4096 per rotation)
   */
  public double getLeftEncoderDistanceRaw() {
    return mLeftClimber.getSelectedSensorPosition(0);
  }

  /**
   * Gets the right encoder value in ticks (4096 per rotation)
   * 
   * @return
   */
  public double getRightEncoderDistanceRaw() {
    return mRightClimber.getSelectedSensorPosition(0);
  }

  /**
   * @return The value of the right drive encoder in inches
   */
  public double getRightEncoderDistance() {
    return mRightClimber.getSelectedSensorPosition(0) * Constants.kClimberEncoderToInches;
  }

  /**
   * @return The average value of the two encoders in inches
   */
  public double getDistance() {
    return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2;

  }

  public void setClimbPostition(){}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
