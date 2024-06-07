https://docs.docker.com/compose/install/linux/

# Guía de Docker

## Redes en Docker

## Crea una red personalizada en Docker para que los contenedores puedan comunicarse entre sí.

```bash
docker network create my_network
```

## Listar Redes en Docker

```bash
docker network ls
```

## Ver Detalles de una Red

```bash
docker network inspect <network_name>
```

## Eliminar una Red

```bash
docker network rm <network_name>
```

## Eliminar todas las redes no predefinidas

```bash
docker network prune
```

## Añadir un Contenedor a una Red

### Al Crear el Contenedor

```bash
docker run -d --name my_container --network my_network nginx
```

### Añadir un Contenedor Existente a una Red

```bash
docker network connect my_network my_container
```

## Ver Redes a las que Pertenece un Contenedor

```bash
docker inspect my_container
```

## Quitar un Contenedor de una Red

```bash
docker network disconnect my_network my_container
```

## Desconectar un contenedor de una red

```bash
docker network disconnect my_network my_container
```

## Ejecutar Docker Compose

```bash
docker compose up -d
```









































https://docs.docker.com/compose/install/linux/

docker run -d --name prometheus \
-p 9090:9090 \
-v /mnt/d/repositorios/soluciones\ de\ software/spring-boot-demo/prometheus/docker/prometheus.yml:/etc/prometheus/prometheus.yml \
prom/prometheus

# docker run: Comando para ejecutar un nuevo contenedor Docker.
# -d: Ejecuta el contenedor en segundo plano (modo desatendido).
# --name prometheus: Asigna el nombre "prometheus" al contenedor.
# -p 9090:9090: Mapea el puerto 9090 del contenedor al puerto 9090 del host, permitiendo el acceso a la interfaz web de Prometheus.
# -v /path/to/your/prometheus.yml:/etc/prometheus/prometheus.yml: Monta el archivo de configuración prometheus.yml desde tu sistema de archivos al contenedor, en la ruta /etc/prometheus/prometheus.yml dentro del contenedor.
# prom/prometheus: Utiliza la imagen oficial de Prometheus desde Docker Hub.




















docker run -d --name prometheus -p 9090:9090 -v /mnt/d/repositorios/soluciones\ de\ software/spring-boot-demo/prometheus/docker/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

docker run -d --name prometheus \
-p 9090:9090 \
-v /mnt/d/repositorios/soluciones\ de\ software/spring-boot-demo/prometheus/docker/prometheus.yml:/etc/prometheus/prometheus.yml \
prom/prometheus

# docker run: Comando para ejecutar un nuevo contenedor Docker.
# -d: Ejecuta el contenedor en segundo plano (modo desatendido).
# --name prometheus: Asigna el nombre "prometheus" al contenedor.
# -p 9090:9090: Mapea el puerto 9090 del contenedor al puerto 9090 del host, permitiendo el acceso a la interfaz web de Prometheus.
# -v /path/to/your/prometheus.yml:/etc/prometheus/prometheus.yml: Monta el archivo de configuración prometheus.yml desde tu sistema de archivos al contenedor, en la ruta /etc/prometheus/prometheus.yml dentro del contenedor.
# prom/prometheus: Utiliza la imagen oficial de Prometheus desde Docker Hub.













# Configurar la Aplicación Spring Boot

- Spring Boot: Configura tu aplicación para exponer métricas utilizando Actuator y Micrometer.
- Prometheus: Configura Prometheus para raspar las métricas de tu aplicación Spring Boot.
- Grafana: Instala Grafana y configúralo para utilizar Prometheus como fuente de datos.
- Dashboards: Crea paneles en Grafana para visualizar y analizar las métricas recopiladas.

### Configuración de Prometheus: `prometheus.yml`

El archivo `prometheus.yml` configura cómo Prometheus recopilará datos de las aplicaciones. A continuación, se explica
cada sección:

```yaml
global:
  scrape_interval: 15s
```

- `global`: Configuraciones globales que se aplican a todos los trabajos de scrape (recopilación de datos).
- `scrape_interval`: Intervalo de tiempo entre cada recolección de datos. En este caso, cada 15 segundos.

```yaml
scrape_configs:
  - job_name: 'spring-boot'
    scrape_interval: 5s
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: [ 'host.docker.internal:8080' ]
```

- `scrape_configs`: Lista de configuraciones de trabajos de scrape.
    - `job_name`: Nombre del trabajo. Aquí, es 'spring-boot'.
    - `scrape_interval`: Intervalo específico para este trabajo (5 segundos).
    - `metrics_path`: Ruta donde Prometheus puede encontrar los métricos. Para aplicaciones Spring Boot, esta es
      típicamente `/actuator/prometheus`.
    - `static_configs`: Configuraciones estáticas de los objetivos que Prometheus monitorea.
        - `targets`: Lista de direcciones y puertos de las aplicaciones que se van a
          monitorear. `host.docker.internal:8080` es la dirección y puerto de tu aplicación Spring Boot. Si tu
          aplicación está en otro host o puerto, cambia esta línea en consecuencia.

### Ejecutando Prometheus con Docker

Para ejecutar Prometheus con Docker, utilizamos el siguiente comando:

```bash
docker run -d --name prometheus \
  -p 9090:9090 \
  -v /path/to/your/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus
```

- `docker run`: Comando para ejecutar un nuevo contenedor Docker.
- `-d`: Ejecuta el contenedor en segundo plano (modo desatendido).
- `--name prometheus`: Asigna el nombre "prometheus" al contenedor.
- `-p 9090:9090`: Mapea el puerto 9090 del contenedor al puerto 9090 del host, permitiendo el acceso a la interfaz web
  de Prometheus.
- `-v /path/to/your/prometheus.yml:/etc/prometheus/prometheus.yml`: Monta el archivo de configuración `prometheus.yml`
  desde tu sistema de archivos al contenedor, en la ruta `/etc/prometheus/prometheus.yml` dentro del contenedor.
- `prom/prometheus`: Utiliza la imagen oficial de Prometheus desde Docker Hub.

### Seguridad y Credenciales en Prometheus

Por defecto, Prometheus no viene con autenticación habilitada. Para un entorno de producción, es recomendable proteger
la interfaz web de Prometheus utilizando un proxy inverso con autenticación, como Nginx o Traefik. Aquí hay un ejemplo
básico de cómo hacerlo con Nginx:

#### Configuración de Nginx

1. **Instala Nginx**:
   ```bash
   sudo apt-get install nginx
   ```

2. **Configura Nginx para usar autenticación básica**:
   Crea un archivo de contraseñas:
   ```bash
   sudo htpasswd -c /etc/nginx/.htpasswd yourusername
   ```

3. **Edita la configuración de Nginx**:
   Abre `/etc/nginx/sites-available/default` y añade la configuración para Prometheus:

   ```nginx
   server {
       listen 80;
       server_name yourdomain.com;

       location / {
           proxy_pass http://localhost:9090;
           auth_basic "Restricted";
           auth_basic_user_file /etc/nginx/.htpasswd;
       }
   }
   ```

4. **Reinicia Nginx**:
   ```bash
   sudo systemctl restart nginx
   ```

Ahora, cualquier intento de acceso a `http://yourdomain.com` requerirá autenticación.

Con estos pasos, has configurado Prometheus para recolectar métricas de tu aplicación Spring Boot y asegurado la
interfaz web de Prometheus con autenticación básica usando Nginx.

El archivo `prometheus.rules.yml` contiene reglas de grabación y alertas. Las reglas de grabación permiten crear nuevas
series de tiempo precomputadas basadas en expresiones PromQL, mientras que las reglas de alerta se utilizan para definir
condiciones bajo las cuales se disparan alertas.

A continuación se muestra un ejemplo de cómo podría ser el contenido de un archivo `prometheus.rules.yml` para una
aplicación Spring Boot:

### Ejemplo de `prometheus.rules.yml`

#### Reglas de grabación

```yaml
groups:
  - name: example
    rules:
      - record: instance:cpu_usage_seconds:rate5m
        expr: rate(node_cpu_seconds_total[5m])
        labels:
          job: "example-job"
      - record: job_instance_mode:node_cpu_seconds:avg_rate5m
        expr: avg by (job, instance, mode) (rate(node_cpu_seconds_total[5m]))
```

#### Reglas de alerta

```yaml
groups:
  - name: example-alerts
    rules:
      - alert: HighCPUUsage
        expr: instance:cpu_usage_seconds:rate5m{job="example-job"} > 0.9
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "High CPU usage detected"
          description: "CPU usage is above 90% for more than 1 minute."

      - alert: InstanceDown
        expr: up{job="example-job"} == 0
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "Instance down"
          description: "Instance {{ $labels.instance }} is down for more than 5 minutes."
```

### Descripción de las Reglas

#### Reglas de Grabación

- **`instance:cpu_usage_seconds:rate5m`**: Calcula la tasa de uso de CPU promedio por instancia en los últimos 5
  minutos.
    - `record`: Nombre de la nueva serie de tiempo.
    - `expr`: Expresión PromQL que calcula la tasa de uso de CPU.
    - `labels`: Etiquetas adicionales que se agregan a la nueva serie de tiempo.

- **`job_instance_mode:node_cpu_seconds:avg_rate5m`**: Calcula la tasa promedio de uso de CPU por trabajo, instancia y
  modo en los últimos 5 minutos.
    - `record`: Nombre de la nueva serie de tiempo.
    - `expr`: Expresión PromQL que calcula la tasa promedio de uso de CPU.

#### Reglas de Alerta

- **`HighCPUUsage`**: Alerta cuando el uso de CPU es superior al 90% durante más de 1 minuto.
    - `alert`: Nombre de la alerta.
    - `expr`: Expresión PromQL que define la condición de la alerta.
    - `for`: Duración que la condición debe ser verdadera antes de que se dispare la alerta.
    - `labels`: Etiquetas adicionales para la alerta.
    - `annotations`: Anotaciones que proporcionan más información sobre la alerta.

- **`InstanceDown`**: Alerta cuando una instancia no responde durante más de 5 minutos.
    - `alert`: Nombre de la alerta.
    - `expr`: Expresión PromQL que define la condición de la alerta.
    - `for`: Duración que la condición debe ser verdadera antes de que se dispare la alerta.
    - `labels`: Etiquetas adicionales para la alerta.
    - `annotations`: Anotaciones que proporcionan más información sobre la alerta.

### Integración en `prometheus.yml`

Asegúrate de que tu archivo `prometheus.yml` tenga una referencia al archivo `prometheus.rules.yml`:

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

  external_labels:
    monitor: 'codelab-monitor'

rule_files:
  - 'prometheus.rules.yml'

scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: [ 'localhost:9090' ]

  - job_name: 'spring-boot'
    scrape_interval: 5s
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: [ 'host.docker.internal:8080' ]
```

Con estas configuraciones, deberías tener tanto las reglas de grabación como las alertas configuradas para tu instancia
de Prometheus, lo que te permitirá monitorear y recibir alertas sobre el estado de tu aplicación Spring Boot y otros
componentes del sistema.











Entendido. Si estás ejecutando Grafana y Prometheus en contenedores Docker, necesitan estar en la misma red Docker para que Grafana pueda conectarse a Prometheus utilizando el nombre del contenedor o la IP interna.

### Solución: Conectar Contenedores a la Misma Red Docker

#### 1. Crear una Red Docker

Primero, crea una red Docker a la que ambos contenedores puedan conectarse.

```sh
docker network create monitoring
```

#### 2. Conectar los Contenedores a la Red

Luego, conecta ambos contenedores a esta red. Puedes hacerlo al iniciar los contenedores o conectándolos si ya están en ejecución.

##### Conectar contenedores ya en ejecución:

```sh
docker network connect monitoring grafana
docker network connect monitoring prometheus
```

##### Iniciar contenedores conectados a la red (si decides reiniciar los contenedores):

```sh
docker run -d --name prometheus --network monitoring -p 9090:9090 prom/prometheus --config.file=/etc/prometheus/prometheus.yml
docker run -d --name grafana --network monitoring -p 3000:3000 grafana/grafana
```

#### 3. Configurar Grafana para Conectarse a Prometheus

Una vez que ambos contenedores están en la misma red, puedes configurar Grafana para que se conecte a Prometheus usando el nombre del contenedor `prometheus` en lugar de `localhost`.

##### Pasos en la interfaz de Grafana:

1. **Abre Grafana**: Ve a la interfaz web de Grafana (por defecto en `http://localhost:3000`).
2. **Configuración de la Fuente de Datos**: Ve a **Configuration** (icono de engranaje) > **Data Sources**.
3. **Editar Fuente de Datos**: Selecciona tu fuente de datos de Prometheus o agrega una nueva fuente de datos si no la tienes configurada.
4. **Configurar la URL**:
    - En el campo **Prometheus server URL**, ingresa `http://prometheus:9090`.
    - Esto indica a Grafana que se conecte al contenedor Prometheus usando el nombre del contenedor dentro de la red Docker.

5. **Save & Test**: Guarda los cambios y prueba la conexión.

### Ejemplo Completo de Configuración en Grafana

```plaintext
Type: Prometheus
Name: prometheus
Prometheus server URL: http://prometheus:9090
Authentication: No Authentication
Timeout: 60
HTTP method: GET
```

### Verificación

Una vez que hayas configurado la URL correctamente y ambos contenedores estén en la misma red Docker, Grafana debería poder conectarse a Prometheus sin problemas.

### Resumen

1. Crea una red Docker (`monitoring`).
2. Conecta los contenedores `grafana` y `prometheus` a esta red.
3. Configura Grafana para usar `http://prometheus:9090` como la URL del servidor Prometheus.
4. Guarda y prueba la configuración en Grafana.

Esto debería resolver el problema de conexión entre Grafana y Prometheus cuando se ejecutan en contenedores Docker separados.