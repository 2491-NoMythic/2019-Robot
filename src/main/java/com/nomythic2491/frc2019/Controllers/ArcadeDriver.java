package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.lib.util.CheesyDriveHelper;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Joystick;

public class ArcadeDriver implements IDriveController {

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
        mJoystick = new Joystick(0);
        mHelper = new CheesyDriveHelper();
    }

    @Override
    public DriveSignal getSignal() {
        return mHelper.cheesyDrive(
                mHelper.handleDeadband(mJoystick.getRawAxis(Constants.kDriverThrottleAxis), Constants.kDeadband),
                mHelper.handleDeadband(mJoystick.getRawAxis(Constants.kDriverTurnAxis), Constants.kDeadband),
                mJoystick.getRawButton(Constants.kQuickturnButton));
    }

    @Override
    public boolean getKillSwitch() {
        return mJoystick.getRawButton(11);
    }

    @Override
    public boolean getSlowSpeed() {
        return mJoystick.getRawButton(0);
    }

}