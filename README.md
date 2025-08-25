# 🐟 어울림(Oullim) — 수산물 온라인 경매 플랫폼 (Backend) ⚡

> “투명하고 신속한 온라인 경매, **어울림**에서 시작하세요.”  
어민, 중도매인, 위판장을 하나의 플랫폼으로 연결하고 **CNN 기반 AI 품질검사**로 신뢰를 더합니다.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-red?logo=openjdk)
![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle)
![PostgreSQL](https://img.shields.io/badge/DB-PostgreSQL-4169E1?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
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

- **Framework**: Spring Boot 3.x  
- **Language**: Java 17+  
- **Database**: PostgreSQL (MySQL 호환 가능)  
- **Storage**: AWS S3  
- **AI 모델**: CNN (품질검사)  
- **Auth**: JWT, RBAC, CSRF 토큰  
- **Infra**: Docker, Docker Compose  
- **Docs & Test**: Swagger, JUnit, Spring REST Docs  

---

## 🚀 시작하기

### 1) 프로젝트 클론
```bash
git clone https://github.com/your-github-id/Oullim_BE.git
cd Oullim_BE
