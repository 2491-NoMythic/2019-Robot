package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.commands.Drivetrain.RunSCurvePath;
import com.nomythic2491.frc2019.subsystems.Climber.ClimberDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.GamepeiceDemand;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickOperator implements IOperatorController {

    private static JoystickOperator mInstance = null;

    private RunSCurvePath test = new RunSCurvePath();

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
        if (mJoystick.getPOV() == 180) {
            return ClimberDemand.Climb;
        } else if (mJoystick.getPOV() == 0) {
            return ClimberDemand.Reset;
        } 
        return ClimberDemand.Stop; //TODO: joystick returns 0 if not plugged in
    }

    @Override
    public GamepeiceDemand getGamepeiceDemand() {
        if (mJoystick.getRawButton(5)) {
            return GamepeiceDemand.CargoOut_Ship;
        } else if (mJoystick.getRawButton(6)) {
            return GamepeiceDemand.CargoFloor;
        } else if (mJoystick.getRawButton(3)) {
            return GamepeiceDemand.Stow;
        } else {
            return GamepeiceDemand.Hold;
        }
    }

    @Override
    public void runPathTest() {
        if(mJoystick.getRawButton(10)){
            test.start();
        }
    }

}