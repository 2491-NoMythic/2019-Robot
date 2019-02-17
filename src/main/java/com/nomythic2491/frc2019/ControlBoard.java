package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.Controllers.*;
import com.nomythic2491.lib.util.DriveSignal;

public class ControlBoard implements IControlBoard {

    private static ControlBoard mInstance = null;

    public static ControlBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ControlBoard();
        }
        return mInstance;
    }

    private IDriveController mDriveController;
    private IOperatorController mOperatorController;

    private ControlBoard() {
        if (true) {
            mDriveController = ArcadeDriver.getInstance();
        }

        if (true) {
            mOperatorController = PS4Operator.getInstance();
        }
    }

    @Override
    public DriveSignal getSignal() {
        return mDriveController.getSignal();
    }

    @Override
    public boolean getKillSwitch() {
        return mDriveController.getKillSwitch();
    }

    @Override
    public boolean getSlowSpeed() {
        return mDriveController.getSlowSpeed();
    }

    @Override
    public boolean getIntakeOut() {
        return mDriveController.getIntakeOut();
    }

    @Override
    public boolean getIntakeIn() {
        return mDriveController.getIntakeIn();
    }
}