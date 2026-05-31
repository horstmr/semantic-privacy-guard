/*
  ============================================================
   INTRO RETRO 8-BIT  -  animacao original (homenagem ao genero)
  ============================================================
   Placa:    ESP32-C3 Super Mini
   Display:  GMT020-02-8P  (ST7789, 240x320)  via SPI
   Setup:    User_Setup.h local na pasta do projeto

   Pinos (devem bater com o User_Setup.h na sua pasta):
     SCL->GPIO4  SDA->GPIO6  CS->GPIO7  DC->GPIO5
     RST->GPIO10 BL->GPIO1   VCC->3V3   GND->GND

   OBS: arte 100% original. Nao usa nenhum asset de jogo
   protegido por copyright. Troque TITLE/SUBTITLE/cores a vontade.
  ============================================================
*/

#include "User_Setup.h"           // carrega do folder do sketch
#define USER_SETUP_LOADED         // evita dupla inclusão
#include <TFT_eSPI.h>
#include <math.h>

TFT_eSPI tft = TFT_eSPI();

// ---- tela em paisagem ----
#define W 320
#define H 240

// ---- texto do banner (mude aqui!) ----
const char* TITLE    = "PIXEL  QUEST";
const char* SUBTITLE = "- nivel 1 -";

// ---- layout do cenario ----
const int groundY = 170;     // topo da grama
const int GRASS    = 10;     // altura da faixa de grama
const int heroBaseY = 152;   // topo do heroi (pes ~180)

// ---- cores (definidas no setup) ----
uint16_t SKY, GRASS_C, GRASS_HI, BRICK, MORTAR;
uint16_t CURTAIN, CURTAIN_DK, CURTAIN_HI;
uint16_t GOLD, GOLD_DK, GOLD_HI;
uint16_t HERO, HERO_HI, HERO_HEAD, HERO_LEG, VISOR, VISOR_HI;
uint16_t BANNER, BANNER_HI, CRATE, CRATE_DK, CRATE_HI, PLATE;

// ---- estado de animacao ----
float cloudX[3] = {40, 150, 250};
int   cloudY[3] = {22, 44, 30};
int   heroX, heroY = heroBaseY;
int   crateX = W/2 + 30;
int   coinX  = W/2 + 90, coinY = 130;
int   plateY = 204;
bool  startOn = false;

// ---------- reconstrucao do cenario num retangulo ----------
void drawBricks(int x, int y, int w, int h) {
  int x2 = x + w, y2 = y + h;
  int gb = groundY + GRASS;
  for (int ly = gb; ly <= y2; ly += 14)
    if (ly >= y) tft.drawFastHLine(x, ly, w, MORTAR);
  for (int ly = gb; ly < y2; ly += 14) {
    int row = (ly - gb) / 14;
    int off = (row % 2) ? 14 : 0;
    int rt = max(ly, y), rb = min(ly + 14, y2);
    if (rt >= rb) continue;
    for (int lx = off; lx <= x2; lx += 28)
      if (lx >= x) tft.drawFastVLine(lx, rt, rb - rt, MORTAR);
  }
}

void drawStage(int x, int y, int w, int h) {
  int yb = y + h;
  int gTop = groundY, gBot = groundY + GRASS;
  if (y < gTop) {                                 // ceu
    int hh = min(yb, gTop) - y;
    tft.fillRect(x, y, w, hh, SKY);
  }
  if (yb > gTop && y < gBot) {                    // grama
    int gy = max(y, gTop);
    int hh = min(yb, gBot) - gy;
    tft.fillRect(x, gy, w, hh, GRASS_C);
    if (gy == gTop) tft.fillRect(x, gTop, w, 2, GRASS_HI);
  }
  if (yb > gBot) {                                // tijolos
    int by = max(y, gBot);
    tft.fillRect(x, by, w, yb - by, BRICK);
    drawBricks(x, by, w, yb - by);
  }
}

// ---------- cortina ----------
void drawCurtainStrip(int y, int h) {
  tft.fillRect(0, y, W, h, CURTAIN);
  for (int x = 0; x < W; x += 26) {
    tft.drawFastVLine(x,     y, h, CURTAIN_DK);   // sombra da dobra
    tft.drawFastVLine(x + 2, y, h, CURTAIN_HI);   // brilho da dobra
  }
}
void drawCurtainHem(int bottom) {
  if (bottom < 4 || bottom > H) return;
  tft.fillRect(0, bottom - 4, W, 4, CURTAIN_DK);
  tft.fillRect(0, bottom - 1, W, 1, GOLD);        // barra dourada
}
void coverCurtain() {
  for (int b = 0; b < H; b += 6) {
    drawCurtainStrip(b, 6);
    drawCurtainHem(b + 6);
    delay(14);
  }
  drawCurtainStrip(0, H);
}
void riseCurtain() {
  for (int b = H; b > 0; b -= 6) {
    int top = max(b - 6, 0);
    drawStage(0, top, W, b - top);                // revela faixa
    drawCurtainHem(top);
    delay(16);
  }
  drawStage(0, 0, W, H);                           // garante cena limpa
}

// ---------- elementos ----------
void drawCloud(int x, int y) {
  uint16_t c = TFT_WHITE;
  tft.fillRoundRect(x, y + 6, 44, 12, 6, c);
  tft.fillCircle(x + 12, y + 8, 9, c);
  tft.fillCircle(x + 24, y + 5, 11, c);
  tft.fillCircle(x + 34, y + 9, 8, c);
}

void drawCrate(int x, int y) {
  tft.fillRect(x, y, 22, 22, CRATE);
  tft.drawRect(x, y, 22, 22, CRATE_DK);
  tft.drawRect(x + 1, y + 1, 20, 20, CRATE_HI);
  tft.drawLine(x, y, x + 21, y + 21, CRATE_DK);
  tft.drawLine(x + 21, y, x, y + 21, CRATE_DK);
}

void drawCoin(int cx, int cy, int phase) {
  drawStage(cx - 11, cy - 11, 22, 22);            // apaga
  int w = abs((phase % 20) - 10);                 // 0..10  (efeito girar)
  if (w < 1) w = 1;
  tft.fillRoundRect(cx - w, cy - 9, 2 * w, 18, (w > 3) ? 4 : 1, GOLD);
  if (w > 3) {
    tft.drawRoundRect(cx - w, cy - 9, 2 * w, 18, 4, GOLD_DK);
    tft.fillRect(cx - 1, cy - 5, 2, 10, GOLD_HI); // reflexo
  }
}

void drawHero(int x, int y, bool step) {
  if (step) {                                     // pernas alternadas
    tft.fillRect(x + 3,  y + 22, 5, 6, HERO_LEG);
    tft.fillRect(x + 12, y + 24, 5, 4, HERO_LEG);
  } else {
    tft.fillRect(x + 3,  y + 24, 5, 4, HERO_LEG);
    tft.fillRect(x + 12, y + 22, 5, 6, HERO_LEG);
  }
  tft.fillRoundRect(x + 2, y + 12, 16, 12, 3, HERO);   // corpo
  tft.fillRect(x + 2, y + 12, 16, 3, HERO_HI);
  tft.fillRect(x,      y + 13, 3, 7, HERO);            // bracos
  tft.fillRect(x + 17, y + 13, 3, 7, HERO);
  tft.fillRoundRect(x + 3, y, 14, 13, 4, HERO_HEAD);   // cabeca
  tft.fillRect(x + 5,  y + 4, 11, 4, VISOR);           // visor
  tft.fillRect(x + 12, y + 4, 3,  4, VISOR_HI);
  tft.drawFastVLine(x + 10, y - 4, 4, HERO);           // antena
  tft.fillCircle(x + 10, y - 5, 2, GOLD);
}

void drawTitle() {
  int bw = 220, bh = 46, bx = W / 2 - bw / 2, by = 64;
  tft.fillRoundRect(bx, by, bw, bh, 8, BANNER);
  tft.drawRoundRect(bx, by, bw, bh, 8, GOLD);
  tft.drawRoundRect(bx + 2, by + 2, bw - 4, bh - 4, 6, BANNER_HI);
  tft.setTextDatum(MC_DATUM);
  tft.setTextColor(GOLD);
  tft.drawString(TITLE, W / 2, by + 18, 4);
  tft.setTextColor(TFT_WHITE);
  tft.drawString(SUBTITLE, W / 2, by + 36, 2);
}

void drawPlate() {
  int pw = 150, ph = 22, px = W / 2 - pw / 2;
  tft.fillRoundRect(px, plateY, pw, ph, 6, PLATE);
  tft.drawRoundRect(px, plateY, pw, ph, 6, GOLD);
}
void blinkStart() {
  startOn = !startOn;
  tft.setTextDatum(MC_DATUM);
  tft.setTextColor(startOn ? GOLD : PLATE, PLATE);
  tft.drawString("PRESS  START", W / 2, plateY + 11, 2);
}

// ---------- sequencias ----------
void heroWalkIn() {
  int target = W / 2 - 70, prev = -28;
  for (int x = -24; x <= target; x += 3) {
    int hop = heroY - (int)(4 * fabs(sin(x * 0.25)));
    drawStage(prev - 2, heroY - 10, 30, 44);
    drawHero(x, hop, (x / 3) % 2);
    prev = x;
    delay(26);
  }
  heroX = target;
}

void animateClouds() {
  for (int i = 0; i < 3; i++) {
    int ox = (int)cloudX[i];
    cloudX[i] -= 0.7;
    if (cloudX[i] < -50) cloudX[i] = W + 10;
    int nx = (int)cloudX[i];
    if (nx != ox) {
      drawStage(ox - 2, cloudY[i] - 2, 52, 26);
      drawCloud(nx, cloudY[i]);
    }
  }
}

void animateHero(int f) {
  int hop = heroY - (int)(5 * fabs(sin(f * 0.18)));
  drawStage(heroX - 2, heroY - 10, 30, 44);
  drawHero(heroX, hop, (f / 6) % 2);
}

// ---------- setup / loop ----------
void setup() {
  // cores
  SKY        = tft.color565(92, 148, 252);
  GRASS_C    = tft.color565(0, 168, 0);
  GRASS_HI   = tft.color565(96, 216, 96);
  BRICK      = tft.color565(200, 76, 12);
  MORTAR     = tft.color565(120, 40, 0);
  CURTAIN    = tft.color565(140, 16, 32);
  CURTAIN_DK = tft.color565(90, 8, 20);
  CURTAIN_HI = tft.color565(190, 40, 60);
  GOLD       = tft.color565(252, 216, 40);
  GOLD_DK    = tft.color565(180, 140, 0);
  GOLD_HI    = tft.color565(255, 248, 180);
  HERO       = tft.color565(0, 160, 160);
  HERO_HI    = tft.color565(80, 220, 220);
  HERO_HEAD  = tft.color565(0, 120, 140);
  HERO_LEG   = tft.color565(40, 40, 80);
  VISOR      = tft.color565(20, 30, 60);
  VISOR_HI   = tft.color565(120, 200, 255);
  BANNER     = tft.color565(20, 20, 60);
  BANNER_HI  = tft.color565(60, 60, 140);
  CRATE      = tft.color565(180, 120, 60);
  CRATE_DK   = tft.color565(110, 70, 30);
  CRATE_HI   = tft.color565(220, 170, 100);
  PLATE      = tft.color565(10, 10, 30);

  tft.init();
  tft.setRotation(1);          // paisagem 320x240
  tft.fillScreen(TFT_BLACK);

#ifdef TFT_BL
  pinMode(TFT_BL, OUTPUT);
  digitalWrite(TFT_BL, HIGH);  // garante backlight ligado
#endif
}

void loop() {
  // monta cena e cobre com a cortina
  drawStage(0, 0, W, H);
  drawCurtainStrip(0, H);
  delay(400);

  // sobe a cortina revelando o cenario
  riseCurtain();

  // detalhes do palco
  drawCrate(crateX, groundY - 22);
  drawCoin(coinX, coinY, 5);

  // heroi entra pulando
  heroWalkIn();

  // titulo + press start
  drawTitle();
  drawPlate();

  // animacao em loop por alguns segundos
  for (int f = 0; f < 220; f++) {
    animateClouds();
    animateHero(f);
    drawCoin(coinX, coinY, f);
    if (f % 16 == 0) blinkStart();
    delay(18);
  }

  // fecha a cortina e recomeca
  coverCurtain();
  delay(300);
}
