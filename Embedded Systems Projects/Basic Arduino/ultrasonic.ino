/*
* ultrasonic.ino
*
* uses an ultrasonic sensor to measure distance when a button is pressed and calculates average distance, velocity, and acceleration when the button is released
* Author: Sage Labesky
* Created: 4/10/2025
* Modified: 4/10/2025
*/
const int BUTTON_PIN = A0;
const int TRIG_PIN = A2;
const int ECHO_PIN = A1;
const unsigned int MAX_DIST = 23200; // maximum distance we want to read from the ultrasonic sensor
const float CM_DIVISOR = 58.0; //number we need to divide value provided by ultrasonic sensor by to recieve the CM distance
// global variables used to keep track of whether the button state has changed on the current iteration
int g_button_state = 0;
int g_prev_button_state = 0;
// global variables used to determine distance, time, velocity, and acceleration when the user stops pressing the button
float g_total_distance_per_press = 0;
int g_times_read_per_press = 0;
unsigned long g_start_time_of_press = 0;
float g_start_distance = 0;
// global variable that is used to keep track of the final distance to help figure out velocity and excelleration
float g_current_distance = 0;


void setup() {
  pinMode(BUTTON_PIN, INPUT);
  pinMode(TRIG_PIN, OUTPUT);
  digitalWrite(TRIG_PIN, LOW);
  pinMode(ECHO_PIN, INPUT);
  Serial.begin(115200);
}

void loop() {
  g_button_state = digitalRead(BUTTON_PIN);
  if (g_button_state == LOW) {
    if (g_prev_button_state == HIGH) {
      //Occurs if the user just pressed the button, resets the variables which may have been assigned values previously
      g_start_time_of_press = millis();
      g_total_distance_per_press = 0;
      g_times_read_per_press = 0;
    }

    digitalWrite(TRIG_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(TRIG_PIN, LOW);
    //handles reading from the ultrasonic sensor
    while (digitalRead(ECHO_PIN) == 0);
    unsigned long t1 = micros();
    while (digitalRead(ECHO_PIN) == 1);
    unsigned long t2 = micros();
    unsigned long pulse_width = t2 - t1;

    if (pulse_width <= MAX_DIST) {
      g_current_distance = pulse_width / CM_DIVISOR; //keeping g_current_distance assigned each iteration so after the final one we know the final distance value
      g_total_distance_per_press += g_current_distance;
      g_times_read_per_press++;

      if (g_times_read_per_press == 1) {
        //on the first iteration we set the start distance but only after actually aquiring it
        g_start_distance = g_current_distance;
      }
    }
  }

  if (g_button_state == HIGH && g_prev_button_state == LOW) {
    // all calculations are handled in the prints to save from extra variables
    Serial.printf("Average Distance While Pressed: %f cm\n", g_total_distance_per_press / g_times_read_per_press);
    Serial.printf("Time Pressed: %d ms\n", millis() - g_start_time_of_press);
    Serial.printf("Average Velocity: %f cm/ms\n", (g_current_distance - g_start_distance) / float(millis() - g_start_time_of_press));
    Serial.printf("Average Acceleration: %f cm/ms^2\n\n", ((g_current_distance - g_start_distance) / float(millis() - g_start_time_of_press)) / float(millis() - g_start_time_of_press));
  }

  g_prev_button_state = g_button_state;
  delay(20);
}