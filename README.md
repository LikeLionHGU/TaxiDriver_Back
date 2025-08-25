# 🐟 어울림(Oullim) — 수산물 온라인 경매 플랫폼 (Backend) ⚡

> “투명하고 신속한 온라인 경매, **어울림**에서 시작하세요.”  
어민, 중도매인, 위판장을 하나의 플랫폼으로 연결하고 **CNN 기반 AI 품질검사**로 신뢰를 더합니다.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-red?logo=openjdk)
![Gradle](https://img.shields.io/badge/Gradle-8.14-02303A?logo=gradle)
![MySQL](https://img.shields.io/badge/DB-MySQL-4479A1?logo=mysql)
![AWS](https://img.shields.io/badge/Storage-S3-FF9900?logo=amazons3)

---

## 🌊 서비스 소개

**어울림**은 전통적인 오프라인 수산물 경매의 한계를 해결하기 위해 만들어진  
**디지털 기반 온라인 경매 플랫폼**입니다.  

- 🎣 **어민/생산자** → 어획물 등록 & 최저수락가 제시  
- 💰 **중도매인** → 온라인 단일가 입찰, 최고가 자동 낙찰  
- 🛠 **관리자** → CNN 품질검사 + 승인/정산 관리  

---

## ✨ 주요 기능

### 👨‍👩‍👧‍👦 사용자별
- **어민**: 다중 이미지 업로드, 어획·포장 정보 등록, 최저수락가 입력  
- **중도매인**: 경매당 1회 단일가 입찰 → 최고가 낙찰  
- **관리자**: CNN 기반 품질검사, 승인/거부, 거래/통계 데이터 관리  

### 📦 공통
- 📊 거래내역 Excel 다운로드  
- 🔐 JWT 인증 + RBAC (역할별 권한 제어)  
- 🖼 이미지 S3 업로드  
- 🗂 OpenAPI(Swagger) 문서 자동 제공  

---

## 🛠️ 기술 스택

- **Framework**: Spring Boot 3.5.4 
- **Language**: Java 17+  
- **Database**: MySQL 
- **Storage**: AWS S3  
- **AI 모델**: CNN (품질검사)  
- **Auth**: JWT, CSRF 토큰  

---

## 🚀 시작하기

### 1) 프로젝트 클론
```bash
git clone https://github.com/LikeLionHGU/TaxiDriver_Back.git
cd TaxiDriver_Back
```

### 2) 환경 설정 (application.properties)
```
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/likelion
spring.datasource.username=oulrim
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret.key=your_jwt_secret_key

# S3
s3.bucket=fish-back
s3.region=ap-northeast-2
s3.access-key=****
s3.secret-key=****
```
### 3) 빌드 및 실행
```
./gradlew build
java -jar fish-0.0.1-SNAPSHOT.jar 
```

