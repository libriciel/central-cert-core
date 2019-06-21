# central-cert-core

[![pipeline](https://gitlab.libriciel.fr/outils/central-cert/central-cert-core/badges/develop/pipeline.svg)](https://gitlab.libriciel.fr/outils/central-cert/central-cert-core/commits/develop) [![coverage](https://gitlab.libriciel.fr/outils/central-cert/central-cert-core/badges/develop/coverage.svg)](https://gitlab.libriciel.fr/outils/central-cert/central-cert-core/commits/develop) [![lines_of_code](https://sonarqube.libriciel.fr/api/project_badges/measure?project=com.libriciel%3Acentral-cert-core&metric=ncloc)](https://sonarqube.libriciel.fr/dashboard?id=com.libriciel%3Acentral-cert-core)  
[![quality_gate](https://sonarqube.libriciel.fr/api/project_badges/measure?project=com.libriciel%3Acentral-cert-core&metric=alert_status)](https://sonarqube.libriciel.fr/dashboard?id=com.libriciel%3Acentral-cert-core) [![maintenability](https://sonarqube.libriciel.fr/api/project_badges/measure?project=com.libriciel%3Acentral-cert-core&metric=sqale_rating)](https://sonarqube.libriciel.fr/dashboard?id=com.libriciel%3Acentral-cert-core) [![reliability](https://sonarqube.libriciel.fr/api/project_badges/measure?project=com.libriciel%3Acentral-cert-core&metric=reliability_rating)](https://sonarqube.libriciel.fr/dashboard?id=com.libriciel%3Acentral-cert-core) [![security](https://sonarqube.libriciel.fr/api/project_badges/measure?project=com.libriciel%3Acentral-cert-core&metric=security_rating)](https://sonarqube.libriciel.fr/dashboard?id=com.libriciel%3Acentral-cert-core)

Cœur Java de l'application de gestion des certificats électroniques "**Central Cert**".

Central Cert est une solution libre à la gestion et la centralisation des certificats électroniques.


### Central-cert-core contient
* *Une application Java Spring Boot*
* *Une application REST faisant le lien avec l'application central-cert-web*

### Nécessités

Afin de faire fonctionner central-cert-core il est nécessaire de :
* *Connecter l'application à une base de données.*
* *Connecter l'application à un SMTP.*


## Public API

Launch the MainApplication, and find the Swagger2 location here :  
http://localhost:8080/swagger-ui.html#!/main45application/


## Installation on an Ubuntu server

#### Installing Java 8 (if needed)

```bash
ll /usr/bin/java
unlink /usr/bin/java
ln -s /opt/jre/bin/java /usr/bin/java
java -version
```

#### Installing the Crypto service

```bash
unzip central-cert-core-*.zip -d /opt/
useradd central-cert-core -s /sbin/nologin
chown -R central-cert-core: /opt/central-cert-core

cp /opt/central-cert-core/central-cert-core.service /etc/systemd/system/
systemctl enable central-cert-core.service

systemctl start central-cert-core
```
