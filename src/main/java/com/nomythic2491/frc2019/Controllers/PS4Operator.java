package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.subsystems.Climber.ClimberDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.GamepeiceDemand;

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
        mJoystick = new Joystick(Constants.kPS4Operator.kId);
    }

    @Override
    public ClimberDemand getClimberDemand() {
        return null;
    }

    @Override
    public GamepeiceDemand getGamepeiceDemand() {
        return null;
    }
}