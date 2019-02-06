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

public class Fork extends Subsystem {

    private static Fork instance;
    private TalonSRX elevator, intakeLeft, intakeRight;
    private DoubleSolenoid hatchLeft, hatchRight, pivotLeft, pivotRight;
    public boolean isElevatorRising;

    public static Fork getInstance() {
        if (instance == null) {
            instance = new Fork();
        }
        return instance;
    }

    private Fork() {
        elevator = TalonSRXFactory.createDefaultTalon(Constants.kElevator);
        intakeLeft = TalonSRXFactory.createDefaultTalon(Constants.kElevatorLeft);
        configureMaster(intakeLeft, true);
        intakeRight = TalonSRXFactory.createPermanentSlaveTalon(Constants.kElevatorRight, Constants.kElevatorLeft);
        hatchLeft = new DoubleSolenoid(Constants.kLeftHatchOutChannelBox, Constants.kLeftHatchInChannelBox);
        hatchRight = new DoubleSolenoid(Constants.kRightHatchOutChannelBox, Constants.kRightHatchInChannelBox);
        pivotLeft = new DoubleSolenoid(Constants.kLeftPivotOutChannel, Constants.kLeftPivotInChannel);
        pivotRight = new DoubleSolenoid(Constants.kRightPivotOutChannel, Constants.kRightPivotInChannel);
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
    * @param speed How fast it goes on a scale of -1 to 1
    */
    public void runIntake(double speed) {
        intakeLeft.set(ControlMode.PercentOutput, speed);
    }

    /**
    * Elevates the intake at a specified speed
    * @param speed How fast it goes on a scale of -1 to 1
    */
    public void elevateIntake(double speed) {
        elevator.set(ControlMode.PercentOutput, speed);
    }

    public double getElevatorHeight() {
        return elevator.getSelectedSensorPosition(0);
    }

    /**
     * Toggles the double solenoids responsible for picking up hatches
     * @param position Is available in either kForward or kReverse
     */
    public void hatchIntake(Value position) {
        hatchLeft.set(position);
        hatchRight.set(position);
    }

    /**
     * Toggles the double solenoids responsible for pivoting the intake device
     * @param position Is available in either kForward or kReverse
     */
    public void pivotIntake(Value position) {
        pivotLeft.set(position);
        pivotRight.set(position);
    }

    /**
    * Stops intake motors
    */
    public void stopIntake() {
        runIntake(0);
    }

    /**
    * Stops the elevator from moving
    */
    public void stopElevator(){
        elevateIntake(0);
    }

    public void resetElevatorEncoder() {
        elevator.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
      }

    @Override
    protected void initDefaultCommand() {
    }
}