# API Taslağı

Bu belge planlanan REST API sözleşmesinin ilk taslağıdır. Endpointler henüz
uygulanmamıştır; geliştirme sırasında öğrenilen gereksinimlere göre kontrollü
biçimde değiştirilebilir.

## Genel kurallar

- Temel yol: `/api`
- Veri biçimi: JSON
- Tarih biçimi: ISO 8601 `YYYY-MM-DD` (örnek: `2026-07-16`)
- Kaynak kimlikleri: Pozitif tam sayı (`Long`)
- İstek ve yanıt alanları: `camelCase`

## HTTP durum kodları

| Kod | Kullanım |
|---|---|
| `200 OK` | Başarılı okuma veya güncelleme |
| `201 Created` | Yeni kayıt başarıyla oluşturuldu |
| `204 No Content` | Kayıt başarıyla silindi |
| `400 Bad Request` | İstek veya alan doğrulaması geçersiz |
| `404 Not Found` | İstenen kaynak bulunamadı |
| `409 Conflict` | İstek mevcut verilerle veya iş kurallarıyla çelişiyor |
| `500 Internal Server Error` | Beklenmeyen sunucu hatası |

## Company endpointleri

| Metot | Yol | Amaç |
|---|---|---|
| `GET` | `/api/companies` | Tüm şirketleri listele |
| `GET` | `/api/companies/{id}` | Bir şirketi görüntüle |
| `POST` | `/api/companies` | Yeni şirket oluştur |
| `PUT` | `/api/companies/{id}` | Bir şirketi tamamen güncelle |
| `DELETE` | `/api/companies/{id}` | Bir şirketi sil |

Örnek oluşturma isteği:

```json
{
  "name": "Example Technology",
  "website": "https://example.com",
  "location": "Istanbul",
  "notes": "Backend internship opportunities"
}
```

Örnek `201 Created` yanıtı:

```json
{
  "id": 1,
  "name": "Example Technology",
  "website": "https://example.com",
  "location": "Istanbul",
  "notes": "Backend internship opportunities"
}
```

## Position endpointleri

| Metot | Yol | Amaç |
|---|---|---|
| `GET` | `/api/positions` | Tüm pozisyonları listele |
| `GET` | `/api/positions/{id}` | Bir pozisyonu görüntüle |
| `GET` | `/api/companies/{companyId}/positions` | Şirketin pozisyonlarını listele |
| `POST` | `/api/companies/{companyId}/positions` | Şirkete pozisyon ekle |
| `PUT` | `/api/positions/{id}` | Pozisyonu güncelle |
| `DELETE` | `/api/positions/{id}` | Pozisyonu sil |

Örnek oluşturma isteği:

```json
{
  "title": "Backend Developer Intern",
  "department": "Engineering",
  "location": "Istanbul / Hybrid",
  "postingUrl": "https://example.com/jobs/backend-intern"
}
```

`companyId`, iç içe endpointin URL'sinden alınır; istek gövdesinde tekrar
gönderilmez.

## Contact endpointleri

| Metot | Yol | Amaç |
|---|---|---|
| `GET` | `/api/contacts` | Tüm iletişim kişilerini listele |
| `GET` | `/api/contacts/{id}` | Bir iletişim kişisini görüntüle |
| `GET` | `/api/companies/{companyId}/contacts` | Şirketin iletişim kişilerini listele |
| `POST` | `/api/companies/{companyId}/contacts` | Şirkete iletişim kişisi ekle |
| `PUT` | `/api/contacts/{id}` | İletişim kişisini güncelle |
| `DELETE` | `/api/contacts/{id}` | İletişim kişisini sil |

Örnek oluşturma isteği:

```json
{
  "firstName": "Ada",
  "lastName": "Yilmaz",
  "email": "ada.yilmaz@example.com",
  "phone": "+90 555 000 00 00",
  "jobTitle": "University Recruiter"
}
```

## Application endpointleri

| Metot | Yol | Amaç |
|---|---|---|
| `GET` | `/api/applications` | Başvuruları listele ve filtrele |
| `GET` | `/api/applications/{id}` | Bir başvuruyu görüntüle |
| `POST` | `/api/applications` | Yeni başvuru oluştur |
| `PUT` | `/api/applications/{id}` | Başvuruyu tamamen güncelle |
| `PATCH` | `/api/applications/{id}/status` | Yalnızca başvuru durumunu güncelle |
| `DELETE` | `/api/applications/{id}` | Başvuruyu sil |

Örnek oluşturma isteği:

```json
{
  "positionId": 10,
  "primaryContactId": 5,
  "status": "APPLIED",
  "applicationDate": "2026-07-16",
  "followUpDate": "2026-07-23",
  "notes": "Application submitted through the company website."
}
```

`primaryContactId`, `followUpDate` ve `notes` isteğe bağlıdır.

Örnek durum güncelleme isteği:

```json
{
  "status": "INTERVIEW"
}
```

Desteklenen durumlar:

```text
PLANNED
APPLIED
INTERVIEW
OFFER
REJECTED
WITHDRAWN
```

## Application filtreleri

Filtreler query parametresi olarak gönderilir:

| Parametre | Örnek | Amaç |
|---|---|---|
| `status` | `?status=APPLIED` | Duruma göre filtrele |
| `companyId` | `?companyId=1` | Şirkete göre filtrele |
| `followUpBefore` | `?followUpBefore=2026-08-01` | Verilen tarihe kadar takip gerekenleri bul |

Örnek birleşik kullanım:

```http
GET /api/applications?status=APPLIED&companyId=1
```

Filtrelerin birlikte nasıl uygulanacağı 8. geliştirme aşamasında kesinleştirilecektir.

## Hata yanıtı taslağı

Tüm hataların benzer bir yapıda dönmesi planlanmaktadır:

```json
{
  "timestamp": "2026-07-16T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Company with id 99 was not found",
  "path": "/api/companies/99"
}
```

Validation hatalarında alan bazlı ayrıntı eklenmesi planlanmaktadır:

```json
{
  "timestamp": "2026-07-16T14:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Request validation failed",
  "path": "/api/companies",
  "fieldErrors": {
    "name": "must not be blank",
    "website": "must be a valid URL"
  }
}
```

## Henüz kesinleşmeyen kararlar

- Liste endpointlerine pagination eklenip eklenmeyeceği
- Aynı şirkete veya pozisyona tekrar başvurunun engellenip engellenmeyeceği
- Durumlar arasında zorunlu geçiş kuralları olup olmayacağı
- Bağlı kayıtları olan şirketlerin ve pozisyonların nasıl silineceği
- API yanıtlarının yalnızca İngilizce mi olacağı
