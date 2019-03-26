package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;
import com.nomythic2491.frc2019.commands.AutoPlaceHatch;

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
        if(mJoystick.getRawButton(4)){
            return ClimberDemand.Climb;
        } else if(mJoystick.getRawButton(5)){
            return ClimberDemand.Reset;
        }else{
            return ClimberDemand.Stop;
        }
    }

    @Override
    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(6)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(9)) {
            return GamepieceDemand.CargoIntake;
        } else if (mJoystick.getRawButton(10)) {
            return GamepieceDemand.HatchLow;
        } else if (mJoystick.getRawButton(8)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(7)){
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
        return mJoystick.getRawButtonPressed(2);
    }

    @Override
    public boolean runControlPins() {
        return mJoystick.getPOV() == 0;
        
    }

    @Override
    public double getElevotrOverride() {
        return 0;
    }

}