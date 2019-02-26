package com.nomythic2491.frc2019.Settings;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Constants {
    // Motor Controllers have ID's || Solenoids have Channels

    public enum GamepeiceDemand {
        Test(0, -500), CargoOut_Ship(21, -1100), CargoFloor(5.2, -500), Hold(0, 0), Stow(21, -1500);

        private double mHeightPoint;
        private double mAnglePoint;

        private GamepeiceDemand(double hight, double angle) {
            mHeightPoint = hight / Math.PI * 4096;
            mAnglePoint = angle; // (angle * 4096)/360;
        }

        public double getHeightPoint() {
            return mHeightPoint;
        }

        public double getAnglePoint() {
            return mAnglePoint;
        }
    }

    public enum IoCargo {
        Out(.75), In(-.75), Stop(0);

        private double mSpeed;

        private IoCargo(double speed) {
            mSpeed = speed;
        }

        public double getSpeed() {
            return mSpeed;
        }
    }

    public enum ClimberDemand {
        Climb(-1, NeutralMode.Brake, true), Reset(1, NeutralMode.Coast, false), Stop(0, NeutralMode.Brake, false);

        double mSpeed;
        NeutralMode mBrake;
        boolean mRatchet;

        private ClimberDemand(double speed, NeutralMode brake, boolean ratchet) {
            mSpeed = speed; // hight / (1.5 * Math.PI) * 4096;
            mBrake = brake;
            mRatchet = ratchet;
        }

        public double getSpeed() {
            return mSpeed;
        }

        public NeutralMode getBrake() {
            return mBrake;
        }

        public boolean getRatchet() {
            return mRatchet;
        }
    }

    // Talons
    public static final int kTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;
    public static final double kDriveVoltageRampRate = 2;
    public static final int kVelocitySlot = 0;

    // Drive
    public static final int kRightDriveMasterId = 10;
    public static final int kRightDriveSlaveId = 11;
    public static final int kLeftDriveMasterId = 1;
    public static final int kLeftDriveSlaveId = 0;

    public static final double kDrivekP = 0.1;
    public static final double kDrivekI = 0;
    public static final int kDriveIZ = 0;
    public static final double kDrivekD = 0;
    public static final double kDrivekF = .05285; // calculated 0.0000487024 other:.05115

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
    
    //MagicFork
    public static final int kMFIntakeRollerId = 2491;
    public static final int kMFElevatorId = 2491;
    public static final int kMFHatchReleaseChannel = 2491;
    public static final int kMFHatchGrabChannel = 2491;
    public static final int kMFIntakeRotateDownChannel = 2491;
    public static final int kMFIntakeRotateUpChannel = 2491;

    public static final double kMFShootSpeed = 2491;
    public static final double kMFIntakeSpeed = 2491;

    public static final double kMFElevatorP = 2491;
    public static final double kMFElevatorI = 2491;
    public static final double kMFElevatorD = 2491;
    public static final double kMFElevatorF = 2491;

    public static final double kMFRollerP = 2491;
    public static final double kMFRollerI = 2491;
    public static final double kMFRollerD = 2491;
    public static final double kMFRollerF = 2491;

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