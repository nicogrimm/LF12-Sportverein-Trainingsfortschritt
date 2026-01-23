FROM node:25 AS js-build

WORKDIR /frontend

COPY frontend /frontend

RUN npm install -g pnpm vite

RUN pnpm install && pnpm run build

FROM maven:3-amazoncorretto-25-debian AS java-build

WORKDIR /backend

COPY backend /backend
COPY --from=js-build /frontend/dist /backend/src/main/resources/static

RUN mvn versions:set -DnewVersion=1

RUN ./mvnw package


FROM amazoncorretto:25-alpine AS runtime

WORKDIR /app

COPY --from=java-build /backend/target/backend-1.jar ./backend.jar

CMD ["java", "-jar", "/app/backend.jar"]