package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;

public interface IOperatorController {

    public ClimberDemand getClimberDemand();

    public GamepieceDemand getGamepieceDemand();

    public void runPathTest();

    public boolean getTipIntake();

    public boolean getHatch();

    public boolean runControlPins();
    
}