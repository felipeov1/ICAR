#!/bin/bash

# --- Configurações ---
# Garante que o script pare imediatamente em caso de erro
set -e

# --- Caminhos dos Projetos ---
BACKEND_DIR="/opt/icarplus/backend"
FRONTEND_DIR="/var/www/icarplus.com.br"

# --- Configurações do Backend ---
JAR_NAME="plataform-0.0.1-SNAPSHOT.jar"
ENV_FILE="/opt/icarplus/.env"
APP_LOG_FILE="$BACKEND_DIR/backend.log"
APP_PORT=8080

# --- Início do Deploy ---
echo "=================================================="
echo "🚀 Iniciando deploy unificado em: $(date)"
echo "=================================================="

#
# === ETAPA 1: DEPLOY DO BACKEND ===
#
echo "--- Iniciando deploy do Backend ---"

# 1.1. Acessa o diretório do backend
echo "➡️  Navegando para o diretório do backend..."
cd $BACKEND_DIR

# 1.2. Atualiza o código com as últimas alterações do Git
# --- INÍCIO DA CORREÇÃO ---
echo "🔄 Forçando a atualização para a versão mais recente (descartando alterações locais)..."
# Substituímos o 'git pull' por este bloco mais robusto
git fetch --all
git reset --hard origin/production # 👈 CONFIRA SE 'production' É A BRANCH CORRETA
git clean -dfx
# --- FIM DA CORREÇÃO ---

# 1.3. Compila a aplicação e gera o novo arquivo .jar (pulando os testes)
echo "☕ Compilando a aplicação com Maven..."
./mvnw clean package -DskipTests

# 1.4. Para a aplicação antiga (se estiver rodando)
echo "🛑 Procurando e parando a aplicação antiga..."
PID=$(lsof -t -i:$APP_PORT || echo "")

if [ -n "$PID" ]; then
  echo "    -> Processo antigo encontrado com PID: $PID. Encerrando..."
  kill -15 $PID
  sleep 5 # Espera para garantir que a porta foi liberada
else
  echo "    -> Nenhuma aplicação antiga rodando."
fi

# 1.5. Inicia a nova aplicação em background
echo "▶️  Iniciando a nova versão da aplicação (Backend)..."

# Verifica se o arquivo .env com as credenciais existe
if [ ! -f "$ENV_FILE" ]; then
    echo "❌ ERRO: Arquivo de credenciais '$ENV_FILE' não encontrado."
    exit 1
fi

# Carrega as variáveis de ambiente do arquivo .env
set -o allexport
source $ENV_FILE
set +o allexport

# Inicia o .jar em background
nohup java -jar "$BACKEND_DIR/target/$JAR_NAME" > "$APP_LOG_FILE" 2>&1 &

# Espera um pouco para a aplicação começar a iniciar
sleep 15

# 1.6. Verifica se a aplicação iniciou corretamente
echo "🔎 Verificando o status da nova aplicação (Backend)..."
NEW_PID=$(lsof -t -i:$APP_PORT || echo "")

if [ -n "$NEW_PID" ]; then
  echo "✅ Sucesso! Backend rodando com o novo PID: $NEW_PID"
else
  echo "❌ ERRO: O Backend falhou ao iniciar. Verifique o log em: $APP_LOG_FILE"
  tail -n 20 $APP_LOG_FILE
  exit 1
fi

#
# === ETAPA 2: DEPLOY DO FRONTEND ===
#
echo ""
echo "--- Iniciando deploy do Frontend ---"

# 2.1. Acessa o diretório do frontend
echo "➡️  Navegando para o diretório do frontend..."
cd $FRONTEND_DIR

# 2.2. Atualiza o código com as últimas alterações do Git
# --- INÍCIO DA CORREÇÃO ---
echo "🔄 Forçando a atualização para a versão mais recente (descartando alterações locais)..."
# Substituímos o 'git pull' aqui também
git fetch --all
git reset --hard origin/production # 👈 CONFIRA SE 'main' É A BRANCH CORRETA PARA O FRONTEND
git clean -dfx
# --- FIM DA CORREÇÃO ---

# 2.3. Instala as dependências e gera a build
echo "📦 Instalando dependências (npm install)..."
npm install

echo "🎨 Gerando a build de produção (npm run build)..."
npm run build

echo "📂 Publicando a nova build do frontend..."
rm -f index.html || true
rm -rf assets || true
mv dist/* .
rmdir dist

echo "✅ Sucesso! Frontend atualizado."

echo "=================================================="
echo "🎉 Deploy unificado finalizado com sucesso!"
echo "=================================================="
