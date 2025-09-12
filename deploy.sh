#!/bin/bash
set -e

# --- Caminhos dos Projetos ---
BACKEND_DIR="/opt/icarplus/backend"
ENV_FILE="/opt/icarplus/.env"
APP_LOG_FILE="$BACKEND_DIR/backend.log"
APP_PORT=8080

echo "=================================================="
echo "🚀 Iniciando deploy unificado em: $(date)"
echo "=================================================="

# === ETAPA 1: DEPLOY DO BACKEND ===
echo "--- Iniciando deploy do Backend ---"

cd $BACKEND_DIR
sudo chown -R felipe:felipe $BACKEND_DIR

git fetch --all
git reset --hard origin/production
git clean -dfx

echo "☕ Compilando a aplicação com Maven..."
./mvnw clean package -DskipTests

# --- Verifica se a porta está ocupada e encerra o processo antigo ---
echo "🛑 Verificando se a porta $APP_PORT está ocupada..."
OLD_PID=$(sudo lsof -t -i:$APP_PORT || echo "")

if [ -n "$OLD_PID" ]; then
    echo "   -> Porta $APP_PORT ocupada pelo PID $OLD_PID. Tentando encerrar..."
    sudo kill -15 $OLD_PID
    echo "   -> Enviado sinal de desligamento gracioso (SIGTERM). Aguardando 10 segundos..."
    sleep 10
    if ps -p $OLD_PID > /dev/null; then
        echo "   -> Processo ainda está rodando. Forçando o encerramento (SIGKILL)..."
        sudo kill -9 $OLD_PID
        sleep 2
    fi
    echo "   -> Processo anterior encerrado."
else
    echo "   -> Porta $APP_PORT livre."
fi

# --- Inicia o backend ---
echo "▶️ Iniciando nova versão do Backend..."
if [ ! -f "$ENV_FILE" ]; then
    echo "❌ ERRO: Arquivo de credenciais '$ENV_FILE' não encontrado."
    exit 1
fi

echo "🔍 Carregando variáveis de ambiente do '$ENV_FILE'..."
export $(grep -v '^#' "$ENV_FILE" | xargs)

if [ -z "$PROD_DB_URL" ]; then
    echo "❌ ERRO: Variável PROD_DB_URL não encontrada."
    exit 1
else
    echo "✅ Variáveis carregadas com sucesso!"
fi

TEMP_SPRING_PROFILE="production"

nohup java \
    -Xms512m \
    -Xmx1024m \
    -Dmercadopago.webhook-secret-key="$MP_WEBHOOK_SECRET" \
    -Dspring.profiles.active="$TEMP_SPRING_PROFILE" \
    -Dspring.datasource.url="$PROD_DB_URL" \
    -Dspring.datasource.username="$PROD_DB_USERNAME" \
    -Dspring.datasource.password="$PROD_DB_PASSWORD" \
    -Dspring.mail.host="$SMTP_HOST" \
    -Dspring.mail.port="$SMTP_PORT" \
    -Dspring.mail.username="$SMTP_USERNAME" \
    -Dspring.mail.password="$SMTP_PASSWORD" \
    -Dapp.api-url="https://api.icarplus.com.br" \
    -Dapp.frontend-url="https://app.icarplus.com.br" \
    -Dapp.gestao-url="https://gestao.icarplus.com.br" \
    -Dapp.logo-url="https://api.icarplus.com.br/uploads/static/logo-icar-email.png" \
    -Dmercadopago.redirect-uri="https://api.icarplus.com.br/api/v1/mercado-pago/callback" \
    -Dstorage.base-url="https://api.icarplus.com.br" \
    -Dstorage.location="/var/www/icarplus/uploads" \
    -jar /opt/icarplus/backend/target/plataform-0.0.1-SNAPSHOT.jar \

sleep 15
NEW_PID=$(lsof -t -i:$APP_PORT || echo "")
if [ -n "$NEW_PID" ]; then
    echo "✅ Sucesso! Backend rodando com PID: $NEW_PID"
else
    echo "❌ ERRO: Backend falhou ao iniciar. Verifique o log:"
    tail -n 100 "$APP_LOG_FILE"
    exit 1
fi

echo "=================================================="
echo "🎉 Deploy finalizado com sucesso!"
echo "=================================================="
