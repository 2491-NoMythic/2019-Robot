/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.nomythic2491.frc2019.commands.Fork;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

public class CargoIntakeUntilSuccess extends CommandBase {

    /**
    * Runs the cargo intake until cargo appears in the magic box.
    */
    public CargoIntakeUntilSuccess() {
     // Use requires() here to declare subsystem dependencies
     // eg. requires(chassis);
     requires(fork);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
     fork.runIntake(Constants.kForkCargoIntakeVelocity);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
     return fork.isCargoIn();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
     fork.runIntake(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
     end();
    }
} 

