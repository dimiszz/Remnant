# Remnant

Seja bem vindo a Remnant! Um jogo turn-based que usa uma arquitetura cliente-servidor que suporta multiplos jogos simultâneos!

Para compilar o projeto, execute o script *build_client.bat*. Ele será compilado em 2 arquivos *.jar* no caminho /out/build/.

Para iniciar a aplicação, use:

Servidor: `java - jar servidor.java <porta>`
Cliente: `java - jar cliente.java <ip-do-servidor> <porta> [DEBUG]`

Para conseguir o ip do servidor, execute *ipconfig* no terminal do Windows ou *ifconfig* no Linux.

# Sessão

O servidor é baseado em Sessões: ao se conectar, insira o seu nome e ele ficará registrado na memória do Servidor. Depois, você fica habilitado a usar os comandos para começar as sessões:
