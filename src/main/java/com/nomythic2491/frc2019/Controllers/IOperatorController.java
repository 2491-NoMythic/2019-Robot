package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.subsystems.Climber.ClimberDemand;
import com.nomythic2491.frc2019.subsystems.MagicBox.GamepeiceDemand;

public interface IOperatorController {

    public ClimberDemand getClimberDemand();

    public GamepeiceDemand getGamepeiceDemand();

    public void runPathTest();
    
}