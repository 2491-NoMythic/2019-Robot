package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepeiceDemand;

public interface IOperatorController {

    public ClimberDemand getClimberDemand();

    public GamepeiceDemand getGamepeiceDemand();

    public void runPathTest();
    
}