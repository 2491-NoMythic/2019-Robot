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
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class MagicBox extends Subsystem {

  private static MagicBox instance;
  private TalonSRX intake, rotateIntake, elevatorLeft, elevatorRight;
  private DoubleSolenoid spindleRight, spindleLeft;

  DigitalInput limitSwitch = new DigitalInput(1);

  private MagicBox() {
    intake = TalonSRXFactory.createDefaultTalon(Constants.kIntakeRollerId);
    rotateIntake = TalonSRXFactory.createDefaultTalon(Constants.kRotator);
    elevatorLeft = TalonSRXFactory.createDefaultTalon(Constants.kElevatorLeft);
    configureMaster(elevatorLeft, true);
    elevatorRight = TalonSRXFactory.createPermanentSlaveTalon(Constants.kElevatorRight, Constants.kElevatorLeft);
    elevatorRight.setInverted(false);
    spindleRight = new DoubleSolenoid(Constants.kRightHatchOutChannel, Constants.kRightHatchInChannel);
    spindleLeft = new DoubleSolenoid(Constants.kLeftHatchOutChannel, Constants.kLeftHatchInChannel);
  }

  private void configureMaster(TalonSRX talon, boolean left) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, 100);
    final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
            100); // primary closed-loop, 100 ms timeout
    if (sensorPresent != ErrorCode.OK) {
        DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent,
                false);
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

  /**
   * Runs the intake at a given speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void runIntake(double speed) {
    intake.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Elevates the intake at a specified speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void elevateIntake(double speed) {
    elevatorLeft.set(ControlMode.PercentOutput, speed);
    elevatorRight.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Gets the elevator height from the left talon
   * @return The height of the elevator
   */
  public double getElevatorHeight() {
    return elevatorLeft.getSelectedSensorPosition(0);
  }
  
  /**
   * Resets the elevator encoder
   */
  public void resetElevatorEncoder() {
    elevatorLeft.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
  }

  /**
   * Rotates the intake at a speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void rotateIntake(double speed) {
    rotateIntake.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Stops the elevator from moving
   */
  public void stopElevator() {
    elevateIntake(0);
  }

  /**
   * Stops the intake from moving
   */
  public void stopIntake() {
    runIntake(0);
  }

  /**
   * Extends the right solenoid
   */
  public void extendRightSolenoid() {
    spindleRight.set(Value.kForward);
  }

  /**
   * Retracts the right solenoid
   */
  public void retractRightSolenoid() {
    spindleRight.set(Value.kReverse);
  }

  /**
   * Extends the left solenoid
   */
  public void extendLeftSolenoid() {
    spindleLeft.set(Value.kForward);
  }

  /**
   * Retracts the left solenoid
   */
  public void retractLeftSolenoid() {
    spindleLeft.set(Value.kReverse);
  }

  /**
   * Determines what position the right solenoid is in
   * 
   * @return The right solenoid's position
   */
  public boolean rightExtended() {
    return spindleRight.get() == Value.kForward || spindleRight.get() == Value.kOff;
  }

  /**
   * Determines what position the left solenoid is in
   * 
   * @return The left solenoid's position
   */
  public boolean leftExtended() {
    return spindleLeft.get() == Value.kForward || spindleLeft.get() == Value.kOff;
  }

  public static MagicBox getInstance() {
    if (instance == null) {
      instance = new MagicBox();
    }
    return instance;
  }

  /**
   * Determines whether the elevator is in the lowest position
   * @return Whether the elevator is in the lowest position
   */
  public boolean isElevatorDown() {
    return limitSwitch.get();
  }

  /**
   * Determines whether the elevator is in the highest position
   * @return Whether the elevator is in the highest position
   */
  public boolean isElevatorUp() {
    return getElevatorHeight() >= (Constants.kElevatorMaxHeight - Constants.kElevatorUncertainty);
  }

  //public boolean isElevatorRising() {
  //  return elevatorLeft.get
  //}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
