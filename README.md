# Vehicle-Path-Tracking
Demonstrating various feedback controllers using LEGO Mindstorms robot with java programming
Lejos hardware library is also used for the actuators and sensors.

Proportional Control uses the difference in expected and calculated value obtained from the 
sensor to decide how much and what direction the robot should turn in. The Integral control
takes the summation of all previous differences to remove the steady state error in the robot's
movement (because with only proportional control, the robot gets indefinitely close to the goal
line)

The ultrasonic sensor detects the distance from the sensor to an obstruction.
The color sensor detects the shade (lightness and darkness) of the surface it is pointed towards.

For the code using a color sensor, the desired path can be drawn onto the ground using black tape, wherein
the color sensor is also facing the ground at the beginning of the tape (starting position).
