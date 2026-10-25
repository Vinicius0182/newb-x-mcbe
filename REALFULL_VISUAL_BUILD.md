# RealFull — Cinematic Visual Build v7

Este é um fork experimental da fonte Newb X / RenderDragon.

## Alterações nesta fonte
- Nuvens proceduralmente realistas com uma oitava extra de detalhe.
- Novo caminho de sombra projetada para o campo de nuvens realistas, calculado a partir da direção do sol/lua e do movimento das nuvens.
- Sol maior, halo e godrays reforçados.
- Glow de texturas emissivas intensificado com vazamento de brilho.
- Água com ondulação mais pronunciada e reflexo de céu/sol reforçado.
- Exposição, saturação, contraste percebido e névoa de distância ajustados para aparência cinematográfica.
- Os efeitos de chuva continuam condicionados ao fator de clima do jogo; não é ativada chuva artificialmente.

## Limitações honestas
- O projeto usa shaders RenderDragon existentes; não implementa um pipeline pós-processamento independente. Portanto, o glow emissivo não equivale a bloom de tela completo a 100%.
- A névoa atmosférica é um efeito de shader baseado em distância e godrays; não é volumetria física real com ray marching.
- Reflexos são aproximados por reflexão do céu e máscaras, não SSR/ray tracing físico.
- A sombra de nuvem foi implementada no código-fonte, mas precisa ser compilada e validada no jogo/loader alvo.
- Não foram adicionados arquivos inúteis para inflar o ZIP: tamanho do arquivo não mede qualidade gráfica.

## Build
Use o build.sh e o workflow do projeto base. Recomenda-se compilar Android e testar primeiro em um mundo de cópia. Os efeitos realistas podem aumentar o custo de GPU, principalmente em aparelhos modestos.
