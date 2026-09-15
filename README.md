# Lab 9 - Spring Boot Transaction

ระบบฝากเงินอย่างง่ายด้วย Spring Boot + JPA + PostgreSQL

ก่อนรันโปรเจกต์ ให้ตั้งค่า password PostgreSQL ใน environment variable:

```powershell
$env:DB_PASSWORD="รหัสผ่าน PostgreSQL ของคุณ"
mvn spring-boot:run
```

## 1. Database

สร้าง PostgreSQL Database:

```sql
CREATE DATABASE lab9;
```

จากนั้นแก้ `src/main/resources/application.properties`

```properties
spring.datasource.password=รหัสผ่าน PostgreSQL ของคุณ
```

## 2. Run

เปิด Terminal ในโฟลเดอร์โปรเจกต์ แล้วใช้:

```powershell
mvn spring-boot:run
```

ถ้าเครื่องมี Maven Wrapper และไฟล์ wrapper ครบ สามารถใช้:

```powershell
./mvnw spring-boot:run
```

## 3. API

### สร้าง Account

POST

`http://localhost:8080/accounts`

Body -> raw -> JSON

```json
{
  "accountNumber": "1234567890",
  "ownerName": "ใส่ชื่อตัวเอง",
  "balance": 0
}
```

### ดู Account

GET

`http://localhost:8080/accounts/1`

เปลี่ยน `1` เป็น id ที่ได้จากการสร้าง Account

### ฝากเงิน

POST

`http://localhost:8080/accounts/1/deposit`

Body:

```json
{
  "amount": 1000
}
```

## 4. ตรวจสอบประวัติฝากเงิน

ใน pgAdmin:

```sql
SELECT * FROM deposit_transaction;
```

## 5. ทดลอง Rollback

เปิดไฟล์:

`src/main/java/com/example/lab9/service/DepositService.java`

เอา comment ออกจาก:

```java
throw new RuntimeException("Test Rollback");
```

ให้เป็น:

```java
throw new RuntimeException("Test Rollback");
```

โดยต้องอยู่หลัง `depositRepository.save(deposit);`

Restart Spring Boot แล้วลองฝากเงินอีกครั้ง

ควรได้ HTTP 500

จากนั้นตรวจ:

```http
GET http://localhost:8080/accounts/1
```

ยอดเงินต้องไม่เพิ่ม

และตรวจ:

```sql
SELECT * FROM deposit_transaction;
```

จำนวนรายการต้องไม่เพิ่ม

## 6. ทดลองไม่มี @Transactional

หลังทดลองข้อ Rollback แล้ว ให้ลบ/comment:

```java
@Transactional
```

แต่ยังคง `throw new RuntimeException("Test Rollback");` ไว้

Restart แล้วฝากเงินอีกครั้ง

ให้สังเกตว่าข้อมูลที่ save ก่อนเกิด exception อาจยังคงอยู่ในฐานข้อมูล ซึ่งใช้เปรียบเทียบกับกรณีที่มี Transaction ตามใบงาน

## 7. ก่อนส่ง

หลังทำการทดลอง Rollback เสร็จ:

- ลบ/comment `throw new RuntimeException("Test Rollback");`
- ใส่ `@Transactional` กลับคืน
- ทดสอบให้ระบบฝากเงินได้ตามปกติ

อ้างอิงขั้นตอนตาม Lab 9 ที่กำหนดให้ส่ง Source Code และ Word ที่มีภาพการทดลอง พร้อมคำอธิบายผล
