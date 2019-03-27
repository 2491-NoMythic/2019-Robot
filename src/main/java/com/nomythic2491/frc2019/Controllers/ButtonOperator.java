package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.Settings.Constants.kButtonOp;

import edu.wpi.first.wpilibj.Joystick;

public class ButtonOperator implements IOperatorController {

    private static ButtonOperator mInstance = null;

    public static ButtonOperator getInstance() {
        if (mInstance == null) {
            mInstance = new ButtonOperator();
        }

        return mInstance;
    }

    private Joystick mJoystick;

    private ButtonOperator() {
        mJoystick = new Joystick(kButtonOp.kId);
    }

    @Override
    public ClimberDemand getClimberDemand() {
        if (mJoystick.getRawButton(12) && mJoystick.getRawAxis(0) == 1) {
            return ClimberDemand.Up;
        } else if (mJoystick.getRawButton(12) && mJoystick.getRawAxis(0) == -1) {
            return ClimberDemand.Down;
        } else {
            return ClimberDemand.Stop;
        }
    }

    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(12)) {
            return GamepieceDemand.Override;
        } else if (mJoystick.getRawButton(10)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(11)) {
            return GamepieceDemand.CargoIntake;
        } else if (mJoystick.getRawButton(8)) {
            return GamepieceDemand.HatchLow;
        } else if (mJoystick.getRawButton(7)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(9)) {
            return GamepieceDemand.CargoLow;
        } else {
            return GamepieceDemand.Hold;
        }
    }

    @Override
    public boolean getTipIntake() {
        return mJoystick.getRawButton(1);
    }

    @Override
    public boolean getHatch() {
        return mJoystick.getRawButton(2);
    }

    @Override
    public boolean runControlPins() {
        return false;
    }

    @Override
    public double getElevotrOverride() {
        if (mJoystick.getRawButton(12)) {
            return mJoystick.getRawAxis(1);
        }
        return 0;
    }

    @Override
    public boolean getAutoClimb() {
        return getClimbEnable() && mJoystick.getRawButtonPressed(5);
    }

    @Override
    public boolean getClimbEnable() {
        return mJoystick.getRawButton(3);
    }

}