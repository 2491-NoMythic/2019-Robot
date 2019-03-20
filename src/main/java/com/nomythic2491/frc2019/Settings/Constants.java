package com.nomythic2491.frc2019.Settings;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Constants {
    // Motor Controllers have ID's || Solenoids have Channels
    
    // Controllers
    public static final double kDeadbandThrottle = 0.1;
    public static final double kDeadbandTurn = 0.05;

    // Talons
    public static final int kTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;
    public static final double kDriveVoltageRampRate = .5;
    public static final int kVelocitySlot = 0;

    public enum GamepieceDemand {
        Test(12), Hold(0), CargoDefault(2), CargoMid(32), CargoLow(14), HatchDefault(0), HatchMid(28), Stop(0);

        private double mHeightPoint;

        private GamepieceDemand(double hight) {
            mHeightPoint = hight / (.7 * Math.PI) * 4096;
        }

        public double getHeightPoint() {
            return mHeightPoint;
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

    public static final class kDrive {
        public static final int kRightDriveMasterId = 10;
        public static final int kRightDriveSlaveId = 11;
        public static final int kLeftDriveMasterId = 1;
        public static final int kLeftDriveSlaveId = 0;

        public static final double kDrivekP = 1;
        public static final double kDrivekI = 0;
        public static final int kDriveIZ = 0;
        public static final double kDrivekD = 300;
        public static final double kDrivekF = .05385; // calculated 0.0000487024 other:.05115
        public static final double kWheelDiameterInches = 2491; // This is very, very important! Change this before
                                                                // trying
                                                                // to
                                                                // use the encoders to do anything.
        public static final double kDriveEncoderToInches = 1 / kWheelDiameterInches * Math.PI / 4096.0;
    }

    public static final class kClimber {
        public static final int kClimberMasterId = 2;
        public static final int kClimberSlaveId = 9;
        public static final int kLeftStringId = 24;
        public static final int kRightStringId = 34;
        public static final int kRatchetChannel = 3;

        public static final int kClimberVelocity = 2491;
        public static final double doneClimbingHeight = 2491;

        public static final double kClimberEncoderToInches = 1;

        public static final int kHatchOutChannel = 4;
        public static final int kHatchInChannel = 5;

        public static final int kBumperInChannel = 1;
        public static final int kBumperOutChannel = 0;

        public static final double kHatchPickupPause = 0.25;
    }

    public static final class kMF {
        public static final int kIntakeRollerId = 3;
        public static final int kElevatorId = 25;
        public static final int kHatchReleaseChannel = 5;
        public static final int kHatchGrabChannel = 4;
        public static final int kIntakeDownChannel = 7;
        public static final int kIntakeUpChannel = 6;
        public static final int kControlPinChannel = 0;
        public static final int kCargoSensor = 0; // May very well need to be changed
        public static final int kHatchSensor = 1; // Again, may need changing

        public static final double kElevatorP = 0.32094;
        public static final double kElevatorI = 0;
        public static final double kElevatorD = 0;
        public static final double kElevatorF = 0.609375;
    }

    public static final class kArcade {
        public static final int kId = 0;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
        public static final int kCargoInButton = 3;
        public static final int kCargoOutButton = 5;
        public static final int kKillSwitchButton = 11;
        public static final int kKillSwitchButton2 = 12;
    }

    public static final class kJoystickOp {
        public static final int kId = 1;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
    }

    public static final class kPS4Op {
        public static final int kId = 1;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
    }
}