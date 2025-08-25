ALTER TABLE public.company_mercado_pago_config
    ADD COLUMN refresh_token VARCHAR(255) NOT NULL,
    ADD COLUMN user_id BIGINT NOT NULL,
    ADD COLUMN live_mode BOOLEAN NOT NULL,
    ADD COLUMN expires_in BIGINT NOT NULL;

COMMENT ON TABLE public.company_mercado_pago_config IS 'Armazena as configurações e credenciais de conexão da conta Mercado Pago de cada empresa (vendedor) com o marketplace.';

COMMENT ON COLUMN public.company_mercado_pago_config.refresh_token IS 'Token utilizado para renovar o access_token do vendedor a cada 6 meses.';
COMMENT ON COLUMN public.company_mercado_pago_config.user_id IS 'ID do usuário vendedor na plataforma do Mercado Pago.';
COMMENT ON COLUMN public.company_mercado_pago_config.live_mode IS 'Indica se as credenciais são para o ambiente de produção (true) ou sandbox (false).';
COMMENT ON COLUMN public.company_mercado_pago_config.expires_in IS 'Tempo de vida do access_token em segundos.';