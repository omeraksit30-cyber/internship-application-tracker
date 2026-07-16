# Veri Modeli Taslağı

Bu belge uygulamanın başlangıç veri modelini açıklar. Model henüz uygulanmamıştır;
JPA ilişkileri geliştirilirken gereksiz karmaşıklıktan kaçınmak için aşamalı olarak
oluşturulacaktır.

## İlişki özeti

```text
Company  1 ─── N Position
Company  1 ─── N Contact
Position 1 ─── N Application
Contact  1 ─── N Application (isteğe bağlı primary contact ilişkisi)
```

- Bir şirketin birden fazla pozisyonu olabilir.
- Bir şirketin birden fazla iletişim kişisi olabilir.
- Her pozisyon bir şirkete aittir.
- Her başvuru bir pozisyona aittir.
- Bir başvurunun birincil iletişim kişisi olmayabilir.
- Şirket bilgisi Application içinde tekrar tutulmaz; Position üzerinden bulunur.

Bu son karar aynı şirket bilgisinin birden fazla yerde tutulmasını önler.

## Company

Başvuru yapılabilecek şirketi temsil eder.

| Alan | Planlanan Java tipi | Zorunlu | Açıklama |
|---|---|---:|---|
| `id` | `Long` | Evet | Veritabanının üreteceği benzersiz kimlik |
| `name` | `String` | Evet | Şirket adı |
| `website` | `String` | Hayır | Şirket web sitesi |
| `location` | `String` | Hayır | Şirketin veya ofisin konumu |
| `notes` | `String` | Hayır | Serbest açıklama |
| `createdAt` | `LocalDateTime` | Evet | Kaydın oluşturulma zamanı |
| `updatedAt` | `LocalDateTime` | Evet | Son güncellenme zamanı |

Başlangıç kuralları:

- `name` boş veya yalnızca boşluklardan oluşamaz.
- `website` verilirse geçerli bir URL olmalıdır.
- Şirket adının benzersiz olup olmayacağı henüz kesinleşmemiştir.

## Position

Bir şirketteki staj veya iş ilanını temsil eder.

| Alan | Planlanan Java tipi | Zorunlu | Açıklama |
|---|---|---:|---|
| `id` | `Long` | Evet | Benzersiz kimlik |
| `title` | `String` | Evet | Pozisyon adı |
| `department` | `String` | Hayır | Departman |
| `location` | `String` | Hayır | Pozisyonun konumu veya çalışma biçimi |
| `postingUrl` | `String` | Hayır | İlan bağlantısı |
| `company` | `Company` | Evet | Pozisyonun ait olduğu şirket |
| `createdAt` | `LocalDateTime` | Evet | Kaydın oluşturulma zamanı |
| `updatedAt` | `LocalDateTime` | Evet | Son güncellenme zamanı |

Başlangıç kuralları:

- `title` boş olamaz.
- `postingUrl` verilirse geçerli bir URL olmalıdır.
- Var olmayan bir şirkete pozisyon eklenemez.

## Contact

Şirkette iletişim kurulan kişiyi temsil eder.

| Alan | Planlanan Java tipi | Zorunlu | Açıklama |
|---|---|---:|---|
| `id` | `Long` | Evet | Benzersiz kimlik |
| `firstName` | `String` | Evet | Kişinin adı |
| `lastName` | `String` | Hayır | Kişinin soyadı |
| `email` | `String` | Hayır | E-posta adresi |
| `phone` | `String` | Hayır | Telefon numarası |
| `jobTitle` | `String` | Hayır | Şirketteki görevi |
| `company` | `Company` | Evet | Çalıştığı veya temsil ettiği şirket |
| `createdAt` | `LocalDateTime` | Evet | Kaydın oluşturulma zamanı |
| `updatedAt` | `LocalDateTime` | Evet | Son güncellenme zamanı |

Başlangıç kuralları:

- `firstName` boş olamaz.
- `email` verilirse geçerli bir e-posta adresi olmalıdır.
- Var olmayan bir şirkete iletişim kişisi eklenemez.

## Application

Bir pozisyona yapılan staj başvurusunu temsil eder.

| Alan | Planlanan Java tipi | Zorunlu | Açıklama |
|---|---|---:|---|
| `id` | `Long` | Evet | Benzersiz kimlik |
| `position` | `Position` | Evet | Başvuru yapılan pozisyon |
| `primaryContact` | `Contact` | Hayır | Başvuruyla ilgili birincil iletişim kişisi |
| `status` | `ApplicationStatus` | Evet | Başvurunun mevcut durumu |
| `applicationDate` | `LocalDate` | Duruma bağlı | Başvurunun gönderildiği tarih |
| `followUpDate` | `LocalDate` | Hayır | Sonraki takip tarihi |
| `notes` | `String` | Hayır | Başvuruyla ilgili notlar |
| `createdAt` | `LocalDateTime` | Evet | Kaydın oluşturulma zamanı |
| `updatedAt` | `LocalDateTime` | Evet | Son güncellenme zamanı |

Başlangıç kuralları:

- Var olmayan bir pozisyona başvuru oluşturulamaz.
- `primaryContact` verilirse bu kişi pozisyonun şirketine ait olmalıdır.
- `followUpDate`, `applicationDate` değerinden önce olamaz.
- `PLANNED` dışındaki durumlarda `applicationDate` gerekebilir; kesin kural
  Application aşamasında belirlenecektir.

## ApplicationStatus

Başvuru durumları başlangıçta Java `enum` olarak tutulacaktır.

| Değer | Anlamı |
|---|---|
| `PLANNED` | Başvuru yapılması planlanıyor |
| `APPLIED` | Başvuru gönderildi |
| `INTERVIEW` | Görüşme sürecine geçildi |
| `OFFER` | Teklif alındı |
| `REJECTED` | Başvuru reddedildi |
| `WITHDRAWN` | Başvuru aday tarafından geri çekildi |

Enum kullanılması, serbest metin nedeniyle oluşabilecek `applied`, `Applied` ve
`APLIED` gibi tutarsız değerleri önler.

## JPA ilişki yaklaşımı

İlk uygulamada ilişkiler mümkün olduğunca sade tutulacaktır:

- `Position`, `Company` için `@ManyToOne` ilişkisi taşıyacaktır.
- `Contact`, `Company` için `@ManyToOne` ilişkisi taşıyacaktır.
- `Application`, `Position` için `@ManyToOne` ilişkisi taşıyacaktır.
- `Application`, isteğe bağlı `Contact` için `@ManyToOne` ilişkisi taşıyabilir.
- Parent entity'lere hemen koleksiyon eklenmeyerek çift yönlü JSON döngülerinden
  kaçınılacaktır.
- API giriş ve çıkışlarında entity yerine request/response DTO'ları kullanılması
  planlanmaktadır.

## Silme davranışları

Silme kuralları henüz kesinleşmemiştir. Başlangıç için en güvenli yaklaşım:

- Pozisyonu bulunan bir şirketin silinmesini engellemek
- İletişim kişisi bulunan bir şirketin silinmesini engellemek
- Başvurusu bulunan bir pozisyonun silinmesini engellemek
- Silme sırasında otomatik zincirleme silme (`cascade remove`) kullanmamak

Bu yaklaşım yanlışlıkla ilişkili verilerin silinmesini önler. Gereksinimler
netleştiğinde soft delete veya kontrollü cascade seçenekleri ayrıca değerlendirilebilir.

## Gelecekte değerlendirilebilecek modeller

İlk sürümü gereksiz yere büyütmemek için aşağıdaki modeller başlangıç kapsamına
dahil değildir:

- Birden fazla takip işlemi için `FollowUp`
- Görüşme turları için `Interview`
- Durum değişikliği geçmişi için `ApplicationStatusHistory`
- Kullanıcı hesapları için `User`

Bu modeller yalnızca temel sürüm tamamlandıktan ve gerçek bir ihtiyaç ortaya
çıktıktan sonra değerlendirilmelidir.
