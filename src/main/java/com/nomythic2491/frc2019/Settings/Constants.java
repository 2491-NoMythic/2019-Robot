package com.nomythic2491.frc2019.Settings;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Constants {
    // Motor Controllers have ID's || Solenoids have Channels

    // Controllers
    public static final double kDeadbandThrottle = 0.1;
    public static final double kDeadbandTurn = 0.05;
    public static final boolean kUseDriveAlternateContoller = false;
    public static final boolean kUseOpAlternateContoller = false;

    // Talons
    public static final int kTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;
    public static final double kDriveVoltageRampRate = .5;
    public static final int kPrimarySlot = 0;
    public static final int kAuxilarySlot = 1;

    public enum ControlState {
        OperatorControl, CommandControl;
      }
    public enum GamepieceDemand {
        Test(12), Hold(0), CargoIntake(1), CargoMid(32), CargoLow(19), HatchLow(0), HatchMid(28), Override(0), Climb(25);

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
        Up(0.75, -0.33, NeutralMode.Brake, true), Down(-0.75, 0.6, NeutralMode.Coast, true), Stop(0, 0, NeutralMode.Brake, false), Forward(0.3, 0.75, NeutralMode.Brake, false);

        double mClimbSpeed;
        double mSpoolRate;
        NeutralMode mBrake;
        boolean mFootUnlock;

        private ClimberDemand(double speed, double spool, NeutralMode brake, boolean foot) {
            mClimbSpeed = speed; // (1.5 * Math.PI) * 4096;
            mSpoolRate = spool;
            mBrake = brake;
            mFootUnlock = foot;
        }

        public double getClimbSpeed() {
            return mClimbSpeed;
        }

        public double getSpoolRate() {
            return mSpoolRate;
        }

        public NeutralMode getBrake() {
            return mBrake;
        }

        public boolean getFootUnlock() {
            return mFootUnlock;
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
        public static final int kSlaveStringId = 12;
        public static final int kMasterStringId = 4;
        public static final int kLockChannel = 3;

        public static final int kClimberVelocity = 2491;
        public static final double doneClimbingHeight = 2491;

        public static final double kClimberEncoderToInches = 1;

        public static final int kHatchOutChannel = 4;
        public static final int kHatchInChannel = 5;

        public static final int kBumperInChannel = 1;
        public static final int kBumperOutChannel = 0;

        public static final double kHatchPickupPause = 0.25;

        public static final class kGains_Distanc {
            public static final double kP = 0;
            public static final double kI = 0;
            public static final int kIzone = 0;
            public static final double kD = 0;
            public static final double kF = 1.27875;
            public static final double kPeakOutput = 1;
        }

        public static final class kGains_Turning {
            public static final double kP = 0.003;
            public static final double kI = 0;
            public static final int kIzone = 0;
            public static final double kD = 0;
            public static final double kF = 0;
            public static final double kPeakOutput = 1;

        }
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

    public static final class kTM {
        public static final int kId = 0;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
        public static final int kCargoInButton = 2;
        public static final int kCargoOutButton = 3;
        public static final int kKillSwitchButton = 10;
        public static final int kKillSwitchButton2 = 5;
    }

    public static final class kJoystickOp {
        public static final int kId = 1;
        public static final int kThrottleAxis = 1;
        public static final int kTurnAxis = 2;
        public static final int kQuickturnButton = 1;
    }

    public static final class kButtonOp {
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