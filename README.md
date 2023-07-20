# community-manager

## Instalar app

La app community manager tiene configurado docker para poder inicializarse, lo unico que necesita es posicionarse en consola en la raiz del proyecto.

posteriormente ejecuta el comando 

```
docker-compose up -d --build
```

Esto levanta la base de datos de postgress dentro del puerto `` 5434 ``, en los properties de la app este ya viene configurado asi como user y password

## Popular la base de datos 

Cuando el proyecto se levanta por primera vez es necesario crear el usuario admin por primera vez para esto se debe iniciar un cliente manegador de base de datos de postgreSQL,
una vez conectado es necesario ejecutar el script contenido en la carpeta resources del proyecto con nombre [data.sql](https://github.com/erikedu5/community-manager/tree/main/src/main/resources), estos se deben ejecutar y acceder a la app

la app se ejecuta dentro el puerto `` 8080 `` en la siguiente url [local](http://localhost:8080/)

