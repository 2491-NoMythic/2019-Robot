package com.nomythic2491.frc2019.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.frc2019.Settings.Constants.kDrive;
import com.nomythic2491.frc2019.commands.Drivetrain.DriveLoop;
import com.nomythic2491.lib.drivers.TalonSRXFactory;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Drivetrain extends PIDSubsystem {

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DriveLoop());
    }

    private static Drivetrain mInstance = null;

    public static Drivetrain getInstance() {
        if (mInstance == null) {
            mInstance = new Drivetrain();
        }
        return mInstance;
    }

    private TalonSRX mLeftMaster, mRightMaster;
    private VictorSPX mLeftSlave, mRightSlave;
    private AHRS gyro;
    public StringBuilder velocity;
    private NetworkTable limelight;
    private NetworkTableEntry tx, ty, ta, tv;
    private boolean mBreak;

    private Drivetrain() {

        super("Drive", Variables.proportionalRotate,Variables.integralRotate, Variables.derivativeRotate);

        // Start all Talons in open loop moded.
        mLeftMaster = TalonSRXFactory.createDefaultTalon(kDrive.kLeftDriveMasterId);
        configureMaster(mLeftMaster, false);

        mLeftSlave = new VictorSPX(kDrive.kLeftDriveSlaveId);
        mLeftSlave.configFactoryDefault();
        mLeftSlave.follow(mLeftMaster);
        mLeftSlave.setInverted(InvertType.FollowMaster);

        mRightMaster = TalonSRXFactory.createDefaultTalon(kDrive.kRightDriveMasterId);
        configureMaster(mRightMaster, true);

        mRightSlave = new VictorSPX(kDrive.kRightDriveSlaveId);
        mRightSlave.configFactoryDefault();
        mRightSlave.follow(mRightMaster);
        mRightSlave.setInverted(InvertType.FollowMaster);

        /**
         * Instantiates the gyro
         */
        gyro = new AHRS(Port.kUSB);

        resetGyro();

        limelight = NetworkTableInstance.getDefault().getTable("limelight");
        limelight.getEntry("ledMode").setNumber(0);
        limelight.getEntry("camMode").setNumber(0);
        tx = limelight.getEntry("tx");
        ty = limelight.getEntry("ty");
        ta = limelight.getEntry("ta");
        tv = limelight.getEntry("tv");
        
        mBreak = false;
    }

    private void configureMaster(TalonSRX talon, boolean left) {
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5, Constants.kLongCANTimeoutMs);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
        // primary closed-loop, 100 ms timeout
        final ErrorCode sensorPresent = talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
                Constants.kLongCANTimeoutMs);
        if (sensorPresent != ErrorCode.OK) {
            DriverStation.reportError("Could not detect " + (left ? "left" : "right") + " encoder: " + sensorPresent,
                    false);
        }
        talon.setInverted(left);
        talon.setSensorPhase(true);
        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_25Ms, Constants.kLongCANTimeoutMs);
        talon.configVelocityMeasurementWindow(2, Constants.kLongCANTimeoutMs);
        talon.configClosedloopRamp(Constants.kDriveVoltageRampRate, Constants.kLongCANTimeoutMs); 
        talon.config_kP(Constants.kVelocitySlot, kDrive.kDrivekP, Constants.kLongCANTimeoutMs);
        talon.config_kI(Constants.kVelocitySlot, kDrive.kDrivekI, Constants.kLongCANTimeoutMs);
        talon.config_kD(Constants.kVelocitySlot, kDrive.kDrivekD, Constants.kLongCANTimeoutMs);
        talon.config_kF(Constants.kVelocitySlot, kDrive.kDrivekF, Constants.kLongCANTimeoutMs);
        talon.configOpenloopRamp(.4, Constants.kLongCANTimeoutMs);
    }

    /**
     * Drives each side of the robot at a given DriveSignal in a given ControlMode
     * 
     * @param mode   CTRE ControlMode for the talons
     * @param signal DriveSignal for the motors
     */
    public void driveDemand(ControlMode mode, DriveSignal signal) {
        if (mBreak != signal.getBrakeMode()) {
            NeutralMode neutral = signal.getBrakeMode() ? NeutralMode.Brake : NeutralMode.Coast;
            mRightMaster.setNeutralMode(neutral);
            mRightSlave.setNeutralMode(neutral);
            mLeftMaster.setNeutralMode(neutral);
            mLeftSlave.setNeutralMode(neutral);
            mBreak = signal.getBrakeMode();
        }

        mLeftMaster.set(mode, signal.getLeft());
        mRightMaster.set(mode, signal.getRight());  
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
        mLeftMaster.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
        mRightMaster.setSelectedSensorPosition(0, Constants.kVelocitySlot, Constants.kTimeoutMs);
    }

    /**
     * @return The value of the left drive encoder in inches
     */
    public double getLeftEncoderDistance() {
        return getLeftEncoderDistanceRaw() * kDrive.kDriveEncoderToInches;
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
        return getRightEncoderDistanceRaw() * kDrive.kDriveEncoderToInches;
    }

    /**
     * @return The average value of the two encoders in inches
     */
    public double getDistance() {
        return (getRightEncoderDistance() + getLeftEncoderDistance()) / 2;
    }

    /**
     * @return The left driverail's velocity in NativeUnitsPer100Ms
     */
    public double getLeftVelocityRaw() {
        return mLeftMaster.getSelectedSensorVelocity(Constants.kVelocitySlot);
    }

    /**
     * @return The right driverail's velocity in NativeUnitsPer100Ms
     */
    public double getRightVelocityRaw() {
        return mRightMaster.getSelectedSensorVelocity(Constants.kVelocitySlot);
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
        return (getRawGyroAngle() % 360 + 360) % 360;
    }

    public double getRawGyroAngle() {
        return gyro.getAngle();
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

    public void positon() {
        System.out.println("Right: " + mRightMaster.getSelectedSensorPosition(0) + " Rerror: " + mRightMaster.getClosedLoopError() 
        + "Left: " + mRightMaster.getSelectedSensorPosition(0) + "Lerror: " + mRightMaster.getClosedLoopError());
      }

      @Override
      protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return getGyroAngle(); 
      }
    
      @Override
      protected void usePIDOutput(double output) {
          driveDemand(ControlMode.PercentOutput, new DriveSignal(0.8 * output, -0.8 *output));
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
      }
}