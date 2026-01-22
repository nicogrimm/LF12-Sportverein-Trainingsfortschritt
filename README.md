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

### Athleten

#### Alle Athleten abrufen
```http
GET /api/athletes
```

**Antwort:** `200 OK`
```json
[
  {
    "athleteId": 1,
    "firstname": "Max",
    "name": "Mustermann"
  }
]
```

#### Einen Athleten abrufen
```http
GET /api/athletes/{id}
```

**Antwort:** `200 OK` oder `404 Not Found`
```json
{
  "athleteId": 1,
  "firstname": "Max",
  "name": "Mustermann"
}
```

#### Athleten erstellen
```http
POST /api/athletes
Content-Type: application/json

{
  "firstname": "Max",
  "name": "Mustermann"
}
```

**Antwort:** `201 Created`
```json
{
  "athleteId": 1,
  "firstname": "Max",
  "name": "Mustermann"
}
```

#### Athleten aktualisieren
```http
PUT /api/athletes/{id}
Content-Type: application/json

{
  "firstname": "Maximilian",
  "name": "Mustermann"
}
```

**Antwort:** `200 OK` oder `404 Not Found`

#### Athleten löschen
```http
DELETE /api/athletes/{id}
```

**Antwort:** `204 No Content` oder `404 Not Found`

---

### Sportarten

#### Alle Sportarten abrufen
```http
GET /api/sports
```

**Antwort:** `200 OK`
```json
[
  {
    "sportId": 1,
    "name": "Laufen",
    "unit": "km"
  }
]
```

#### Eine Sportart abrufen
```http
GET /api/sports/{id}
```

**Antwort:** `200 OK` oder `404 Not Found`
```json
{
  "sportId": 1,
  "name": "Laufen",
  "unit": "km"
}
```

#### Sportart erstellen
```http
POST /api/sports
Content-Type: application/json

{
  "name": "Laufen",
  "unit": "km"
}
```

**Antwort:** `201 Created`
```json
{
  "sportId": 1,
  "name": "Laufen",
  "unit": "km"
}
```

#### Sportart aktualisieren
```http
PUT /api/sports/{id}
Content-Type: application/json

{
  "name": "Joggen",
  "unit": "km"
}
```

**Antwort:** `200 OK` oder `404 Not Found`

#### Sportart löschen
```http
DELETE /api/sports/{id}
```

**Antwort:** `204 No Content` oder `404 Not Found`

---

### Trainings

#### Alle Trainings abrufen
```http
GET /api/trainings
```

**Antwort:** `200 OK`
```json
[
  {
    "trainingId": 1,
    "athleteId": 1,
    "sportId": 1,
    "date": "2026-01-15T08:00:00+01:00",
    "metric": 5.5
  }
]
```

#### Alle Trainings eines Athleten abrufen
```http
GET /api/trainings/athlete/{athleteId}
```

**Antwort:** `200 OK`
```json
[
  {
    "trainingId": 1,
    "athleteId": 1,
    "sportId": 1,
    "date": "2026-01-15T08:00:00+01:00",
    "metric": 5.5
  }
]
```

#### Ein bestimmtes Training eines Athleten abrufen
```http
GET /api/trainings/athlete/{athleteId}/training/{trainingId}
```

**Antwort:** `200 OK` oder `404 Not Found`
```json
{
  "trainingId": 1,
  "athleteId": 1,
  "sportId": 1,
  "date": "2026-01-15T08:00:00+01:00",
  "metric": 5.5
}
```

#### Training für einen Athleten erstellen
```http
POST /api/trainings/athlete/{athleteId}
Content-Type: application/json

{
  "sportId": 1,
  "date": "2026-01-22T10:00:00+01:00",
  "metric": 7.5
}
```

**Antwort:** `201 Created` oder `400 Bad Request`
```json
{
  "trainingId": 7,
  "athleteId": 1,
  "sportId": 1,
  "date": "2026-01-22T10:00:00+01:00",
  "metric": 7.5
}
```

#### Training eines Athleten aktualisieren
```http
PUT /api/trainings/athlete/{athleteId}/training/{trainingId}
Content-Type: application/json

{
  "sportId": 1,
  "date": "2026-01-22T11:00:00+01:00",
  "metric": 8.0
}
```

**Antwort:** `200 OK`, `404 Not Found` oder `400 Bad Request`

#### Training eines Athleten löschen
```http
DELETE /api/trainings/athlete/{athleteId}/training/{trainingId}
```

**Antwort:** `204 No Content` oder `404 Not Found`

---

### Testdaten (nur im dev-Profil verfügbar)

#### Testdaten laden
```http
POST /api/test-data/load
```

**Antwort:** `200 OK`
```json
"Test data loaded successfully!"
```

#### Alle Daten löschen
```http
DELETE /api/test-data/clear
```

**Antwort:** `200 OK`
```json
"All data cleared successfully!"
```

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