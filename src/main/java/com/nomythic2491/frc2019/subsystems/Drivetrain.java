package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.lib.drivers.TalonSRXFactory;
import com.nomythic2491.lib.drivers.VictorSPXFactory;
import com.nomythic2491.frc2019.commands.Drivetrain.Drive;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {

    private static Drivetrain mInstance;
    private TalonSRX mLeftMaster, mRightMaster;
    private VictorSPX mLeftSlave, mRightSlave;
    private AHRS gyro;
    public StringBuilder velocity;
    private NetworkTable limelight;
    private NetworkTableEntry tx, ty, ta, tv;
    private Solenoid controlPins;

    private Drivetrain() {
        
        // Start all Talons in open loop mode.
        mLeftMaster = TalonSRXFactory.createDefaultTalon(Constants.kLeftDriveMasterId);
        configureMaster(mLeftMaster, true);

        mLeftSlave = VictorSPXFactory.createPermanentSlaveTalon(Constants.kLeftDriveSlaveId,
                Constants.kLeftDriveMasterId);
        mLeftSlave.setInverted(false);

        mRightMaster = TalonSRXFactory.createDefaultTalon(Constants.kRightDriveMasterId);
        configureMaster(mRightMaster, false);

        mRightSlave = VictorSPXFactory.createPermanentSlaveTalon(Constants.kRightDriveSlaveId,
                Constants.kRightDriveMasterId);
        mRightSlave.setInverted(true);

        // Corrects sensor direction to match throttle direction
        mLeftMaster.setSensorPhase(true);
        mRightMaster.setSensorPhase(true);

        /* Configures FPID constants for Talon's Velocity mode */
        setTalonPIDF(Constants.kVelocitykP, Constants.kVelocitykI, Constants.kVelocitykD, Constants.kVelocitykF);

        /**
         * Instantiates the gyro
         */
        try {
            gyro = new AHRS(Port.kUSB);
        } catch (Exception e) {
            DriverStation.reportError("NavX instantiation failure! Check gyro USB cable", Variables.debugMode);

            if (Variables.debugMode) {
                System.out.println(e);
            }
        }

        resetGyro();

        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        limelight.getEntry("ledMode").setNumber(0);
        limelight.getEntry("camMode").setNumber(0);
        tx = limelight.getEntry("tx");
        ty = limelight.getEntry("ty");
        ta = limelight.getEntry("ta");
        tv = limelight.getEntry("tv");
    }

    public static Drivetrain getInstance() {
        if (mInstance == null) {
            mInstance = new Drivetrain();
        }
        return mInstance;
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

    private void setTalonPIDF(double proportional, double iterative, double derivative, double feedForward) {
        mLeftMaster.config_kP(Constants.kVelocitySlotId, proportional, Constants.kTimeoutMs);
        mRightMaster.config_kP(Constants.kVelocitySlotId, proportional, Constants.kTimeoutMs);

        mLeftMaster.config_kI(Constants.kVelocitySlotId, iterative, Constants.kTimeoutMs);
        mRightMaster.config_kI(Constants.kVelocitySlotId, iterative, Constants.kTimeoutMs);

        mLeftMaster.config_kD(Constants.kVelocitySlotId, derivative, Constants.kTimeoutMs);
        mRightMaster.config_kD(Constants.kVelocitySlotId, derivative, Constants.kTimeoutMs);
    }

    public void setTalonF(double feedForward) {
        mLeftMaster.config_kF(Constants.kVelocitySlotId, feedForward, Constants.kTimeoutMs);
        mRightMaster.config_kF(Constants.kVelocitySlotId, feedForward, Constants.kTimeoutMs);
    }

    /**
     * Drives both sides of the robot at the same speed
     * 
     * @param speed Speed of all drivetrain wheels in inches per second
     */
    public void driveVelocity(double speed) {
        driveLeftVelocity(speed);
        driveRightVelocity(speed);
    }

    /**
     * Drives each side of the robot at different speeds
     * 
     * @param leftSpeed  Speed of left wheels in inches per second
     * @param rightSpeed Speed of right wheels in inches per second
     */
    public void driveVelocity(double leftSpeed, double rightSpeed) {
        driveLeftVelocity(leftSpeed);
        driveRightVelocity(rightSpeed);
    }

    /**
     * Drives the left side of the robot
     * 
     * @param speed Speed of left wheels in inches per second
     */
    public void driveLeftVelocity(double speed) {
        mLeftMaster.set(ControlMode.Velocity, speed);
    }

    /**
     * Drives the right side of the robot
     * 
     * @param speed Speed of the right wheels in inches per second
     */
    public void driveRightVelocity(double speed) {
        mRightMaster.set(ControlMode.Velocity, speed);
    }

    /**
     * Drives both sides of the robot at the same speed, ranging from -1 to 1
     * 
     * @param speed Speed of all drivetrain wheels, ranging from -1 to 1
     */
    public void drivePercentOutput(double speed) {
        drivePercentOutput(speed, speed);
    }

    /**
     * Drives each side of the robot at a given speed, ranging from -1 to 1
     * 
     * @param leftSpeed  Speed of the left wheels from -1 to 1
     * @param rightSpeed Speed of the right wheels from -1 to 1
     */
    public void drivePercentOutput(double leftSpeed, double rightSpeed) {
        driveLeftPercentOutput(leftSpeed);
        driveRightPercentOutput(rightSpeed);
    }

    /**
     * Drives the left side of the robot at a given speed, ranging from -1 to 1
     * 
     * @param speed Speed of the left wheels from -1 to 1
     */
    public void driveLeftPercentOutput(double speed) {
        mLeftMaster.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Drives the right side of the robot at a given speed, ranging from -1 to 1
     * 
     * @param speed Speed of the right wheels from -1 to 1
     */
    public void driveRightPercentOutput(double speed) {
        mRightMaster.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Stops the drivetrain wheels
     */
    public void stop() {
        drivePercentOutput(0);
    }

    /**
     * Sets left and right encoders to 0
     */
    public void resetEncoders() {
        resetLeftEncoder();
        resetRightEncoder();
    }

    /**
     * Sets the left encoder to 0
     */
    public void resetLeftEncoder() {
        mLeftMaster.setSelectedSensorPosition(0, Constants.kVelocitySlotId, Constants.kTimeoutMs);
    }

    /**
     * Sets the right encoder to 0
     */
    public void resetRightEncoder() {
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
        gyro.reset();
    }

    /**
     * The gyro's angle from 0-360 degrees
     * 
     * @return
     */
    public double getGyroAngle() {
        return (gyro.getAngle() % 360 + 360) % 360;
    }

    public double getRawGyroAngle() {
        return gyro.getAngle();
    }

    /**
     * Gets the drivetrain gyro
     * 
     * @return The drivetrain gyro
     */
    public AHRS getGyro() {
        return gyro;
    }

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

    public void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }
}