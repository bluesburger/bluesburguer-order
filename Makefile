install: down up

down-all: down down-local sonarqube-down

up:
	@ echo Up service
	@ docker compose up -d --build
	
up-local:
	@ echo Up service
	@ docker compose -f docker-compose-local.yml up -d	
	
up-local-app:
	@ echo Up service
	@ docker compose -f docker-compose-local.yml up -d application
	
down:
	@ echo Down services
	@ docker compose down --volumes --remove-orphans
	
down-local:
	@ echo Down services
	@ docker compose -f docker-compose-local.yml down --volumes --remove-orphans
	
down-local-app:
	@ echo Down application container
	@ docker compose -f docker-compose-local.yml down application --volumes --remove-orphans

sonarqube-up:
	@ docker compose -f sonarqube.yml up -d

sonarqube-down:
	@ docker compose -f sonarqube.yml down --volumes --remove-orphans
	
sonarqube-status:
	@ docker compose -f sonarqube.yml ps

sonarqube-scanner:
	@ mvnw clean verify sonar:sonar \
  		-Dsonar.projectKey=Orderingsystem \
  		-Dsonar.projectName='OrderApi' \
  		-Dsonar.host.url=http://127.0.0.1:9000 \
  		-Dsonar.token=sqp_251d910e55a9ea46dceb2443cc67a3d270790dfd
