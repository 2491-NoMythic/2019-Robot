package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.IControlBoard;
import com.nomythic2491.frc2019.Controllers.*;

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
    public double getThrottle() {
        return mDriveController.getThrottle();
    }

    @Override
    public double getTurn() {
        return mDriveController.getTurn();
    }

    @Override
    public boolean getKillSwitch() {
        return mDriveController.getKillSwitch();
    }

    @Override
    public boolean getTankTurnLeft() {
        return mDriveController.getTankTurnLeft();
    }

    @Override
    public boolean getTankTurnRight() {
        return mDriveController.getTankTurnRight();
    }

    @Override
    public boolean getSlowSpeed() {
        return mDriveController.getSlowSpeed();
    }
}