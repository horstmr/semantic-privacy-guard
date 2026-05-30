/*
  ============================================================
   INTRO RETRO 8-BIT  -  com controle remoto via WiFi
  ============================================================
   Placa:    ESP32-C3 Super Mini
   Display:  GMT020-02-8P  (ST7789, 240x320)  via SPI
   WiFi:     modo AP (hotspot)  -  acesse 192.168.4.1
   Lib:      TFT_eSPI + WebServer nativa
  ============================================================
*/

#include <TFT_eSPI.h>
#include <WiFi.h>
#include <WebServer.h>
#include <math.h>

TFT_eSPI tft = TFT_eSPI();
WebServer server(80);

// ---- config WiFi ----
const char* SSID = "PIXEL_QUEST";
const char* PASSWORD = "12345678";

// ---- tela em paisagem ----
#define W 320
#define H 240

// ---- estado remoto ----
struct RemoteControl {
  bool running = true;
  int animSpeed = 100;  // percentual (50-150)
  String title = "PIXEL  QUEST";
  String subtitle = "- nivel 1 -";
  bool colorScheme = 0;  // 0=original, 1=alt
} remote;

// ---- layout do cenario ----
const int groundY = 170;
const int GRASS = 10;
const int heroBaseY = 152;

// ---- cores ----
uint16_t SKY, GRASS_C, GRASS_HI, BRICK, MORTAR;
uint16_t CURTAIN, CURTAIN_DK, CURTAIN_HI;
uint16_t GOLD, GOLD_DK, GOLD_HI;
uint16_t HERO, HERO_HI, HERO_HEAD, HERO_LEG, VISOR, VISOR_HI;
uint16_t BANNER, BANNER_HI, CRATE, CRATE_DK, CRATE_HI, PLATE;

// ---- animacao ----
float cloudX[3] = {40, 150, 250};
int cloudY[3] = {22, 44, 30};
int heroX, heroY = heroBaseY;
int crateX = W/2 + 30;
int coinX = W/2 + 90, coinY = 130;
int plateY = 204;
bool startOn = false;

void setColorScheme(int scheme) {
  if (scheme == 0) {
    // Original
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
  } else {
    // Alternativo: roxo/verde
    SKY        = tft.color565(120, 80, 160);
    GRASS_C    = tft.color565(0, 200, 100);
    GRASS_HI   = tft.color565(100, 255, 150);
    BRICK      = tft.color565(200, 100, 200);
    MORTAR     = tft.color565(100, 50, 100);
    CURTAIN    = tft.color565(200, 120, 180);
    CURTAIN_DK = tft.color565(140, 70, 140);
    CURTAIN_HI = tft.color565(255, 180, 220);
    GOLD       = tft.color565(0, 255, 200);
    GOLD_DK    = tft.color565(0, 180, 140);
    GOLD_HI    = tft.color565(150, 255, 230);
    HERO       = tft.color565(255, 100, 150);
    HERO_HI    = tft.color565(255, 150, 200);
    HERO_HEAD  = tft.color565(200, 80, 140);
    HERO_LEG   = tft.color565(80, 40, 100);
    VISOR      = tft.color565(60, 30, 100);
    VISOR_HI   = tft.color565(200, 150, 255);
    BANNER     = tft.color565(40, 20, 80);
    BANNER_HI  = tft.color565(100, 60, 160);
    CRATE      = tft.color565(180, 150, 100);
    CRATE_DK   = tft.color565(120, 100, 50);
    CRATE_HI   = tft.color565(220, 200, 150);
    PLATE      = tft.color565(30, 10, 50);
  }
}

// ---- desenho ----
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
  if (y < gTop) {
    int hh = min(yb, gTop) - y;
    tft.fillRect(x, y, w, hh, SKY);
  }
  if (yb > gTop && y < gBot) {
    int gy = max(y, gTop);
    int hh = min(yb, gBot) - gy;
    tft.fillRect(x, gy, w, hh, GRASS_C);
    if (gy == gTop) tft.fillRect(x, gTop, w, 2, GRASS_HI);
  }
  if (yb > gBot) {
    int by = max(y, gBot);
    tft.fillRect(x, by, w, yb - by, BRICK);
    drawBricks(x, by, w, yb - by);
  }
}

void drawCurtainStrip(int y, int h) {
  tft.fillRect(0, y, W, h, CURTAIN);
  for (int x = 0; x < W; x += 26) {
    tft.drawFastVLine(x, y, h, CURTAIN_DK);
    tft.drawFastVLine(x + 2, y, h, CURTAIN_HI);
  }
}

void drawCurtainHem(int bottom) {
  if (bottom < 4 || bottom > H) return;
  tft.fillRect(0, bottom - 4, W, 4, CURTAIN_DK);
  tft.fillRect(0, bottom - 1, W, 1, GOLD);
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
    drawStage(0, top, W, b - top);
    drawCurtainHem(top);
    delay(16);
  }
  drawStage(0, 0, W, H);
}

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
  drawStage(cx - 11, cy - 11, 22, 22);
  int w = abs((phase % 20) - 10);
  if (w < 1) w = 1;
  tft.fillRoundRect(cx - w, cy - 9, 2 * w, 18, (w > 3) ? 4 : 1, GOLD);
  if (w > 3) {
    tft.drawRoundRect(cx - w, cy - 9, 2 * w, 18, 4, GOLD_DK);
    tft.fillRect(cx - 1, cy - 5, 2, 10, GOLD_HI);
  }
}

void drawHero(int x, int y, bool step) {
  if (step) {
    tft.fillRect(x + 3, y + 22, 5, 6, HERO_LEG);
    tft.fillRect(x + 12, y + 24, 5, 4, HERO_LEG);
  } else {
    tft.fillRect(x + 3, y + 24, 5, 4, HERO_LEG);
    tft.fillRect(x + 12, y + 22, 5, 6, HERO_LEG);
  }
  tft.fillRoundRect(x + 2, y + 12, 16, 12, 3, HERO);
  tft.fillRect(x + 2, y + 12, 16, 3, HERO_HI);
  tft.fillRect(x, y + 13, 3, 7, HERO);
  tft.fillRect(x + 17, y + 13, 3, 7, HERO);
  tft.fillRoundRect(x + 3, y, 14, 13, 4, HERO_HEAD);
  tft.fillRect(x + 5, y + 4, 11, 4, VISOR);
  tft.fillRect(x + 12, y + 4, 3, 4, VISOR_HI);
  tft.drawFastVLine(x + 10, y - 4, 4, HERO);
  tft.fillCircle(x + 10, y - 5, 2, GOLD);
}

void drawTitle() {
  int bw = 220, bh = 46, bx = W / 2 - bw / 2, by = 64;
  tft.fillRoundRect(bx, by, bw, bh, 8, BANNER);
  tft.drawRoundRect(bx, by, bw, bh, 8, GOLD);
  tft.drawRoundRect(bx + 2, by + 2, bw - 4, bh - 4, 6, BANNER_HI);
  tft.setTextDatum(MC_DATUM);
  tft.setTextColor(GOLD);
  tft.drawString(remote.title.c_str(), W / 2, by + 18, 4);
  tft.setTextColor(TFT_WHITE);
  tft.drawString(remote.subtitle.c_str(), W / 2, by + 36, 2);
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

// ---- animacao ----
void heroWalkIn() {
  int target = W / 2 - 70, prev = -28;
  for (int x = -24; x <= target; x += 3) {
    if (!remote.running) return;
    int hop = heroY - (int)(4 * fabs(sin(x * 0.25)));
    drawStage(prev - 2, heroY - 10, 30, 44);
    drawHero(x, hop, (x / 3) % 2);
    prev = x;
    delay(26 * 100 / remote.animSpeed);
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

void runAnimation() {
  drawStage(0, 0, W, H);
  drawCurtainStrip(0, H);
  delay(400);

  if (!remote.running) return;
  riseCurtain();

  drawCrate(crateX, groundY - 22);
  drawCoin(coinX, coinY, 5);

  heroWalkIn();

  drawTitle();
  drawPlate();

  for (int f = 0; f < 220; f++) {
    if (!remote.running) return;
    animateClouds();
    animateHero(f);
    drawCoin(coinX, coinY, f);
    if (f % 16 == 0) blinkStart();
    delay(18 * 100 / remote.animSpeed);
  }

  if (remote.running) {
    coverCurtain();
    delay(300);
  }
}

// ---- servidor web ----
const char* HTML_PAGE = R"(
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Pixel Quest - Control</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
      font-family: 'Arial', sans-serif;
      background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
      color: #fff;
      min-height: 100vh;
      padding: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .container {
      background: rgba(0,0,0,0.6);
      border-radius: 15px;
      padding: 30px;
      max-width: 400px;
      width: 100%;
      box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
      border: 2px solid #fcd804;
    }
    h1 {
      text-align: center;
      margin-bottom: 30px;
      color: #fcd804;
      text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
    }
    .control-group {
      margin-bottom: 25px;
    }
    label {
      display: block;
      margin-bottom: 10px;
      font-weight: bold;
      color: #fcd804;
    }
    input[type="text"], input[type="range"] {
      width: 100%;
      padding: 10px;
      border: none;
      border-radius: 8px;
      font-size: 14px;
      background: rgba(255,255,255,0.1);
      color: #fff;
      border: 1px solid #fcd804;
    }
    input[type="range"] {
      padding: 0;
      cursor: pointer;
    }
    input[type="text"]::placeholder {
      color: rgba(255,255,255,0.5);
    }
    .button-group {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }
    button {
      flex: 1;
      padding: 12px;
      border: none;
      border-radius: 8px;
      font-weight: bold;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 14px;
    }
    button.play {
      background: #00a854;
      color: white;
    }
    button.play:hover {
      background: #00d65c;
      transform: scale(1.05);
    }
    button.pause {
      background: #d9534f;
      color: white;
    }
    button.pause:hover {
      background: #ff6e6e;
      transform: scale(1.05);
    }
    button.color {
      background: #9966ff;
      color: white;
    }
    button.color:hover {
      background: #bb99ff;
      transform: scale(1.05);
    }
    .speed-display {
      text-align: center;
      color: #fcd804;
      margin-top: 10px;
      font-weight: bold;
    }
    .info {
      background: rgba(31, 38, 135, 0.37);
      border-left: 4px solid #fcd804;
      padding: 15px;
      border-radius: 8px;
      font-size: 13px;
      line-height: 1.6;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>▶ PIXEL QUEST</h1>

    <div class="control-group">
      <label>Título:</label>
      <input type="text" id="title" value="PIXEL  QUEST" maxlength="20">
    </div>

    <div class="control-group">
      <label>Subtítulo:</label>
      <input type="text" id="subtitle" value="- nivel 1 -" maxlength="15">
    </div>

    <div class="control-group">
      <label>Velocidade:</label>
      <input type="range" id="speed" min="50" max="150" value="100">
      <div class="speed-display"><span id="speedVal">100</span>%</div>
    </div>

    <div class="button-group">
      <button class="play" onclick="sendCmd('play')">▶ PLAY</button>
      <button class="pause" onclick="sendCmd('pause')">⏸ PAUSE</button>
      <button class="color" onclick="sendCmd('color')">🎨 CORES</button>
    </div>

    <div class="info">
      <strong>Conectado ao:</strong> PIXEL_QUEST<br>
      <strong>IP:</strong> 192.168.4.1<br>
      <strong>Próximo: WiFi remoto em breve!</strong>
    </div>
  </div>

  <script>
    document.getElementById('speed').addEventListener('input', function(e) {
      document.getElementById('speedVal').innerText = e.target.value;
      fetch('/api/speed?val=' + e.target.value);
    });

    document.getElementById('title').addEventListener('change', function(e) {
      fetch('/api/title?val=' + encodeURIComponent(e.target.value));
    });

    document.getElementById('subtitle').addEventListener('change', function(e) {
      fetch('/api/subtitle?val=' + encodeURIComponent(e.target.value));
    });

    function sendCmd(cmd) {
      fetch('/api/' + cmd);
    }
  </script>
</body>
</html>
)";

void handleRoot() {
  server.send(200, "text/html", HTML_PAGE);
}

void handleSpeed() {
  if (server.hasArg("val")) {
    remote.animSpeed = server.arg("val").toInt();
    remote.animSpeed = constrain(remote.animSpeed, 50, 150);
  }
  server.send(200, "text/plain", "OK");
}

void handleTitle() {
  if (server.hasArg("val")) {
    remote.title = server.arg("val");
    if (remote.title.length() == 0) remote.title = "PIXEL  QUEST";
  }
  server.send(200, "text/plain", "OK");
}

void handleSubtitle() {
  if (server.hasArg("val")) {
    remote.subtitle = server.arg("val");
    if (remote.subtitle.length() == 0) remote.subtitle = "- nivel 1 -";
  }
  server.send(200, "text/plain", "OK");
}

void handlePlay() {
  remote.running = true;
  server.send(200, "text/plain", "OK");
}

void handlePause() {
  remote.running = false;
  server.send(200, "text/plain", "OK");
}

void handleColor() {
  remote.colorScheme = 1 - remote.colorScheme;
  setColorScheme(remote.colorScheme);
  server.send(200, "text/plain", "OK");
}

void setupWiFi() {
  WiFi.mode(WIFI_AP);
  WiFi.softAP(SSID, PASSWORD);

  Serial.println("\n[WiFi]");
  Serial.print("SSID: ");
  Serial.println(SSID);
  Serial.print("Password: ");
  Serial.println(PASSWORD);
  Serial.print("IP: ");
  Serial.println(WiFi.softAPIP());
}

void setupServer() {
  server.on("/", handleRoot);
  server.on("/api/speed", handleSpeed);
  server.on("/api/title", handleTitle);
  server.on("/api/subtitle", handleSubtitle);
  server.on("/api/play", handlePlay);
  server.on("/api/pause", handlePause);
  server.on("/api/color", handleColor);
  server.begin();
}

// ---- setup / loop ----
void setup() {
  Serial.begin(115200);
  delay(1000);

  setColorScheme(0);

  tft.init();
  tft.setRotation(1);
  tft.fillScreen(TFT_BLACK);

#ifdef TFT_BL
  pinMode(TFT_BL, OUTPUT);
  digitalWrite(TFT_BL, HIGH);
#endif

  setupWiFi();
  setupServer();
}

void loop() {
  server.handleClient();

  if (remote.running) {
    runAnimation();
  } else {
    delay(100);
  }
}
