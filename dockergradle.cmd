@Echo off
rem test
rem docker volume create --name maven-repo
docker run --rm -v maven-repo:/root/.m2 -v "%cd%":/app -w /app gradle:alpine gradle %*