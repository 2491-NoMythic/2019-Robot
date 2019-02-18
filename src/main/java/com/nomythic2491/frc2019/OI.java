/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.nomythic2491.frc2019;

import com.nomythic2491.frc2019.Settings.ControllerMap;
import com.nomythic2491.frc2019.commands.Climber.ManualClimb;
import com.nomythic2491.frc2019.commands.MagicBox.ElevateBox;
import com.nomythic2491.frc2019.commands.MagicBox.GamepieceLoop;
import com.nomythic2491.frc2019.commands.MagicBox.RotateMagicBoxToPosition;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  private final Joystick[] controllers = new Joystick[1];
  Button CargoPickup;
  Button Stowed;
  Button togglePivot;
  Button CargoMed;
  Button CargoLow;
  Button Flat;
  Button Hatch, Hatch2;
  Button Cargo, Cargo2;
  Button HatchMed;
  Button Bottom;
  POVButton Deploy, Retract;

  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  public void init()
  {
    // controllers[0] = new Joystick(0);//Drive
    controllers[0] = new Joystick(1);//Op

    //pickupHatch = new JoystickButton(controllers[ControllerMap.operatorController], ControllerMap.hatchButton);

    Stowed = new JoystickButton(controllers[ControllerMap.operatorController], 2);
    Hatch = new JoystickButton(controllers[ControllerMap.operatorController],6);
    Hatch2 = new JoystickButton(controllers[ControllerMap.operatorController],4);
    Cargo = new JoystickButton(controllers[ControllerMap.operatorController],5);
    Cargo2 = new JoystickButton(controllers[ControllerMap.operatorController],3);
    Flat = new JoystickButton(controllers[ControllerMap.operatorController], 1);

    //CargoMed = new JoystickButton(controllers[ControllerMap.operatorController], 7);
    CargoLow = new JoystickButton(controllers[ControllerMap.operatorController], 9);
    CargoPickup = new JoystickButton(controllers[ControllerMap.operatorController], 11);
    HatchMed = new JoystickButton(controllers[ControllerMap.operatorController], 8);
    Bottom = new JoystickButton(controllers[ControllerMap.operatorController], 12);

    Deploy = new POVButton(controllers[ControllerMap.operatorController], 180);
    Retract = new POVButton(controllers[ControllerMap.operatorController], 0);

    //Right hand is cargo left hand is hatch

    //CargoMed.whenPressed(new ElevateBox(49));
    //HatchMed.whenPressed(new ElevateBox(28));
    CargoLow.whenPressed(new ElevateBox(21));
    CargoPickup.whenPressed(new ElevateBox(3));
    Bottom.whenPressed(new ElevateBox(0));

    Hatch.whenPressed(new RotateMagicBoxToPosition(-1414.0));
    Hatch2.whenPressed(new RotateMagicBoxToPosition(-1414.0)); //1314
    Cargo.whenPressed(new RotateMagicBoxToPosition(-1100));
    Cargo2.whenPressed(new RotateMagicBoxToPosition(-1100));

    Flat.whenPressed(new RotateMagicBoxToPosition(-570));

  }

  /**
	 * Get a button from a controller
	 * 
	 * @param joystickID
	 *			The id of the controller. 0 = left or driver, 1 = right or codriver.
	 * @param axisID
	 *			The id of the button (for use in getRawButton)
	 * @return the result from running getRawButton(button)
	 */

	public boolean getButton(int joystickID, int buttonID) {
		return controllers[joystickID].getRawButton(buttonID);
	}

  public double getAxisDeadzonedSquared(int joystickID, int axisID, double deadzone) {
		double result = controllers[joystickID].getRawAxis(axisID);
		result = result * Math.abs(result);
		return Math.abs(result) > deadzone ? result : 0;
  }
}
