/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.GamepeiceDemand;
import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
import com.nomythic2491.frc2019.Settings.Constants.kManipulator;
import com.nomythic2491.frc2019.commands.MagicBox.GamepieceLoop;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The subsystem of talons, solenoids and limit switches that make up the Magic
 * Box system.
 */
public class MagicBox extends Subsystem {

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new GamepieceLoop());
  }

  private static MagicBox instance = null;

  public static MagicBox getInstance() {
    if (instance == null) {
      instance = new MagicBox();
    }
    return instance;
  }

  private TalonSRX intake, rotateIntake;
  /**
   * Master is left, Slave is right
   */
  private TalonSRX elevatorMaster, elevatorSlave;

  public Solenoid CorralPins;
  private DoubleSolenoid spindle, flippyBumper;
  private DigitalInput hatchPresent, cargoPresent;

  private MagicBox() {
    intake = TalonSRXFactory.createDefaultTalon(Constants.kRollerId);

    initManipulator();

    spindle = new DoubleSolenoid(Constants.kHatchOutChannel, Constants.kHatchInChannel);
    flippyBumper = new DoubleSolenoid(Constants.kBumperInChannel, Constants.kBumperOutChannel);
    hatchPresent = new DigitalInput(0);
    cargoPresent = new DigitalInput(1);
  }

  private void initManipulator() {
    // Intake arm Rotation
    rotateIntake = TalonSRXFactory.createDefaultTalon(Constants.kRotatorId);
    configureMaster(rotateIntake, false, 0.04);

    final ErrorCode rotateSensorPresent = rotateIntake
        .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kLongCANTimeoutMs);
    if (rotateSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect arm rotation encoder: " + rotateSensorPresent, false);
    }

    rotateIntake.selectProfileSlot(Constants.kSlot_MotMagic, 0);
    rotateIntake.config_kF(Constants.kSlot_MotMagic, 1.076842105263158, Constants.kLongCANTimeoutMs);
    rotateIntake.config_kP(Constants.kSlot_MotMagic, 0.6, Constants.kLongCANTimeoutMs); // .12
    rotateIntake.config_kI(Constants.kSlot_MotMagic, 0, Constants.kLongCANTimeoutMs); // .00001
    rotateIntake.config_kD(Constants.kSlot_MotMagic, 220, Constants.kLongCANTimeoutMs); // 25

    rotateIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
    rotateIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);

    rotateIntake.configMotionCruiseVelocity(200, Constants.kLongCANTimeoutMs);
    rotateIntake.configMotionAcceleration(400, Constants.kLongCANTimeoutMs);

    // Caridge
    elevatorMaster = TalonSRXFactory.createDefaultTalon(Constants.kElevatorMasterId);
    configureMaster(elevatorMaster, true, 0.04);

    elevatorSlave = new TalonSRX(Constants.kElevatorSlaveId);
    elevatorSlave.configFactoryDefault(Constants.kLongCANTimeoutMs);

    final ErrorCode slaveSensorPresent = elevatorSlave
        .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kLongCANTimeoutMs);
    if (slaveSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect right elevator encoder: " + slaveSensorPresent, false);
    }

    elevatorMaster.configRemoteFeedbackFilter(elevatorSlave.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor,
        0, Constants.kLongCANTimeoutMs);

    elevatorMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    final ErrorCode masterSensorPresent = elevatorMaster.configSensorTerm(SensorTerm.Sum1,
        FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kLongCANTimeoutMs);
    if (masterSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect left elevator encoder: " + masterSensorPresent, false);
    }

    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.kPID_Primary,
        Constants.kLongCANTimeoutMs);
    elevatorMaster.configSelectedFeedbackCoefficient(0.5, Constants.kPID_Primary, Constants.kLongCANTimeoutMs);

    elevatorMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    elevatorMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.CTRE_MagEncoder_Relative,
        Constants.kLongCANTimeoutMs);
    // if right falls behind, number is positive
    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorDifference, Constants.kPID_Auxliary,
        Constants.kLongCANTimeoutMs);
    elevatorMaster.configSelectedFeedbackCoefficient(1, Constants.kPID_Auxliary, Constants.kLongCANTimeoutMs);

    elevatorMaster.setSensorPhase(kManipulator.kMasterSensorPhase);
    elevatorSlave.setSensorPhase(kManipulator.kSlaveSensorPhase);
    elevatorSlave.follow(elevatorMaster, FollowerType.AuxOutput1);
    elevatorSlave.setInverted(InvertType.OpposeMaster);

    elevatorMaster.config_kP(Constants.kSlot_MotMagic, 0.032, Constants.kLongCANTimeoutMs); // .12
    elevatorMaster.config_kI(Constants.kSlot_MotMagic, 0, Constants.kLongCANTimeoutMs); // .00001
    elevatorMaster.config_kD(Constants.kSlot_MotMagic, 0, Constants.kLongCANTimeoutMs);
    elevatorMaster.config_kF(Constants.kSlot_MotMagic, 0.1364, Constants.kLongCANTimeoutMs);

    elevatorMaster.config_kP(Constants.kSlot_Adjustment, 0, Constants.kLongCANTimeoutMs); // .12
    elevatorMaster.config_kI(Constants.kSlot_Adjustment, 0, Constants.kLongCANTimeoutMs); // .00001
    elevatorMaster.config_kD(Constants.kSlot_Adjustment, 0, Constants.kLongCANTimeoutMs);// 25
    elevatorMaster.config_kF(Constants.kSlot_Adjustment, 0, Constants.kLongCANTimeoutMs);
    elevatorMaster.configAllowableClosedloopError(Constants.kSlot_Adjustment, 0, Constants.kLongCANTimeoutMs);

    elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 20, Constants.kTimeoutMs);
    elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
    elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
    elevatorMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 20, Constants.kTimeoutMs);
    elevatorSlave.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kTimeoutMs);

    elevatorMaster.configMotionCruiseVelocity(kManipulator.kMaxVel, Constants.kLongCANTimeoutMs);
    elevatorMaster.configMotionAcceleration(kManipulator.kMaxAccel, Constants.kLongCANTimeoutMs);
    // elevatorMaster.getSensorCollection().syncQuadratureWithPulseWidth(bookend0,
    // bookend1, bCrossZeroOnInterval, offset, timeoutMs)

    // assigns a slot of saved PIDF values to a talon's primary or auxilary loops
    elevatorMaster.selectProfileSlot(Constants.kSlot_MotMagic, Constants.kPID_Primary);
    elevatorMaster.selectProfileSlot(Constants.kSlot_Adjustment, Constants.kPID_Auxliary);

    /**
     * configAuxPIDPolarity(boolean invert, int timeoutMs) false means talon's local
     * output is PID0 + PID1, and other side Talon is PID0 - PID1 true means talon's
     * local output is PID0 - PID1, and other side Talon is PID0 + PID1
     */
    elevatorMaster.configAuxPIDPolarity(false, Constants.kLongCANTimeoutMs);

    elevatorMaster.setSelectedSensorPosition(0);
  }

  private void configureMaster(TalonSRX talon, boolean left, double nominalV) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
    // primary closed-loop
    talon.setInverted(!left);
    talon.setSensorPhase(false);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
    //talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms, Constants.kLongCANTimeoutMs); //TODO: what is this???
    talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
    talon.configNeutralDeadband(0.04, Constants.kLongCANTimeoutMs);
  }

  public void followGamepeiceDemand(GamepeiceDemand demand) {
    if (demand != GamepeiceDemand.Hold) {
      elevatorMaster.set(ControlMode.MotionMagic, demand.getHeightPoint(), DemandType.AuxPID, 0);
      rotateIntake.set(ControlMode.MotionMagic, demand.getAnglePoint());
    }
  }

  /**
   * Runs the intake at a given speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void runIoCargo(IoCargo demand) {
    // if(cargoPresent.get() && demand == IoCargo.In) {
    // intake.set(ControlMode.PercentOutput, 0);
    // } else {
    intake.set(ControlMode.PercentOutput, demand.getSpeed());
    // }
  }

  /**
   * Uses a limit switch to determine if cargo is in the intake
   * 
   * @return Whether or not cargo is in the intake
   */
  public boolean isCargoIn() {
    return cargoPresent.get();
  }

  /**
   * Toggles the HatchGrabber in and out
   * 
   * @param engadge true to grab, flase to release.
   */
  public void toggleHatchGrabber(boolean engadge) {
    spindle.set(engadge ? Value.kForward : Value.kReverse);
  }

  /**
   * Determines whether the HatchGrabber is grabbing or releasing
   * 
   * @return the HatchGrabber's state
   */
  public boolean getHatchGrabber() {
    return spindle.get() == Value.kForward ? true : false;
  }

  /**
   * Toggles the FlippyBumpper in and out
   * 
   * @param engadge true to deploy, flase to retract.
   */
  public void toggleFlippyBumpers(boolean engadge) {
    flippyBumper.set(engadge ? Value.kForward : Value.kReverse);
  }

  /**
   * Determines whether the FlippyBummpers are delpoyed or retracted
   * 
   * @return the FlippyBummper's state
   */
  public boolean getFlippyBumppers() {
    return flippyBumper.get() == Value.kForward ? true : false;
  }

  /**
   * Toggles the corral pins on and off
   * 
   * @param engadge true to deploy, flase to retract.
   */
  public void toggleCorralPins(boolean engadge) {
    CorralPins.set(engadge);
  }

  /**
   * Determines whether the corral pins are delpoyed or retracted
   * 
   * @return the corral pin's state
   */
  public boolean getCorralPins() {
    return CorralPins.get();
  }

  /**
   * Resets the elevator encoder
   */
  public void resetElevatorEncoder() {
    elevatorMaster.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);
  }

  public double getCurrentAnglePoint() {
    return rotateIntake.getSelectedSensorPosition();
  }

}
