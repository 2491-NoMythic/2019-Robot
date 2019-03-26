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
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.ControlBoard;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
import com.nomythic2491.frc2019.Settings.Constants.kMF;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The system of talons, solenoids and sensors that make up the Magic Fork
 * subsystem..
 */
public class MagicFork extends Subsystem {

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new);
    }

    private static MagicFork mInstance = null;

    public static MagicFork getInstance() {
        if (mInstance == null) {
            mInstance = new MagicFork();
        }
        return mInstance;
    }

    ControlBoard mBoard = ControlBoard.getInstance();

    @Override
    public void periodic() {
        GamepieceDemand(mBoard.getGamepieceDemand(), mBoard.getElevotrOverride());
        runIoCargo(mBoard.getIoCargo());
        tipIntake(mBoard.getTipIntake());
        engageControlPins(mBoard.runControlPins());
        releaseHatch(mBoard.getHatch());
    }

    private TalonSRX mIntakeRoller, mCaridge;
    private DoubleSolenoid hatchPickup, intakeAngle;
    private Solenoid controlPins;
    private AnalogInput cargoPresent, hatchPresent;

    private MagicFork() {

        /* Pnematics */
        hatchPickup = new DoubleSolenoid(kMF.kHatchReleaseChannel, kMF.kHatchGrabChannel);
        intakeAngle = new DoubleSolenoid(kMF.kIntakeDownChannel, kMF.kIntakeUpChannel);
        controlPins = new Solenoid(kMF.kControlPinChannel);

        /* Sensors */
        cargoPresent = new AnalogInput(0);
        hatchPresent = new AnalogInput(1);

        /* Intake Roller */
        mIntakeRoller = TalonSRXFactory.createDefaultTalon(kMF.kIntakeRollerId);

        /* Caridge */
        mCaridge = TalonSRXFactory.createDefaultTalon(kMF.kElevatorId);
        configureMaster(mCaridge, false);

        mCaridge.config_kP(Constants.kPrimarySlot, kMF.kElevatorP, Constants.kLongCANTimeoutMs);
        mCaridge.config_kI(Constants.kPrimarySlot, kMF.kElevatorI, Constants.kLongCANTimeoutMs);
        mCaridge.config_kD(Constants.kPrimarySlot, kMF.kElevatorD, Constants.kLongCANTimeoutMs);
        mCaridge.config_kF(Constants.kPrimarySlot, kMF.kElevatorF, Constants.kLongCANTimeoutMs);

        mCaridge.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
        mCaridge.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);

        mCaridge.configMotionCruiseVelocity(1600, Constants.kLongCANTimeoutMs);
        mCaridge.configMotionAcceleration(6400, Constants.kLongCANTimeoutMs);

        mCaridge.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
                Constants.kLongCANTimeoutMs);
        mCaridge.configClearPositionOnLimitR(true, Constants.kLongCANTimeoutMs);
        mCaridge.setSelectedSensorPosition(0);
    }

    private void configureMaster(TalonSRX talon, boolean left) {
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
        // primary closed-loop
        final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
                Constants.kLongCANTimeoutMs);
        if (sensorPresent != ErrorCode.OK) {
            DriverStation.reportError("Could not detect " + (left ? "left" : "right") + "encoder: " + sensorPresent,
                    false);
        }
        talon.setInverted(left);
        talon.setSensorPhase(true);
        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_25Ms, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementWindow(8, Constants.kLongCANTimeoutMs);
        talon.configClosedloopRamp(.25, Constants.kLongCANTimeoutMs);
        talon.configNeutralDeadband(0.04, Constants.kLongCANTimeoutMs);
    }

    /**
     * I don't know what da heck this does. Okay, I can make an educated guess, but
     * still (this is why we need java docs on everything-- so people looking at old
     * code can know what it does)
     * 
     * @param demand eh?
     */
    public void GamepieceDemand(GamepieceDemand demand, double override) {
        if (demand == GamepieceDemand.Override) {
            mCaridge.set(ControlMode.PercentOutput, override * 0.6);
        } else if (demand != GamepieceDemand.Hold) {
            mCaridge.set(ControlMode.MotionMagic, demand.getHeightPoint());
        }
    }

    public void runIoCargo(IoCargo demand) {
        if (!isCargoIn()) {
            mIntakeRoller.set(ControlMode.PercentOutput, demand.getSpeed());
        } else {
            mIntakeRoller.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Retracts hatch pistons to grab a hatch panel
     */
    public void releaseHatch(boolean release) {
        hatchPickup.set(release ? Value.kForward : Value.kReverse);
    }

    /**
     * Checks to see if the hatch intake is in the released position
     * 
     * @return True if released; false if grabbed
     */
    public boolean isHatchHeld() {
        return hatchPickup.get() == Value.kReverse ? true : false;
    }

    /**
     * Drops the control pins
     */
    public void engageControlPins(boolean down) {
        controlPins.set(down);
    }

    /**
     * Checks to see if control pins are down
     * 
     * @return True if down, false if retracted
     */
    public boolean areControlPinsDown() {
        return controlPins.get();
    }

    /**
     * Extends angle pistons to tip the intake up
     */
    public void tipIntake(boolean up) {
        intakeAngle.set(up ? Value.kReverse : Value.kForward);
    }

    /**
     * Checks to see if the intake is in the tilted up position
     * 
     * @return True if up; false if down
     */
    public boolean isIntakeTippedUp() {
        return intakeAngle.get() == Value.kReverse ? true : false;
    }

    /**
     * Determines whether or not the cargo sensor senses a cargo
     * 
     * @return Whether or not the volts is above 0.83 (1.33v when cargo is in, 0.33
     *         when it isn't)
     */
    public boolean isCargoIn() {
        return cargoPresent.getVoltage() > 0.83;
    }

    /**
     * Determines whether or not the hatch sensor senses a cargo
     * 
     * @return Whether or not the volts is above 0.83 (1.33v when hatch is in, 0.33
     *         when it isn't)
     */
    public boolean isHatchIn() {
        return hatchPresent.getVoltage() > 0.83;
    }
}
