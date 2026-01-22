# LF12 - Sportverein Trainingsfortschritt

Eine Anwendung zur Verwaltung und Verfolgung von Trainingsfortschritten in einem Sportverein.

## Projekt-Setup

### Voraussetzungen

- Java 17 oder höher
- Docker und Docker Compose
- Maven

### Datenbank starten

```bash
cd backend
docker-compose up -d
```

Die PostgreSQL-Datenbank läuft dann auf `localhost:5432`.

### Backend starten

```bash
cd backend
./mvnw spring-boot:run
```

Die Spring Boot-Anwendung läuft dann auf `http://localhost:8080`.

## Testdaten

Die Anwendung bietet im **dev-Profil** REST-Endpoints zum Laden und Löschen von Testdaten.

### Testdaten laden

Die Testdaten umfassen:
- 4 Sportarten (Laufen, Schwimmen, Radfahren, Rudern)
- 4 Athleten (Max Mustermann, Anna Schmidt, Tom Meyer, Lisa Weber)
- 6 Trainingseinheiten mit verschiedenen Daten

**Mit curl:**
```bash
curl -X POST http://localhost:8080/api/test-data/load
```

**Mit PowerShell:**
```powershell
Invoke-WebRequest -Uri http://localhost:8080/api/test-data/load -Method POST
```

**Mit REST Client (VS Code Extension):**
```http
POST http://localhost:8080/api/test-data/load
```

### Testdaten löschen

Löscht alle Daten aus der Datenbank (Trainings, Athleten, Sportarten).

**Mit curl:**
```bash
curl -X DELETE http://localhost:8080/api/test-data/clear
```

**Mit PowerShell:**
```powershell
Invoke-WebRequest -Uri http://localhost:8080/api/test-data/clear -Method DELETE
```

**Mit REST Client (VS Code Extension):**
```http
DELETE http://localhost:8080/api/test-data/clear
```

## API-Endpoints

### Testdaten (nur im dev-Profil verfügbar)

- `POST /api/test-data/load` - Testdaten in die Datenbank laden
- `DELETE /api/test-data/clear` - Alle Daten aus der Datenbank löschen

## Technologie-Stack

### Backend
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway (Database Migrations)
- Lombok

### Datenbank
- PostgreSQL 18.1
- Docker

## Entwicklung

### Datenbankzugriff

- **Host:** localhost
- **Port:** 5432
- **Datenbank:** sportverein
- **Benutzer:** sportverein
- **Passwort:** secret-sportverein-db-password

### Profile

- **dev:** Entwicklungsprofil mit aktivierten Testdaten-Endpoints
- **prod:** Produktionsprofil (Testdaten-Endpoints deaktiviert)

Aktuelles Profil kann in `application.properties` geändert werden:
```properties
spring.profiles.active=dev
```

## Lizenz

Dieses Projekt ist ein Schulprojekt für das LF12-Modul am OSZ IMT.