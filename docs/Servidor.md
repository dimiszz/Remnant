# Servidor

O main do servidor é algo simples: uma validação para o argumento de entrada (porta) e um loop que espera conexão de clientes. Assim que um cliente se conecta, uma Thread `Usuario` é iniciada e o servidor volta a aguardar a próxima conexão.

## Usuario

A classe `Usuario` é a ponte que conecta o cliente e o servidor. É uma Thread que lida com toda essa comunicação. Ele possui os atributos essencias para a comunicação: `Socket`, `BufferedWriter` e `BufferedReader`, outros para controle da instância: `idUsuario`, `username`, `flagPartida` e `active` e alguns estáticos para controle de todos os Usuários: uma lista `usuarios` e `livre`, que define o ID dele.

Nele há o método write que escreve mensagens usando o `BUfferedWriter` para o cliente. Todas as classes do pacote podem escrever no usuário. Seu ID é definido de forma síncrona para evitar que 2 usuários tenham o mesmo ID. Também temos o `closeEverything` que finaliza as conexões com o cliente e remove todas as pendências do usuário no Servidor, como Partidas e Sessões que ele está.

Em seu loop principal, ele olha para o ``BufferedReader`` e decodifica a mensagem pela função `decodifica`, espera ocorrer tudo o que precisa e volta a olhar para o `BufferedReader`. Portanto, ele lida com as mensagens de um cliente específico sequencialmente. Caso o usuário queira finalizar a conexão ou que ela seja feita inesperadamente, é chamado o método `closeEverything` para finalizar o Usuário sem deixar rastros.

No método decodifica, temos as mensagens separadas entre mensagens de Sessão e mensagens de Partida. Assim, se o usuário está em Partida, só pode enviar mensagens de Partida, não de Sessão. Assim, o Usuario decodifica o código da mensagem e chama a função equivalente para tratá-la.