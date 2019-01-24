package com.nomythic2491.frc2019.Controllers;

public interface IDriveController {
    
    public double getThrottle();

    public double getTurn();

    public boolean getKillSwitch();

    public boolean getTankTurnLeft();

    public boolean getTankTurnRight();

    public boolean getSlowSpeed();
}