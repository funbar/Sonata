#include <Time.h>

//int m, d, y;
int h, mi, s;
unsigned long prevDisplay;

int lightPin = 0;  //define a pin for Photo resistor
// row 1
int ledPin = 9;     //define a pin for LED
// row 2
int ledPin1 = 10;
// row 4
int ledPin2 = 6;
// row 3
int ledPin3 = 11;
int photocellReading;
int LEDbrightness;
bool isOn = false;
char incomingByte;

void allOn();

void setup()
{
    Serial.begin(9600);  //Begin serial communcation
    pinMode( ledPin, OUTPUT );
  //  getClockInfo();
   // setTime(h,mi,s,0,0,0);
   // prevDisplay = now();
}

void loop()
{

  photocellReading = analogRead(lightPin) - 888;
  //photocellReading = analogRead(lightPin);
 // Serial.println(photocellReading);

  LEDbrightness = map(photocellReading, 0, 111, 0, 255);
  //Serial.println(photocellReading);
  
  
    if (Serial.available() > 0) {  // if the data came
    
    incomingByte = Serial.read(); // read byte
    
    if(incomingByte == '0') {
      isOn = false;
       //digitalWrite(ledPin, LOW);  // if 1, switch LED Off
       Serial.println("LED OFF. Press 1 to LED ON!");  // print message
    }
    if(incomingByte == '1') {
      isOn = true;
       //digitalWrite(ledPin, HIGH); // if 0, switch LED on
       Serial.println("LED ON. Press 0 to LED OFF!");
    }
  }
  
  if (isOn)
  {
    firstRowLED();
    delay(5000);
    secondRowLED();
    delay(5000); 
    thirdRowLED();
    delay(5000);
    fourthRowLED();
    delay(2000); //short delay for faster response to light.
    allOff();
    // if statement here for sleep flag
    delay(10000);
    //if(photocellReading < 90){
      //allOn();
    //}
    //else if(photocellReading > 30){
    //  darkCase();
    //}
    //else {
    //  reading(photocellReading);
    //}
  }

}

void reading(int photocellReading)
{
  int bright = 255;
  int lessBright = 80;
  int dim = 25;
  int dark = 10;  
 if(photocellReading < 25){
  //Serial.println(" - Bright room");
  analogWrite(ledPin, bright);
 }
 else if(photocellReading < 50){ 
  //Serial.println(" - Slightly bright");
  analogWrite(ledPin, lessBright);
 }
 else if(photocellReading < 80){
  //Serial.println(" - Dim room");
   analogWrite(ledPin, dim);
 }
 else if(photocellReading > 80){
  //Serial.println(" - Dark");
   analogWrite(ledPin, dark);
 }
}
void firstRowLED(){
  analogWrite(ledPin, 255);
}

void secondRowLED(){
  int bright = 255;
  int lessBright = 80;
  int dim = 25;
  int dark = 10; 
  analogWrite(ledPin1, 255);
}
void thirdRowLED(){
  int bright = 255;
  int lessBright = 80;
  int dim = 25;
  int dark = 10; 
  analogWrite(ledPin3, 255);
}

void fourthRowLED(){
  int bright = 255;
  int lessBright = 80;
  int dim = 25;
  int dark = 10; 
  analogWrite(ledPin2, 255);
}

void firstRowOff(){
  analogWrite(ledPin, 0);
}

void secondRowOff(){
  analogWrite(ledPin1, 0);
}

void thirdRowOff(){
  analogWrite(ledPin2, 0);
}

void fourthRowOff(){
  analogWrite(ledPin3, 0);
}

void allOn()
{
 firstRowLED();
 secondRowLED();
 thirdRowLED();
 fourthRowLED();
}

void allOff()
{
  firstRowOff();
  secondRowOff();
  thirdRowOff();
  fourthRowOff();
 //digitalWrite(ledPin, LOW); 
}

void lightsDim()
{
 digitalWrite(ledPin, (photocellReading));
}

void darkCase(){
  analogWrite(ledPin, 10);
  delay(15000);
  analogWrite(ledPin, 25);
  analogWrite(ledPin1, 10);
  delay(15000);
  analogWrite(ledPin, 80);
  analogWrite(ledPin1, 25);
  analogWrite(ledPin2, 10);
  delay(15000);
  analogWrite(ledPin, 160);
  analogWrite(ledPin1, 80);
  analogWrite(ledPin2, 25);
  analogWrite(ledPin3, 10);
  delay(15000);
  analogWrite(ledPin, 255);
  analogWrite(ledPin1, 160);
  analogWrite(ledPin2, 80);
  analogWrite(ledPin3, 25);
  delay(15000);
  analogWrite(ledPin1, 255);
  analogWrite(ledPin2, 160);
  analogWrite(ledPin3, 80);
  delay(15000);
  analogWrite(ledPin2, 255);
  analogWrite(ledPin3, 160);
  delay(15000);
  analogWrite(ledPin3, 255);
  delay(90000000);
}

void getClockInfo(){
    int userInput;
    char buffer[4];

      Serial.println("Please enter the current Hour:");
      //Serial.println(buffer);
      //hour
  do{
      userInput = -1;
      while(Serial.available() == 0); //wait for input
          int length = Serial.readBytesUntil('\n', buffer, 3);
            if(length < 4){
              buffer[length] = '\0';
            if(length == 1){
              buffer[2] = '\0';
            }
      }
    if(isdigit(buffer[0]) && (isdigit(buffer[1]) || buffer[1] == '\0') && buffer[2] == '\0'){
          userInput = atoi(buffer);
    }
    if(userInput < 0 || userInput > 23){
          Serial.println("Invalid input. Please enter a number from 0-23.");
    }
      Serial.end();
      Serial.begin(9600);
  }while(userInput < 0 || userInput > 23);
        h = userInput;
      Serial.println("Please enter the current Minutes:");
  //minute
  do{
        userInput = -1;
        while(Serial.available() == 0); //wait for input
          int length = Serial.readBytesUntil('\n', buffer, 3);
            if(length < 4){
                buffer[length] = '\0';
            if(length == 1){
                buffer[2] = '\0';
            }
      }
    if(isdigit(buffer[0]) && (isdigit(buffer[1]) || buffer[1] == '\0') && buffer[2] == '\0'){
            userInput = atoi(buffer);
    }
    if(userInput < 0 || userInput > 59){
            Serial.println("Invalid input. Please enter a number from 0-59.");
    }
            Serial.end();
            Serial.begin(9600);
  }while(userInput < 0 || userInput > 59);
        mi = userInput;
  
            Serial.println("Please enter the current seconds:");
  //second
  do{
        userInput = -1;
        while(Serial.available() == 0); //wait for input
            int length = Serial.readBytesUntil('\n', buffer, 3);
    //null terminator setting
          if(length < 4){
            buffer[length] = '\0';
              if(length == 1){
                buffer[2] = '\0';
              }
          }
    if(isdigit(buffer[0]) && (isdigit(buffer[1]) || buffer[1] == '\0') && buffer[2] == '\0'){
          userInput = atoi(buffer);
    }
    if(userInput < 0 || userInput > 59){
          Serial.println("Invalid input. Please enter a number from 0-59.");
    }
          Serial.end();
          Serial.begin(9600);
  }while(userInput < 0 || userInput > 59);
      s = userInput;
  
    return;
}

void serialClockDisplay(){

      if(hour() < 10){
      }
      else{
      }
        Serial.print(hour());
        Serial.print(":");
      if(minute() < 10){
        Serial.print('0');
      }
        Serial.print(minute());
        Serial.print(":");
      if(second() < 10){
        Serial.print('0');
      }
      Serial.print(second());
}

