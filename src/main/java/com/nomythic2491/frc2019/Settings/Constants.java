package com.nomythic2491.frc2019.Settings;

public class Constants {
    // Motor Controllers have ID's || Solenoids have Channels

    // Talons
    public static final int kTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;
    public static final double kDriveVoltageRampRate = 2;
    public static final int kPrimarySlotIdx = 0;
    public static final int kVelocitySlotId = 2491; //Make this not 2491

    // Drive
    public static final int kRightDriveMasterId = 10;
    public static final int kRightDriveSlaveId = 11;
    public static final int kLeftDriveMasterId = 1;
    public static final int kLeftDriveSlaveId = 0;

    public static final double kDrivekP = .5;
    public static final double kDrivekI = .001;
    public static final double kDrivekD = .001;
    public static final double kDrivekF = 1000;

    public static final double kDriveEncoderToInches = 1 / Constants.kWheelDiameterInches * Math.PI / 4096.0;
    public static final double kWheelDiameterInches = 2491; // This is very, very important! Change this before trying
                                                            // to
                                                            // use the encoders to do anything.
    // Climber
    public static final int kPoleMasterId = 2;
    public static final int kPoleSlaveId = 3;

    public static final int kSkidsChannel = 6;
    public static final int kRatchetChannel = 3;

    public static final int kClimberVelocity = 2491;
    public static final double doneClimbingHeight = 2491;

    public static final double kClimberEncoderToInches = 1;

    // Box
    public static final int kRollerId = 9;
    public static final int kRotatorId = 7;
    public static final int kElevatorMasterId = 6;
    public static final int kElevatorSlaveId = 8;

    public static final int kHatchOutChannel = 4;
    public static final int kHatchInChannel = 5;

    public static final int kBumperInChannel = 1; 
    public static final int kBumperOutChannel = 0;

    public static final double kHatchPickupPause = 0.25;
    // public static final double kElevatorGroundHeight = 0; //Limit switch will
    // remove this
    public static final double kElevatorMaxHeight = 2491;
    public static final double kElevatorUncertainty = 2491;
    public static final double kElevatorVelocity = 2491;
    public static final double kBoxCargoShootSpeed = 2491; // This will have to be negative (assuming
                                                           // kCargoIntakeVelocity is positive)
    public static final double kBoxCargoIntakeVelocity = -.75;
    public static final double kBoxRotateVelocity = 2491;
    public static final double kBoxTipDownPosition = 2491; // The position in encoder ticks of the lowest position of
                                                           // the intake rotator
    public static final double kBoxTipUpPosition = 2491; // The position in encoder ticks of the up position of the
                                                         // intake rotator
    public static final double kBoxTipBackPosition = 2491; // The position in encoder ticks of the back position of the
                                                           // intake rotator


    //Fork
    public static final int kElevator = 2491;
    public static final int kElevatorLeft = 2491;
    public static final int kElevatorRight = 2491;
    public static final int kRightHatchOutChannelBox = 2491;
    public static final int kRightHatchInChannelBox = 2491;
    public static final int kPivotOutChannel = 2491;
    public static final int kPivotInChannel = 2491;

    public static final int kForkCargoIntakeVelocity = 2491;
    

    // Controllers
    public static final double kDeadband = 0.05;

    public static final class ArcadeDriver {
        public static final int kId = 0;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
        public static final int kCargoInButton = 3;
        public static final int kCargoOutButton = 5;
        public static final int kKillSwitchButton = 11;
        public static final int kKillSwitchButton2 = 12;
    }

    public static final class JoysticOpertator {
        public static final int kId = 1;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
    }
    public static final class PS4Operator {
        public static final int kId = 1;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
    }
}