# Aplicación Spring Boot - Demo

Esta es una aplicación Spring Boot que expone un endpoint RESTful. La aplicación utiliza una base de datos H2 en memoria.

## Requisitos

- Java JDK 17 o superior instalado.

## Ejecución de la Aplicación

Para ejecutar la aplicación, sigue estos pasos:

1. Descarga el archivo `demo-0.0.1-SNAPSHOT.jar` a tu máquina local.(que se encuentra en la raiz del proyecto)
2. Abre una terminal o línea de comandos.
3. Navega al directorio donde descargaste el archivo JAR.
4. Ejecuta el siguiente comando bash:

   java -jar demo-0.0.1-SNAPSHOT.jar

5. Copiar e importar el siguiente CURL en postman o cualquier otra herramienta:

curl --location 'localhost:8080/api/users' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Juan Rodriguez",
"email": "aaaaaaa@dominio.cl",
"password": "Useadmin1$",
"phones": [
{
"number": "1234567",
"citycode": "1",
"countrycode": "57"
}
]
}'

Se deberia ver una respuesta con la siguiente estructura:

{
"id": "2645b423-52ec-4fb4-b44e-ed8901b42471",
"name": "Juan Rodriguez",
"email": "aaaaaaa@dominio.cl",
"created": "2023-11-23T12:32:07.157986",
"modified": "2023-11-23T12:32:07.158024",
"lastLogin": "2023-11-23T12:32:07.081774",
"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWFhYWFhQGRvbWluaW8uY2wiLCJpYXQiOjE3MDA3NTM1MjcsImV4cCI6MTcwMDc1NDQyN30.Yf199teIEbf2PPwa_VIGk2pFI_g7EPWnwebIi4k7KI7xJ1XcUythizAPYNkVcgtCg6kVql0oZK6r8JQPxBSjOQ",
"isActive": true
}