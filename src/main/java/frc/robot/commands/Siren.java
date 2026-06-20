// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.HornConstants;
import frc.robot.subsystems.HornSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Siren extends SequentialCommandGroup {
  private HornSubsystem m_HornSubsystem;

  /** Creates a new Siren. */
  public Siren(HornSubsystem horn) {

    m_HornSubsystem = horn;
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
      m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote)
    );
  }
}
