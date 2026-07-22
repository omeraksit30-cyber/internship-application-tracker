# Internship Application Tracker

Internship Application Tracker, staj başvurularını oluşturmak; durum, çalışma
modeli ve şirket/pozisyon adına göre takip etmek için geliştirilen bir Spring
Boot REST API projesidir. Proje, Java ve backend geliştirme konularını pratik
yaparak öğrenmek amacıyla küçük ve anlaşılır aşamalarla geliştirilmektedir.

## Features

- Staj başvuruları için CRUD endpointleri
- Jakarta Validation ile request doğrulama
- Pagination ve güvenli sorting
- Durum ve çalışma modeline göre filtreleme
- Şirket ve pozisyon adında metin arama
- Standart API hata cevapları
- Swagger UI ve OpenAPI dokümantasyonu
- Runtime ortamında PostgreSQL, otomatik testlerde H2

## Technologies

- Java 21
- Spring Boot 4.1.0
- Maven Wrapper
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- PostgreSQL
- H2 Database
- JUnit
- Docker ve Docker Compose

## Prerequisites

Projeyi yerel olarak kullanmak için aşağıdaki araçlar gereklidir:

- Java 21
- Docker
- Docker Compose

Maven'i ayrıca kurmak gerekmez; repository içindeki Maven Wrapper kullanılır.

## Running tests locally

```bash
./mvnw test
```

Otomatik testler bellek içinde çalışan H2 veritabanını kullanır. Testleri
çalıştırmak için Docker, PostgreSQL veya `.env` dosyası gerekmez.

## Running with PostgreSQL and Docker

Örnek environment dosyasını yerel `.env` dosyasına kopyalayın:

```bash
cp .env.example .env
```

Ardından `.env` içindeki `POSTGRES_PASSWORD` değerini yalnızca yerel
geliştirmede kullanacağınız güçlü bir parola ile değiştirin. `.env` dosyasını
Git'e eklemeyin.

PostgreSQL ve uygulama container'larını başlatın:

```bash
docker compose up --build -d
```

## Checking containers

```bash
docker compose ps
docker compose logs -f app
docker compose logs -f postgres
```

`logs -f` komutu canlı log akışını açar. Çıkmak için `Ctrl+C` kullanın.

## Application URLs

- API: http://localhost:8080/api/applications
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Stopping containers

Container'ları durdurup Compose kaynaklarını kaldırmak için:

```bash
docker compose down
```

Bu komut named volume'u silmez; PostgreSQL verileri korunur.

## Deleting local database data

```bash
docker compose down -v
```

Bu komut `postgres_data` named volume'unu ve içindeki bütün yerel veritabanı
verilerini kalıcı olarak siler. Verileri korumak istiyorsanız `-v` kullanmayın.

## Environment variables

| Variable | Purpose |
| --- | --- |
| `POSTGRES_DB` | PostgreSQL içinde oluşturulacak veritabanının adı |
| `POSTGRES_USER` | PostgreSQL kullanıcı adı |
| `POSTGRES_PASSWORD` | Yerel PostgreSQL parolası; `.env` içinde tutulur |
| `POSTGRES_PORT` | PostgreSQL'in host bilgisayarında açılacağı port |
| `DB_URL` | Spring Boot uygulamasının JDBC bağlantı adresi |
| `DB_USERNAME` | Uygulamanın veritabanı kullanıcı adı |
| `DB_PASSWORD` | Uygulamanın veritabanı parolası |
| `JPA_DDL_AUTO` | Hibernate şema yönetim davranışı |

Gerçek parolaları veya başka secret değerlerini repository'ye eklemeyin.

## Documentation

- [Development roadmap](docs/ROADMAP.md)
- [API design](docs/API.md)
- [Data model](docs/DATA_MODEL.md)

## License

Bu proje [MIT Lisansı](LICENSE) altında yayımlanmaktadır.
