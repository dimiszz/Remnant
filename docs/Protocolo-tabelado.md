# Documentação de Mensagens do Cliente e Servidor

## Mensagens do Cliente

Na interface do cliente, os comandos são inseridos no formato ``/comando`` e são traduzidos para códigos que o servidor reconhece.


| Comando                     | Código   | Descrição                                           |
|-----------------------------|----------|----------------------------------------------------|
| `/ajuda`                    | 101      | Exibe ajuda sobre os comandos disponíveis.          |
| `/listar_sessoes`          | 103      | Lista as sessões disponíveis.                       |
| `/criar_sessao`            | 104      | Cria uma nova sessão.                              |
| `/entrar_sessao {sessao_id}` | 105 {sessao_id} | Entra em uma sessão específica.                    |
| `/sair_sessao`             | 106      | Sai da sessão atual.                               |
| `/escolher {classe}`       | 113 {classe} | Escolhe uma classe ou ação específica.            |
| `/combate {ação}`          | 114 {ação} | Executa uma ação de combate.                       |
| `/sair_partida`            | 115      | Sai da partida atual.                              |
| `/fechar`                  | 999      | Encerra a aplicação.                               |
| `/escrever {mensagem}`     | 1102 {mensagem} | Indica uma mensagem enviada por um jogador.      |

## Mensagens do Servidor

As respostas do servidor são codificadas e retornam informações sobre o estado do jogo e as interações dos jogadores.

| Código                                     | Descrição                                                                 |
|--------------------------------------------|---------------------------------------------------------------------------|
| 200                                        | Boas-vindas quando o cliente se conecta.                                |
| 201                                        | Mostrar os comandos disponíveis.                                         |
| 203 {sessao_id};{player1_nome};{player2_nome...} | Lista de sessões disponíveis.                                        |
| 204 {sessao_id};{player1_nome}            | Retorno da criação da partida.                                          |
| 205 {sessao_id};{player1_nome};{player2_nome} | Confirmação da entrada na sessão com id da sessão e nome dos jogadores. |
| 206 {sessao_id}                            | Confirmação da saída da sessão.                                         |
| 207 {player_id};{sessao}                  | Jogador criou uma sessão.                                              |
| 300                                        | Início de nova partida.                                                 |
| 301                                        | Comandos usados dentro da partida.                                      |
| 303 {classe}                               | Classe selecionada.                                                    |
| 304 {mensagem}                             | Mensagem personalizada da partida. Pode indicar erros.                 |
| 305 {partida_id}                           | Sai da partida.                                                        |
| 306 {mensagem}                             | Mensagem aguardando o outro jogador.                                   |
| 400 {player1_nome};{classe1};{player2_nome};{classe2} | Nome e classe dos jogadores na partida.                            |
| 401 {tipo_combate};{jogador1_nome};{classe1};{vida1};{defesa1};{fisico1};{inteligencia1};{jogador2_nome};{classe2};{vida2};{defesa2};{fisico2};{inteligencia2};{numero_rodada} | Retorna informações da partida. |
| 402 {flag_dano};{sua_escolha};{adversario_nome};{escolha_adversario};{dano};{vida_restante} | Retorna as informações de combate após um dano. Flag_dano indica se você recebeu dano ou não. |
| 403 {username}                             | Retorna o nome do inimigo.                                             |
| 404 {resultado}                            | Resultado da partida: 1 para vitória, 0 para derrota.                  |
| 999                                        | Encerra a aplicação.                                                   |
| 1201 {player_nome}                         | Inicia o chat para o cliente.                                         |
| 1202 {player_nome};{mensagem}             | Indica uma mensagem enviada por um player.                             |

## Código sendo executado


| Mensagem Cliente            | Resposta Servidor                                          | Explicação                                               |
|-----------------------------|-----------------------------------------------------------|---------------------------------------------------------|
| 101                         | 201                                                       | Exibe ajuda sobre os comandos disponíveis.               |
| 103                         | 203  {quantidade_sessoes};{sessao1_id};{sessao1_player1};{sessao1_player2}{sessao2_id}... | Lista as sessões disponíveis.               |
| 104                         | 204 {sessao_id};{player_id} ou 204 {mensagem} e 207 {player_id};{sessao_id} | Cria uma nova sessão e envia um broadcast 207 para todos os outros usuários conectados. envia 204 {mensagem} em caso de erro. |
| 105 {sessao_id}            | 205 {sessao_id};{player1_nome};{player2_nome} ou 205 {mensagem}, 306 {mensagem}, 300 e 1201 | Entra em uma sessão específica. Também envia 306 para avisar que o usuário entrou, 300 para começar a partida e 1201 para começar o chat. |
| 106                         | 206 {sessao_id}                                         | Sai da sessão atual.                                   |
| 113 {classe}               | 303 {mensagem} ou 306 {mensagem}                        | Escolhe uma classe. Avisa o outro jogador que já escolheu a classe.  Se ele ainda não escolheu, retorna 306. |
| 114 {ação}                 | 304 {mensagem}, 306 {mensagem}, 402;{olhar-tabela_acima} 403 {tipo_combate} ou 404 {resultado} | 304 para escolha de jogada e erros, 306 se está esperando o outro, 402 para informações de dano, 403 para indicar meio turno e 404 para indicar o resultado da partida para ambos os jogadores. |
| 115                         | 305 {partida_id}                                       | Sai da partida atual.                                  |
| 999                         | 999                                                     | Encerra a aplicação.                                   |
| 1102 {mensagem}            | 1202 {player_nome};{mensagem}                          | Indica uma mensagem enviada por um jogador.            |