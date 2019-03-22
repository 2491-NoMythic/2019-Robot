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
    mClimberMaster = TalonSRXFactory.createDefaultTalon(kClimber.kClimberMasterId);
    configureMaster(mClimberMaster, true);

    mClimberSlave = new TalonSRX(kClimber.kClimberSlaveId);
    mClimberSlave.configFactoryDefault(Constants.kLongCANTimeoutMs);
    mClimberSlave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kLongCANTimeoutMs);
    mClimberSlave.follow(mClimberMaster);
    mClimberSlave.setInverted(InvertType.OpposeMaster);
    

    mStringMaster = TalonSRXFactory.createDefaultTalon(kClimber.kClimberMasterId);
    configureMaster(mStringMaster, true);
    
    mStringSlave = TalonSRXFactory.createPermanentSlaveTalon(kClimber.kLeftStringId, kClimber.kRightStringId);
    mStringSlave.follow(mStringMaster);
  }

  public void runClimberDemand(ClimberDemand demand) {
    mClimberMaster.setNeutralMode(demand.getBrake());
    mClimberSlave.setNeutralMode(demand.getBrake());
    mClimberMaster.set(ControlMode.MotionMagic, demand.getHeightPoint(), DemandType.AuxPID, 0);
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
    mClimberMaster.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
    mClimberSlave.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
  }
}
