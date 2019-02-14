package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.commands.CommandBase;

public class OutputCargoManual extends CommandBase {

  /**
   * Runs the cargo intake as long as a button is held
   */
  public OutputCargoManual() {
      // Use requires() here to declare subsystem dependencies
      // eg. requires(chassis);
      requires(magicbox);
    }
    
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
      magicbox.runIntake(Constants.kBoxCargoShootSpeed);
    }
    
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
      return false;
    }
    
    // Called once after isFinished returns true
    @Override
    protected void end() {
      magicbox.stopIntake();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
}
