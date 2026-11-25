# Club Sawaid (Full Stack)

This repository now hosts both the Spring Boot API (`src/`) and the Angular front-end (`club-sawaid-ui/`), so you can run a full-stack instance locally.

## Backend (Spring Boot)
1. Ensure Java 17 is installed.
2. Start the API with `./mvnw spring-boot:run` from the repository root.
3. The backend listens on `http://localhost:8080` and exposes `/api/...` endpoints that the Angular app consumes. CORS is configured to accept requests from `http://localhost:4200`.

## Frontend (Angular `club-sawaid-ui`)
1. Install Node/NPM 11+ and run `npm install` inside `club-sawaid-ui/` the first time.
2. The dev server watches `src/` and proxies to the API via the `environment.apiBaseUrl` (`http://localhost:8080/api`). Adjust `src/environments/environment.ts` if you need a different backend host.
3. Start the UI with `npm start` (runs `ng serve`).

## Running them together
1. Launch the Spring Boot API (`./mvnw spring-boot:run`).
2. Launch the Angular dev server in `club-sawaid-ui/` (`npm start`).
3. Visit http://localhost:4200 to see sample data, including the events that are fetched from the `/api/events/upcoming` endpoint.
4. Authentication workflows use the shared DTO layer between the front-end and back-end for consistent payloads.

If you plan to build for production, run `npm run build` inside `club-sawaid-ui/` and deploy the contents of `dist/` to a static host, pointed at the API's base URL.
