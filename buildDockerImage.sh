cp $(ls -t build/libs/*all.jar | head -1) build/libs/latest.jar
docker build -q -t $1 .