# ⚡ Quick Start - 3 Passos

## 1️⃣ Instale o Board + Bibliotecas

**Arduino IDE → Gerenciador de Placas:**
```
Procure "esp32" → Instale "esp32 by Espressif Systems"
```

**Arduino IDE → Gerenciador de Bibliotecas:**
```
Procure "TFT_eSPI" → Instale versão 2.5.0+
```

## 2️⃣ Prepare Seu Projeto

Na **mesma pasta** onde vai o `.ino`:

1. Coloque o arquivo `User_Setup.h` (do projeto)
2. Coloque o arquivo `.ino` que escolheu:
   - `retro_intro_esp32c3.ino` ← versão simples
   - `retro_intro_esp32c3_wifi.ino` ← com controle remoto

**Pronto! Não precisa mexer em mais nada.**

```
meu-projeto-pixel/
├── retro_intro_esp32c3_wifi.ino  ← abra esse no Arduino
├── User_Setup.h                   ← config já está aqui
└── (User_Setup.h.bak)            ← backup, opcional
```

## 3️⃣ Compile & Carregue

1. Abra o `.ino` no Arduino IDE
2. **Ferramentas → Placa** → `ESP32C3 Dev Module`
3. **Ferramentas → Porta** → escolha a porta COM/ttyUSB
4. **Sketch → Carregar** (Ctrl+U)

Espere a mensagem:
```
Hard resetting via RTS pin...
```

## ✅ Pronto!

**Versão simples:**
- Animação toca sozinha no display

**Versão WiFi:**
- Conecte seu celular à rede `PIXEL_QUEST` (senha: `12345678`)
- Abra `192.168.4.1` no navegador
- Controle tudo!

---

## 🆘 Dica: Se Der Erro de User_Setup

Se o Arduino reclamar que não acha `User_Setup.h`, significa que o arquivo não está no mesmo folder do `.ino`.

**Solução:**
1. Confirme que `User_Setup.h` está na mesma pasta
2. Reinicie o Arduino IDE
3. Tente novamente

Se ainda não funcionar, você pode usar a forma "manual" (copiar para a biblioteca), veja `README_ESP32C3.md`.
