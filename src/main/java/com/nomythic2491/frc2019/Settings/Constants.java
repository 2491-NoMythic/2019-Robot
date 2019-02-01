package com.nomythic2491.frc2019.Settings;

public class Constants {
    //Motor Controllers have ID's || Solenoids have Channels
    
    // Talon Id's
    public static int kLongCANTimeoutMs;
    public static double kDriveVoltageRampRate;

    public static final int kTimeoutMs = 10;
    public static final int kVelocitySlotId = 0;
    public static final double kVelocitykP = 2491;
    public static final double kVelocitykI = 2491;
    public static final double kVelocitykD = 2491;
    public static final double kVelocitykF = 2491;

    // Drive
    public static int kRightDriveMasterId = 2491;
    public static int kRightDriveSlaveId = 2491;
    public static int kLeftDriveMasterId = 2491;
    public static int kLeftDriveSlaveId = 2491;

    public static final double kDriveEncoderToInches = 1 / Constants.kWheelDiameterInches * Math.PI / 4096.0;
    public static final double kWheelDiameterInches = 2491; // This is very, very important! Change this before trying to
                                                           // use the encoders to do anything.

    // Climber
    public static final int kPoleMasterId = 2491;
    public static final int kPoleSlaveId = 2491;
    public static final int kRollerJointId = 2491; //not used for progned
    public static final int kPoleEncoderDifference = 2491;

    public static final double kClimberEncoderToInches = 1;

    // Box
    public static final int kLeftHatchOutChannel = 2491;
    public static final int kLeftHatchInChannel = 2491;
    public static final int kRightHatchInChannel = 2491;
    public static final int kRightHatchOutChannel = 2491;
    public static final int kIntakeRollerId = 2491;
    public static final int kRotator = 2491;
    public static final int kElevatorLeft = 2491;
    public static final int kElevatorRight = 2491;

}