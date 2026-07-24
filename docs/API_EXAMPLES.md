# API Examples

This document contains detailed request and response examples for the Internship
Application Tracker API. All companies, contacts and email addresses are
fictional.

## 1. Base URL

The local base URL is:

```text
http://localhost:8080
```

All application endpoints start with:

```text
/api/applications
```

## 2. Create Application

```bash
curl -i -X POST 'http://localhost:8080/api/applications' \
  -H 'Content-Type: application/json' \
  --data '{
    "companyName": "Example Tech",
    "positionTitle": "Backend Engineering Intern",
    "status": "APPLIED",
    "workMode": "REMOTE",
    "location": "Remote",
    "applicationDate": "2026-07-24",
    "followUpDate": "2026-07-31",
    "contactName": "Test Contact",
    "contactEmail": "test@example.com",
    "jobUrl": "https://example.com/jobs/backend-intern",
    "notes": "Application submitted through the careers page."
  }'
```

Expected status: `201 Created`

The response includes `Location: /api/applications/1` and a body such as:

```json
{
  "id": 1,
  "companyName": "Example Tech",
  "positionTitle": "Backend Engineering Intern",
  "status": "APPLIED",
  "workMode": "REMOTE",
  "location": "Remote",
  "applicationDate": "2026-07-24",
  "followUpDate": "2026-07-31",
  "contactName": "Test Contact",
  "contactEmail": "test@example.com",
  "jobUrl": "https://example.com/jobs/backend-intern",
  "notes": "Application submitted through the careers page.",
  "createdAt": "2026-07-24T12:00:00",
  "updatedAt": "2026-07-24T12:00:00"
}
```

## 3. List Applications

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications'
```

Expected status: `200 OK`

```json
{
  "content": [
    {
      "id": 1,
      "companyName": "Example Tech",
      "positionTitle": "Backend Engineering Intern",
      "status": "APPLIED",
      "workMode": "REMOTE",
      "location": "Remote",
      "applicationDate": "2026-07-24",
      "followUpDate": "2026-07-31",
      "contactName": "Test Contact",
      "contactEmail": "test@example.com",
      "jobUrl": "https://example.com/jobs/backend-intern",
      "notes": "Application submitted through the careers page.",
      "createdAt": "2026-07-24T12:00:00",
      "updatedAt": "2026-07-24T12:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

## 4. Pagination

Page numbers are zero-based, so `page=0` requests the first page.

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?page=0&size=5'
```

Expected status: `200 OK`

```json
{
  "content": [],
  "page": 0,
  "size": 5,
  "totalElements": 0,
  "totalPages": 0,
  "numberOfElements": 0,
  "first": true,
  "last": true,
  "empty": true
}
```

Valid page sizes are between 1 and 100.

## 5. Filtering by Status

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?status=HR_INTERVIEW'
```

Expected status: `200 OK`

```json
{
  "content": [
    {
      "id": 2,
      "companyName": "Demo Software",
      "positionTitle": "Java Intern",
      "status": "HR_INTERVIEW",
      "workMode": "HYBRID",
      "location": "Demo City",
      "applicationDate": "2026-07-20",
      "followUpDate": "2026-07-28",
      "contactName": "Demo Contact",
      "contactEmail": "test@example.com",
      "jobUrl": "https://example.com/jobs/java-intern",
      "notes": "HR interview scheduled.",
      "createdAt": "2026-07-20T10:00:00",
      "updatedAt": "2026-07-22T09:30:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

Supported status values are `PLANNED`, `APPLIED`, `HR_INTERVIEW`,
`TECHNICAL_INTERVIEW`, `OFFER`, `REJECTED` and `WITHDRAWN`.

## 6. Filtering by Work Mode

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?workMode=ON_SITE'
```

Expected status: `200 OK`

```json
{
  "content": [
    {
      "id": 3,
      "companyName": "Sample Bank",
      "positionTitle": "Software Engineering Intern",
      "status": "PLANNED",
      "workMode": "ON_SITE",
      "location": "Sample City",
      "applicationDate": null,
      "followUpDate": null,
      "contactName": null,
      "contactEmail": null,
      "jobUrl": "https://example.com/jobs/software-intern",
      "notes": "Prepare the application.",
      "createdAt": "2026-07-24T11:00:00",
      "updatedAt": "2026-07-24T11:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

Supported work modes are `ON_SITE`, `HYBRID` and `REMOTE`.

## 7. Text Search

Search is case-insensitive and checks both company and position names.

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?search=example'
```

Expected status: `200 OK`

```json
{
  "content": [
    {
      "id": 1,
      "companyName": "Example Tech",
      "positionTitle": "Backend Engineering Intern",
      "status": "APPLIED",
      "workMode": "REMOTE",
      "location": "Remote",
      "applicationDate": "2026-07-24",
      "followUpDate": "2026-07-31",
      "contactName": "Test Contact",
      "contactEmail": "test@example.com",
      "jobUrl": "https://example.com/jobs/backend-intern",
      "notes": "Application submitted through the careers page.",
      "createdAt": "2026-07-24T12:00:00",
      "updatedAt": "2026-07-24T12:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

## 8. Combining Filters

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?status=APPLIED&workMode=REMOTE&search=Backend&page=0&size=10'
```

Expected status: `200 OK`

```json
{
  "content": [
    {
      "id": 1,
      "companyName": "Example Tech",
      "positionTitle": "Backend Engineering Intern",
      "status": "APPLIED",
      "workMode": "REMOTE",
      "location": "Remote",
      "applicationDate": "2026-07-24",
      "followUpDate": "2026-07-31",
      "contactName": "Test Contact",
      "contactEmail": "test@example.com",
      "jobUrl": "https://example.com/jobs/backend-intern",
      "notes": "Application submitted through the careers page.",
      "createdAt": "2026-07-24T12:00:00",
      "updatedAt": "2026-07-24T12:00:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "numberOfElements": 1,
  "first": true,
  "last": true,
  "empty": false
}
```

## 9. Safe Sorting

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications?sortBy=createdAt&direction=desc'
```

Expected status: `200 OK`

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0,
  "numberOfElements": 0,
  "first": true,
  "last": true,
  "empty": true
}
```

Example allow-listed sort fields are `companyName`, `positionTitle`,
`createdAt`, `applicationDate` and `status`. Direction must be `asc` or `desc`.

## 10. Get by ID

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications/1'
```

Expected status: `200 OK`

```json
{
  "id": 1,
  "companyName": "Example Tech",
  "positionTitle": "Backend Engineering Intern",
  "status": "APPLIED",
  "workMode": "REMOTE",
  "location": "Remote",
  "applicationDate": "2026-07-24",
  "followUpDate": "2026-07-31",
  "contactName": "Test Contact",
  "contactEmail": "test@example.com",
  "jobUrl": "https://example.com/jobs/backend-intern",
  "notes": "Application submitted through the careers page.",
  "createdAt": "2026-07-24T12:00:00",
  "updatedAt": "2026-07-24T12:00:00"
}
```

## 11. Update Application

`PUT` replaces all editable fields, so send every value that should remain on
the record.

```bash
curl -X PUT 'http://localhost:8080/api/applications/1' \
  -H 'Content-Type: application/json' \
  --data '{
    "companyName": "Example Tech",
    "positionTitle": "Backend Engineering Intern",
    "status": "TECHNICAL_INTERVIEW",
    "workMode": "REMOTE",
    "location": "Remote",
    "applicationDate": "2026-07-24",
    "followUpDate": "2026-08-02",
    "contactName": "Test Contact",
    "contactEmail": "test@example.com",
    "jobUrl": "https://example.com/jobs/backend-intern",
    "notes": "Technical interview scheduled."
  }'
```

Expected status: `200 OK`

```json
{
  "id": 1,
  "companyName": "Example Tech",
  "positionTitle": "Backend Engineering Intern",
  "status": "TECHNICAL_INTERVIEW",
  "workMode": "REMOTE",
  "location": "Remote",
  "applicationDate": "2026-07-24",
  "followUpDate": "2026-08-02",
  "contactName": "Test Contact",
  "contactEmail": "test@example.com",
  "jobUrl": "https://example.com/jobs/backend-intern",
  "notes": "Technical interview scheduled.",
  "createdAt": "2026-07-24T12:00:00",
  "updatedAt": "2026-07-24T13:00:00"
}
```

## 12. Delete Application

```bash
curl -i -X DELETE 'http://localhost:8080/api/applications/1' \
  -H 'Content-Type: application/json'
```

Expected status: `204 No Content`

The successful response has no JSON body.

## 13. Validation Error

```bash
curl -X POST 'http://localhost:8080/api/applications' \
  -H 'Content-Type: application/json' \
  --data '{
    "companyName": "",
    "positionTitle": "",
    "contactEmail": "invalid-email"
  }'
```

Expected status: `400 Bad Request`

```json
{
  "timestamp": "2026-07-24T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/applications",
  "fieldErrors": {
    "companyName": "Company name is required",
    "positionTitle": "Position title is required",
    "contactEmail": "Contact email must be valid"
  }
}
```

## 14. Not Found Error

```bash
curl -H 'Content-Type: application/json' \
  'http://localhost:8080/api/applications/999'
```

Expected status: `404 Not Found`

```json
{
  "timestamp": "2026-07-24T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Internship application not found with id: 999",
  "path": "/api/applications/999",
  "fieldErrors": {}
}
```

## 15. Malformed JSON Error

```bash
curl -X POST 'http://localhost:8080/api/applications' \
  -H 'Content-Type: application/json' \
  --data '{"companyName":"Example Tech","positionTitle":"Backend Intern"'
```

Expected status: `400 Bad Request`

```json
{
  "timestamp": "2026-07-24T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Malformed or unreadable request body",
  "path": "/api/applications",
  "fieldErrors": {}
}
```
