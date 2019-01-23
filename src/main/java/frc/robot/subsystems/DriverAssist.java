/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class DriverAssist extends Subsystem {
  private NetworkTable limelight;
  private NetworkTableEntry tx, ty, ta, tv;
  private static DriverAssist instance;
  
  public static DriverAssist GetInstance() {
      if(instance == null) {
        instance = new DriverAssist();
      }
      return instance;
  }
  
  public DriverAssist() {
    limelight = NetworkTableInstance.getDefault().getTable("limelight");
    limelight.getEntry("ledMode").setNumber(0);
    limelight.getEntry("camMode").setNumber(0);
    tx = limelight.getEntry("tx");
    ty = limelight.getEntry("ty");
    ta = limelight.getEntry("ta");
    tv = limelight.getEntry("tv");
  }

    protected void initDefaultCommand() {
    }
}
