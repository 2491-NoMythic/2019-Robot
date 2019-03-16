package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.commands.AutoPlaceHatch;
import com.nomythic2491.frc2019.commands.Drivetrain.RunSCurvePath;
import com.nomythic2491.frc2019.subsystems.MagicFork;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickOperator implements IOperatorController {

    private static JoystickOperator mInstance = null;

    private AutoPlaceHatch test;

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
        return ClimberDemand.Stop; //TODO: joystick returns 0 if not plugged in
    }

    @Override
    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(6)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(9)) {
            return GamepieceDemand.CargoDefault;
        } else if (mJoystick.getRawButton(10)) {
            return GamepieceDemand.HatchDefault;
        } else if (mJoystick.getRawButton(8)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(7)){
            return GamepieceDemand.CargoLow;
        } else if (mJoystick.getRawButton(12)){
            return GamepieceDemand.Stop;
        } else {
            return GamepieceDemand.Hold;
        }
    }

    @Override
    public void runPathTest() {
        /*if(mJoystick.getRawButton(10)){
            test = new AutoPlaceHatch(true);
            test.init();
        }*/
    }

    @Override
    public boolean getTipIntake() {
       return mJoystick.getRawButton(1);
    }

    @Override
    public boolean getHatch() {
        return mJoystick.getRawButtonPressed(2);
    }

    @Override
    public boolean runControlPins() {
        return mJoystick.getPOV() == 0;
        
    }

}