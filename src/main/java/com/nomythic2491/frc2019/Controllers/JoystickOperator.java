package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;

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
            return ClimberDemand.Stop;
    }

    @Override
    public GamepieceDemand getGamepieceDemand() {
        if (mJoystick.getRawButton(12)) {
            return GamepieceDemand.Override;
        } else if (mJoystick.getRawButton(7)) {
            return GamepieceDemand.CargoMid;
        } else if (mJoystick.getRawButton(11)) {
            return GamepieceDemand.CargoIntake;
        } else if (mJoystick.getRawButton(10)) {
            return GamepieceDemand.HatchLow;
        } else if (mJoystick.getRawButton(8)) {
            return GamepieceDemand.HatchMid;
        } else if (mJoystick.getRawButton(9)) {
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
        return mJoystick.getRawButton(2);
    }

    @Override
    public boolean runControlPins() {
        return false;
        
    }

    @Override
    public double getElevatorOverride() {
        if (mJoystick.getPOV(0) == 180) {
            return -1;
        } else if (mJoystick.getPOV(0)== 0) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean getClimbEnable() {
        return mJoystick.getRawButton(5);
    }

    @Override
    public boolean getLevel2Climb() {
        return false;
    }

    @Override
    public boolean getLevel3Climb() {
        return mJoystick.getRawButton(6);
    }

}