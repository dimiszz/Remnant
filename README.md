# Remnant

Seja bem vindo a Remnant! Um jogo turn-based que usa uma arquitetura cliente-servidor que suporta multiplos jogos simultâneos!

Para compilar o projeto, execute o script *build_client.bat*. Ele será compilado em 2 arquivos *.jar* no caminho /out/build/.

Para iniciar a aplicação, use:

Servidor: `java - jar servidor.java <porta>`
Cliente: `java - jar cliente.java <ip-do-servidor> <porta> [DEBUG]`

Para conseguir o ip do servidor, execute *ipconfig* no terminal do Windows ou *ifconfig* no Linux para descobrir qual é o IPV4 daquela rede.

# Sessão

O servidor é baseado em Sessões: ao se conectar, insira o seu nome e ele ficará registrado na memória do Servidor. Depois, você fica habilitado a usar os comandos para começar as sessões:

- "/listar_sessoes" para ver a lista de sessões disponíveis.
- "/criar_sessao" para criar uma sessão.
- "/entrar_sessao {id}" para se juntar a uma sessão.
- "/sair_sessao" para sair da sessão atual.

Caso queira sair, pode usar o comando `/fechar`.

Assim que um usuário entrar em uma sessão, a partida começa.

# Partida

Em partida, os usuários tem 3 opções:

- "/escolher {classe}" &rarr; escolhe a classe que vai jogar. Pode ser uma entre Guerreiro, Feiticeiro e Paladino.
- Use "/combate {ataque/defesa}" para escolher o tipo de ataque ou defesa.
- Use "/sair_partida" para sair da partida.

Caso queira finalizar a aplicação, use `/fechar`.

# O jogo

Como funciona o jogo? Primeiramente, os jogadores irão escolher suas classes. A partida vai definir aleatoriamente quem vai começar defendendo e atacando. Assim, quem ataca vai escolher entre 3 opções: Físico, Mágico e Especial. O dano será equivalente à força, inteligência e a soma destes, respectivamente.

Enquanto isso, quem defende escolhe entre defesa Física, Mágica e Counter. No caso de ataque Físico, somente uma defesa Física conseguirá mitigar parte do dano. Para um ataque Mágico, uma defesa Física mitiga pouca parte do dano e uma Mágica usa os pontos de Inteligência como parte da defesa.

Por fim, temos o que deixa o jogo interessante: o Ataque Especial e a defesa Counter. Caso o atacante escolha Especial, o dano é igual a soma de sua Força com sua Inteligência. Defesas comuns não conseguem mitigar o ataque Especial. Para tanto, o defensor deve usar o Counter. Essa defesa reverte todo o dano que iria receber como dano para quem atacou.

O jogo termina quando um dos jogadores chega a 0 pontos de vida e o outro é declarado como vencedor.