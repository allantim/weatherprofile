SERVICE := weathertracker

db-up:
	docker-compose -f src/test/resources/test-compose.yaml up -d

db-down:
	docker-compose -f src/test/resources/test-compose.yaml down

app-build:
	docker-compose build

app-run:
	docker run -d \
		-p 8080:8080 \
		-e DATABASE_URL=jdbc:mysql://mysql_db:3306/weathertracker \
		-e DATABASE_USERNAME=weatheruser \
		-e DATABASE_PASSWORD=ABC \
		-e RETRIEVE_INTERVAL=60 \
		-e OPEN_WEATHER_API_KEY=2a5fb3ca31cc264d05fa69eeadd72ef8 \
		-e OPEN_WEATHER_API_URL=https://api.openweathermap.org/data/2.5/weather \
		--network tim-net \
		--name $(SERVICE) \
		tim/$(SERVICE)

app-cleanup:
	docker stop $(SERVICE)
	docker rm $(SERVICE)