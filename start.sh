docker-compose down

# build backend image
docker build -t backend-desafio:latest ./backend

# build frontend image
docker build -t frontend-desafio:latest ./frontend

# start environment
docker-compose up --build --force-recreate --remove-orphans
