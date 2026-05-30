// =====================================================================
//  User_Setup.h  -  TFT_eSPI
//  Placa:   ESP32-C3 Super Mini
//  Display: GMT020-02-8P  (ST7789, 240x320, SPI)
//
//  Copie este arquivo para a pasta da biblioteca TFT_eSPI,
//  substituindo o User_Setup.h padrao, OU referencie via
//  User_Setup_Select.h. Os pinos abaixo batem com a fiacao do projeto.
// =====================================================================

#define USER_SETUP_INFO "ESP32C3_GMT020_ST7789"

// ---- driver do controlador ----
#define ST7789_DRIVER

// ---- resolucao do painel (orientacao 0 / retrato) ----
#define TFT_WIDTH  240
#define TFT_HEIGHT 320

// Alguns paineis ST7789 precisam de uma destas opcoes de cor/offset.
// Se a imagem aparecer deslocada ou com bordas, teste comentar/trocar:
// #define CGRAM_OFFSET

// ---- pinos (ESP32-C3 Super Mini) - REAL ----
#define TFT_MOSI 3    // SDA do display (VIOLETA)
#define TFT_SCLK 4    // SCL do display (CINZA)
#define TFT_CS    2   // CS (AZUL)
#define TFT_DC    0   // DC (AMARELO)
#define TFT_RST  21   // RST (LARANJA)
#define TFT_BL    1   // BL - backlight (VERDE)
#define TFT_BACKLIGHT_ON HIGH

// ---- fontes ----
#define LOAD_GLCD
#define LOAD_FONT2
#define LOAD_FONT4
#define LOAD_FONT6
#define LOAD_FONT7
#define LOAD_GFXFF
#define SMOOTH_FONT

// ---- SPI ----
// Se houver glitches visuais, baixe para 20000000 ou 27000000.
#define SPI_FREQUENCY  40000000
#define SPI_READ_FREQUENCY 20000000
