# Servidor

O main do servidor é algo simples: uma validação para o argumento de entrada (porta) e um loop que espera conexão de clientes. Assim que um cliente se conecta, uma Thread `Usuario` é iniciada e o servidor volta a aguardar a próxima conexão.

## Usuario

A classe `Usuario` é a ponte que conecta o cliente e o servidor. É uma Thread que lida com toda essa comunicação. Ele possui os atributos essencias para a comunicação: `Socket`, `BufferedWriter` e `BufferedReader`, outros para controle da instância: `idUsuario`, `username`, `flagPartida` e `active` e alguns estáticos para controle de todos os Usuários: uma lista `usuarios` e `livre`, que define o ID dele.

Nele há o método write que escreve mensagens usando o `BUfferedWriter` para o cliente. Todas as classes do pacote podem escrever no usuário. Seu ID é definido de forma síncrona para evitar que 2 usuários tenham o mesmo ID. Também temos o `closeEverything` que finaliza as conexões com o cliente e remove todas as pendências do usuário no Servidor, como Partidas e Sessões que ele está.

Em seu loop principal, ele olha para o ``BufferedReader`` e decodifica a mensagem pela função `decodifica`, espera ocorrer tudo o que precisa e volta a olhar para o `BufferedReader`. Portanto, ele lida com as mensagens de um cliente específico sequencialmente. Caso o usuário queira finalizar a conexão ou que ela seja feita inesperadamente, é chamado o método `closeEverything` para finalizar o Usuário sem deixar rastros.

No método decodifica, temos as mensagens separadas entre mensagens de Sessão e mensagens de Partida. Assim, se o usuário está em Partida, só pode enviar mensagens de Partida, não de Sessão. Assim, o Usuario decodifica o código da mensagem e chama a função equivalente para tratá-la.

## Sessão

Ela possui como atributos um ``HashMap`` para busca da sessão específica de um usuário baseada no ID, um int para o máximo de sessões e um livre para marcar o próximo ID livre. Também possui 2 Usuários e uma partida.

Essa classe tem por objetivo gerenciar o estado das Sessões que os Usuários iniciam, além de enviar as mensagens entre os chats dos clientes. Os seus métodos escrevem as mensagens para os usuários respectivos que chamam as funções de gerenciamento.

## Partida

Possui 2 jogadores, um indicador `CombateIniciado`, outro `metadeRodada` e um int para definir a rodada.

Um `Jogador` serve como uma interface para conectar informações importantes: `Usuário`, `Classe`, `Turno` e `Jogada`.

Nela, temos as funções `setClasse()`, `comecaCombate()`, `combate()`, `comecaPartida()`, `finalizarPartida()` e `inverteTurno()`.

A função principal é a `combate()`. Nela há toda lógica de batalha. Quando um usuário chama essa função, ela identifica se o usuário está atacando ou defendendo e se ele já escolheu a ação. Assim, caso já tenha escolhido e o outro jogador ainda não, apenas avisa que este jogador está esperando. No momento que o usuário que falta fazer a ação, os cálculos do combate são feitos.

A classe `Classe` contém a lógica para o cálculo e recebimento de dano. Esses métodos são chamados durante o combate.

O método `finalizaPartida()` é chamado quando um dos jogadores tem a vida reduzida a 0 e envia as respectivas mensagens para o vencedor e o perdedor. Depois, a partida é finalizada e os jogadores voltam para o lobby (onde podem executar os comandos de sessões)