# Documentação de Mensagens do Cliente e Servidor

## Mensagens do Cliente

Na interface do cliente, os comandos são inseridos no formato ``/comando`` e são traduzidos para códigos que o servidor reconhece.

| Comando                       | Código          | Descrição                                      |
|-------------------------------|-----------------|------------------------------------------------|
| /ajuda                        | **101**        | Exibe ajuda sobre os comandos disponíveis.     |
| /listar_sessoes              | **103**        | Lista as sessões disponíveis.                  |
| /criar_sessao                | **104**        | Cria uma nova sessão.                          |
| /entrar_sessao {sessao_id}   | **105 {sessao_id}** | Entra em uma sessão específica.                |
| /sair_sessao                 | **106**        | Sai da sessão atual.                           |
| /escolher {classe}           | **113 {classe}** | Escolhe uma classe ou ação específica.        |
| /combate {ação}              | **114 {ação}** | Executa uma ação de combate.                   |
| /sair_partida                | **115**        | Sai da partida atual.                          |
| /fechar                       | **999**        | Encerra a aplicação.                           |
| /escrever {mensagem}         | **1102 {mensagem}** | Indica uma mensagem enviada por um jogador.  |

## Mensagens do Servidor

As respostas do servidor são codificadas e retornam informações sobre o estado do jogo e as interações dos jogadores.

| Código      | Descrição                                              |
|-------------|-------------------------------------------------------|
| **200**    | Boas-vindas quando o cliente se conecta.             |
| **201**    | Mostrar os comandos disponíveis.                       |
| **203** {sessao_id};{player1_nome};{player2_nome...} | Lista de sessões disponíveis.                   |
| **204** {sessao_id};{player1_nome}                   | Retorno da criação da partida.                   |
| **205** {sessao_id};{player1_nome};{player2_nome}   | Confirmação da entrada na sessão com id da sessão e nome dos jogadores. |
| **206** {sessao_id}                                  | Confirmação da saída da sessão.                  |
| **300**    | Início de nova partida.                              |
| **301**    | Comandos usados dentro da partida.                   |
| **303** {classe}                                    | Classe selecionada.                               |
| **304** {mensagem}                                  | Mensagem personalizada da partida. Pode indicar erros. |
| **305** {partida_id}                                | Sai da partida.                                   |
| **306** {mensagem}                                  | Mensagem personalizada da partida. Pode indicar erros. |
| **400** {player1_nome};{classe1};{player2_nome};{classe2} | Nome e classe dos jogadores na partida.        |
| **401** {tipo_combate};{jogador1_nome};{classe1};{vida1};{defesa1};{fisico1};{inteligencia1};{jogador2_nome};{classe2};{vida2};{defesa2};{fisico2};{inteligencia2};{numero_rodada} | Retorna informações da partida. |
| **402** {flag_dano};{sua_escolha};{adversario_nome};{escolha_adversario};{dano};{vida_restante} | Retorna as informações de combate após um dano. Flag_dano indica se você recebeu dano ou não. |
| **403** {username}                                    | Retorna o nome do inimigo.                       |
| **404** {resultado}                                   | Resultado da partida: 1 para vitória, 0 para derrota. |
| **999**    | Encerra a aplicação.                                |
| **1201** {player_nome}                               | Inicia o chat para o cliente.                    |
| **1202** {player_nome};{mensagem}                   | Indica uma mensagem enviada por um player.       |



| **Mensagem Cliente**        | **Resposta Servidor**                                    | **Explicação**                                    |
|-----------------------------|---------------------------------------------------------|--------------------------------------------------|
| **101**                     | **201**                                                | Exibe ajuda sobre os comandos disponíveis.       |
| **103**                     | **203**                                                | Lista as sessões disponíveis.                     |
| **104**                     | **204**                                                | Cria uma nova sessão.                             |
| **105 {sessao_id}**        | **205 {sessao_id};{player1_nome};{player2_nome}**     | Entra em uma sessão específica.                   |
| **106**                     | **206 {sessao_id}**                                    | Sai da sessão atual.                             |
| **113 {classe}**           | **303 {classe}**                                       | Escolhe uma classe ou ação específica.           |
| **114 {ação}**             | **304 {mensagem}** ou **401 {tipo_combate};...**      | Executa uma ação de combate.                     |
| **115**                     | **305 {partida_id}**                                   | Sai da partida atual.                            |
| **999**                     | **999**                                                | Encerra a aplicação.                             |
| **1102 {mensagem}**        | **1202 {player_nome};{mensagem}**                      | Indica uma mensagem enviada por um jogador.      |
