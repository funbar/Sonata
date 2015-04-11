#include <Time.h>

//int m, d, y;
int h, mi, s;
unsigned long prevDisplay;

int lightPin = 0;  //define a pin for Photo resistor
int ledPin = 9;     //define a pin for LED
int photocellReading;
int LEDbrightness;

void allOn();

void setup()
{
    Serial.begin(9600);  //Begin serial communcation
    pinMode( ledPin, OUTPUT );
    getClockInfo();
    setTime(h,mi,s,0,0,0);
    prevDisplay = now();
}

void loop()
{
      if( now() != prevDisplay){
      prevDisplay = now();
      serialClockDisplay(); 
      Serial.print("\n"); 
    }
  photocellReading = analogRead(lightPin) - 888;
  //photocellReading = analogRead(lightPin);
 // Serial.println(photocellReading);

  LEDbrightness = map(photocellReading, 0, 111, 0, 255);
  reading(photocellReading);
  
  delay(2000); //short delay for faster response to light.

}

void reading(int photocellReading)
{
  int bright = 255;
  int lessBright = 80;
  int dim = 25;
  int dark = 10;  
 if(photocellReading < 25){
  //Serial.println(" - Bright room");
  //analogWrite(ledPin, LEDbrightness);
  //allOn();
  analogWrite(ledPin, bright);
 }
 else if(photocellReading < 50){ 
  //Serial.println(" - Slightly bright");
 // analogWrite(ledPin, LEDbrightness);
  //dim();
  analogWrite(ledPin, lessBright);
 }
 else if(photocellReading < 80){
  //Serial.println(" - Dim room");
  //analogWrite(ledPin, LEDbrightness);
  //dim();
   analogWrite(ledPin, dim);
 }
 else if(photocellReading > 80){
  //Serial.println(" - Dark");
  //analogWrite(ledPin, LEDbrightness);
   analogWrite(ledPin, dark);
  //allOff();
 }
}

void allOn()
{
 digitalWrite(ledPin, HIGH); 
}

void allOff()
{
 digitalWrite(ledPin, LOW); 
}

void lightsDim()
{
 digitalWrite(ledPin, (photocellReading));
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

