package com.nomythic2491.frc2019.commands.Fork;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.subsystems.Fork;

/**
 * Run intake sys manually via controller input
 */
public class ManualCargoRetrieval extends CommandBase {

    public ManualCargoRetrieval() {
        requires(fork);
    }

    protected void initialize() {
    }

    protected void execute() {
        if(!oi.getButton(ControllerMap.operatorController, ControllerMap.manualCargoPickup)) {
            fork.runIntake(oi.getAxisDeadzonedSquared(ControllerMap.operatorController, ControllerMap.cargoIntakeAxis, 0.05));
        }
        else{
            fork.stopIntake();
        }
    }

    // Will return true when command does not need further execution
    protected boolean isFinished() {
        return false;
    }

    // Called when isFinished returns true
    protected void end() {
        fork.stopIntake();
    }

    // Called when another command which requires one or more of the same
	// subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}