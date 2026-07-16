# Geliştirme Yol Haritası

Bu yol haritası projeyi küçük, test edilebilir ve öğrenme odaklı aşamalara
ayırır. Bir aşama tamamlanmadan sonraki aşamaya geçilmemesi hedeflenir.

## 1. Planlama ve dokümantasyon

- [x] Projenin amacını ve başlangıç kapsamını belirle
- [x] İlk API endpoint taslağını hazırla
- [x] İlk veri modeli taslağını hazırla
- [x] README ve yol haritasını oluştur

**Öğrenme hedefi:** Kod yazmadan önce kapsam ve teknik kararların nasıl
belgelendiğini görmek.

**Tamamlanma ölçütü:** README, roadmap, API ve veri modeli belgeleri birbiriyle
uyumlu olmalıdır.

## 2. Spring Boot ve Maven kurulumu

- [ ] Java 21 uyumlu Spring Boot sürümünü seç
- [ ] Maven proje yapısını oluştur
- [ ] Gerekli başlangıç bağımlılıklarını ekle
- [ ] Temel package yapısını belirle
- [ ] Boş uygulamanın ve testlerin çalıştığını doğrula

**Öğrenme hedefi:** Maven bağımlılıklarını, standart proje yapısını ve Spring
Boot uygulamasının başlangıç sürecini anlamak.

**Tamamlanma ölçütü:** Uygulama hatasız başlamalı ve başlangıç testi geçmelidir.

## 3. Company CRUD işlemleri

- [ ] `Company` entity'sini oluştur
- [ ] Repository, service ve controller katmanlarını ekle
- [ ] Şirket oluşturma, listeleme, görüntüleme, güncelleme ve silme işlemlerini ekle
- [ ] H2 veritabanında kayıtların saklandığını doğrula

**Öğrenme hedefi:** Controller–Service–Repository akışını ve temel CRUD
işlemlerini tek bir model üzerinde öğrenmek.

**Tamamlanma ölçütü:** Company endpointleri beklenen HTTP kodlarıyla çalışmalıdır.

## 4. Position ve JPA ilişkileri

- [ ] `Position` entity'sini oluştur
- [ ] Position ile Company arasında ilişki kur
- [ ] Şirkete pozisyon ekleme ve pozisyonları listeleme endpointlerini oluştur
- [ ] İlişki davranışlarını test et

**Öğrenme hedefi:** Foreign key, `@ManyToOne` ve entity ilişkilerinin sahibini
anlamak.

**Tamamlanma ölçütü:** Var olan bir şirkete pozisyon eklenebilmeli; olmayan bir
şirket için uygun hata dönmelidir.

## 5. Contact yönetimi

- [ ] `Contact` entity'sini oluştur
- [ ] Contact ile Company arasında ilişki kur
- [ ] Contact CRUD işlemlerini ekle
- [ ] E-posta ve zorunlu alan doğrulamalarını ekle

**Öğrenme hedefi:** Daha önce öğrenilen katmanlı CRUD yapısını yeni bir modele
uygulamak.

**Tamamlanma ölçütü:** İletişim kişileri şirket bazında yönetilebilmelidir.

## 6. Application ve durum yönetimi

- [ ] `Application` entity'sini oluştur
- [ ] Application ile Position arasında ilişki kur
- [ ] İsteğe bağlı birincil iletişim kişisini destekle
- [ ] `ApplicationStatus` enum'unu ekle
- [ ] Başvuru CRUD ve durum güncelleme işlemlerini ekle

**Öğrenme hedefi:** Enum kullanımı, birden fazla modelle çalışma ve iş
kurallarını service katmanında uygulama.

**Tamamlanma ölçütü:** Bir pozisyona başvuru oluşturulabilmeli ve başvuru durumu
kontrollü biçimde güncellenebilmelidir.

## 7. Validation ve merkezi hata yönetimi

- [ ] Request DTO'larına doğrulama kuralları ekle
- [ ] Kayıt bulunamadı hatalarını standartlaştır
- [ ] Validation hata yanıtlarını standartlaştır
- [ ] İş kuralı ihlalleri için uygun HTTP kodlarını belirle

**Öğrenme hedefi:** Bean Validation, exception kullanımı ve tutarlı API hata
yanıtlarını öğrenmek.

**Tamamlanma ölçütü:** Hatalı istekler aynı JSON hata yapısında ve uygun HTTP
durum kodlarıyla dönmelidir.

## 8. Filtreleme ve takip tarihleri

- [ ] Başvuruları duruma göre filtrele
- [ ] Başvuruları şirkete göre filtrele
- [ ] Belirli tarihten önceki takip kayıtlarını listele
- [ ] Boş veya geçersiz query parametrelerini ele al

**Öğrenme hedefi:** Spring Data JPA sorgu metotları, query parametreleri ve
`LocalDate` ile çalışmak.

**Tamamlanma ölçütü:** Desteklenen filtreler tek başına ve birlikte doğru sonuç
döndürmelidir.

## 9. Otomatik testler

- [ ] Service sınıfları için JUnit ve Mockito testleri yaz
- [ ] Repository sorgularını H2 ile test et
- [ ] Controller endpointlerini MockMvc ile test et
- [ ] Başarılı ve hatalı senaryoları kapsa

**Öğrenme hedefi:** Unit, repository ve web/integration testlerinin farkını
anlamak.

**Tamamlanma ölçütü:** Temel iş kuralları ve endpoint senaryoları otomatik
testlerle doğrulanmalıdır.

## 10. Son düzenleme ve portföy hazırlığı

- [ ] README'ye kurulum ve çalıştırma talimatlarını ekle
- [ ] Gerçek uygulamayla uyumlu örnek JSON istekleri ve yanıtları ekle
- [ ] API ve veri modeli belgelerini son kodla karşılaştır
- [ ] Kod temizliği ve son testleri tamamla
- [ ] Portföyde anlatılacak teknik kararları belgeleyerek sürüm oluştur

**Öğrenme hedefi:** Bir projeyi başka geliştiricilerin kurup inceleyebileceği
şekilde sunmak.

**Tamamlanma ölçütü:** Yeni bir geliştirici README'yi izleyerek projeyi
çalıştırabilmeli ve API'yi deneyebilmelidir.
