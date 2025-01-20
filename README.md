<p align="center">
	<img src="https://github.com/user-attachments/assets/d0f58885-adc3-40a4-8787-5b0463cd0df6" alt="lotto logo">
</p>
 <h2 align="center">
	 Lotto Experience Project
 </h2>
 <div align="center">
    로또 체험 프로그램
</div>

## <프로젝트 개요>
- 로또 6/45 구매 경험이 없는 사용자를 위한 가상 로또 체험 프로그램
---
- ### Experience the Lotto!
- 2024.07.29 ~ 08.06
- Collaborators
	- [이학석](https://github.com/HSLee1013)
	- [구예은](https://github.com/goho11)
---
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openJDK&logoColor=white" alt="Java" style="zoom: 1.5;" /> <img src="https://img.shields.io/badge/Java_Swing-5382A1?style=for-the-badge&logo=java&logoColor=white" alt="Java Swing" style="zoom: 1.5;" /> <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white" alt="Eclipse" style="zoom: 1.5;" /> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" style="zoom: 1.5;" />

---
## 1. 담당 업무
- ### 1) Git 협업
	![git 브랜치 전략](https://github.com/user-attachments/assets/f9953b41-c750-4c66-8450-1f9de843838e)
	 - Git 사용 경험이 많지 않아서 조원들 모두가 이해할 수 있는 간단한 브랜치 전략을 수립
	![충돌정리 ppt 버전](https://github.com/user-attachments/assets/44209db2-8ea0-4017-9cb5-49d542080f05)

	 - merge 시 문제 발생할 경우 정리해서 master에 push
- ### 2) 가상 추첨 기능 구현
	![로또 결과창](https://github.com/user-attachments/assets/e9175d81-d701-471a-89bb-83ba2c3f033c)
	- 랜덤으로 중복되지 않는 6개 + 1개의 당첨 번호를 정하기 위해 Set을 활용
	- 정렬된 순서가 중요하기 때문에 TreeSet 활용
	- 디스플레이 되는 공의 색상으로, 당첨 번호 일치를 쉽게 확인 가능
	- 여러 장을 구매할 경우< > 버튼과 드롭다운 버튼을 통해 확인 가능
- #### 당첨 화면 구현에서 겪은 어려움과 배운 점
	- 로또 번호 공의 위치나 각종 버튼의 위치를 각 창을 기준으로 Absolute Layout으로 설정
	- 추가적인 기능 구현을 위해 버튼이나 라벨을 창에 추가할 경우 다시 좌표 계산이 필요
	- 생산성 향상을 위해, 화면을 모듈 단위로 작게 나눠서 제작할 필요성에 대해 학습
- ### 3) 추첨 화면을 재활용, 추가 기능 구현
	![구매한 로또 번호 확인](https://github.com/user-attachments/assets/0a7e2381-03ce-479d-9c73-07e264d48921)
	- 여러 장 구입했을 경우 사용자가 이전에 작성한 번호 확인 가능
	![이전 회차 정보 확인](https://github.com/user-attachments/assets/d3a28294-176d-4fad-ba50-f0a6470ec338)
	- 이전 회차 당첨 결과를 확인 가능
- ### 4) 데이터 저장
	![텍스트파일에 기록 남기는 방식](https://github.com/user-attachments/assets/39d4db95-895d-46e9-b25d-077178ac8413)
	- DB에 대해 배우기 전이었기 때문에 구분자를 활용하여 데이터 저장
## 2. 프로젝트 개인 감상
- #### 변수 이름, 주석, 리팩토링의 중요성
	- 화면 구성 요소들의 객체 변수 이름을 축약어를 사용해서 네이밍하여, 구성 요소를 재활용하는 기능의 담당 팀원이 내가 작성한 코드를 이해하기 어려운 문제 발생
	- 이에 따라 팀원에게 추가적인 설명 시간이 소요
	- 팀원을 배려해서 변수 이름을 정하고 주석으로 설명 추가 및 메서드를 보기 좋게 정리하는 리팩토링의 중요성 학습
- #### 다회차 추첨을 고려하지 않은 코드 작성으로 겪은 어려움
	- 변하지 않는 요소(요소들의 위치)와 변하는 요소(공 안의 숫자, 라벨의 텍스트)를 정하는 코드를 분리해서 작성하지 않음
	- 로또를 여러 장 구매할 경우 화면을 전환 시 숫자와 텍스트 변경 필요
	- 화면 전환 시 모든 요소를 지우고 다시 만드는 불필요한 과정이 발생
	- 메서드 분리를 통해 화면 전환 시 필요한 코드만 실행하도록 코드를 리팩토링
## 3. 그 외 기능
- ### 초기 화면 디자인
	![초기 화면 디자인](https://github.com/user-attachments/assets/06d72225-02b6-4f76-afad-225d6f6e4714)
- 

### 2. Program 주요 기능([이동](#2-Program-주요-기능))
- 메인 화면
- 구매
- 숫자 선택
- 로또 모의 추첨
- 현재 구매한 로또 번호 확인
- 이전 회차 기록
- 로또 데이터 저장 방식
- 손익결산
- 통계
---
## 1. 프로젝트 설계 단계




- ### Git 협업 과정



## 2. Program 주요 기능
- ### 메인 화면
	
![로또 메인 화면](https://github.com/user-attachments/assets/5b7a8852-2776-4621-91ab-ea77c4be217c)

- ### 구매



- ### 숫자 선택

![숫자 선택 화면](https://github.com/user-attachments/assets/a65df7a2-51c6-4467-9014-352a5b0e8986)

- ### 로또 모의 추첨



- ### 현재 구매한 로또 번호 확인



- ### 이전 회차 기록



- ### 로또 데이터 저장 방식(텍스트)



- ### 손익결산

![로또 손익결산](https://github.com/user-attachments/assets/352c9bf4-c02a-4ba1-a289-ccd0ad0d54d9)

- ### 통계

![로또 통계](https://github.com/user-attachments/assets/e0659dc3-5803-4125-9c6a-79e0a1c20eee)

---
## Authors
- 이강호
- 깃허브 닉네임: LeeKangHo1
- [깃허브 프로필 링크](https://github.com/LeeKangHo1)
