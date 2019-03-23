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
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
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
  private TalonSRX mClimberMaster, mClimberSlave, mStringMaster, mStringSlave;
  DigitalInput limitSwitch;

  private Climber() {

    // Master
    mClimberMaster = TalonSRXFactory.createDefaultTalon(kClimber.kClimberMasterId);
    configureMaster(mClimberMaster, true);
    mClimberMaster.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, Constants.kLongCANTimeoutMs);

    // Slave
    mClimberSlave = new TalonSRX(kClimber.kClimberSlaveId);
    mClimberSlave.configFactoryDefault(Constants.kLongCANTimeoutMs);
    mClimberSlave.follow(mClimberMaster);
    mClimberSlave.setInverted(InvertType.FollowMaster);
    

    // PIDF

    final ErrorCode rightSensorPresent = mClimberSlave
        .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kLongCANTimeoutMs);

    if (rightSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect right climber encoder: " + rightSensorPresent, false);
    }

    mClimberMaster.configRemoteFeedbackFilter(mClimberSlave.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor,
        0, Constants.kLongCANTimeoutMs);

    mClimberMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    final ErrorCode leftSensorPresent = mClimberMaster.configSensorTerm(SensorTerm.Sum1,
        FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kLongCANTimeoutMs);

    if (leftSensorPresent != ErrorCode.OK) {
      DriverStation.reportError("Could not detect left climber encoder: " + rightSensorPresent, false);
    }

    mClimberMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.RemoteSensor0, Constants.kLongCANTimeoutMs);
    mClimberMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.CTRE_MagEncoder_Relative,
        Constants.kLongCANTimeoutMs);

    mClimberMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, Constants.kLongCANTimeoutMs);
    mClimberMaster.configSelectedFeedbackCoefficient(0.5, 0, Constants.kLongCANTimeoutMs);

    mClimberMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorDifference, 1, Constants.kLongCANTimeoutMs);
    mClimberMaster.configSelectedFeedbackCoefficient(1, 1, Constants.kLongCANTimeoutMs);

    /* Set status frame periods to ensure we don't have stale data */
		mClimberMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
		mClimberMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
		mClimberMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
		mClimberMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20, Constants.kTimeoutMs);
    mClimberSlave.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);

    /* FPID Gains for distance servo */
    mClimberMaster.config_kP(Constants.kPrimarySlot, kClimber.kGains_Distanc.kP, Constants.kTimeoutMs);
    mClimberMaster.config_kI(Constants.kPrimarySlot, kClimber.kGains_Distanc.kI, Constants.kTimeoutMs);
    mClimberMaster.config_kD(Constants.kPrimarySlot, kClimber.kGains_Distanc.kD, Constants.kTimeoutMs);
    mClimberMaster.config_kF(Constants.kPrimarySlot, kClimber.kGains_Distanc.kF, Constants.kTimeoutMs);
    // mClimberMaster.config_IntegralZone(Constants.kPrimarySlot, kClimber.kGains_Distanc.kIzone, Constants.kTimeoutMs);
    // mClimberMaster.configClosedLoopPeakOutput(Constants.kPrimarySlot, kClimber.kGains_Distanc.kPeakOutput,
    //     Constants.kTimeoutMs);
    mClimberMaster.configAllowableClosedloopError(Constants.kPrimarySlot, 0, Constants.kTimeoutMs);

    /* FPID Gains for turn servo */
    mClimberMaster.config_kP(Constants.kAuxilarySlot, kClimber.kGains_Turning.kP, Constants.kTimeoutMs);
    mClimberMaster.config_kI(Constants.kAuxilarySlot, kClimber.kGains_Turning.kI, Constants.kTimeoutMs);
    mClimberMaster.config_kD(Constants.kAuxilarySlot, kClimber.kGains_Turning.kD, Constants.kTimeoutMs);
    mClimberMaster.config_kF(Constants.kAuxilarySlot, kClimber.kGains_Turning.kF, Constants.kTimeoutMs);
    // mClimberMaster.config_IntegralZone(Constants.kAuxilarySlot, (int) kClimber.kGains_Turning.kIzone,
    //     Constants.kTimeoutMs);
    // mClimberMaster.configClosedLoopPeakOutput(Constants.kAuxilarySlot, kClimber.kGains_Turning.kPeakOutput,
    //     Constants.kTimeoutMs);
    mClimberMaster.configAllowableClosedloopError(Constants.kAuxilarySlot, 0, Constants.kTimeoutMs);
    mClimberMaster.configMotionAcceleration(20000, Constants.kLongCANTimeoutMs);
    mClimberMaster.configMotionCruiseVelocity(200, Constants.kLongCANTimeoutMs);

    // Winch
    mStringMaster = TalonSRXFactory.createDefaultTalon(kClimber.kMasterStringId);
    configureMaster(mStringMaster, true);
    mStringSlave = TalonSRXFactory.createPermanentSlaveTalon(kClimber.kSlaveStringId, kClimber.kMasterStringId);
    mStringSlave.follow(mStringMaster);
    mStringSlave.setInverted(InvertType.FollowMaster);

    mClimberMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
  }

  public void runClimberDemand(ClimberDemand demand) {
    mClimberMaster.setNeutralMode(demand.getBrake());
    mClimberSlave.setNeutralMode(demand.getBrake());
    mClimberMaster.set(ControlMode.MotionMagic, demand.getHeightPoint(), DemandType.AuxPID, 0);
  }

  public double getEncoderDiffrence(){
    return Math.abs( mClimberMaster.getSelectedSensorPosition() - mClimberSlave.getSelectedSensorPosition() ); 
  }

  private void configureMaster(TalonSRX talon, boolean left) {
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
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
    mClimberMaster.setSelectedSensorPosition(0, Constants.kPrimarySlot, Constants.kTimeoutMs);
    mClimberSlave.setSelectedSensorPosition(0, Constants.kPrimarySlot, Constants.kTimeoutMs);
  }
}
