package com.nomythic2491.frc2019.commands.Fork;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.Fork;

/**
 * Run intake motors
 */
public class AutoCargoRetrieval extends CommandBase {
    private double power;

    /**
     * Runs intake motors
     * @param desiredPower The power fed to the intake motors
     */
    public AutoCargoRetrieval(double desiredPower) {
        requires(fork);
        power = desiredPower;
    }

    protected void initialize() {
        fork.runIntake(power);
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