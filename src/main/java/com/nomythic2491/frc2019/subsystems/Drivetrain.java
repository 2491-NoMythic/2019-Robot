package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.lib.drivers.TalonSRXFactory;
import com.nomythic2491.lib.drivers.VictorSPXFactory;
import com.nomythic2491.lib.util.DriveSignal;
import com.nomythic2491.frc2019.commands.Drivetrain.Drive;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {

    private static Drivetrain mInstance = null;
    private TalonSRX mLeftMaster, mRightMaster;
    private VictorSPX mLeftSlave, mRightSlave;
    private AHRS gyro;
    public StringBuilder velocity;
    private NetworkTable limelight;
    private NetworkTableEntry tx, ty, ta, tv;
    private Solenoid controlPins;

    public static Drivetrain getInstance() {
        if (mInstance == null) {
            mInstance = new Drivetrain();
        }
        return mInstance;
    }

    private Drivetrain() {
        
        // Start all Talons in open loop mode.
        mLeftMaster = TalonSRXFactory.createDefaultTalon(Constants.kLeftDriveMasterId);
        configureMaster(mLeftMaster, true);

        mLeftSlave = new VictorSPX(Constants.kLeftDriveSlaveId);
        mLeftSlave.configFactoryDefault();
        mLeftSlave.setInverted(true);
        mLeftSlave.follow(mLeftMaster);

        mRightMaster = TalonSRXFactory.createDefaultTalon(Constants.kRightDriveMasterId);
        configureMaster(mRightMaster, false);

        mRightSlave = new VictorSPX(Constants.kRightDriveSlaveId);
        mRightSlave.configFactoryDefault();
        mRightSlave.follow(mRightMaster);

        // Corrects sensor direction to match throttle direction
        mLeftMaster.setSensorPhase(true);
        mRightMaster.setSensorPhase(true); 

        /* Configures FPID constants for Talon's Velocity mode */
        setTalonPIDF(Constants.kVelocitykP, Constants.kVelocitykI, Constants.kVelocitykD, Constants.kVelocitykF);

        /**
         * Instantiates the gyro
         */
        //gyro = new AHRS(Port.kUSB);

        resetGyro();

        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        limelight.getEntry("ledMode").setNumber(0);
        limelight.getEntry("camMode").setNumber(0);
        tx = limelight.getEntry("tx");
        ty = limelight.getEntry("ty");
        ta = limelight.getEntry("ta");
        tv = limelight.getEntry("tv");
    }

    private void configureMaster(TalonSRX talon, boolean left) {
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, 100);
        final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
                100); // primary closed-loop, 100 ms timeout
        if (sensorPresent != ErrorCode.OK) {
            DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent,
                    false);
        }
        talon.setInverted(left);
        talon.setSensorPhase(true);
        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
        // talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms,
        // Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementWindow(1, Constants.kLongCANTimeoutMs);
        talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs);
        talon.configNeutralDeadband(0.04, 0);
    }

    private void setTalonPIDF(double proportional, double iterative, double derivative, double feedForward) {
        mLeftMaster.config_kP(Constants.kVelocitySlotId, proportional, Constants.kTimeoutMs);
        mRightMaster.config_kP(Constants.kVelocitySlotId, proportional, Constants.kTimeoutMs);

        mLeftMaster.config_kI(Constants.kVelocitySlotId, iterative, Constants.kTimeoutMs);
        mRightMaster.config_kI(Constants.kVelocitySlotId, iterative, Constants.kTimeoutMs);

        mLeftMaster.config_kD(Constants.kVelocitySlotId, derivative, Constants.kTimeoutMs);
        mRightMaster.config_kD(Constants.kVelocitySlotId, derivative, Constants.kTimeoutMs);

        mLeftMaster.config_kF(Constants.kVelocitySlotId, feedForward, Constants.kTimeoutMs);
        mRightMaster.config_kF(Constants.kVelocitySlotId, feedForward, Constants.kTimeoutMs);
    }

    /**
     * Drives each side of the robot at a given DriveSignal in a given ControlMode
     * 
     * @param mode  CTRE ControlMode for the talons
     * @param signal DriveSignal for the motors
     */
    public void driveDemand(ControlMode mode, DriveSignal signal) {
        mLeftMaster.set(mode, signal.getLeft());
        mRightMaster.set(mode, signal.getRight());
        mRightMaster.setNeutralMode(signal.getBrakeMode() ? NeutralMode.Brake : NeutralMode.Coast);
        mLeftMaster.setNeutralMode(signal.getBrakeMode() ? NeutralMode.Brake : NeutralMode.Coast);
    }

    /**
     * Stops the drivetrain wheels
     */
    public void stop() {
        driveDemand(ControlMode.PercentOutput, DriveSignal.BRAKE);
    }

    /**
     * Sets left and right encoders to 0
     */
    public void resetEncoders() {
        mLeftMaster.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
        mRightMaster.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
    }

    /**
     * @return The value of the left drive encoder in inches
     */
    public double getLeftEncoderDistance() {
        return mLeftMaster.getSelectedSensorPosition(0) * Constants.kDriveEncoderToInches;
    }

    /**
     * Gets the left encoder value in ticks (4096 per rotation)
     */
    public double getLeftEncoderDistanceRaw() {
        return mLeftMaster.getSelectedSensorPosition(0);
    }

    /**
     * Gets the right encoder value in ticks (4096 per rotation)
     * 
     * @return
     */
    public double getRightEncoderDistanceRaw() {
        return mRightMaster.getSelectedSensorPosition(0);
    }

    /**
     * @return The value of the right drive encoder in inches
     */
    public double getRightEncoderDistance() {
        return mRightMaster.getSelectedSensorPosition(0) * Constants.kDriveEncoderToInches;
    }

    /**
     * @return The average value of the two encoders in inches
     */
    public double getDistance() {
        return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2;
    }

    /**
     * @return The speed of the left motor in RPS
     */
    // public double getLeftEncoderRate() {
    // return mLeftMaster.getSelectedSensorVelocity(0) *
    // Constants.driveEncoderVelocityToRPS;
    // }

    /**
     * @return The speed of the right motor in RPS
     */
    // public double getRightEncoderRate() {
    // return mRightMaster.getSelectedSensorVelocity(0) *
    // Constants.driveEncoderVelocityToRPS;
    // }

    /**
     * 
     * @return The average speed of both motors in RPS
     */
    // public double getEncoderRate() {
    // return (getRightEncoderRate() + getLeftEncoderRate()) / 2;
    // }

    /**
     * @return The left driverail's velocity in NativeUnitsPer100Ms
     */
    public double getLeftVelocityRaw() {
        return mLeftMaster.getSelectedSensorVelocity(Constants.kVelocitySlotId);
    }

    /**
     * @return The right driverail's velocity in NativeUnitsPer100Ms
     */
    public double getRightVelocityRaw() {
        return mRightMaster.getSelectedSensorVelocity(Constants.kVelocitySlotId);
    }

    /**
     * Sets the value of the gyro to 0
     */
    public void resetGyro() {
        //gyro.reset();
    }

    /**
     * The gyro's angle from 0-360 degrees
     * 
     * @return
     */
    public double getGyroAngle() {
        return 0;//(gyro.getAngle() % 360 + 360) % 360;
    }

    public double getRawGyroAngle() {
        return 0;//gyro.getAngle();
    }

    /**
     * Gets the drivetrain gyro
     * 
     * @return The drivetrain gyro
     */
    // public AHRS getGyro() {
    //     return gyro;
    // }

    /**
     * Gives the displacement of the target from the center point of the limelight
     * on the x-axis
     * 
     * @return x-value of the target's position
     */
    public double getX() {
        return tx.getDouble(0);
    }

    /**
     * Gives the displacement of the target from the center point of the limelight
     * on the y-axis
     * 
     * @return y-value of the target's position
     */
    public double getY() {
        return ty.getDouble(0);
    }

    /**
     * Gives the portion of the camera that the target fills
     * 
     * @return size of target from 0 to 100
     */
    public double getArea() {
        return ta.getDouble(0);
    }

    /**
     * Says whether or not the limelight is tracking
     * 
     * @return 0 if not tracking or 1 if tracking
     */
    public boolean hasTarget() {
        return tv.getDouble(0) == 1;
    }

    /**
     * Sets the robot pipeline to the necessary setting
     */
    public void setVisionMode() {
        limelight.getEntry("ledMode").setNumber(0);
        limelight.getEntry("camMode").setNumber(0);
    }

    /**
     * Sets the driverstation pipeline to the necessary settings
     */
    public void setCameraMode() {
        limelight.getEntry("ledMode").setNumber(1);
        limelight.getEntry("camMode").setNumber(1);
    }
    
    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }
}