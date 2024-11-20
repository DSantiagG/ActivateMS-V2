docker-compose down
docker rmi user_management_ms-user-server:latest

:: Crear .jar en un subproceso
start /wait cmd /c "mvn clean package -DskipTests"

:: Levantar contenedor
docker-compose up -d
cd ..
echo "User Service is running"
