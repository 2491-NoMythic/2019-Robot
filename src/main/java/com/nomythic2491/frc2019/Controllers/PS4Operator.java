package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;

import edu.wpi.first.wpilibj.Joystick;

public class PS4Operator implements IOperatorController {

    private static PS4Operator mInstance = null;

    public static PS4Operator getInstance() {
        if (mInstance == null) {
            mInstance = new PS4Operator();
        }
        return mInstance;
    }

    private final Joystick mJoystick;

    private PS4Operator() {
        mJoystick = new Joystick(Constants.PS4Operator.kId);
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

    
}