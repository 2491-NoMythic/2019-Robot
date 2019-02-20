package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.subsystems.MagicBox.IoCargo;
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
        System.out.println(-mJoystick.getRawAxis(Constants.ArcadeDriver.kThrottleAxis));
        return mHelper.cheesyDrive(
                mHelper.handleDeadband(-mJoystick.getRawAxis(Constants.ArcadeDriver.kThrottleAxis), Constants.kDeadband),
                mHelper.handleDeadband(mJoystick.getRawAxis(Constants.ArcadeDriver.kTurnAxis), Constants.kDeadband),
                mJoystick.getRawButton(Constants.ArcadeDriver.kQuickturnButton));
    }

    @Override
    public boolean getKillSwitch() {
        return mJoystick.getRawButton(Constants.ArcadeDriver.kKillSwitchButton) || mJoystick.getRawButton(Constants.ArcadeDriver.kKillSwitchButton2);
    }

    @Override
    public boolean getSlowSpeed() {
        return mJoystick.getRawButton(0);
    }

    @Override
    public IoCargo getIoCargo() {
        if (mJoystick.getRawButton(Constants.ArcadeDriver.kCargoInButton)) {
            return IoCargo.In;
        } else if (mJoystick.getRawButton(Constants.ArcadeDriver.kCargoOutButton)) {
            return IoCargo.Out;
        } else {
            return IoCargo.Stop;
        }
    }
}