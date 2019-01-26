package com.nomythic2491.frc2019.Settings;

public class Constants {

    // Talon Id's
    public static int kRightDriveMasterId;
    public static int kRightDriveSlaveId;
    public static int kLeftDriveMasterId;
    public static int kLeftDriveSlaveId;

    public static int kLongCANTimeoutMs;
    public static double kDriveVoltageRampRate;

    // Drive
    public static final int driveTalonLeftChannel = 2491;
    public static final int driveTalonRightChannel = 2491;
    public static final int driveVictorLeftChannel = 2491;
    public static final int driveVictorRightChannel = 2491;

    public static final double driveEncoderToInches = 1 / Constants.wheelDiameterInches * Math.PI / 4096.0;
    public static final double wheelDiameterInches = 2491; // This is very, very important! Change this before trying to
                                                           // use the encoders to do anything.

    // Climber
    public static final int climberRodTalonLeftChannel = 2491;
    public static final int climberRodTalonRightChannel = 2491;
    public static final int climberRollerTalonChannel = 2491;

    // Box
    public static final int hatchClawSolenoidLeftChannel = 2491;
    public static final int hatchClawSolenoidRightChannel = 2491;
    public static final int rollerTalonChannel = 2491;

    // TalonSRX
    public static final int kTimeoutMs = 2491;
    public static final int kVelocitySlotId = 2491;
    public static final double kVelocitykP = 2491;
    public static final double kVelocitykI = 2491;
    public static final double kVelocitykD = 2491;
    public static final double kVelocitykF = 2491;

}