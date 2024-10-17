#!/bin/bash
# =========================
# Configuração do Cliente
# =========================

# Exclui o diretório 'out' se ele existir para garantir o clean build
if [ -d "out" ]; then
    echo "Preparando clean build..."
    rm -rf out
fi

# Verifica se o diretório out/manual/rods existe e cria se necessário
if [ ! -d "out/manual/rods" ]; then
    echo "Criando o diretório out/manual/rods..."
    mkdir -p out/manual/rods
fi

# Verifica se o arquivo manifest_cliente.txt do Cliente existe e cria se necessário
if [ ! -f "out/manual/rods/manifest_cliente.txt" ]; then
    echo "Criando manifest_cliente.txt..."
    cat <<EOL > out/manual/rods/manifest_cliente.txt
Manifest-Version: 1.0
Main-Class: main.Cliente
EOL
fi

# Navega até o diretório src
cd src

# Compila o arquivo Cliente.java e coloca os arquivos .class no diretório out/manual/rods
javac main/Cliente.java -d ../out/manual/rods
if [ $? -ne 0 ]; then
    echo "Erro na compilação do Cliente. Verifique o código."
    read -p "Pressione Enter para sair."
    exit 1
fi

# Volta para o diretório anterior
cd ..

# Cria o arquivo cliente.jar, usando o manifest_cliente.txt e os arquivos do diretório out/manual/rods
jar cfm out/build/cliente.jar out/manual/rods/manifest_cliente.txt -C out/manual/rods/ .
if [ $? -ne 0 ]; then
    echo "Erro ao criar o arquivo JAR do Cliente. Verifique os arquivos."
    read -p "Pressione Enter para sair."
    exit 1
fi

# =========================
# Configuração do Servidor
# =========================

# Verifica se o arquivo manifest_servidor.txt do Servidor existe e cria se necessário
if [ ! -f "out/manual/rods/manifest_servidor.txt" ]; then
    echo "Criando manifest_servidor.txt..."
    cat <<EOL > out/manual/rods/manifest_servidor.txt
Manifest-Version: 1.0
Main-Class: main.Servidor
EOL
fi

# Navega até o diretório src
cd src

# Compila o arquivo Servidor.java e coloca os arquivos .class no diretório out/manual/rods
javac main/Servidor.java -d ../out/manual/rods
if [ $? -ne 0 ]; then
    echo "Erro na compilação do Servidor. Verifique o código."
    read -p "Pressione Enter para sair."
    exit 1
fi

# Volta para o diretório anterior
cd ..

# Cria o arquivo servidor.jar, usando o manifest_servidor.txt e os arquivos do diretório out/manual/rods
jar cfm out/build/servidor.jar out/manual/rods/manifest_servidor.txt -C out/manual/rods/ .
if [ $? -ne 0 ]; then
    echo "Erro ao criar o arquivo JAR do Servidor. Verifique os arquivos."
    read -p "Pressione Enter para sair."
    exit 1
fi

echo "Operação concluída com sucesso para Cliente e Servidor."
read -p "Pressione Enter para sair."