package com.nomythic2491.frc2019.commands.Fork;

import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.subsystems.Fork;

/**
 * Run intake sys manually via controller input
 */
public class ManualCargoOutput extends CommandBase {

    public ManualCargoOutput() {
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
      if(!oi.getButton(ControllerMap.operatorController, ControllerMap.manualCargoOutput)) {
        fork.runIntake(oi.getAxisDeadzonedSquared(ControllerMap.operatorController, ControllerMap.cargoOutputAxis, 0.05));
      }
      else{
        fork.stopOutput();
      }
    }
    
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
      return false;
    }
    
    // Called once after isFinished returns true
    @Override
    protected void end() {
      fork.stopOutput();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
      end();
    }
}
