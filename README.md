# Internship Application Tracker

Internship Application Tracker; staj arama sürecindeki şirketleri, pozisyonları,
iletişim kişilerini, başvuruları ve takip tarihlerini yönetmek için geliştirilecek
bir backend REST API projesidir.

## Projenin amacı

Bu proje, Java ve Spring Boot kullanarak gerçekçi bir backend uygulaması
geliştirme pratiği yapmak amacıyla hazırlanmıştır. Proje küçük ve anlaşılır
aşamalar hâlinde ilerletilecek; her aşamada kullanılan kavramların nedenleri
öğrenilecektir.

## Planlanan özellikler

- Şirketleri oluşturma, listeleme, güncelleme ve silme
- Şirketlere ait staj pozisyonlarını yönetme
- Şirketlerdeki iletişim kişilerini kaydetme
- Staj başvurusu oluşturma ve güncelleme
- Başvuru durumlarını takip etme
- Takip tarihlerini görüntüleme
- Başvuruları durum, şirket ve takip tarihine göre filtreleme
- Gelen verileri doğrulama
- Tutarlı API hata yanıtları üretme
- Unit ve integration testleri yazma

## Planlanan teknolojiler

- Java 21
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- JUnit
- Git

Spring Boot'un kesin sürümü, proje kurulurken Java 21 uyumluluğu doğrulanarak
seçilecektir.

## Planlanan veri modeli

- `Company`: Başvuru yapılan şirket
- `Position`: Şirketteki staj pozisyonu
- `Contact`: Şirkette iletişim kurulan kişi
- `Application`: Yapılan staj başvurusu
- `ApplicationStatus`: Başvurunun mevcut durumu

Veri modelinin alanları ve ilişkileri [docs/DATA_MODEL.md](docs/DATA_MODEL.md)
dosyasında açıklanmıştır.

## API taslağı

Planlanan endpointler, örnek istekler ve HTTP yanıtları
[docs/API.md](docs/API.md) dosyasında yer almaktadır.

## Proje durumu

Proje şu anda planlama ve dokümantasyon aşamasındadır. Henüz Spring Boot kaynak
kodu veya Maven yapılandırması oluşturulmamıştır.

Geliştirme aşamaları [docs/ROADMAP.md](docs/ROADMAP.md) dosyasından takip
edilecektir.

## Başlangıç kapsamı

- Uygulama ilk sürümde tek kullanıcı için çalışacaktır.
- Kullanıcı hesabı ve kimlik doğrulama ilk sürümün kapsamında değildir.
- API, JSON istek ve yanıtları kullanacaktır.
- H2 veritabanı öğrenme ve yerel geliştirme amacıyla kullanılacaktır.
- İlk sürümde her başvuru için tek bir takip tarihi tutulacaktır.

## Lisans

Bu proje [MIT Lisansı](LICENSE) altında yayımlanmaktadır.
