install: down up

down-all: down down-local sonarqube-down

build:
	@ .\mvnw clean install -Ppackage

build-image:
	@docker build -t ordering-system-order .

build-image-local: build
	@ docker build -f .\Dockerfile.local -t ordering-system-order .
	@ docker tag ordering-system-order:latest 637423186279.dkr.ecr.us-east-1.amazonaws.com/ordering-system-order:latest
	
push-ecr:
	@ docker push 637423186279.dkr.ecr.us-east-1.amazonaws.com/ordering-system-order:latest

build-local-and-push: build-image-local push

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

sonarqube-status:
	@ docker compose -f sonarqube.yml ps
	
sonarqube-logs:
	@ docker compose -f sonarqube.yml logs -f
	
sonarqube-down:
	@ docker compose -f sonarqube.yml down --volumes

sonarqube-publish:
	@ .\mvnw sonar:sonar
	
sonarqube-analyze: build sonarqube-publish

## Test

unit-test:
	@.\mvnw $(MVN_ARGS) test -Punit-test

integration-test:
	@.\mvnw $(MVN_ARGS) test -Pintegration-test

system-test:
	@ .\mvnw $(MVN_ARGS) test -Psystem-test
	@ echo $(TIMESTAMP) [INFO] cucumber HTML report generate in: target/cucumber-reports/cucumber.html

performance-test:
	@ .\mvnw $(MVN_ARGS) gatling:test -Pperformance-test

test: unit-test integration-test