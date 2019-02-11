package com.nomythic2491.frc2019.Controllers;

import com.nomythic2491.lib.util.DriveSignal;

public interface IDriveController {
    
    public DriveSignal getSignal();

    public boolean getKillSwitch();

    public boolean getSlowSpeed();
}