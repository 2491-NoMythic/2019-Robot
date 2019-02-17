package com.nomythic2491.frc2019.Settings;

public class Constants {
    //Motor Controllers have ID's || Solenoids have Channels

    // Talons
    public static int kLongCANTimeoutMs;
    public static double kDriveVoltageRampRate = 2;

    public static final int kTimeoutMs = 100;
    public static final int kVelocitySlotId = 0;
    public static final double kVelocitykP = .5;
    public static final double kVelocitykI = .001;
    public static final double kVelocitykD = .001;
    public static final double kVelocitykF = 1000;

    // Drive
    public static final int kControlPinsChannel = 0;

    public static int kRightDriveMasterId = 10;
    public static int kRightDriveSlaveId = 11;
    public static int kLeftDriveMasterId = 1;
    public static int kLeftDriveSlaveId = 0;

    public static final double kDriveEncoderToInches = 1 / Constants.kWheelDiameterInches * Math.PI / 4096.0;
    public static final double kWheelDiameterInches = 2491; // This is very, very important! Change this before trying to
                                                           // use the encoders to do anything.
    // Climber
    public static final int kPoleMasterId = 2;
    public static final int kPoleSlaveId = 3;
    public static final int kClimberVelocity = 2491;
    public static final int kRollerJointId = 2491; //not used for progned
    public static final int kPoleEncoderDifference = 2491;
    public static final int kSkidChannel = 6; 
    public static final int kBrakeChannel = 3;
    public static final int kPCMCANID = 0;
    public static final double doneClimbingHeight = 2491;

    public static final double kClimberEncoderToInches = 1;
    

    // Box
    public static final int kHatchOutChannel = 4;
    public static final int kHatchInChannel = 5;
    public static final int kIntakeRollerId = 9;
    public static final int kRotator = 7;
    public static final int kElevatorLeft = 6;
    public static final int kElevatorRight = 8;
    
    public static final double kHatchPickupPause = 0.25;
    //public static final double kElevatorGroundHeight = 0; //Limit switch will remove this
    public static final double kElevatorMaxHeight = 2491;
    public static final double kElevatorUncertainty = 2491;
    public static final double kElevatorVelocity = 2491;
    public static final double kBoxCargoShootSpeed = 2491; //This will have to be negative (assuming kCargoIntakeVelocity is positive)
    public static final double kBoxCargoIntakeVelocity = -.75;
    public static final double kBoxRotateVelocity = 2491;
    public static final double kBoxTipDownPosition = 2491; //The position in encoder ticks of the lowest position of the intake rotator
    public static final double kBoxTipUpPosition = 2491; //The position in encoder ticks of the up position of the intake rotator
    public static final double kBoxTipBackPosition = 2491; //The position in encoder ticks of the back position of the intake rotator

    //Controllers
    public static final int kDriverThrottleAxis = 1;
    public static final int kDriverTurnAxis = 2;
    public static final double kDeadband = 0.05;
    public static final int kQuickturnButton = 1;
}