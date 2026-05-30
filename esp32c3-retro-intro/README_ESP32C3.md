# Intro Retro 8-bit — ESP32-C3 + Display TFT ST7789

Animacao original no estilo plataforma 8-bit (cortina, nuvens, heroi pulando,
moeda girando, banner de titulo com "PRESS START") rodando num display TFT
de 2.0" controlado por um ESP32-C3 Super Mini.

> A arte e 100% original. O projeto **nao** usa sprites, logos ou musicas de
> jogos protegidos por copyright.

## Hardware

| Item | Descricao |
|------|-----------|
| MCU | ESP32-C3 Super Mini |
| Display | GMT020-02-8P — TFT 2.0", 240x320, controlador ST7789, SPI, 3.3V |

### Ligacao (display → ESP32-C3)

| Display | Funcao | GPIO |
|--------|--------|------|
| GND | Terra | GND |
| VCC | Alimentacao | **3V3** (nunca 5V) |
| SCL | Clock (SCLK) | GPIO4 |
| SDA | Dados (MOSI) | GPIO6 |
| RST | Reset | GPIO10 |
| DC  | Data/Command | GPIO5 |
| CS  | Chip Select | GPIO7 |
| BL  | Backlight | GPIO1 (ou direto no 3V3) |

GPIO8 (LED) e GPIO9 (BOOT) ficaram livres de proposito (pinos de strapping).

## Software

1. Instale o **Arduino IDE** + suporte a placas ESP32 (board manager da Espressif).
2. Instale a biblioteca **TFT_eSPI** (Library Manager).
3. Copie o `User_Setup.h` deste repositorio para a pasta da biblioteca
   `TFT_eSPI`, substituindo o arquivo padrao.
4. Abra `retro_intro_esp32c3.ino`.
5. Selecione a placa **ESP32C3 Dev Module** e a porta correta.
6. Compile e envie.

## Ajustes rapidos

- **Cores invertidas:** adicione `tft.invertDisplay(true);` no fim do `setup()`.
- **Imagem deslocada / bordas:** teste outra `setRotation(0..3)` ou habilite
  `#define CGRAM_OFFSET` no `User_Setup.h`.
- **Glitch visual / lixo na tela:** baixe `SPI_FREQUENCY` para `27000000`.
- **Trocar o titulo:** edite `TITLE` e `SUBTITLE` no topo do `.ino`.
- **Trocar cores/personagem:** todas as cores estao agrupadas no inicio do
  `setup()`; o desenho do heroi esta na funcao `drawHero()`.

## Arquivos

- `retro_intro_esp32c3.ino` — sketch principal
- `User_Setup.h` — configuracao da TFT_eSPI (pinos/driver)
- `README.md` — este arquivo

## Proximos passos (ideias)

- Controle por WiFi: pagina web pra trocar o texto/cor da telinha.
- Buzzer num GPIO pra um efeito sonoro de abertura (melodia original).
- Mais cenas / selecao de "fase".
