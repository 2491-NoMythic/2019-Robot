package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
import com.nomythic2491.frc2019.Settings.Constants.kTM;
import com.nomythic2491.lib.util.CheesyDriveHelper;
import com.nomythic2491.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Joystick;
import sun.tools.java.Environment;

public class TMdriver implements IDriveController {

    private static TMdriver mInstance = null;
    private CheesyDriveHelper mHelper;

    public static TMdriver getInstance() {
        if (mInstance == null) {
            mInstance = new TMdriver();
        }
        return mInstance;
    }

    private Joystick mJoystick;

    private TMdriver() {
        mJoystick = new Joystick(kTM.kId);
        mHelper = new CheesyDriveHelper();
    }

    @Override
    public DriveSignal getSignal() {
        Constants.replayOfDrive = Constants.replayOfDrive + getThrottle() + "," + getTurn() + "," + mJoystick.getRawButton(kTM.kQuickturnButton) + "\r\n"; 
        return mHelper.cheesyDrive(getThrottle(), getTurn(), mJoystick.getRawButton(kTM.kQuickturnButton));
    }

    @Override
    public boolean getKillSwitch() {
        return mJoystick.getRawButton(kTM.kKillSwitchButton) || mJoystick.getRawButton(kTM.kKillSwitchButton2);
    }

    @Override
    public boolean getSlowSpeed() {
        return mJoystick.getRawButton(0);
    }

    @Override
    public IoCargo getIoCargo() {
        if (mJoystick.getRawButton(kTM.kCargoInButton)) {
            return IoCargo.In;
        } else if (mJoystick.getRawButton(kTM.kCargoOutButton)) {
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
        return -mJoystick.getRawAxis(kTM.kThrottleAxis);
    }

    @Override
    public double getTurn() {
        return mJoystick.getRawAxis(kTM.kTurnAxis);
    }

    @Override
    public boolean getLineupLock() {
        return mJoystick.getRawButton(12); //TODO: set this button
    }
}