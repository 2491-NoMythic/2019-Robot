package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
import com.nomythic2491.frc2019.Settings.Constants.kArcade;
import com.nomythic2491.lib.util.CheesyDriveHelper;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Joystick;

public class ArcadeDriver implements IDriveController {
    //TODO: update Controller implentation;
    private static ArcadeDriver mInstance = null;
    private CheesyDriveHelper mHelper;

    public static ArcadeDriver getInstance() {
        if (mInstance == null) {
            mInstance = new ArcadeDriver();
        }
        return mInstance;
    }

    private Joystick mJoystick;

    private ArcadeDriver() {
        mJoystick = new Joystick(kArcade.kId);
        mHelper = new CheesyDriveHelper();
    }

    @Override
    public DriveSignal getSignal() {
        return mHelper.cheesyDrive(-mJoystick.getRawAxis(kArcade.kThrottleAxis),
                mJoystick.getRawAxis(kArcade.kTurnAxis), mJoystick.getRawButton(kArcade.kQuickturnButton));
    }

    @Override
    public boolean getKillSwitch() {
        return mJoystick.getRawButton(kArcade.kKillSwitchButton)
                || mJoystick.getRawButton(kArcade.kKillSwitchButton2);
    }

    @Override
    public boolean getSlowSpeed() {
        return mJoystick.getRawButton(0);
    }

    @Override
    public IoCargo getIoCargo() {
        if (mJoystick.getRawButton(kArcade.kCargoInButton)) {
            return IoCargo.In;
        } else if (mJoystick.getRawButton(kArcade.kCargoOutButton)) {
            return IoCargo.Out;
        } else {
            return IoCargo.Stop;
        }
    }

    @Override
    public boolean lineUp() {
        return mJoystick.getRawButton(4);
    }

    @Override
    public double getThrottle() {
        return 0;
    }

    @Override
    public double getTurn() {
        return 0;
    }

    @Override
    public boolean getLineupLock() {
        return mJoystick.getRawButton(2);
    }
}