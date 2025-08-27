#!/bin/bash
set -e

# --- Caminhos dos Projetos ---
BACKEND_DIR="/opt/icarplus/backend"
FRONTEND_SRC_DIR="/opt/icarplus/frontend-src"
FRONTEND_PUBLISH_DIR="/var/www/icarplus.com.br"
APP_CLIENTE_DIR="/var/www/app-cliente"
APP_GESTAO_DIR="/var/www/app-gestao"

# --- Configurações do Backend ---
JAR_NAME="plataform-0.0.1-SNAPSHOT.jar"
ENV_FILE="/opt/icarplus/.env"
APP_LOG_FILE="$BACKEND_DIR/backend.log"
APP_PORT=8080

echo "=================================================="
echo "🚀 Iniciando deploy unificado em: $(date)"
echo "=================================================="

# === ETAPA 1: DEPLOY DO BACKEND ===
echo "--- Iniciando deploy do Backend ---"
cd $BACKEND_DIR
git fetch --all
git reset --hard origin/production
git clean -dfx

echo "☕ Compilando a aplicação com Maven..."
./mvnw clean package -DskipTests

# --- Verifica se a porta está ocupada e encerra o processo antigo ---
echo "🛑 Verificando se a porta $APP_PORT está ocupada..."
OLD_PID=$(lsof -t -i:$APP_PORT || echo "")
if [ -n "$OLD_PID" ]; then
  echo "    -> Porta $APP_PORT ocupada pelo PID $OLD_PID. Encerrando..."
  kill -15 $OLD_PID
  sleep 5
else
  echo "    -> Porta $APP_PORT livre."
fi

# --- Inicia o backend ---
echo "▶️ Iniciando nova versão do Backend..."
if [ ! -f "$ENV_FILE" ]; then
    echo "❌ ERRO: Arquivo de credenciais '$ENV_FILE' não encontrado."
    exit 1
fi

# Carrega variáveis do .env
export $(grep -v '^#' $ENV_FILE | xargs)

TEMP_SPRING_PROFILE="production"
nohup java -jar "$BACKEND_DIR/target/$JAR_NAME" --spring.profiles.active="$TEMP_SPRING_PROFILE" > "$APP_LOG_FILE" 2>&1 &

sleep 15
NEW_PID=$(lsof -t -i:$APP_PORT || echo "")
if [ -n "$NEW_PID" ]; then
  echo "✅ Sucesso! Backend rodando com PID: $NEW_PID"
else
  echo "❌ ERRO: Backend falhou ao iniciar. Últimas 100 linhas do log:"
  tail -n 100 "$APP_LOG_FILE"
  exit 1
fi

# === ETAPA 2: DEPLOY DO FRONTEND ===
if [ -d "$FRONTEND_SRC_DIR" ]; then
    echo ""
    echo "--- Iniciando deploy do Frontend ---"
    cd $FRONTEND_SRC_DIR
    git fetch --all
    git reset --hard origin/production
    git clean -dfx

    npm install
    npm run build

    echo "📂 Publicando build..."
    sudo rsync -av --delete dist/ $FRONTEND_PUBLISH_DIR/
    sudo rsync -av --delete dist/ $APP_CLIENTE_DIR/
    sudo rsync -av --delete dist/ $APP_GESTAO_DIR/

    sudo chown -R www-data:www-data $FRONTEND_PUBLISH_DIR $APP_CLIENTE_DIR $APP_GESTAO_DIR
    echo "✅ Frontend atualizado."
else
    echo "⚠️ Diretório do frontend não encontrado. Pulando deploy."
fi

echo "=================================================="
echo "🎉 Deploy finalizado com sucesso!"
echo "=================================================="
