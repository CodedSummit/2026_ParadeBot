// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.ShaveAndAHaircut;
import frc.robot.commands.Siren;
import frc.robot.commands.TurnToAngle;
import frc.robot.commands.TurnToAngleProfiled;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HornSubsystem;
import frc.robot.subsystems.SlowModeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();

  private final SlewRateLimiter throttleLimiter = new SlewRateLimiter(2);
  private final SlewRateLimiter rotationLimiter = new SlewRateLimiter(3);

  // The driver's controller
  CommandXboxController m_driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
  private final SlowModeSubsystem slowModeSubsystem = new SlowModeSubsystem(m_driverController, rumblePulse(0.5), m_robotDrive);

  //The bot's horn
  HornSubsystem m_HornSubsystem = new HornSubsystem();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    setupDashboard();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive    
    m_robotDrive.setDefaultCommand(
      // A split-stick arcade command, with forward/backward controlled by the left
      // hand, and turning controlled by the right.
      new RunCommand(
        () -> {
          if (!slowModeSubsystem.sticksPressed) {
            m_robotDrive.curvatureDrive(
              rotationLimiter.calculate(m_robotDrive.squareInput(-m_driverController.getRightX())),
              throttleLimiter.calculate(m_robotDrive.squareInput(m_driverController.getLeftY())),
              true
            );
          } else {
            m_robotDrive.curvatureDrive(0, 0, false);
          }
        }, m_robotDrive
      )
    );
        
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link PS4Controller}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    m_driverController.leftBumper().onTrue(new InstantCommand(() -> m_robotDrive.zeroHeading()));

    // Drive at half speed when the right bumper is held
    m_driverController.rightTrigger()
        .onTrue(new InstantCommand(() -> m_robotDrive.setMaxOutput(1.25)))
        .onFalse(new InstantCommand(() -> m_robotDrive.setMaxOutput(1)));
    m_driverController.leftTrigger()
      .onTrue(new InstantCommand(() -> m_robotDrive.setMaxOutput(.5)))
      .onFalse(new InstantCommand(() -> m_robotDrive.setMaxOutput(1)));

    // Stabilize robot to drive straight with gyro when left bumper is held
/*    m_driverController.leftBumper()
        .whileTrue(
            new PIDCommand(
                new PIDController(
                    DriveConstants.kStabilizationP,
                    DriveConstants.kStabilizationI,
                    DriveConstants.kStabilizationD),
                // Close the loop on the turn rate
                m_robotDrive::getTurnRate,
                // Setpoint is 0
                0,
                // Pipe the output to the turning controls
                output -> m_robotDrive.arcadeDrive(-m_driverController.getLeftY(), output),
                // Require the robot drive
                m_robotDrive));
*/
    // Turn to 90 degrees when the 'X' button is pressed, with a 5 second timeout
    m_driverController.x()
          .whileTrue(m_HornSubsystem.continuousHonkHigh());

    // Turn to -90 degrees with a profile when the Circle button is pressed, with a 5 second timeout
    m_driverController.b()
          .whileTrue(m_HornSubsystem.continuousHonkLow());

    m_driverController.a()
          .onTrue(new Siren(m_HornSubsystem));

    m_driverController.y()
          .onTrue(new ShaveAndAHaircut(m_HornSubsystem));

    m_driverController.rightBumper().onTrue(slowModeSubsystem.updateSlowMode());

    m_driverController.leftStick().and(m_driverController.rightStick())
      .onTrue(slowModeSubsystem.updateSticksPressed(true))
      .onFalse(slowModeSubsystem.updateSticksPressed(false));
  }

  private void setupDashboard(){
    Shuffleboard.getTab("Drive")
     .add("Drive", m_robotDrive);
  }
  
  private Command rumblePulse(double durationSeconds){
    if (durationSeconds == 0.0) durationSeconds = 0.5;
    return Commands.sequence(
      Commands.runOnce(()->m_driverController.setRumble(RumbleType.kBothRumble, 1)),
      Commands.waitSeconds(durationSeconds),
      Commands.runOnce(()->m_driverController.setRumble(RumbleType.kBothRumble, 0))
    );
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // no auto
    return new InstantCommand();
  }
}
