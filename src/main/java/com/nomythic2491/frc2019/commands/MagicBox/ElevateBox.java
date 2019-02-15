/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands.MagicBox;

import com.nomythic2491.frc2019.Settings.Constants;
import com.nomythic2491.frc2019.Settings.MotionProfiles;
import com.nomythic2491.frc2019.Settings.Variables;
import com.nomythic2491.frc2019.commands.CommandBase;
import com.nomythic2491.frc2019.subsystems.MagicBox.positionElevator;

public class ElevateBox extends CommandBase {
  
  /**
   * Raises/lowers the magic box along the bars
   */
  public ElevateBox() {
    requires(magicbox);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (Variables.currentElevatorPostion == positionElevator.UP){
      magicbox.runElevatorMotionProfile(MotionProfiles.ElevatorMotionProfile, MotionProfiles.ElevatorMotionProfile.length, true);
    }else{
      magicbox.runElevatorMotionProfile(MotionProfiles.ElevatorMotionProfile, MotionProfiles.ElevatorMotionProfile.length, false);
    }
  }
  

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return magicbox.getIsElevatorRunningMotionProfile();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
