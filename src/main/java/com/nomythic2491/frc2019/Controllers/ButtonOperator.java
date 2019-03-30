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
        if (mJoystick.getRawButton(12) && mJoystick.getRawButton(13)) {
            return ClimberDemand.Up;
        } else if (mJoystick.getRawButton(12) && mJoystick.getRawButton(14)) {
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
        if (!mJoystick.getRawButton(6)) {
            return mJoystick.getRawButton(1);
        } else {
            return false;
        }
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
    public double getElevatorOverride() {
        if (mJoystick.getRawButton(12)) {

            if (mJoystick.getRawButton(15)) {
                return 1;

            } else if (mJoystick.getRawButton(16)) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean getClimbEnable() {
        return mJoystick.getRawButton(3);
    }

    @Override
    public boolean getLevel2Climb() {
        return mJoystick.getRawButtonPressed(4);
    }

    @Override
    public boolean getLevel3Climb() {
        return mJoystick.getRawButtonPressed(5);
    }

}