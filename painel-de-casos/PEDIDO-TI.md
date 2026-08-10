# Pedido ao TI — subdomínio com redirecionamento para o Painel de Casos

Texto pronto para encaminhar. Substitua os campos entre `<>` antes de enviar.

---

## Solicitação

Solicito a criação do subdomínio abaixo, com redirecionamento HTTP permanente
para um aplicativo interno hospedado no Google Workspace da instituição.

| Item | Valor |
|---|---|
| Subdomínio solicitado | `painel.policiacientifica.sc.gov.br` |
| Tipo | Redirecionamento HTTP 301 (permanente) |
| Destino | `<COLE AQUI A URL /exec DO APP DA WEB>` |
| Certificado TLS | Necessário para o subdomínio (o destino é HTTPS) |
| Preservar caminho/query | Não. Todo acesso vai para a URL de destino, sem repasse. |

A URL de destino tem o formato:

```
https://script.google.com/a/macros/policiacientifica.sc.gov.br/s/<ID_DA_IMPLANTACAO>/exec
```

### Justificativa

O painel já está publicado e restrito às contas do domínio. O subdomínio serve
apenas para dar um endereço divulgável (ofício, assinatura de e-mail, QR code)
no lugar da URL longa gerada pelo Google. O redirecionamento não intermedia
dados: o navegador é encaminhado direto ao serviço do Google, e a autenticação
e o controle de acesso continuam integralmente no Workspace institucional.

### Observação sobre manutenção

A URL de destino é estável desde que as atualizações do aplicativo sejam
publicadas como **nova versão da implantação existente**. Caso seja necessário
trocar o destino no futuro, o pedido será feito pelo mesmo canal.

---

## Como implementar (para a equipe técnica)

A escolha depende do que já existe sob o domínio. Da mais simples para a mais
trabalhosa:

### 1. Já existe servidor web ou proxy reverso sob o domínio

Uma regra de redirecionamento em um vhost dedicado.

**nginx**

```nginx
server {
    listen 443 ssl;
    server_name painel.policiacientifica.sc.gov.br;

    # ssl_certificate / ssl_certificate_key conforme o padrão do órgão

    return 301 https://script.google.com/a/macros/policiacientifica.sc.gov.br/s/<ID>/exec;
}
```

**Apache**

```apache
<VirtualHost *:443>
    ServerName painel.policiacientifica.sc.gov.br
    Redirect permanent / https://script.google.com/a/macros/policiacientifica.sc.gov.br/s/<ID>/exec
</VirtualHost>
```

**IIS** — regra de redirecionamento em `web.config`, tipo `Permanent (301)`,
com "Append query string" desmarcado.

DNS: `painel` como `A`/`AAAA` apontando para o servidor, ou `CNAME` para o
host que já atende o domínio.

### 2. O domínio passa por CDN/WAF (Cloudflare, Azure Front Door, etc.)

Não precisa de servidor. Basta uma regra de redirecionamento na plataforma
(no Cloudflare: **Rules > Redirect Rules**, `Hostname equals
painel.policiacientifica.sc.gov.br` → destino, status 301) com o registro DNS
proxiado. O certificado é emitido pela própria plataforma.

### 3. O DNS é gerenciado por provedor com "encaminhamento de URL"

Vários provedores oferecem *URL forwarding* / *encaminhamento de domínio* nativo.
Confirmar apenas que o encaminhamento é **301 com HTTPS**, e não mascarado
por frame — o encaminhamento mascarado recria o problema de iframe que este
pedido justamente resolve.

### 4. Nada disso existe

Balanceador HTTPS no Google Cloud com regra de redirecionamento no *URL map*
(não exige backend nem VM; custo mensal baixo). É a alternativa de último caso,
por exigir um projeto GCP.

---

## O que **não** resolve

- **Encaminhamento mascarado / com frame.** Devolve o painel para dentro de um
  iframe, com altura fixa e rolagem dupla — exatamente o que se quer evitar.
- **Domínio personalizado apontado para o Google Sites.** Deixa o endereço
  bonito, mas mantém o painel dentro do bloco de incorporação do Sites, com as
  mesmas limitações de layout.
- **Encurtador público de URL.** Endereço institucional não deve depender de
  serviço de terceiros, e o Google descontinuou o próprio encurtador.
