# Pixel Quest - Controle Remoto via WiFi

Versão com servidor web integrado para controlar a animação direto do seu celular.

## 🎮 Controles Disponíveis

Via interface web (192.168.4.1):

- **▶ PLAY** — resume a animação
- **⏸ PAUSE** — pausa a exibição
- **🎨 CORES** — alterna entre tema original (azul/ouro) e alternativo (roxo/verde)
- **Velocidade** — slider de 50% (mais lento) até 150% (mais rápido)
- **Título/Subtítulo** — mude o texto da tela em tempo real

## 📱 Como Conectar do Celular

1. **Compile e carregue** `retro_intro_esp32c3_wifi.ino` no ESP32-C3
2. **No seu celular:**
   - Vá em Configurações → WiFi
   - Procure pela rede `PIXEL_QUEST`
   - Senha: `12345678`
3. **Após conectar:**
   - Abra o navegador
   - Acesse `192.168.4.1`
   - Controle tudo pela interface

## ⚙️ Hardware

**Pinos (ST7789):**
```
SCL (Clock)  → GPIO 4
SDA (MOSI)   → GPIO 6
CS (Chip Sel)→ GPIO 7
DC (Data/Cmd)→ GPIO 5
RST (Reset)  → GPIO 10
BL (Backlight)→ GPIO 1
VCC → 3V3
GND → GND
```

📌 **Atenção:** Configure os mesmos pinos em `User_Setup.h` antes de compilar.

## 📦 Bibliotecas Necessárias

Via Arduino IDE → Gerenciador de Bibliotecas:

- **TFT_eSPI** by Bodmer (versão 2.5.0+)
- WebServer (nativa do ESP32 — não precisa instalar)
- WiFi (nativa do ESP32 — não precisa instalar)

## 🔧 Configuração Inicial

1. Abra `User_Setup.h` e descomente/configure:
   ```cpp
   #define TFT_MOSI 6     // SDA
   #define TFT_SCLK 4     // SCL
   #define TFT_CS   7
   #define TFT_DC   5
   #define TFT_RST  10
   #define TFT_BL   1
   #define SPI_FREQUENCY 40000000
   #define ST7789_DRIVER
   #define TFT_WIDTH 240
   #define TFT_HEIGHT 320
   #define TFT_ROTATION 1
   ```

2. **Copie** `User_Setup.h` para a pasta de instalação do TFT_eSPI:
   ```
   Arduino/libraries/TFT_eSPI/User_Setup.h
   ```

3. Abra `retro_intro_esp32c3_wifi.ino` e compile ✓

## 🎯 Exemplo de Uso

**Seu próprio evento/festa:**

1. Carregue o sketch no ESP32
2. Conecte do seu celular à rede `PIXEL_QUEST`
3. Acesse `192.168.4.1`
4. Customize:
   - Título → seu nome/evento
   - Subtítulo → mensagem personalizada
   - Cores → escolha o tema
   - Velocidade → ajuste de acordo com o ritmo

**Terminal (debug):**

Abra o Monitor Serial (115200 baud) para ver:
- SSID e Password
- IP do hotspot
- Comandos recebidos

## 🚀 Próximas Melhorias

- [ ] Controlar via WiFi remoto (não só hotspot)
- [ ] Efeitos visuais customizáveis
- [ ] Histórico de configurações
- [ ] Integração com Bluetooth
- [ ] App nativa (Android/iOS)

## 🐛 Troubleshooting

**Não consigo me conectar ao WiFi:**
- Verifique se o ESP32 ligou (Serial mostra logs?)
- Reinicie a busca de WiFi no celular
- Tente a senha `12345678` novamente

**A página não carrega (192.168.4.1):**
- Desconecte e reconecte ao WiFi `PIXEL_QUEST`
- Abra em modo anônimo (sem cache)
- Tente `http://192.168.4.1` (sem https)

**Animação travando:**
- Verifique se o `User_Setup.h` tem os pinos corretos
- Teste com a versão original (`retro_intro_esp32c3.ino`) primeiro
- Reduza a velocidade via slider

---

**Feito com ❤️ para 8-bit lovers**
