
package frc.robot.settings;

//If the value is 2491, it is a placeholder for the actual channel and will need to be changed to work on the robot.

public class Constants {

  //Drive
  public static final int driveTalonLeftChannel = 2491;
  public static final int driveTalonRightChannel = 2491;
  public static final int driveVictorLeftChannel = 2491;
  public static final int driveVictorRightChannel = 2491;

  public static final double driveEncoderToInches = 1 / Constants.wheelDiameterInches * Math.PI / 4096.0;
  public static final double wheelDiameterInches = 2491; //This is very, very important! Change this before trying to use the encoders to do anything.

  //Climber
  public static final int climberRodTalonLeftChannel = 2491;
  public static final int climberRodTalonRightChannel = 2491;
  public static final int climberRollerTalonChannel = 2491;

  //Box
  public static final int hatchClawSolenoidLeftChannel = 2491;
  public static final int hatchClawSolenoidRightChannel = 2491;
  public static final int rollerTalonChannel = 2491;

  //TalonSRX
  public static final int kTimeoutMs = 2491;
  public static final int kVelocitySlotId = 2491;
}