/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019.commands;

import com.nomythic2491.frc2019.subsystems.Drivetrain;
import com.nomythic2491.frc2019.subsystems.MagicFork;
import com.nomythic2491.frc2019.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public abstract class CommandBase extends Command {
	protected static Drivetrain drivetrain;
	protected static Climber climber;
	protected static MagicFork magicfork;

	public static void init() {

		drivetrain = Drivetrain.getInstance();
		climber = Climber.getInstance();
		magicfork = MagicFork.getInstance();

	}

	/**
	 * The base for all commands. All atomic commands should subclass CommandBase.
	 * CommandBase stores creates and stores each control system. To access a
	 * subsystem elsewhere in your code in your code use
	 * CommandBase.exampleSubsystem
	 * 
	 * @param name The name that shows up on the SmartDashboard in association with
	 *             any command created using this parameter.
	 */
	public CommandBase(String name) {
		super(name);
	}

	/**
	 * The base for all commands. All atomic commands should subclass CommandBase.
	 * CommandBase stores creates and stores each control system. To access a
	 * subsystem elsewhere in your code in your code use
	 * CommandBase.exampleSubsystem
	 */
	public CommandBase() {
		super();
	}
}
