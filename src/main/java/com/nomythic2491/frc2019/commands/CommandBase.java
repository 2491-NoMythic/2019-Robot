/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.nomythic2491.frc2019.OI;
import com.nomythic2491.frc2019.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public abstract class CommandBase extends Command {
  protected static OI oi;
	protected static Drivetrain drivetrain;
	
	public static void init() {
		oi = new OI();
		
    drivetrain = Drivetrain.GetInstance();
    
    oi.init();
		// This MUST be here. If the OI creates Commands (which it very likely
		// will), constructing it during the construction of CommandBase (from
		// which commands extend), subsystems are not guaranteed to be
		// yet. Thus, their requires() statements may grab null pointers. Bad
		// news. Don't move it.
		
		// Show what command your subsystem is running on the SmartDashboard
	}
	
	/**
	 * The base for all commands. All atomic commands should subclass CommandBase. CommandBase stores creates and stores each control system. To access a subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
	 * 
	 * @param name
	 *			The name that shows up on the SmartDashboard in association with any command created using this parameter.
	 */
	public CommandBase(String name) {
		super(name);
	}
	
	/**
	 * The base for all commands. All atomic commands should subclass CommandBase. CommandBase stores creates and stores each control system. To access a subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
	 */
	public CommandBase() {
		super();
	}
}
