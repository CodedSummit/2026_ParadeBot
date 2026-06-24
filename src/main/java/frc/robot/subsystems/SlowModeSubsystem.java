package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


public class SlowModeSubsystem extends Command {
  public boolean sticksPressed = false;
  public boolean slowModeActive = false;
  private CommandXboxController controller;
  private Command rumbleCommand;
  private DriveSubsystem m_robotDrive;

  public SlowModeSubsystem(CommandXboxController driveController, Command rumble, DriveSubsystem m_drive) {
    controller = driveController;
    rumbleCommand = rumble;
    m_robotDrive = m_drive;
  }

  public Command updateSticksPressed(boolean pressed) {
    return new InstantCommand(() ->
      sticksPressed = pressed
    );
  }

  public boolean sticksInPosition() {
    return controller.getLeftY() <= -0.95 && controller.getRightY() >= 0.95;
  }
  public Command rumbleTest() {
    return rumbleCommand;
  }
  public Command updateSlowMode() {
    return new ConditionalCommand(
      new ParallelCommandGroup(
        new InstantCommand(() -> slowModeActive = !slowModeActive),
        rumbleCommand,
        new ConditionalCommand(
          new InstantCommand(() -> m_robotDrive.setMaxOutput(.75)),
          new InstantCommand(() -> m_robotDrive.setMaxOutput(1)),
          () -> slowModeActive
        )
      ),
      
      new InstantCommand(),
      () -> sticksPressed && sticksInPosition()
    );
  }
}