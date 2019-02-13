/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The subsystem of talons, solenoids and limit switches that make up the Magic
 * Box system.
 */
public class MagicBox extends Subsystem {

  private static MagicBox instance;
  private TalonSRX intake, rotateIntake, elevatorLeft, elevatorRight;
  private DoubleSolenoid spindle;
  // private Solenoid leftSpindle, rightSpindle; //These will only be needed if we
  // decide that we want to have two solenoids controlling the hatch intake. If
  // it's just one, we don't need these.
  public boolean isElevatorRising, isBoxFlippedDown, isBoxFlippedMiddle;
  public Solenoid controlPins;
  DigitalInput elevatorLimitSwitch = new DigitalInput(0);
  DigitalInput cargoLimitSwitch = new DigitalInput(1);

  public enum positionRotate {
    GROUND, FLAT, BACK
  }

  public enum positionElevator {
    UP, DOWN
  }

  private MagicBox() {
    intake = TalonSRXFactory.createDefaultTalon(Constants.kIntakeRollerId);
    rotateIntake = TalonSRXFactory.createDefaultTalon(Constants.kRotator);
    elevatorLeft = TalonSRXFactory.createDefaultTalon(Constants.kElevatorLeft);
    configureMaster(elevatorLeft, true);
    elevatorRight = TalonSRXFactory.createPermanentSlaveTalon(Constants.kElevatorRight, Constants.kElevatorLeft);
    elevatorRight.setInverted(false);
    spindle = new DoubleSolenoid(Constants.kHatchOutChannel, Constants.kHatchInChannel);

    /**
     * Configures the Magic Box rotation feedback sensor
     */
    rotateIntake.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kVelocitySlotId,
        Constants.kTimeoutMs);
  }

  private void configureMaster(TalonSRX talon, boolean left) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, 100);
    final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100); // primary
                                                                                                                         // closed-loop,
                                                                                                                         // 100
                                                                                                                         // ms
                                                                                                                         // timeout
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

  /**
   * Runs the intake at a given speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void runIntake(double speed) {
    intake.set(ControlMode.PercentOutput, speed);
  }

  /**
   * Uses a limit switch to determine if cargo is in the intake
   * 
   * @return Whether or not cargo is in the intake
   */
  public boolean isCargoIn() {
    return cargoLimitSwitch.get();
  }

  /**
   * Resets the elevator encoder
   */
  public void resetElevatorEncoder() {
    elevatorLeft.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
  }

  public double getIntakeRotatorPosition() {
    return rotateIntake.getSelectedSensorPosition(0);
  }

  /**
   * Stops the intake from moving
   */
  public void stopIntake() {
    runIntake(0);
  }

  /**
   * Extends the hatch intake solenoid
   */
  public void extendSolenoid() {
    spindle.set(Value.kForward);
  }

  /**
   * Retracts the hatch intake solenoid
   */
  public void retractSolenoid() {
    spindle.set(Value.kReverse);
  }

  /**
   * Determines what position the hatch intake solenoid is in
   * 
   * @return The hatch intake solenoid's position
   */
  public Value hatchIntakeExtended() {
    return spindle.get();
  }

  public void runElevatorMotionProfile(double[][] profile, int totalCount, boolean isInverted) {
    TrajectoryPoint point = new TrajectoryPoint();
    elevatorLeft.clearMotionProfileTrajectories();
    for (int i = 0; i < totalCount; ++i) {
      /* for each point, fill our structure and pass it to API */
      point.position = profile[i][0];
      if (isInverted) {
        point.velocity = -profile[i][1];
      } else {
        point.velocity = profile[i][1];
      }
      point.timeDur = (int) profile[i][2];
      point.profileSlotSelect0 = 0; /* which set of gains would you like to use? */
      /*
       * set true to not do any position servo, just velocity feedforward
       */
      point.zeroPos = false;
      if (i == 0)
        point.zeroPos = true; /* set this to true on the first point */

      point.isLastPoint = false;
      if ((i + 1) == totalCount)
        point.isLastPoint = true; /* set this to true on the last point */

      elevatorLeft.pushMotionProfileTrajectory(point);
    }
    elevatorLeft.set(ControlMode.MotionProfile, 1);
  }

  public void runMagicboxMotionProfile(double[][] profile, int totalCount, boolean isInverted) {
    TrajectoryPoint point = new TrajectoryPoint();
    rotateIntake.clearMotionProfileTrajectories();
    for (int i = 0; i < totalCount; ++i) {
      /* for each point, fill our structure and pass it to API */
      point.position = profile[i][0];
      if (isInverted) {
        point.velocity = -profile[i][1];
      } else {
        point.velocity = profile[i][1];
      }
      point.timeDur = (int) profile[i][2];
      point.profileSlotSelect0 = 0; /* which set of gains would you like to use? */
      /*
       * set true to not do any position servo, just velocity feedforward
       */
      point.zeroPos = false;
      if (i == 0)
        point.zeroPos = true; /* set this to true on the first point */

      point.isLastPoint = false;
      if ((i + 1) == totalCount)
        point.isLastPoint = true; /* set this to true on the last point */

      rotateIntake.pushMotionProfileTrajectory(point);
    }
    rotateIntake.set(ControlMode.MotionProfile, 1);
  }

  public positionElevator getElevatorPosition() {
    return Variables.currentElevatorPostion;
  }

  public positionRotate getMagicBoxPosition() {
    return Variables.currentMagicboxPosition;
  }

  public void toggleControlPins() {
    if (controlPins.get()) {
      controlPins.set(false);
    } else {
      controlPins.set(true);
    }
  }

  /**
   * Determines whether the control pins are up or down
   * 
   * @return The control pin solenoid's value
   */
  public boolean controlPinsExtended() {
    return controlPins.get();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public static MagicBox getInstance() {
    if (instance == null){
      instance = new MagicBox();
    }
    return instance;
  }
}
