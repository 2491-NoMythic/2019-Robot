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
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
import com.nomythic2491.frc2019.Settings.Constants.kMF;
import com.nomythic2491.frc2019.commands.MagicFork.GamepieceLoop;
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

    private TalonSRX intakeRoller, elevator;
    private DoubleSolenoid hatchPickup, intakeAngle;
    private Solenoid controlPins;

    private AnalogInput cargoSensor, hatchSensor;

    private static MagicFork mInstance = null;

    public static MagicFork getInstance() {
        if (mInstance == null) {
            mInstance = new MagicFork();
        }
        return mInstance;
    }

    private MagicFork() {
        hatchPickup = new DoubleSolenoid(kMF.kHatchReleaseChannel, kMF.kHatchGrabChannel);
        intakeAngle = new DoubleSolenoid(kMF.kIntakeDownChannel, kMF.kIntakeUpChannel);
        controlPins = new Solenoid(kMF.kControlPinChannel);

        intakeRoller = TalonSRXFactory.createDefaultTalon(kMF.kIntakeRollerId);

        elevator = TalonSRXFactory.createDefaultTalon(kMF.kElevatorId);
        configureMaster(elevator, false);

        elevator.config_kP(Constants.kVelocitySlot, kMF.kElevatorP, Constants.kLongCANTimeoutMs);
        elevator.config_kI(Constants.kVelocitySlot, kMF.kElevatorI, Constants.kLongCANTimeoutMs);
        elevator.config_kD(Constants.kVelocitySlot, kMF.kElevatorD, Constants.kLongCANTimeoutMs);
        elevator.config_kF(Constants.kVelocitySlot, kMF.kElevatorF, Constants.kLongCANTimeoutMs);
        elevator.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kLongCANTimeoutMs);
        elevator.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kLongCANTimeoutMs);
        elevator.configMotionCruiseVelocity(1600, Constants.kLongCANTimeoutMs);
        elevator.configMotionAcceleration(6400, Constants.kLongCANTimeoutMs);

        elevator.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        elevator.configClearPositionOnLimitR(true, Constants.kLongCANTimeoutMs);
        elevator.setSelectedSensorPosition(0);

        cargoSensor = new AnalogInput(kMF.kCargoSensor);
        hatchSensor = new AnalogInput(kMF.kHatchSensor);

        double cargoSensorVolts = cargoSensor.getVoltage();
        double hatchSensorVolts = hatchSensor.getVoltage();
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
     * Runs the intake at a given velocity
     * 
     * @param speed The velocity the intake is run at
     */
    public void runIntake(double speed) {
        intakeRoller.set(ControlMode.PercentOutput, speed);
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
     * 
     * @return True if released; false if grabbed
     */
    public Value hatchIntakeInReleasePosition() {
        return hatchPickup.get();
    }

    /**
     * Drops the control pins
     */
    public void dropControlPins() {
        controlPins.set(true);
    }

    /**
     * Raises the control pins
     */
    public void raiseControlPins() {
        controlPins.set(false);
    }

    /**
     * Checks to see if control pins are down
     * 
     * @return True if down, false if retracted
     */
    public boolean areControlPinsDown() {
        return controlPins.get();
    }

    // /**
    // * Checks to see if the intake is in the tilted up position
    // * @return True if up; false if down
    // */
    // public boolean isIntakeTippedUp() {
    // return intakeAngle.get();
    // }

    /**
     * Extends angle pistons to tip the intake up
     */
    public void tipIntake(boolean up) {
        intakeAngle.set(up ? Value.kForward : Value.kReverse);
    }

    public void runIoCargo(IoCargo demand) {
        // this needs code!
        intakeRoller.set(ControlMode.PercentOutput, demand.getSpeed());
    }

    /**
     * Runs magic fork elevator at given speed
     * 
     * @param speed speed elevator runs at from -1 to 1
     */
    public void runElevator(double speed) {
        elevator.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Moves the elevator to a set point
     * 
     * @param setpoint The point the elevator will move to
     */
    private void elevateToPoint(double setpoint) {
        elevator.set(ControlMode.MotionMagic, setpoint);
    }

    /**
     * I don't know what da heck this does. Okay, I can make an educated guess, but
     * still (this is why we need java docs on everything-- so people looking at old
     * code can know what it does)
     * 
     * @param demand eh?
     */
    public void GamepieceDemand(GamepieceDemand demand) {
        if (demand != GamepieceDemand.Hold) {
            elevateToPoint(demand.getHeightPoint());
        }
    }

    /**
     * Checks to see if the motion profile is finished
     * 
     * @return if motion profile has finished
     */
    public boolean getIsElevatorRunningMotionProfile() {
        return elevator.isMotionProfileFinished();
    }

    // /**
    // * Using this to return the voltage of the cargo sensor
    // */
    // public void testCargoSensor() {
    // System.out.println(cargoSensorVolts);
    // }

    // /**
    // * Determines whether or not the cargo sensor senses a cargo
    // * @return Whether or not the volts is above 0.83 (1.33v when cargo is in,
    // 0.33 when it isn't)
    // */
    // public boolean isCargoIn() {
    // return cargoSensorVolts > 0.83;
    // }

    // /**
    // * Using this to return the voltage of the hatch sensor
    // */
    // public void testHatchSensor() {
    // System.out.println(hatchSensorVolts);
    // }

    // /**
    // * Determines whether or not the hatch sensor senses a cargo
    // * @return Whether or not the volts is above 0.83 (1.33v when hatch is in,
    // 0.33 when it isn't)
    // */
    // public boolean isHatchIn() {
    // return hatchSensorVolts > 0.83;
    // }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new GamepieceLoop());
    }
}
