// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.HornSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShaveAndAHaircut extends SequentialCommandGroup {

  private final HornSubsystem m_HornSubsystem;
  /** Creates a new ShaveAndAHaircut. */
  public ShaveAndAHaircut(HornSubsystem horn) {
    m_HornSubsystem = horn;
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
    m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote), 
    m_HornSubsystem.honkLow(Constants.HornConstants.eigthNote), 
    m_HornSubsystem.honkLow(Constants.HornConstants.eigthNote),
    m_HornSubsystem.honkHigh(Constants.HornConstants.eigthNote),
    m_HornSubsystem.honkLow(Constants.HornConstants.quarterNote),
    new WaitCommand(Constants.HornConstants.quarterNote),
    m_HornSubsystem.honkLow(Constants.HornConstants.eigthNote),
    m_HornSubsystem.honkHigh(Constants.HornConstants.quarterNote)
    );
  }
}
