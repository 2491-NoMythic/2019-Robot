package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.subsystems.Climber.ClimberDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.GamepeiceDemand;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickOperator implements IOperatorController {

    private static JoystickOperator mInstance = null;

    public static JoystickOperator getInstance() {
        if (mInstance == null) {
            mInstance = new JoystickOperator();
        }
        return mInstance;
    }

    private final Joystick mJoystick;

    private JoystickOperator() {
        mJoystick = new Joystick(1);
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