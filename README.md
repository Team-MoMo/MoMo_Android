# 💙 MoMo Android 💙

## 🌼 목차

## 🎁 프로젝트 설명
* SOPT 27th APPJAM
* 2020.12.28 ~ 2021.01.16

## 🍰 Rule Setting

### 1️⃣ Naming Rule
- id naming : (Activity or Fragment) _ (Where) _ (구성요소) _ (역할)

- Method 이름은 '동사'로 시작하는 '동사구' 형태를 사용하되, 동사 원형을 사용  
ex) showList, updateContacts
- 한 단어 내에서는 대소문자 변경 없이 사용  
ex) InVisible → Invisible
- 파일 이름 : UpperCamelCase로 작성  
ex) SignInActivity, ChangeFragment

### 2️⃣ git, branch 전략
* 이슈 별로 branch 생성 
* branch 명명 : **feature/#00 (issue number)**
* Issue + Projects 활용

### 3️⃣ Commit Convention
- [type] body
- type
    - feat : 새로운 파일 생성, 새로운 기능 추가
    - refactor : 코드 리팩토링
    - fix : 버그 수정
    - chore : 동작에 영향을 주지 않는 사항들 (주석, 정렬 등등)
- body : 구현한 기능 설명
    
### 4️⃣ Package
- ui - activity
- network - interface 등 통신에 필요한 기초 파일
- util - 커스텀 파일, 확장 함수 등
- 그 외 기능 별로 분류
    - ui
    - data
    - adapter
    - etc..

### 5️⃣ Communication
* Gather Town
* Slack
* Notion
* GitHub - Issue & Projects
* Zoom

### ⛄ 각자 맡은 부분 및 역할 작성
* **현진** : 다이어리(일기 상세보기), 로그인 & 회원가입
* **윤소** : 메인화면(Home)
* **희정** : 온보딩, 일기 작성
* **희원** : 일기 리스트, 스플래시

## 👩‍👩‍👧‍👧 MoMoroid Developer
<table style="text-align: center;">
  <tr>
    <th>조현진 🐹</th>
    <th>최윤소 🐱</th>
    <th>나희정 🐯</th>
    <th>강희원 🐿</th>
  </tr>
  <tr>
    <th><a href="https://github.com/jinyand">jinyand</a></th>
    <th><a href="https://github.com/yxnsx">yxnsx</a></th>
    <th><a href="https://github.com/NaHui999">NaHui999</a></th>
    <th><a href="https://github.com/ymcho24">ymcho24</a></th>
  </tr>
  <tr>
    <td colspan="4"><img src="https://user-images.githubusercontent.com/38918396/103541742-33e12a00-4edf-11eb-8dab-42c246256d5f.png" /></td>
  </tr>
</table>

-----------------------------------------------------------------------------------
(편집중)

### 🎄 사용된 라이브러리


### 🎅 사용한 기술 스택 작성 ( ex. AAC - DataBinding, ViewModel.. ,  비동기 작업 - Coroutine , DI - Dagger2 .. ,디자인 패턴 - MVP, MVVM , Factory Pattern 등등 자유롭게 작성)

### 🗂 프로그램 구조
package 분류 이미지
    
### 💻 핵심 기능 구현 코드 및 방법 정리 ( 실제 구현 화면 포함 )

### A-1(회의록)이 적혀있는 링크 /  필요 시 A - 2의 5번 6번
5. 별도의 다른 Layout을 사용할 경우 사용 이유와  xml 파일 링크 README에 작성    (사용한 이유가 타당해야 인정)  
6. 단순한 도형 및 상태변환 Drawable은 ShapeDrawable과 StateListDrawable를 사용할 것.  
- 다음과 같은 경우 사용한 이유를 README에 작성  
- 단순 도형을 에셋 받아 사용했을 경우  
- 상태 변화가 나타나야하는 뷰에 StateListDrawable을 사용하지 않았을 경우

