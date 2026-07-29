# MaineMed

Aplicativo de documentação clínica para macOS (Apple Silicon): grava a
consulta, transcreve **localmente** com Whisper (MLX, GPU do chip M) e gera o
prontuário estruturado via API da Anthropic. O áudio nunca sai do Mac — só a
transcrição em texto é enviada na etapa de redação do prontuário, o que
simplifica bastante a conversa de LGPD com clínicas.

## Arquitetura

| Etapa | Onde roda | Tecnologia |
|---|---|---|
| Gravação (16 kHz mono) | local | `sounddevice` (PortAudio) |
| Transcrição | local (GPU do chip M) | `mlx-whisper` — modelo baixado do Hugging Face na 1ª utilização, para `~/Library/Application Support/MaineMed/models` |
| Prontuário | API Anthropic | modelo `claude-opus-5` (configurável para `claude-sonnet-5`) |
| Exportação | local | `python-docx` / `reportlab` |

O áudio é passado ao Whisper como array numpy — **não há ffmpeg no bundle**.

Dados do usuário ficam em `~/Library/Application Support/MaineMed/`:
`config.json` (chave da API, permissão 600), `models/`, `logs/` e
`consultas/` — uma pasta por consulta com proveniência separada de máquina e
humano (`transcricao-original`, `transcricao-revisada`, `prontuario-gerado`,
`prontuario-final`, `consulta.json`); ver `provenancia.py` e `ROADMAP.md`.

## Desenvolvimento (em um Mac com chip M)

```sh
cd mainemed
python3.12 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
PYTHONPATH=src python -m mainemed.main
```

## Build e distribuição (GitHub Actions)

O workflow `.github/workflows/mainemed-macos.yml` roda num runner
`macos-15` (Apple Silicon), builda com PyInstaller, assina ad-hoc, valida o
bundle (`--selfcheck`), zipa com `ditto` e publica uma Release.

Disparo manual: aba **Actions → MaineMed macOS → Run workflow** (escolha o
branch e a versão), ou push de uma tag `mainemed-v*`.

A release fica em:
`https://github.com/horstmr/semantic-privacy-guard/releases/download/mainemed-v1.0.0/MaineMed-macOS-arm64.zip`

Instruções de instalação para o usuário final: [INSTALACAO-MAC.md](INSTALACAO-MAC.md).

## Versão web (MVP de demonstração)

`web/index.html` — página estática, arquivo único, sem login e sem servidor:

- **Ditado** pela Web Speech API do navegador (Chrome/Edge/Safari), sem chave;
- **Dados mock**: três consultas fictícias de exemplo para demonstrar o fluxo;
- **Prontuário em modo demonstração** (gerado localmente, sem IA) por padrão;
  colando uma chave da API em Configurações, chama a API direto do navegador
  (`anthropic-dangerous-direct-browser-access`) — recomende chave de um
  workspace com limite de gasto;
- Exporta por copiar, `.txt` e imprimir/PDF.

Depois do merge na `main`, o Pages publica em
`https://horstmr.github.io/semantic-privacy-guard/mainemed/web/` (requer
GitHub Pages habilitado no repositório). Alternativa: baixar o arquivo e
abrir localmente — funciona até offline no modo demonstração.

Diferença de privacidade honesta: o ditado da versão web passa pelo serviço
de voz do navegador; a transcrição 100% local é exclusividade do app de Mac.

### Por que essas escolhas de empacotamento

- **Runner macOS arm64 obrigatório** — PyInstaller não cross-compila.
- **`NSMicrophoneUsageDescription` no Info.plist** — sem a chave o macOS não
  pede permissão e o microfone grava silêncio, sem erro algum.
- **Assinatura ad-hoc, sem hardened runtime** — não há conta Apple Developer;
  ad-hoc simples é o que funciona com as bibliotecas nativas do bundle. O
  usuário passa uma vez pelo Gatekeeper (ver INSTALACAO-MAC.md).
- **`ditto -c -k --keepParent`** — zip comum destrói symlinks/assinatura do
  `.app` (o clássico "o aplicativo está danificado").
- **Modelos fora do bundle** — app assinado não pode escrever em si mesmo.
