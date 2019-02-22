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
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Variables;
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
    // Set the default command for a subsystem here.
    setDefaultCommand(new GamepieceLoop());
  }
  public enum GamepeiceDemand {
    Test(0, -500),
    CargoOut_Ship(21, -1100),
    CargoFloor(3, -600),
    Hold(0,0);

    private double mHeightPoint;
    private double mAnglePoint;

    private GamepeiceDemand(double hight, double angle) {
      mHeightPoint = hight/Math.PI * 4096;
      mAnglePoint = angle; //(angle * 4096)/360;
    }

    public double getHeightPoint() {
      return mHeightPoint;
    }

    public double getAnglePoint() {
      return mAnglePoint;
    }
  }
  public enum IoCargo {
    Out(.75), In(-.75), Stop(0);

    private double mSpeed;

    private IoCargo(double speed) {
      mSpeed = speed;
    }

    public double getSpeed() {
      return mSpeed;
    }
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
  DigitalInput hatchPresent = new DigitalInput(0);
  DigitalInput cargoPresent = new DigitalInput(1);

  public enum PositionRotate {
    GROUND, FLAT, BACK
  }

  public enum PositionElevator {
    UP, DOWN
  }

  private MagicBox() {
    intake = TalonSRXFactory.createDefaultTalon(Constants.kRollerId);

    rotateIntake = TalonSRXFactory.createDefaultTalon(Constants.kRotatorId);
    configureMaster(rotateIntake, false, 0.04);

    rotateIntake.selectProfileSlot(Constants.kSlot_MotMagic, 0);
    rotateIntake.config_kF(Constants.kSlot_MotMagic, 1.076842105263158, Constants.kLongCANTimeoutMs);
    rotateIntake.config_kP(Constants.kSlot_MotMagic, 0.6, Constants.kLongCANTimeoutMs); // .12
    rotateIntake.config_kI(Constants.kSlot_MotMagic, 0, Constants.kLongCANTimeoutMs); // .00001
    rotateIntake.config_kD(Constants.kSlot_MotMagic, 220, Constants.kLongCANTimeoutMs); // 25

    rotateIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
    rotateIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);

    rotateIntake.configMotionCruiseVelocity(200, Constants.kLongCANTimeoutMs);
    rotateIntake.configMotionAcceleration(400, Constants.kLongCANTimeoutMs);

    initManipulator();

    initQuadrature();

    spindle = new DoubleSolenoid(Constants.kHatchOutChannel, Constants.kHatchInChannel);
    flippyBumper = new DoubleSolenoid(Constants.kBumperInChannel, Constants.kBumperOutChannel);
  }

  private void initManipulator() {
    elevatorMaster = TalonSRXFactory.createDefaultTalon(Constants.kElevatorMasterId);
    configureMaster(elevatorMaster, true, 0.04);

    elevatorSlave = new TalonSRX(Constants.kElevatorSlaveId);
    elevatorSlave.configFactoryDefault(Constants.kLongCANTimeoutMs);

    final ErrorCode slaveSensorPresent = elevatorSlave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
        Constants.kLongCANTimeoutMs);
    if (slaveSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect right elevtor encoder: " + slaveSensorPresent, false);
    }

    elevatorMaster.configRemoteFeedbackFilter(elevatorSlave.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, Constants.kLongCANTimeoutMs);

    elevatorMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    final ErrorCode masterSensorPresent = elevatorMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kLongCANTimeoutMs);
    if (masterSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect left elevator encoder: " + masterSensorPresent, false);
    }

    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.kPID_Primary, Constants.kLongCANTimeoutMs);
    elevatorMaster.configSelectedFeedbackCoefficient(0.5, Constants.kPID_Primary, Constants.kLongCANTimeoutMs);

    elevatorMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    elevatorMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kLongCANTimeoutMs);
    //if right falls behind, number is positive
    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorDifference, Constants.kPID_Auxliary, Constants.kLongCANTimeoutMs);
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
    //elevatorMaster.getSensorCollection().syncQuadratureWithPulseWidth(bookend0, bookend1, bCrossZeroOnInterval, offset, timeoutMs)

    //assigns a slot of saved PIDF values to a talon's primary or auxilary loops
    elevatorMaster.selectProfileSlot(Constants.kSlot_MotMagic, Constants.kPID_Primary);
    elevatorMaster.selectProfileSlot(Constants.kSlot_Adjustment, Constants.kPID_Auxliary);

    /**
		 * configAuxPIDPolarity(boolean invert, int timeoutMs)
		 * false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		 * true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		 */
    elevatorMaster.configAuxPIDPolarity(false, Constants.kLongCANTimeoutMs);

    elevatorMaster.setSelectedSensorPosition(0);
  }

  public void initQuadrature() {
    /* get the absolute pulse width position */
    // rotateIntake.setSelectedSensorPosition(0);

    // rotateIntake.getSensorCollection().setPulseWidthPosition(0, 100;
    // rotateIntake.getSensorCollection().setQuadraturePosition(0, 100);
    // rotateIntake.getSensorCollection().setAnalogPosition(0, 100);

    int pulseWidth = rotateIntake.getSensorCollection().getPulseWidthPosition();

    /**
     * Mask out the bottom 12 bits to normalize to [0,4095], or in other words, to
     * stay within [0,360) degrees
     */
    pulseWidth = pulseWidth & 0xFFF;

    /* Update Quadrature position */
    rotateIntake.getSensorCollection().setQuadraturePosition(pulseWidth, Constants.kTimeoutMs);
  }

  private void configureMaster(TalonSRX talon, boolean left, double nominalV) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
    // primary closed-loop
    talon.setInverted(!left);
    talon.setSensorPhase(false);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
    // talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms,
    // Constants.kLongCANTimeoutMs);
    talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
    talon.configNeutralDeadband(nominalV, 100);
  }

  /**
   * Runs the intake at a given speed
   * 
   * @param speed How fast it goes on a scale of -1 to 1
   */
  public void runIoCargo(IoCargo demand) {
    // if(cargoPresent.get() && demand == IoCargo.In) {
    //   intake.set(ControlMode.PercentOutput, 0);
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
   * Resets the elevator encoder
   */
  public void resetElevatorEncoder() {
    elevatorMaster.setSelectedSensorPosition(0, 0, Constants.kTimeoutMs);
  }

  public double getIntakeRotatorPosition() {
    return rotateIntake.getSelectedSensorPosition();
  }

  /**
   * @param units CTRE mag encoder sensor units
   * @return degrees rounded to tenths.
   */
  String ToDeg(int units) {
    double deg = units * 360.0 / 4096.0;

    /* truncate to 0.1 res */
    deg *= 10;
    deg = (int) deg;
    deg /= 10;

    return "" + deg;
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
   * Retracts the flippy bumper solenoid
   */
  public void retractBumperSolenoid() {
    flippyBumper.set(Value.kReverse);
  }
  /**
   * Extends the flippy bumper solenoid
   */
  public void extendBumperSolenoid() {
    flippyBumper.set(Value.kForward);
  }
  /**
   * Determines what position the hatch intake solenoid is in
   * 
   * @return The hatch intake solenoid's position
   */
  public Value hatchIntakeExtended() {
    return spindle.get();
  }

  public void followGamepeiceDemand(GamepeiceDemand demand) {
    if(demand != GamepeiceDemand.Hold) {
      elevateToPoint(demand.getHeightPoint());
      rotateToPoint(demand.getAnglePoint());
    }
  }

  private void elevateToPoint(double setpoint) {
    elevatorMaster.set(ControlMode.MotionMagic, setpoint, DemandType.AuxPID, 0);
  }

  private void rotateToPoint(double setpoint) {
    rotateIntake.set(ControlMode.MotionMagic, setpoint);
  }

  public PositionElevator getElevatorPosition() {
    return Variables.currentElevatorPostion;
  }

  public PositionRotate getMagicBoxPosition() {
    return Variables.currentMagicboxPosition;
  }

  public void toggleControlPins() {
    if (CorralPins.get()) {
      CorralPins.set(false);
    } else {
      CorralPins.set(true);
    }
  }

  /**
   * Determines whether the control pins are up or down
   * 
   * @return The control pin solenoid's value
   */
  public boolean controlPinsExtended() {
    return CorralPins.get();
  }

  public void positon() {
    System.out.println(elevatorMaster.getSelectedSensorPosition(0));
    System.out.println(elevatorMaster.getClosedLoopError());
  }
}
