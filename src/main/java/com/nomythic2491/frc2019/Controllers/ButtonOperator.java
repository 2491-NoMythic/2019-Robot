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
        return null;
    }

    @Override
    public GamepieceDemand getGamepieceDemand() {
        return null;
    }

    @Override
    public void runPathTest() {

    }

    @Override
    public boolean getTipIntake() {
        return false;
    }

    @Override
    public boolean getHatch() {
        return false;
    }

    @Override
    public boolean runControlPins() {
        return false;
    }

}