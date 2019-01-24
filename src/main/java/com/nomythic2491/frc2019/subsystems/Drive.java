package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.nomythic2491.frc2019.Constants;
import com.nomythic2491.lib.drivers.TalonSRXFactory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drive extends Subsystem {

    private static Drive mInstance;
    private TalonSRX mLeftMaster, mLeftSlave, mRightMaster, mRightSlave;

    private void configureMaster(TalonSRX talon, boolean left) {
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, 100);
        final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice
                .CTRE_MagEncoder_Relative, 0, 100); //primary closed-loop, 100 ms timeout
        if (sensorPresent != ErrorCode.OK) {
            DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent, false);
        }
        talon.setInverted(!left);
        talon.setSensorPhase(true);
        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
        talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
        talon.configNeutralDeadband(0.04, 0);
}
    private Drive() {
        // Start all Talons in open loop mode.
        mLeftMaster = TalonSRXFactory.createDefaultTalon(Constants.kLeftDriveMasterId);
        configureMaster(mLeftMaster, true);

        mLeftSlave = TalonSRXFactory.createPermanentSlaveTalon(Constants.kLeftDriveSlaveId,
                Constants.kLeftDriveMasterId);
        mLeftSlave.setInverted(false);

        mRightMaster = TalonSRXFactory.createDefaultTalon(Constants.kRightDriveMasterId);
        configureMaster(mRightMaster, false);

        mRightSlave = TalonSRXFactory.createPermanentSlaveTalon(Constants.kRightDriveSlaveId,
                Constants.kRightDriveMasterId);
        mRightSlave.setInverted(true);
    }

    public static Drive getInstance() {
        return mInstance;
}
    @Override
    protected void initDefaultCommand() {

    }

}