# 🎨 Customização - Pixel Quest

Guia prático para personalizar cores, animações e comportamentos.

## 📝 Alterar Texto Estático

### Via Interface Web (Recomendado)

Depois de carregar `retro_intro_esp32c3_wifi.ino`, mude pelo celular em tempo real:

1. Conecte à rede `PIXEL_QUEST`
2. Acesse `192.168.4.1`
3. Digite novo título/subtítulo nos campos de texto
4. Pronto! A tela atualiza ao próximo ciclo da animação

### Via Código

No sketch `retro_intro_esp32c3.ino` ou `retro_intro_esp32c3_wifi.ino`:

```cpp
const char* TITLE    = "MEU PROJETO";
const char* SUBTITLE = "- v2.0 -";
```

Ou no struct de controle (versão WiFi):

```cpp
struct RemoteControl {
  String title = "MEU PROJETO";
  String subtitle = "- v2.0 -";
  // ...
};
```

## 🎭 Esquemas de Cor

Dois temas pré-definidos na versão WiFi. Para adicionar mais:

### Adicionar Nova Paleta

No arquivo `.ino`, expanda a função `setColorScheme()`:

```cpp
void setColorScheme(int scheme) {
  if (scheme == 0) {
    // Original: azul/ouro
    SKY = tft.color565(92, 148, 252);
    // ...
  } else if (scheme == 1) {
    // Roxo/Verde
    SKY = tft.color565(120, 80, 160);
    // ...
  } else if (scheme == 2) {
    // NOVO: Tema Neon
    SKY = tft.color565(10, 10, 30);
    GRASS_C = tft.color565(255, 0, 255);
    HERO = tft.color565(0, 255, 255);
    // ...
  }
}
```

Depois atualize o loop remoto para ciclar entre temas:

```cpp
void handleColor() {
  remote.colorScheme = (remote.colorScheme + 1) % 3;  // 3 temas
  setColorScheme(remote.colorScheme);
  server.send(200, "text/plain", "OK");
}
```

### RGB565 - Como Calcular Cores

```cpp
// RGB565 = (R>>3)<<11 | (G>>2)<<5 | (B>>3)
// Ou use tft.color565(R, G, B) onde R/G/B = 0-255

// Exemplos:
tft.color565(255, 0, 0);     // Vermelho puro
tft.color565(0, 255, 0);     // Verde puro
tft.color565(0, 0, 255);     // Azul puro
tft.color565(255, 255, 0);   // Amarelo
tft.color565(255, 0, 255);   // Magenta
tft.color565(0, 255, 255);   // Cyan
```

**Online:** Use [RGB565 Color Picker](https://chir.ag/projects/ntsc/) ou similar.

## 🏃 Ajustar Velocidade da Animação

### Via Interface Web ✅ (Fácil)

Slider de velocidade em `192.168.4.1` — vai de 50% até 150%

### Via Código

No array de delays do loop:

```cpp
for (int f = 0; f < 220; f++) {
  animateClouds();
  animateHero(f);
  drawCoin(coinX, coinY, f);
  if (f % 16 == 0) blinkStart();
  delay(18);  // ← mude aqui (ms)
}
```

- `delay(9)` → muito rápido
- `delay(18)` → normal
- `delay(35)` → bem lento

## 🎬 Modificar Elementos da Cena

### Posição do Herói

```cpp
const int heroBaseY = 152;  // altura Y do chão
int heroX, heroY = heroBaseY;
```

Aumente para descer, diminua para subir.

### Nuvens

```cpp
float cloudX[3] = {40, 150, 250};  // posição X inicial
int cloudY[3] = {22, 44, 30};       // altura Y de cada nuvem
```

### Caixa e Moeda

```cpp
int crateX = W/2 + 30;              // caixa à direita
int coinX = W/2 + 90, coinY = 130;  // moeda acima
```

## 🔊 Som (Opcional)

Se tiver speaker/buzzer:

```cpp
#define BUZZER_PIN 8

void beep(int freq, int duration) {
  tone(BUZZER_PIN, freq, duration);
}

// No setup:
pinMode(BUZZER_PIN, OUTPUT);

// No loop (ex: ao pausar):
void handlePause() {
  remote.running = false;
  beep(1000, 200);  // beep de 1kHz por 200ms
  server.send(200, "text/plain", "OK");
}
```

## 📊 Debug & Monitoramento

Abra Serial Monitor (115200) para ver logs:

```cpp
// Adicione no seu código:
Serial.print("animSpeed = ");
Serial.println(remote.animSpeed);

Serial.print("title = ");
Serial.println(remote.title);
```

## 🎯 Ideias de Customização

### Para Eventos
- **Título:** Nome da pessoa / Empresa
- **Subtítulo:** Data / Mensagem motivacional
- **Cores:** Cores corporativas

### Para Arte
- **Velocidade:** Sincronizar com música (ajuste pelo smartphone)
- **Múltiplos Temas:** Cicle cores de acordo com sensores

### Para Aprendizado
- **Adicione Texto:** Score ou contador
- **Reação:** Mudança de cores ao tocar botão no celular
- **Efeitos:** Pisca a tela toda ao pausar

---

**Dica:** Depois de customizar, faça commit de suas mudanças! `git add . && git commit -m "Custom theme"`
