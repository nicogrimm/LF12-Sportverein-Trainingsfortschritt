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

### Error Handling

Alle Endpoints geben bei Fehlern ein strukturiertes Error-Response-Objekt zurück:

```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Athlet mit ID 999 wurde nicht gefunden",
  "path": "/api/athletes/999"
}
```

**Mögliche HTTP-Status-Codes:**
- `200 OK` - Erfolgreiche Anfrage
- `201 Created` - Ressource erfolgreich erstellt
- `204 No Content` - Erfolgreiche Löschung (kein Body)
- `400 Bad Request` - Ungültige Eingabedaten
- `404 Not Found` - Ressource nicht gefunden
- `500 Internal Server Error` - Serverfehler

---

### Athleten

#### Alle Athleten abrufen
```http
GET /api/athletes
```

**Erfolgreiche Antwort:** `200 OK`
```json
[
  {
    "athleteId": 1,
    "firstname": "Max",
    "name": "Mustermann"
  }
]
```

**Fehler-Antwort:** `500 Internal Server Error`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Fehler beim Abrufen der Athleten: ...",
  "path": "/api/athletes"
}
```

#### Einen Athleten abrufen
```http
GET /api/athletes/{id}
```

**Erfolgreiche Antwort:** `200 OK`
```json
{
  "athleteId": 1,
  "firstname": "Max",
  "name": "Mustermann"
}
```

**Fehler-Antwort:** `404 Not Found`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Athlet mit ID 999 wurde nicht gefunden",
  "path": "/api/athletes/999"
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

**Erfolgreiche Antwort:** `201 Created`
```json
{
  "athleteId": 1,
  "firstname": "Max",
  "name": "Mustermann"
}
```

**Fehler-Antwort (fehlende Daten):** `400 Bad Request`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Der Vorname des Athleten darf nicht leer sein",
  "path": "/api/athletes"
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

**Erfolgreiche Antwort:** `200 OK`
**Fehler-Antwort:** `404 Not Found` oder `400 Bad Request`

#### Athleten löschen
```http
DELETE /api/athletes/{id}
```

**Erfolgreiche Antwort:** `204 No Content`
**Fehler-Antwort:** `404 Not Found`

---

### Sportarten

#### Alle Sportarten abrufen
```http
GET /api/sports
```

**Erfolgreiche Antwort:** `200 OK`
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

**Erfolgreiche Antwort:** `200 OK`
```json
{
  "sportId": 1,
  "name": "Laufen",
  "unit": "km"
}
```

**Fehler-Antwort:** `404 Not Found`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Sportart mit ID 999 wurde nicht gefunden",
  "path": "/api/sports/999"
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

**Erfolgreiche Antwort:** `201 Created`
```json
{
  "sportId": 1,
  "name": "Laufen",
  "unit": "km"
}
```

**Fehler-Antwort (fehlende Daten):** `400 Bad Request`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Der Name der Sportart darf nicht leer sein",
  "path": "/api/sports"
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

**Erfolgreiche Antwort:** `200 OK`
**Fehler-Antwort:** `404 Not Found` oder `400 Bad Request`

#### Sportart löschen
```http
DELETE /api/sports/{id}
```

**Erfolgreiche Antwort:** `204 No Content`
**Fehler-Antwort:** `404 Not Found`

---

### Trainings

#### Alle Trainings abrufen
```http
GET /api/trainings
```

**Erfolgreiche Antwort:** `200 OK`
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

**Erfolgreiche Antwort:** `200 OK`
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

**Erfolgreiche Antwort:** `200 OK`
```json
{
  "trainingId": 1,
  "athleteId": 1,
  "sportId": 1,
  "date": "2026-01-15T08:00:00+01:00",
  "metric": 5.5
}
```

**Fehler-Antwort:** `404 Not Found`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Training mit ID 999 für Athlet 1 wurde nicht gefunden",
  "path": "/api/trainings/athlete/1/training/999"
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

**Erfolgreiche Antwort:** `201 Created`
```json
{
  "trainingId": 7,
  "athleteId": 1,
  "sportId": 1,
  "date": "2026-01-22T10:00:00+01:00",
  "metric": 7.5
}
```

**Fehler-Antwort (ungültige Daten):** `400 Bad Request`
```json
{
  "timestamp": "2026-01-23T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Der Metrikwert muss größer als 0 sein",
  "path": "/api/trainings/athlete/1"
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

**Erfolgreiche Antwort:** `200 OK`
**Fehler-Antwort:** `404 Not Found` oder `400 Bad Request`

#### Training eines Athleten löschen
```http
DELETE /api/trainings/athlete/{athleteId}/training/{trainingId}
```

**Erfolgreiche Antwort:** `204 No Content`
**Fehler-Antwort:** `404 Not Found`

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