/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The system of talons, solenoids and sensors that make up the Magic Fork subsystem..
 */
public class MagicFork extends Subsystem {

    private TalonSRX intakeRoller, elevator;
    private DoubleSolenoid hatchPickup, intakeAngle;
    
    private static MagicFork mInstance = null;
    
    public static MagicFork getInstance() {
        if (mInstance == null) {
            mInstance = new MagicFork();
        }
        return mInstance;
    }

    private MagicFork() {
        hatchPickup = new DoubleSolenoid(Constants.kMFHatchReleaseChannel, Constants.kMFHatchGrabChannel);
        intakeAngle = new DoubleSolenoid(Constants.kMFIntakeRotateUpChannel, Constants.kMFIntakeRotateDownChannel);
    
        intakeRoller = TalonSRXFactory.createDefaultTalon(Constants.kMFIntakeRollerId);
        elevator = TalonSRXFactory.createDefaultTalon(Constants.kMFElevatorId);

        intakeRoller.selectProfileSlot(Constants.kPrimarySlotIdx, 0);
        intakeRoller.config_kP(Constants.kPrimarySlotIdx, Constants.kMFRollerP, Constants.kLongCANTimeoutMs);
        intakeRoller.config_kI(Constants.kPrimarySlotIdx, Constants.kMFRollerI, Constants.kLongCANTimeoutMs);
        intakeRoller.config_kD(Constants.kPrimarySlotIdx, Constants.kMFRollerD, Constants.kLongCANTimeoutMs);
        intakeRoller.config_kF(Constants.kPrimarySlotIdx, Constants.kMFRollerF, Constants.kLongCANTimeoutMs);
        // intakeRoller.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
        // intakeRoller.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);
        // intakeRoller.configMotionCruiseVelocity(200, Constants.kLongCANTimeoutMs);
        // intakeRoller.configMotionAcceleration(400, Constants.kLongCANTimeoutMs);

        elevator.selectProfileSlot(Constants.kPrimarySlotIdx, 0);
        elevator.config_kP(Constants.kPrimarySlotIdx, Constants.kMFElevatorP, Constants.kLongCANTimeoutMs);
        elevator.config_kI(Constants.kPrimarySlotIdx, Constants.kMFElevatorI, Constants.kLongCANTimeoutMs);
        elevator.config_kD(Constants.kPrimarySlotIdx, Constants.kMFElevatorD, Constants.kLongCANTimeoutMs);
        elevator.config_kF(Constants.kPrimarySlotIdx, Constants.kMFElevatorF, Constants.kLongCANTimeoutMs);
        // elevator.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
        // elevator.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);
        // elevator.configMotionCruiseVelocity(6650, Constants.kLongCANTimeoutMs);
        // elevator.configMotionAcceleration(6650, Constants.kLongCANTimeoutMs);
    }

    // private void configureMaster(TalonSRX talon, boolean left, double nominalV) {
    //     talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
    //     // primary closed-loop
    //     final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
    //         Constants.kLongCANTimeoutMs);
    //     if (sensorPresent != ErrorCode.OK) {
    //       DriverStation.reportError("Could not detect " + (left ? "left" : "right") + "encoder: " + sensorPresent, false);
    //     }
    //     talon.setInverted(!left);
    //     talon.setSensorPhase(false);
    //     talon.enableVoltageCompensation(true);
    //     talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
    //     // talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms,
    //     // Constants.kLongCANTimeoutMs);
    //     talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
    //     talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
    //     talon.configNeutralDeadband(nominalV, 100);
    //   }
    
    /**
     * Runs the intake at a given velocity
     * @param speed The velocity the intake is run at
     */
    public void runIntake(double speed) {
        intakeRoller.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Intakes the cargo at a motor velocity designated in Constants.java
     */
    public void intakeCargo() {
        runIntake(Constants.kMFIntakeSpeed);
    }

    /**
     * Outputs the cargo at a motor velocity designated in Constants.java 
     */
    public void outputCargo() {
        runIntake(Constants.kMFShootSpeed);
    }

    /**
     * Retracts hatch pistons to grab a hatch panel
     */
    public void grabHatch() {
        hatchPickup.set(Value.kReverse);
    }

    /**
     * Extends hatch pistons to release a hatch panel
     */
    public void dropHatch() {
        hatchPickup.set(Value.kForward);
    }

    /**
     * Checks to see if the hatch intake is in the released position
     * @return True if released; false if grabbed
     */
    public Value hatchIntakeInReleasePosition() {
        return hatchPickup.get();
    }

    /**
     * Extends angle pistons to tip the intake up
     */
    public void tipIntakeUp() {
        intakeAngle.set(Value.kForward);
    }

    /**
     * Retracts angle pistons to tip the intake down
     */
    public void tipIntakeDown() {
        intakeAngle.set(Value.kReverse);
    }

    @Override
    protected void initDefaultCommand() {
    }
}
