/*
* ControllingServoMotor.ino
*
* controls the movement of a servo by releasing a button or typing commands into the serial monitor
*
* Author: Sage Labesky
* Created: 4/14/2025
* Modified: 4/17/2025
*/
#include <ESP32Servo.h>


const int SERIAL_RATE_MS = 2000;
const int SERVO_START_POS = 0;
const int LED_PIN = D7;
const int BUTTON_PIN = A1;
const int SERVO_MOVE_RATE_MS = 15;
// consts used to store trings for easy comparison when reading serial input
const String START = String("start");
const String STOP = String("stop");
// consts to control the LED flash rate during the two states where it changes
const int RUN_FLASH_RATE_MS = 250;
const int STOPPING_FLASH_RATE_MS = 50;

// global variables used for timing of prints, LED flashed, and movement
int g_time_of_last_print_ms = 0;
int g_time_of_last_flash_ms = 0;
int g_time_of_last_move_ms = 0;
// global variables used to store the states of the system, led, and button
int g_state = 0; // 0 = stopped state, 1 = running state, 2 = stopping state
int g_led_state = LOW;
int g_button_state = LOW;

int g_step = -1; //global variable used to simplify the process of the pos changing and remove the loop present in the previous code
String g_cmd = String(); // global variable representing the command string
// global variables relating to the servo
Servo g_servo; 
int g_pos_deg = 0;    // variable to store the servo position


void setup() {
  Serial.begin(115200);
  g_servo.attach(A0);
  pinMode(LED_PIN, OUTPUT);
  g_servo.write(SERVO_START_POS);
  digitalWrite(LED_PIN, g_led_state);

}

void loop() {
  // prints the waiting message at the speed defined by SERIAL_RATE
  if(millis() - g_time_of_last_print_ms >= SERIAL_RATE_MS){
    Serial.printf("Waiting\n");
    g_time_of_last_print_ms = millis();
  }
  // conditional that reads Serial input
  if(Serial.available() > 0){
    char c = Serial.read();
    if(c == '\n' || c == '\r'){
      // When a command is entered it is read and depending on what is read the state changes
      if(g_cmd.equalsIgnoreCase(START)){
        g_state = 1;
      } else if(g_cmd.equalsIgnoreCase(STOP) && g_state == 1){
        g_state = 2;
      } else {
        Serial.printf("Unrecognized Command: %s \n", g_cmd.c_str());
      }
      g_cmd = String();
    } else {
      // building the command one character at a time
      g_cmd.concat(c);
    }
  }
  // the servo movement helper function is only called at intervals of SERVO_MOVE_RATE to keep it from moving very fast
  if (millis() - g_time_of_last_move_ms >= SERVO_MOVE_RATE_MS){
    move_servo();
    g_time_of_last_move_ms = millis();
  }
  check_button();
}

void move_servo(){
  // helper function that moves the servo differently depending on state
  if(g_state == 1){ // handles running state
    if(g_pos_deg >= 180 || g_pos_deg <= 0){ // changes direction when it reaches 180 or 0
      g_step = g_step * -1;
    }
    g_pos_deg += g_step;
    g_servo.write(g_pos_deg); 
    blink_led(RUN_FLASH_RATE_MS);
  } else if(g_state == 2) { // handles stopping state
    if(g_pos_deg >= 180){
      g_step = g_step * -1;
      g_pos_deg += g_step;
    } else if(g_pos_deg <= 0){ // extra conditional because it must return to stopped state rather than change directions when reaching 0
      g_state = 0;
    } else{
      g_pos_deg += g_step;
    }
    g_servo.write(g_pos_deg); 
    blink_led(STOPPING_FLASH_RATE_MS);
  } else{ // handles stopped case
    digitalWrite(LED_PIN, 0);
    g_pos_deg = 0;
  }
}

void blink_led(const int rate){
  // helper function to blink the led at a certain rate
  if(millis() - g_time_of_last_flash_ms >= rate){
        digitalWrite(LED_PIN, g_led_state);
        g_led_state = !g_led_state;
        g_time_of_last_flash_ms = millis();
    }
}

void check_button(){
  // helper function to read input from the button
  int prev_button_state = g_button_state;
  g_button_state = digitalRead(BUTTON_PIN);
  if(prev_button_state == HIGH && g_button_state == LOW){ // the button only triggers a state change when it is released
    if(g_state == 1){ // state can go from 1 to 2 or from 0 or 2 to 1
      g_state = 2;
    } else {
      g_state = 1;
    }
  }
}