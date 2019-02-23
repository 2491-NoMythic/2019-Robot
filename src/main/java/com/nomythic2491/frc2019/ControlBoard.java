package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.Controllers.*;
import com.nomythic2491.frc2019.subsystems.Climber.ClimberDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.GamepeiceDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.IoCargo;
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
            mOperatorController = JoystickOperator.getInstance();
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
    public IoCargo getIoCargo() {
        return mDriveController.getIoCargo();
    }

    @Override
    public ClimberDemand getClimberDemand() {
        return mOperatorController.getClimberDemand();
    }

    @Override
    public GamepeiceDemand getGamepeiceDemand() {
        return mOperatorController.getGamepeiceDemand();
    }

    @Override
    public void runPathTest() {
        mOperatorController.runPathTest();
    }
}