<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

---

## 🚀 1단계 - 지하철역 인수 테스트 작성
### ✏️ 기능 요구사항
- [x] 지하철역 관련 인수 테스트를 완성
  - [x] 지하철역 목록 조회 인수 테스트 작성
  - [x] 지하철역 삭제 인수 테스트 작성

### 프로그래밍 요구사항
- [x] 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링

---

## 🚀 2단계 - 지하철 노선 기능
### ✏️ 기능 요구사항
- [x] 요구사항 설명에서 제공되는 인수 조건을 기반으로 지하철 노선 관리 기능을 구현하세요.
- [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.

### ✏️ 기능 목록
- [x] 지하철 노선 생성
  - [x] 테스트 코드, 리팩토링
- [x] 지하철 노선 목록 조회
  - [x] 테스트 코드, 리팩토링
- [x] 지하철 노선 조회
  - [x] 테스트 코드, 리팩토링
- [x] 지하철 노선 수정
  - [x] 테스트 코드, 리팩토링
- [x] 지하철 노선 삭제

### ✏️ 프로그래밍 요구사항
- [x] 인수 조건을 검증하는 인수 테스트 작성
  - [x] 인수 테스트를 충족하는 기능 구현
  - [x] 인수 테스트를 서로 격리
- [x] 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링

### 🤝 인수 조건

지하철노선 생성
```
When 지하철 노선을 생성하면  
Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.  
```
<br>

지하철노선 목록 조회
```
Given 2개의 지하철 노선을 생성하고  
When 지하철 노선 목록을 조회하면  
Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.  
```
<br>

지하철노선 조회
```
Given 지하철 노선을 생성하고  
When 생성한 지하철 노선을 조회하면  
Then 생성한 지하철 노선의 정보를 응답받을 수 있다.  
```
<br>

지하철노선 수정
```
Given 지하철 노선을 생성하고
When 생성한 지하철 노선을 수정하면
Then 해당 지하철 노선 정보는 수정된다.
```
<br>

지하철노선 삭제
```
Given 지하철 노선을 생성하고
When 생성한 지하철 노선을 삭제하면
Then 해당 지하철 노선 정보는 삭제된다.
```

## 🚀 3단계 - 구간 추가 기능
> 기능 구현 전에 인수 조건을 만족하는지 검증하는 인수 테스트를 먼저 만들고 기능구현을 해보자.

### ✏️ 기능 요구사항
- 요구사항 설명에서 제공되는 요구사항을 기반으로 지하철 구간 추가 기능을 구현
- 요구사항을 정의한 인수 조건을 조출
- 인수 조건을 검증하는 인수 테스트를 작성
- 예외 케이스에 대한 검증 포함

### ✏️ 기능 목록
- [x] 역 사이에 새로운 역을 등록할 경우
- [x] 새로운 역을 상행 종점으로 등록할 경우
- [x] 새로운 역을 하행 종점으로 등록할 경우
- [x] 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
- [x] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
- [x] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음

### ✏️ 프로그래밍 요구사항
- [x] 인수 조건을 검증하는 인수 테스트 작성
  - [x] 인수 테스트를 충족하는 기능 구현
  - [x] 인수 테스트를 서로 격리
- [ ] 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링