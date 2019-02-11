package com.nomythic2491.frc2019.Settings;

public class Constants {
    //Motor Controllers have ID's || Solenoids have Channels

    // Talons
    public static int kLongCANTimeoutMs;
    public static double kDriveVoltageRampRate;

    public static final int kTimeoutMs = 2491;
    public static final int kVelocitySlotId = 2491;
    public static final double kVelocitykP = 2491;
    public static final double kVelocitykI = 2491;
    public static final double kVelocitykD = 2491;
    public static final double kVelocitykF = 2491;

    // Drive
    public static final int kControlPinsChannel = 2491;

    public static int kRightDriveMasterId = 1;
    public static int kRightDriveSlaveId = 0;
    public static int kLeftDriveMasterId = 14;
    public static int kLeftDriveSlaveId = 15;

    public static final double kDriveEncoderToInches = 1 / Constants.kWheelDiameterInches * Math.PI / 4096.0;
    public static final double kWheelDiameterInches = 2491; // This is very, very important! Change this before trying to
                                                           // use the encoders to do anything.
    // Climber
    public static final int kPoleMasterId = 2491;
    public static final int kPoleSlaveId = 2491;
    public static final int kRollerJointId = 2491; //not used for progned
    public static final int kPoleEncoderDifference = 2491;
    public static final int kSkidChannel = 2491; 
    public static final int kBrakeChannel = 2491;
    public static final int kPCMCANID = 2491;

    public static final double kClimberEncoderToInches = 1;
    

    // Box
    public static final int kHatchOutChannel = 2491;
    public static final int kHatchInChannel = 2491;
    public static final int kIntakeRollerId = 2491;
    public static final int kRotator = 2491;
    public static final int kElevatorLeft = 2491;
    public static final int kElevatorRight = 2491;
    
    public static final double kHatchPickupPause = 0.25;
    //public static final double kElevatorGroundHeight = 0; //Limit switch will remove this
    public static final double kElevatorMaxHeight = 2491;
    public static final double kElevatorUncertainty = 2491;
    public static final double kElevatorVelocity = 2491;
    public static final double kBoxCargoShootSpeed = 2491;
    public static final double kCargoIntakeVelocity = 2491;
    public static final double kBoxRotateVelocity = 2491;
    public static final double kBoxTipDownPosition = 2491; //The position in encoder ticks of the lowest position of the intake rotator
    public static final double kBoxTipUpPosition = 2491; //The position in encoder ticks of the up position of the intake rotator
    public static final double kBoxTipBackPosition = 2491; //The position in encoder ticks of the back position of the intake rotator

    //Fork
    public static final int kElevator = 2491;
    public static final int kIntakeLeft = 2491;
    public static final int kIntakeRight = 2491;
    public static final int kLeftHatchInChannelBox = 2491;
    public static final int kLeftHatchOutChannelBox = 2491;
    public static final int kRightHatchOutChannelBox = 2491;
    public static final int kRightHatchInChannelBox = 2491;
    public static final int kPivotInChannel = 2491;
    public static final int kPivotOutChannel = 2491;

    //Controllers
    public static final int kDriverThrottleAxis = 1;
    public static final int kDriverTurnAxis = 2;
    public static final double kDeadband = 0.05;
    public static final int kQuickturnButton = 2;
}