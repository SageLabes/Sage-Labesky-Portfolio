# Basic Arduino
This folder contains a collection of small projects completed in a class focused on embedded software development. They are all simple and demonstrate the process I took in learning to use of different peripherals
### To run any of these programs, a basic arduino board and other specified components are required. Specified pins may need to change depending on the development board used but otherwise the code should work for any comparable hardware. The code must be loaded onto a board using Arduino IDE.
## Hardware used
- XIAO ESP32C3: Arduino development board used in every project in this file
- Basic servo motor (ControllingServoMotor)
- arduino button (ControllingServoMotor, ultrasonic)
- breadboard (ControllingServoMotor, ultrasonic)
- breadboard wires (ControllingServoMotor, ultrasonic)
- basic LED (ControllingServoMotor)
## Information on the projects
- JsonOverSerial: programmed the board to recieve specific JSON messages and respond to them in specific ways
- ControllingServoMotor: programmed a board connected to a button, an LED light, and a servo motor. Motor and button respond to commands either in the form of the user pressing the button to toggle them on or off or typing "start" or "stop" into serial
- ultrasonic: Programmed a board connected to an ultrasonic sensor to read distance while a button is pressed and calculates average distance, velocity, and acceleration