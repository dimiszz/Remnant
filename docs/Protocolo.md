# Mensagens do Cliente

Na interface do cliente, ele utiliza os comandos escrevendo ``/comando`` e isso é traduzido para o servidor para códigos.


# Mensagens do Servidor (Comandos)

- /ajuda &rarr; **101** → exibe ajuda sobre os comandos disponíveis.

- /listar_sessoes &rarr; **103** → lista as sessões disponíveis.

- /criar_sessao &rarr; **104** → cria uma nova sessão.

- /entrar_sessao {sessao_id} &rarr; **105 {sessao_id}** → entra em uma sessão específica.

- /sair_sessao &rarr; **106** → sai da sessão atual.

- /escolher {classe} &rarr; **113 {classe}** → escolhe uma classe ou ação específica.

- /combate {ação} &rarr; **114 {ação}** → executa uma ação de combate.

- /sair_partida &rarr; **115** → sai da partida atual.

- /fechar &rarr; **999** → encerra a aplicação.

- /escrever {mensagem} &rarr; **1102 {mensagem}** → indica uma mensagem enviada por um jogador.



# Mensagens do Servidor

- **200** → boas-vindas quando o cliente se conecta.

- **201** → mostrar os comandos disponíveis.

- **203** {sessao_id};{player1_nome};{player2_nome...} → lista de sessões disponíveis.

- **204** {sessao_id};{player1_nome} → retorno da criação da partida.

- **205** {sessao_id};{player1_nome};{player2_nome} → confirmação da entrada na sessão com id da sessão e nome dos jogadores.

- **206** {sessao_id} → confirmação da saída da sessão.

- **300** → início de nova partida.

- **301** → comandos usados dentro da partida.

- **303** {classe} → classe selecionada.

- **304** {mensagem} → mensagem personalizada da partida. Pode indicar erros.

- **305** {partida_id} → sai da partida.

- **306** {mensagem} → mensagem personalizada da partida. Pode indicar erros.

- **400** {player1_nome};{classe1};{player2_nome};{classe2} → nome e classe dos jogadores na partida.

- **401** {tipo_combate};{jogador1_nome};{classe1};{vida1};{defesa1};{fisico1};{inteligencia1};{jogador2_nome};{classe2};{vida2};{defesa2};{fisico2};{inteligencia2};{numero_rodada} → retorna informações da partida.

- **402** {flag_dano};{sua_escolha};{adversario_nome};{escolha_adversario};{dano};{vida_restante} → retorna as informações de combate após um dano. flag_dano indica se você recebeu dano ou não.

- **403** {username} → retorna o nome do inimigo.

- **404** {resultado} → resultado da partida: 1 para vitória, 0 para derrota.

- **999** → encerra a aplicação.

- **1201** {player_nome} → inicia o chat para o cliente.

- **1202** {player_nome};{mensagem} → indica uma mensagem enviada por um player.


# Relação entre Comandos e Mensagens do Servidor

Considere a seguinte estrutura:

Mensagem cliente &rarr; resposta servidor &rarr; explicação


- **101** → **201** → exibe ajuda sobre os comandos disponíveis.

- **103** → **203** → lista as sessões disponíveis.

- **104** → **204** → cria uma nova sessão.

- **105 {sessao_id}** → **205 {sessao_id};{player1_nome};{player2_nome}** → entra em uma sessão específica.

- **106** → **206 {sessao_id}** → sai da sessão atual.

- **113 {classe}** → **303 {classe}** → escolhe uma classe ou ação específica.

- **114 {ação}** → **304 {mensagem}** ou **401 {tipo_combate};{jogador1_nome};...** → executa uma ação de combate.

- **115** → **305 {partida_id}** → sai da partida atual.

- **999** → **999** → encerra a aplicação.

- **1102 {mensagem}** → **1202 {player_nome};{mensagem}** → indica uma mensagem enviada por um jogador.
