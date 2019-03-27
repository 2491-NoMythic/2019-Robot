package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.Controllers.ArcadeDriver;
import com.nomythic2491.frc2019.Controllers.ButtonOperator;
import com.nomythic2491.frc2019.Controllers.IDriveController;
import com.nomythic2491.frc2019.Controllers.IOperatorController;
import com.nomythic2491.frc2019.Controllers.JoystickOperator;
import com.nomythic2491.frc2019.Controllers.TMdriver;
import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.Settings.Constants.IoCargo;
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
        if (Constants.kUseDriveAlternateContoller) {
            mDriveController = ArcadeDriver.getInstance();
        } else {
            mDriveController = TMdriver.getInstance();
        }

        if (Constants.kUseOpAlternateContoller) {
            mOperatorController = JoystickOperator.getInstance();
        } else {
            mOperatorController = ButtonOperator.getInstance();
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
    public GamepieceDemand getGamepieceDemand() {
        return mOperatorController.getGamepieceDemand();
    }

    @Override
    public boolean getTipIntake() {
        return mOperatorController.getTipIntake();
    }

    @Override
    public boolean getHatch() {
        return mOperatorController.getHatch();
    }

    @Override
    public boolean runControlPins() {
        return mOperatorController.runControlPins();
    }

    @Override
    public boolean lineUp() {
        return mDriveController.lineUp();
    }

    @Override
    public double getElevotrOverride() {
        return mOperatorController.getElevotrOverride();
    }

    @Override
    public boolean getAutoClimb() {
        return mOperatorController.getAutoClimb();
    }
}