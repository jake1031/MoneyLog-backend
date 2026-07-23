# 💰 MoneyLog (가계부 서비스 백엔드)

스마트한 자산 관리를 위한 RESTful API 백엔드 서비스 프로젝트입니다.

---

## 📅 개발 일지

### 🟢 1일차: 프로젝트 초기 세팅 & 아키텍처 설계
- [x] **아키텍처 및 요구사항 정의:** REST API, ERD, 설계 (`docs/architecture.md`)
- [x] **Git 저장소 세팅:** `.gitignore` 환경 설정 및 GitHub 원격 저장소(`MoneyLog-backend`) 연결
- [x] **프로젝트 문서화**

### 🟢 2일차: 도메인 계층 구현 및 H2 데이터베이스 연동
- [x] **도메인 & 엔티티 설계:** `User`, `Category`, `Transaction` 엔티티 연관관계 설정 및 JPA Auditing 적용
- [x] **카테고리 & 거래내역 CRUD:** 비인증 환경에서도 동작하는 카테고리/거래내역 기능 구현
- [x] **H2 데이터베이스 연동:** 로컬 개발용 In-Memory DB 연동 및 초기 시드 데이터(`data.sql`) 구축
- [x] **트러블슈팅 및 예외 처리:**
    - Spring Security 설정 조정을 통한 비인증 API 테스트 환경 확보 (`/api/**`, `/h2-console/**` 허용)
    - `data.sql` 실행 시 `created_at` NOT NULL 제약조건 위반 이슈 해결 (`NOW()` 및 `@EnableJpaAuditing` 적용)
    - Jackson 직렬화 시 DTO `@Getter` 누락으로 인한 빈 객체(`{}`) 응답 현상 수정

---