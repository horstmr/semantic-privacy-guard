/*
  TESTE SUPER BASICO - Display ST7789

  Pinos: User_Setup.h (local)
  Testa: cores basicas + texto
*/

#include "User_Setup.h"
#define USER_SETUP_LOADED
#include <TFT_eSPI.h>

TFT_eSPI tft = TFT_eSPI();

void setup() {
  Serial.begin(115200);
  delay(1000);

  Serial.println("\n[TESTE DISPLAY]");

  tft.init();
  tft.setRotation(1);

  Serial.println("Display inicializado");
}

void loop() {
  // Branco
  Serial.println("Branco...");
  tft.fillScreen(TFT_WHITE);
  delay(500);

  // Vermelho
  Serial.println("Vermelho...");
  tft.fillScreen(TFT_RED);
  delay(500);

  // Verde
  Serial.println("Verde...");
  tft.fillScreen(TFT_GREEN);
  delay(500);

  // Azul
  Serial.println("Azul...");
  tft.fillScreen(TFT_BLUE);
  delay(500);

  // Preto com texto
  Serial.println("Texto...");
  tft.fillScreen(TFT_BLACK);
  tft.setTextDatum(MC_DATUM);
  tft.setTextColor(TFT_WHITE);
  tft.drawString("OK!", 160, 120, 4);
  delay(1000);
}
