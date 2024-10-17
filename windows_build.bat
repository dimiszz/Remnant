@echo off
REM =========================
REM Configuração do Cliente
REM =========================

REM Exclui o diretório out se ele existir para garantir o clean build
IF EXIST out (
    echo Preparando clean build...
    rmdir /s /q out
)
REM Verifica se o diretório out/manual/rods existe e cria se necessário

IF NOT EXIST out\manual\rods (
    echo Criando o diretório out\manual\rods...
    mkdir out\manual\rods
)

REM Verifica se o arquivo manifest.txt do Cliente existe e cria se necessário
IF NOT EXIST out\manual\rods\manifest_cliente.txt (
    echo Criando manifest_cliente.txt...
    (
        echo Manifest-Version: 1.0
        echo Main-Class: main.Cliente
    ) > out\manual\rods\manifest_cliente.txt
)

REM Navega até o diretório src
cd src

REM Compila o arquivo Cliente.java e coloca os arquivos .class no diretório out/manual/rods
javac main\Cliente.java -d ..\out\manual\rods
IF %ERRORLEVEL% NEQ 0 (
    echo Erro na compilação do Cliente. Verifique o código.
    pause
    exit /b %ERRORLEVEL%
)

REM Volta para o diretório anterior
cd ..

REM Cria o arquivo cliente.jar, usando o manifest_cliente.txt e os arquivos do diretório out/manual/rods
jar cfm out\build\cliente.jar out\manual\rods\manifest_cliente.txt -C out\manual\rods\ .
IF %ERRORLEVEL% NEQ 0 (
    echo Erro ao criar o arquivo JAR do Cliente. Verifique os arquivos.
    pause
    exit /b %ERRORLEVEL%
)

REM =========================
REM Configuração do Servidor
REM =========================

REM Verifica se o arquivo manifest.txt do Servidor existe e cria se necessário
IF NOT EXIST out\manual\rods\manifest_servidor.txt (
    echo Criando manifest_servidor.txt...
    (
        echo Manifest-Version: 1.0
        echo Main-Class: main.Servidor
    ) > out\manual\rods\manifest_servidor.txt
)

REM Navega até o diretório src
cd src

REM Compila o arquivo Servidor.java e coloca os arquivos .class no diretório out/manual/rods
javac main\Servidor.java -d ..\out\manual\rods
IF %ERRORLEVEL% NEQ 0 (
    echo Erro na compilação do Servidor. Verifique o código.
    pause
    exit /b %ERRORLEVEL%
)

REM Volta para o diretório anterior
cd ..

REM Cria o arquivo servidor.jar, usando o manifest_servidor.txt e os arquivos do diretório out/manual/rods
jar cfm out\build\servidor.jar out\manual\rods\manifest_servidor.txt -C out\manual\rods\ .
IF %ERRORLEVEL% NEQ 0 (
    echo Erro ao criar o arquivo JAR do Servidor. Verifique os arquivos.
    pause
    exit /b %ERRORLEVEL%
)

echo Operacao concluida com sucesso para Cliente e Servidor.
pause
