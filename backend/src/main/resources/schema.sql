
CREATE TABLE IF NOT EXISTS transacao (
  id SERIAL primary key,
  razao_social varchar(255),
  identificador_empresa varchar(255),
  reservado_cabecalho varchar(255),
  tipo_transacao varchar(1),
  valor_transacao decimal,
  conta_origem bigint,
  conta_destino bigint,
  reservado_rodape varchar(255)
);
