# 머니로그 아키텍처 및 설계서

> 프로젝트 정보
> - 작성자: 곽승욱  
> - 문서 버전: v1.0  
> - 관련 문서: requirements.md (요구사항 정의서)

---

## 1. ERD (Entity Relationship Diagram)

### 엔티티 구조 및 관계
- users (1) ─── (N) categories: 유저는 여러 커스텀 카테고리를 가질 수 있음 (기본 카테고리는 user_id = null)
- users (1) ─── (N) transactions: 유저는 여러 거래 내역을 가질 수 있음
- categories (1) ─── (N) transactions: 하나의 카테고리는 여러 거래 내역에 지정될 수 있음

---

## 2. REST API SPEC 명세

> 모든 인증이 필요한 API는 Header에 Authorization: Bearer {JWT_TOKEN} 전송 필수

### 1) Auth (인증)
| 기능 | Method | Endpoint | Request Body | Response |
| :--- | :---: | :--- | :--- | :--- |
| 회원가입 | POST | /api/v1/auth/signup | email, password, name | userId, email |
| 로그인 | POST | /api/v1/auth/login | email, password | accessToken, tokenType |

### 2) Categories (카테고리)
| 기능 | Method | Endpoint | Query / Body | Response |
| :--- | :---: | :--- | :--- | :--- |
| 카테고리 목록 조회 | GET | /api/v1/categories | type (선택) | 카테고리 리스트 (기본 + 사용자 정의) |
| 카테고리 추가 | POST | /api/v1/categories | name, type | 생성된 카테고리 정보 |
| 카테고리 삭제 | DELETE | /api/v1/categories/{id} | - | 성공 여부 |

### 3) Transactions (거래 내역)
| 기능 | Method | Endpoint | Query / Body | Response |
| :--- | :---: | :--- | :--- | :--- |
| 거래 등록 | POST | /api/v1/transactions | categoryId, type, amount, memo, date | 생성된 거래 정보 |
| 거래 목록 조회 (페이징) | GET | /api/v1/transactions | yearMonth, type, categoryId, page, size | 거래 내역 페이징 목록 |
| 거래 단건 상세 조회 | GET | /api/v1/transactions/{id} | - | 거래 상세 정보 |
| 거래 수정 | PUT | /api/v1/transactions/{id} | categoryId, type, amount, memo, date | 수정된 거래 정보 |
| 거래 삭제 | DELETE | /api/v1/transactions/{id} | - | 성공 여부 |

### 4) Stats (통계)
| 기능 | Method | Endpoint | Query | Response |
| :--- | :---: | :--- | :--- | :--- |
| 월별 통계 조회 | GET | /api/v1/stats/monthly | yearMonth (예: 2026-07) | 총수입, 총지출, 잔액, 카테고리별 비중(%) |

---

## 3. 공통 응답 규격

### 성공 응답 (200 OK / 201 Created)
```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": { ... }
}
### 예외 응답 (4xx / 5xx)
JSON
{
  "success": false,
  "code": "INVALID_INPUT_VALUE",
  "message": "금액은 0보다 커야 합니다.",
  "data": null
}

## 4. 프론트엔드 화면 연동 흐름
로그인 / 회원가입 화면: /api/v1/auth/signup, /api/v1/auth/login 연동 ➔ 토큰 로컬스토리지 저장

거래 내역 목록 화면: /api/v1/transactions (월별 필터 + 페이징) 연동

거래 등록 / 수정 화면: /api/v1/categories (카테고리 드롭다운) 및 /api/v1/transactions (C/U) 연동

월별 통계 화면: /api/v1/stats/monthly 연동 ➔ 파이 차트 및 지출/수입 요약 표시