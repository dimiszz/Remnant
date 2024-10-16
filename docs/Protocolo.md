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
