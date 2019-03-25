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
        // if (mJoystick.getRawButton(5)){
        //     return ClimberDemand.Climb;
        // }
        return ClimberDemand.Stop;
    }



    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(10)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(11)) {
            return GamepieceDemand.CargoIntake;
        } else if (mJoystick.getRawButton(8)) {
            return GamepieceDemand.HatchDefault;
        } else if (mJoystick.getRawButton(7)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(9)){
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

}