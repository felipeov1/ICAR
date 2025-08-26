#!/bin/bash

# Garante que o script pare imediatamente em caso de erro
set -e

# --- Caminhos dos Projetos ---
BACKEND_DIR="/opt/icarplus/backend"
FRONTEND_SRC_DIR="/opt/icarplus/frontend-src"     # <-- Pasta do CÓDIGO-FONTE do Frontend
FRONTEND_PUBLISH_DIR="/var/www/icarplus.com.br"  # <-- Pasta de PUBLICAÇÃO do Frontend
APP_CLIENTE_DIR="/var/www/app-cliente"
APP_GESTAO_DIR="/var/www/app-gestao"

# --- Configurações do Backend ---
JAR_NAME="plataform-0.0.1-SNAPSHOT.jar"
ENV_FILE="/opt/icarplus/.env"
APP_LOG_FILE="$BACKEND_DIR/backend.log"
APP_PORT=8080

# --- Início do Deploy ---
echo "=================================================="
echo "🚀 Iniciando deploy unificado em: $(date)"
echo "=================================================="

# === ETAPA 1: DEPLOY DO BACKEND ===
echo "--- Iniciando deploy do Backend ---"
cd $BACKEND_DIR
echo "🔄 Atualizando o código-fonte do Backend..."
git fetch --all
git reset --hard origin/production
git clean -dfx

echo "☕ Compilando a aplicação com Maven..."
./mvnw clean package -DskipTests

echo "🛑 Procurando e parando a aplicação antiga..."
PID=$(lsof -t -i:$APP_PORT || echo "")
if [ -n "$PID" ]; then
  echo "    -> Processo antigo encontrado com PID: $PID. Encerrando..."
  kill -15 $PID
  sleep 5
else
  echo "    -> Nenhuma aplicação antiga rodando."
fi

echo "▶️  Iniciando a nova versão da aplicação (Backend)..."
if [ ! -f "$ENV_FILE" ]; then
    echo "❌ ERRO: Arquivo de credenciais '$ENV_FILE' não encontrado."
    exit 1
fi
set -o allexport
source $ENV_FILE
set +o allexport
nohup java -jar "$BACKEND_DIR/target/$JAR_NAME" > "$APP_LOG_FILE" 2>&1 &
sleep 15
echo "🔎 Verificando o status da nova aplicação (Backend)..."
NEW_PID=$(lsof -t -i:$APP_PORT || echo "")
if [ -n "$NEW_PID" ]; then
  echo "✅ Sucesso! Backend rodando com o novo PID: $NEW_PID"
else
  echo "❌ ERRO: O Backend falhou ao iniciar. Verifique o log em: $APP_LOG_FILE"
  tail -n 20 $APP_LOG_FILE
  exit 1
fi

# === ETAPA 2: DEPLOY DO FRONTEND ===
echo ""
echo "--- Iniciando deploy do Frontend ---"
# NAVEGA PARA A PASTA DE CÓDIGO-FONTE CORRETA
cd $FRONTEND_SRC_DIR
echo "🔄 Atualizando o código-fonte do Frontend..."
git fetch --all
git reset --hard origin/production # 👈 Verifique se 'production' é a branch correta do frontend
git clean -dfx

echo "📦 Instalando dependências e compilando o Frontend..."
npm install
npm run build

echo "📂 Publicando a nova build para os 3 sites..."
# COPIA o resultado da build da pasta de código-fonte (dist/) para as pastas de publicação (/var/www/...)
sudo rsync -av --delete dist/ $FRONTEND_PUBLISH_DIR/
sudo rsync -av --delete dist/ $APP_CLIENTE_DIR/
sudo rsync -av --delete dist/ $APP_GESTAO_DIR/


echo "🔐 Corrigindo permissões das pastas de publicação..."
sudo chown -R www-data:www-data $FRONTEND_PUBLISH_DIR $APP_CLIENTE_DIR $APP_GESTAO_DIR

echo "✅ Sucesso! Frontend atualizado."
echo "=================================================="
echo "🎉 Deploy unificado finalizado com sucesso!"
echo "=================================================="
