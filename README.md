# 💙 MoMo Android 💙

<img src="https://user-images.githubusercontent.com/38918396/104698570-e40f2800-5754-11eb-8548-7bad4b78f68d.png" width="250" height="250">

* SOPT 27th APPJAM - **TEAM MOMO**
* PROJECT : 2020.12.28 ~ 2021.01.16

<br>

## 🌼 목차
* [프로젝트 설명](#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%A4%EB%AA%85)
* [Rule Setting](#-rule-setting)
* [회의록](#-%ED%9A%8C%EC%9D%98%EB%A1%9D)
* [역할 분담](#-%EC%97%AD%ED%95%A0-%EB%B6%84%EB%8B%B4)
* [MoMoroid Developer](#-momoroid-developer)
* [사용된 라이브러리](#-%EC%82%AC%EC%9A%A9%EB%90%9C-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC)
* [사용한 기술 스택 작성]()
* [프로그램 구조](#-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EA%B5%AC%EC%A1%B0)
* [핵심 기능 구현 코드 및 방법]()

<br>

## 🎁 프로젝트 설명
![Git_Main](https://user-images.githubusercontent.com/38918396/104698429-b1fdc600-5754-11eb-8103-b8cb583c36d3.png)
* * *
MOMO, 책 속의 문장을 제공함으로써, 보다 깊이 있는 감정 기록을 도와주는 일기 앱 서비스.

감정 카테고리를 선택하면, 감정과 어울리는 3개의 문장이 제공됩니다. 하나의 문장을 선택하여 그날의 일기를 기록하고, 2M에서 심해까지 감정의 깊이를 선택하여 저장합니다. 저장된 일기는 해당 깊이의 바다를 떠다니는 물방울이 됩니다.

사용자는 문장을 통해 감수성을 자극받으므로, 감정을 보다 섬세하고 구체적으로 기록할 수 있습니다. 따라서 평소에 일기를 쓰면서 표현력의 한계를 느꼈던 사람들에게 MOMO가 해결책이 되어줄 것입니다.
* * *

<br>

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

<br>

## 📝 회의록
[여기에서 확인할 수 있어요! Click ✔](https://www.notion.so/1-cfdb90161b5b4829bda8ce257add69fe)

<br>

## ⛄ 역할 분담
* **현진** : 다이어리(일기 상세보기), 로그인 & 회원가입
* **윤소** : 메인화면(Home)
* **희정** : 온보딩, 일기 작성
* **희원** : 일기 리스트, 스플래시

<br>

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

<br>

[UP⏫](#-momo-android-)

-----------------------------------------------------------------------------------

### 🎄 사용된 라이브러리


### 🎅 사용한 기술 스택 작성

### 🗂 프로그램 구조
package 분류 이미지
    
### 💻 핵심 기능 구현 코드 및 방법

### 필요 시 A - 2의 5번 6번
5. 별도의 다른 Layout을 사용할 경우 사용 이유와  xml 파일 링크 README에 작성    (사용한 이유가 타당해야 인정)  
6. 단순한 도형 및 상태변환 Drawable은 ShapeDrawable과 StateListDrawable를 사용할 것.  
- 다음과 같은 경우 사용한 이유를 README에 작성  
- 단순 도형을 에셋 받아 사용했을 경우  
- 상태 변화가 나타나야하는 뷰에 StateListDrawable을 사용하지 않았을 경우

