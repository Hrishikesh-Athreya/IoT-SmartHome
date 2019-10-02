#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <Servo.h>

#define WIFI_SSID "SSID"
#define WIFI_PASSWORD "PASSWORD"
#define FIREBASE_HOST "example.firebaseio.com"
#define FIREBASE_AUTH "AUTH_TOKEN"

#define LED D0 

//int ledPin = D0; // choose pin for the LED
int inputPin = D1; // choose input pin (for Infrared sensor) 
int val = 0; // variable for reading the pin status


FirebaseData ledobject;
FirebaseData curtainobject;
Servo curtain;
Servo lock;

void setup() {
   pinMode(inputPin, INPUT);
  pinMode(LED, OUTPUT);
  curtain.attach(2);//D4
  lock.attach(D2);
  curtain.write(0);
  lock.write(0);
  
  Serial.begin(9600);
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);             
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID); Serial.println(" ...");

  int teller = 0;
  while (WiFi.status() != WL_CONNECTED)
  {                                       // Wait for the Wi-Fi to connect
    delay(1000);
    Serial.print(++teller); Serial.print(' ');
  }

  Serial.println('\n');
  Serial.println("Connection established!");  
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());         // Send the IP address of the ESP8266 to the computer
}

void loop() {
  //LED : ON-1, OFF-0
if (Firebase.getInt(ledobject, "/Appliances/LED")) {

    if (ledobject.intData() == 0) {
      Serial.println(ledobject.intData());
      digitalWrite(LED, LOW);
    }
  else {
    Serial.println(ledobject.intData());
    digitalWrite(LED, HIGH);
  }
  } 
//delay(1000);
// curtains: 1-open, 0-closed
if (Firebase.getInt(curtainobject, "/Appliances/curtains")) {

    if (curtainobject.intData() == 0) {
      //Serial.println(curtainobject.intData());
      curtain.write(180);
    }
  else {
    Serial.println(curtainobject.intData());
    curtain.write(0);
  }
  } 

  if (Firebase.getInt(curtainobject, "/Appliances/lock")) {

    if (curtainobject.intData() == 0) {
      //Serial.println(curtainobject.intData());
      lock.write(140);
    }
  else {
    Serial.println(curtainobject.intData());
    lock.write(0);
  }
  } 

  val = digitalRead(inputPin); // read input value 
   if (val == HIGH)
   { // check if the input is HIGH
      Firebase.setInt(curtainobject, "/Appliances/intruder",0); // turn LED OFF   
   } 
    else{
     Firebase.setInt(curtainobject, "/Appliances/intruder",1); // turn LED ON  
    }l
}
