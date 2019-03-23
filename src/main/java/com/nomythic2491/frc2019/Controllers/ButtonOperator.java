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

    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(1)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(3)) {
            return GamepieceDemand.CargoIntake;
        } else if (mJoystick.getRawButton(5)) {
            return GamepieceDemand.HatchDefault;
        } else if (mJoystick.getRawButton(4)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(2)){
            return GamepieceDemand.CargoLow;
        } else {
            return GamepieceDemand.Hold;
        }
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