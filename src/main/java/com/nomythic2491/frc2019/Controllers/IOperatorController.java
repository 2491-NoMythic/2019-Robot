package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.frc2019.Settings.Constants.ClimberDemand;
import com.nomythic2491.frc2019.Settings.Constants.GamepieceDemand;

public interface IOperatorController {

    public ClimberDemand getClimberDemand();

    public GamepieceDemand getGamepieceDemand();

    public boolean getTipIntake();

    public boolean getHatch();

    public boolean runControlPins();

    public double getElevotrOverride();

    public boolean getAutoClimb();
}