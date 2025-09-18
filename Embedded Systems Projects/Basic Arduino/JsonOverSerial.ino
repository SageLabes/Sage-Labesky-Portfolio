/*
* JsonOverSerial.ino
*
* allows Json files to be sent back and forth from the chip to the system using Serial
*
* Author: Sage Labesky
* Created: 4/24/2025
* Modified: 4/24/2025
*/
#include <ArduinoJson.h>

const int CYCLE_TIME_MS = 500;

StaticJsonDocument<200> g_doc; // global variable storing the document
String g_json = String(); // global variable used to store the json information

void setup() {
  Serial.begin(115200);
}

void loop() {
  if(Serial.available() > 0){
    char c = Serial.read();
    if(c == '\n' || c == '\r' || c == '\n\r'){
      // catches any deserialization errors if the input value is not json
      DeserializationError error = deserializeJson(g_doc, g_json);
      if (error) {
        Serial.print("deserializeJson() returned ");
        Serial.println(error.c_str());
      } else {
        int value = g_doc["value"] | (-1); // includes | to provide specific default values for easier coding to determine if correct value was passed
        String mtype = g_doc["mtype"] | "error";

        if(!catch_extraction_errors(value, mtype)){ // checks to see if the two important values have been passed
          g_doc["value"] = value + 1;
          g_doc["mtype"] = "pong";
          serializeJson(g_doc, Serial); // output
        }
        Serial.println();
      }
      g_json = String(); // resets the string when it is completed
    } else {
      // continues to build the command
      g_json.concat(c);
    }
  }
}

void loop_serialize() {
  static int s_current_val = 0; // variable keeping track of val for loop_serialize
  // function that just outpurs a json every 500 ms
  static int time_of_last_print;
  if(millis()-time_of_last_print >= CYCLE_TIME_MS){
    // creating the json
    g_doc["mtype"] = "pong";
    s_current_val += 1;
    g_doc["value"] = s_current_val;
    g_doc["millis"] = millis();
    // outputing the json
    serializeJson(g_doc, Serial);
    Serial.println();

    time_of_last_print = millis();
  }
  
}

bool catch_extraction_errors(int value, String mtype){
  // checks to see if the default values are present and errors if they are
  if(value == -1){
    Serial.printf("Error: value of incorrect type or value not provided \n");
    return true;
  } else if(mtype == "error" || !(mtype == "ping")){
    Serial.printf("Error: mtype is incorrect, missing, or is not a String \n");
    return true;
  } else {
    return false;
  }
}