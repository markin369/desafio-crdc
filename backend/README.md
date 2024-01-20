# Desafio Backend Pagnet

Esse projeto foi desenvolvido usando como base [esse desafio](https://gold-box-906.notion.site/CRDC-Desafio-Full-Stack-Java-developer-42fffd9591844a7f8023bc5fc355fe14) para uma vaga fullstack na CRDC. A solução desenvolvida nesse subprojeto é o backend e utiliza Spring Batch para o processamento do arquivo CNAB.

## Como Executar
Para executar localmente é necessário ter o [PostgresSQL](https://www.postgresql.org/) instalado ou executá-lo via Docker, para tal seguir a documentação da ferramenta. Lembrar de ajustar o usuário e senha utilizados e o nome do banco, caso necessário.

- Clonar repositório git:
```
git clone https://github.com/markin369/desafio-crdc.git
```
- Buildar o projeto:
```
./backend/mvn clean install -DskipTests -f backend/pom.xml
```
- Executar o projeto:
```
java -jar backend/target/desafio-backend-crdc-0.0.1-SNAPSHOT.jar
```

## Como Testar

- Upload do arquivo:
```
curl -X POST -F "file=@/path/to/file/CNAB.txt" http://localhost:8080/cnab/upload
``` 
- Lista das operações importadas com totalizador por nome da loja:
```
curl http://localhost:8080/transacoes
```
