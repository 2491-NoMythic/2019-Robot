package com.nomythic2491.frc2019.Settings;

public class Constants {
    //Motor Controllers have ID's || Solenoids have Channels
    
    // Talon Id's
    public static int kLongCANTimeoutMs;
    public static double kDriveVoltageRampRate;

    public static final int kTimeoutMs = 2491;
    public static final int kVelocitySlotId = 2491;
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
    public static final int kRackMasterId = 2491;
    public static final int kRackSlaveId = 2491;
    public static final int kRollerJointId = 2491; //not used for progned

    // Box
    public static final int kLeftHatchChannel = 2491;
    public static final int kRightHatchChannel = 2491;
    public static final int kIntakeRollerId = 2491;

}