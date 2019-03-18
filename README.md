# FRC2019
The code for Gear it Forward's 2019 robot, "Milk, but also Vanguard".

## Drivetrain
Single-speed drop-center drivetrain with 2 REV NEOs per gearbox.
It has a max speed just above 12 ft/s and uses open-loop curvature control for manual driving.
Curvature control is based on *Chezy Drive*.

## Elevator
Two stage elevator with cascade rigging modified to use a single chain loop for the first stage,
and a cable loop for the second stage. This means that one pulley (or sprocket) can power up or down.
It uses closed loop motion profiling with the help of the Talon SRX's onboard Motion Magic control mode.

## Claw
Multipurpose wheeled claw for manipulating both cargo and hatches.
Uses servos to engage piston brakes allowing easy switching between cargo and hatch modes.
Also records its current mode to allow for intelligent elevator positioning. This means
that less elevator position buttons have to be programmed, as it can move to the proper
low, mid, or high position depending on the game piece it is holding.

## Climber
Quad piston pneumatic lift for raising and moving the drivebase onto HAB level 3. The front pistons 
are controlled via flow valve, while the rear pistons use closed loop position control via cable winch.
A shaft runs down to the rear wheels, allowing the robot to drive forward while suspended in the air.

## BackHatch
Simple pneumatic actuator for placing a hatch during the sandstorm period. It is not capable of collecting,
and is only used once at the very beginning of the match.