# Real Full — perfil visual

Perfil ajustado a partir do código-fonte enviado, inspirado nas três capturas de referência.

## Ajustes em `src/newb/config.h`
- Saturação: `0.41`
- Sombras: `0.99` (sombras mais profundas)
- Luz solar: `5.0`
- Sol: `2.2`
- Godrays: `0.72`
- Halo solar: `0.38`
- Nuvens soft: densidade `0.62`, opacidade `0.94`
- Reflexo do chão reduzido para `0.35` para evitar custo visual excessivo
- Sem ativar uma segunda camada de nuvens ou aumentar as amostras das nuvens, priorizando celular

## Temas por bioma e clima
O shader original já mistura cores do céu e neblina com fatores de ambiente/clima. Esta alteração preserva essa lógica em vez de substituir todos os biomas por uma cor fixa. O visual pode variar conforme bioma, hora do dia e chuva.

## Importante
Este pacote contém o projeto-fonte completo com ajustes de configuração. Não foi possível compilar nem testar no Minecraft/MBLoader neste ambiente. Os valores de halo e godrays usam os efeitos existentes no projeto; eles não equivalem a ray tracing nem criam sombras geométricas reais em blocos se o pipeline do jogo não fornecer esse recurso. O resultado e o FPS precisam ser verificados no aparelho.
