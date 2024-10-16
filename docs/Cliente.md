# Cliente

Começando pela `main`, olharemos para o cliente.

Ele possui atributos essenciais para a execução: `Socket`, `BufferedReader`, `BufferedWriter`, `BlockingQueue` e `AtomicBoolean`. Os 3 primeiros tem por objetivo fazer a comunicação entre ele e o servidor, enquanto os outros 2 possuem objetivos mais internos.

Seus métodos são simples: um para iniciar a Thread do `Leitor`, outro para a do `Escritor`, um para finalizar tudo e o loop principal da Thread principal. 

Antes de entrar no loop, iniciamos uma Thread que olha somente para o Scanner para que o loop principal tenha apenas o objetivo de gerenciar as outras Threads de leitura e escrita.

Assim que o programa é iniciado, há um código para validar se a entrada foi feita corretamente para o IP e a porta. Depois, é pedido para inserir o nome e a conexão com o servidor incia, junto com as Threads de escrita e leitura.

## Envio de mensagens para o servidor

Para fazer a comunicação com o Servidor, usamos a `BlockingQueue` instanciada no início. A thread `Escritor` está constantemente olhando para a cabeça dessa fila. Assim, quando alguma mensagem é adicionada, Ela é tratada pela função `CodificaDecodifica.codifica(String mensagem)` que retorna uma mensagem que o servidor entende. Assim, o `Escritor` escreve para o servidor usando o `BufferedWriter` do Cliente.

Na aplicação, tanto o `Chat` quanto o `Scanner` que olha para o terminal adicionam mensagens nessa fila.

## Recebimento de mensagens do Servidor

Temos a Thread `Leitor` que tem o objetivo de receber e tratar as mensagens enviadas pelo servidor. Assim que há alguma coisa no `BufferedReader`, ela captura a mensagem e chama a função `CodificaDecodifica.decofica(String mensagem)` para fazer o tratamento da mensagem enviada pelo servidor. Caso seja uma mensagem para o chat, não imprime nada na tela e encaminha diretamente para o chat.

## Logs

A fim de descobrir o motivo de algum bug, na inicialização, podemos deixar o console de Logs aberto ao usar a opção de inicialização DEBUG.

## Chat

O `Chat` é uma interface simples feita usando a biblioteca `java.swing`. Ele possui uma saída de texto, uma entrada e um botão enviar (também é possível enviar usando o enter).

Ao enviar mensagem, chama o método `writeMessage` que escreve a mensagem na tela e a coloca na `BlockingQueue` para enviar ao outro cliente. Quando recebe uma mensagem pelo `CodificaDecodifica.decodifica`, mostra na tela com o nome do usuário que a enviou.