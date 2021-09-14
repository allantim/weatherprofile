
env-up:
	docker-compose -f src/test/resources/test-compose.yaml up -d

env-down:
	docker-compose -f src/test/resources/test-compose.yaml down