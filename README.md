<h1 align="center">
  CRDC - Desafio - Full Stack Java developer
</h1>

A solução desenvolvida possui projetos front e backend e utiliza Spring Batch para o processamento de um arquivo CNAB e exibe os seus lançamentos importados em uma interface SPA com Vue3.js.

## Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JDBC](https://spring.io/projects/spring-data-jdbc)
- [Spring Batch](https://spring.io/projects/spring-batch)
- [Vite](https://vitejs.dev)
- [Vue](https://vuejs.org/)
- [Tailwind](https://tailwindcss.com)
- [Postgres](https://www.postgresql.org/)

## Como Executar

A executação deve ser feita com o Docker Compose, caso desejado, é possível executar cada projeto manualmente e localmente seguindo as instruções de seus respectivos arquivos README.

- Clonar repositório git:
```
git clone https://github.com/markin369/desafio-crdc.git
```
- Executar o script de inicialização:
```
chmod +x start.sh
./start.sh
```
- Acessar aplicação em `http://localhost:9090`. O arquivo de upload a ser usado deve ser no formato CNAB, anexado em `files`.

## Ambiente

Os projetos foram publicados no [Render](https://render.com) e o sistema pode ser acessado [nesse link](https://frontend-desafio.onrender.com/).

## Decisões de Arquitetura

- O controle de unicidade das transações é feito por arquivo CNAB, o que significa que o processamento das transações é feito apenas uma vez por arquivo.
- O arquivo CNAB deve ser nomeado com um id ou timestamp, pois ele será passado como
parâmetro do job e só pode ser importado uma única vez.
- Caso seja informado um arquivo já importado, deve ser informada uma mensagem de
erro ao usuário.
- Caso haja erro no processamento é possível submeter o mesmo arquivo várias vezes para habilitar o restart de onde o processamento parou.
- Se o arquivo for muito grande, é possível utilizar uma estratégia de particionamento
no job, melhorando assim a performance.