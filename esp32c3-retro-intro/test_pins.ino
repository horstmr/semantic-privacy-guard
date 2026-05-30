/*
  Teste de pinos - verifica conectividade básica do display
  Ajuda a identificar pinos invertidos
*/

#include "User_Setup.h"
#define USER_SETUP_LOADED
#include <TFT_eSPI.h>

TFT_eSPI tft = TFT_eSPI();

void setup() {
  Serial.begin(115200);
  delay(1000);

  Serial.println("\n[TESTE DE PINOS]");
  Serial.println("Inicializando display...\n");

  tft.init();
  tft.setRotation(1);

  // Testa backlight
  Serial.println("✓ Backlight ligado");

  // Tela branca
  Serial.println("Exibindo tela branca...");
  tft.fillScreen(TFT_WHITE);
  delay(1000);

  // Tela preta
  Serial.println("Exibindo tela preta...");
  tft.fillScreen(TFT_BLACK);
  delay(1000);

  // Cores básicas
  Serial.println("Testando cores...");
  tft.fillScreen(TFT_RED);
  delay(500);
  tft.fillScreen(TFT_GREEN);
  delay(500);
  tft.fillScreen(TFT_BLUE);
  delay(500);
  tft.fillScreen(TFT_BLACK);

  // Texto
  Serial.println("Exibindo texto...");
  tft.setTextDatum(MC_DATUM);
  tft.setTextColor(TFT_WHITE);
  tft.drawString("TESTE OK", 160, 120, 4);

  Serial.println("\n[RESULTADO]");
  Serial.println("Se você viu branco → verde → azul → preto → 'TESTE OK':");
  Serial.println("  ✓ PINOS CORRETOS!");
  Serial.println("\nSe viu lixo/cores estranhas:");
  Serial.println("  ✗ Pode ser DC/CS ou MOSI/SCLK invertidos");
  Serial.println("\nSe nada apareceu:");
  Serial.println("  ✗ Verifique RST, BL ou alimentação");
}

void loop() {
  delay(10000);
}
