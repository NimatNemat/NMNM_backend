# NMNM_backend
NimatNemat(니맛내맛) 프로젝트의 백엔드 repositary입니다.

## 소개
니맛내맛 프로젝트는 사용자의 식당 평가를 기반으로 식당을 추천해주는 시스템입니다. 
사용자는 자신이 선호하는 식당 그룹을 선택하고 그에 따라 식당 추천을 받을 수 있습니다. 
이후 식당에 대한 평가가 반복되면서 추천에 정확도와 그룹 내에서도 나에게 더 직접적인 추천을 받을 수 있습니다.
백엔드는 Spring Boot로 개발되었으며, MongoDB를 사용하여 음식점 및 사용자 선호도 정보를 저장합니다.

## API 문서
제공하는 API에 대한 내용은 다음 문서에서 확인할 수 있습니다.
Swegger를 이용하여 API에 대한 문서를 생성하고 있습니다.

## 시작하기

프로젝트를 로컬에서 설치하고 실행하려면 다음 단계를 따라야 합니다.

### 설치

### 설정

### 사용

## Git Commit message규칙

### feat : 새로운 기능 추가
### fix : 버그 수정
### docs : 문서 수정
### test : 테스트 코드 추가
### refact : 코드 리팩토링
### style : 코드 의미에 영향을 주지 않는 변경사항
### chore : 빌드 부분 혹은 패키지 매니저 수정사항

### 예시
### <타입> : <제목> 의 형식으로 제목을 아래 공백줄에 작성
제목은 50자 이내 / 변경사항이 "무엇"인지 명확히 작성 / 끝에 마침표 금지
예) feat : 로그인 기능 추가

### 본문(구체적인 내용)을 아랫줄에 작성
여러 줄의 메시지를 작성할 땐 "-"로 구분 (한 줄은 72자 이내)

### 꼬릿말(footer)을 아랫줄에 작성 (현재 커밋과 관련된 이슈 번호 추가 등)
예) Close #7