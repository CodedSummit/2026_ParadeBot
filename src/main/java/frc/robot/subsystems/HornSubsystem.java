// CopyLow (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

public class HornSubsystem extends SubsystemBase {
    private final WPI_VictorSPX  m_LowHorn;
    private final WPI_VictorSPX  m_highHorn; 
    private GenericEntry nt_lowVol;
    private GenericEntry nt_highVol;
    private double m_lowVol = 0;
    private double m_highVol = 0;

  /** Creates a new HornSubsystem. */
  public HornSubsystem() {
    m_LowHorn = new WPI_VictorSPX (6);
    m_highHorn = new WPI_VictorSPX (5);

    setupShuffleboard();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command honkLow(double x){
    Command z = new SequentialCommandGroup(new InstantCommand(() -> m_LowHorn.set(getLowHornVolume())),
    new WaitCommand(x),
    new InstantCommand(()-> m_LowHorn.set(0)));
    return z;
  }

    public Command honkHigh(double x){
    Command z = new SequentialCommandGroup(new InstantCommand(() -> m_highHorn.set(getLowHornVolume())),
    new WaitCommand(x),
    new InstantCommand(()-> m_highHorn.set(0)));
    return z;
  }

  public Command continuousHonkLow(){
    return new StartEndCommand(()-> m_LowHorn.set(getLowHornVolume()), ()-> m_LowHorn.set(0));
  }

  public Command continuousHonkHigh(){
    return new StartEndCommand(()-> m_highHorn.set(gethighHornVolume()), ()-> m_highHorn.set(0));
  }

  public Command stopHonking(){
    return new SequentialCommandGroup(new InstantCommand(()-> m_highHorn.set(0)), new InstantCommand(()-> m_LowHorn.set(0)));
  }

  public double gethighHornVolume(){
    m_highVol = nt_highVol.getDouble(Constants.HornConstants.highHornVolume);
    return m_highVol;
  }

    public double getLowHornVolume(){
    m_lowVol = nt_lowVol.getDouble(Constants.HornConstants.lowHornVolume);
    return m_lowVol;
  }

  private void setupShuffleboard(){

    ShuffleboardTab hornTab = Shuffleboard.getTab("Horn");
    
    nt_highVol = hornTab.addPersistent("High Horn Volume", Constants.HornConstants.highHornVolume)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", Constants.HornConstants.hornMin, "max", 1))
      .getEntry();

    nt_lowVol = hornTab.addPersistent("Low Horn Volume", Constants.HornConstants.lowHornVolume)
      .withWidget(BuiltInWidgets.kNumberSlider)
      .withProperties(Map.of("min", Constants.HornConstants.hornMin, "max", 1))
      .getEntry();
  }
}
