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
* [프로그램 구조](#-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EA%B5%AC%EC%A1%B0)
* [사용된 라이브러리, 사용한 기술 스택](#-%EC%82%AC%EC%9A%A9%EB%90%9C-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%EC%82%AC%EC%9A%A9%ED%95%9C-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)
* [핵심 기능 구현 코드 및 방법](#-%ED%95%B5%EC%8B%AC-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%EC%BD%94%EB%93%9C-%EB%B0%8F-%EB%B0%A9%EB%B2%95)
* [MoMoroid Developer](#-momoroid-developer)

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
* **현진** : 일기 상세보기(다이어리), 일기 수정, 로그인&회원가입
* **윤소** : 메인-홈, 메인-스크롤
* **희정** : 온보딩, 일기 작성
* **희원** : 일기 리스트&필터, 스플래시, 환경설정

<br>

### 🗂 프로그램 구조
<img src="https://user-images.githubusercontent.com/38918396/104733695-6feb7900-5782-11eb-8419-1df9e866f65b.png" width="300">

<br>

### 🎄 사용된 라이브러리, 사용한 기술 스택
* **ViewBinding**
    * 뷰와 상호 작용하는 코드를보다 쉽게 작성하기 위해 사용
* **Retrofit2**
    * REST API를 통해 서버와 통신하기 위해 사용
* **gson/gson converter**
    * Retrofit2 라이브러리를 통해 서버로부터 받은 데이터를 파싱된 형태로 이용하기 위해 사용
* **real time blur view**
    * 로그인 및 깊이선택 뷰에서 배경 오브제의 블러 처리를 위해 사용
* **vertical seekbar**
    * 깊이선택 뷰 및 스크롤뷰의 깊이를 세로 형태의 시크바로 나타내기 위해 사용
* **Lottie**
    * 온보딩 및 스플래시 뷰에 애니메이션 효과를 주기 위해 사용
* **neumorphism**
    * 감정선택 뷰에서 커스텀 그림자가 적용된 버튼을 나타내기 위해 사용
<br>

<br>

### 💻 핵심 기능 구현 코드 및 방법
[Wiki에서 확인할 수 있어요! Click ✔](https://github.com/Team-MoMo/MoMo_Android/wiki)

* [Splash](https://github.com/Team-MoMo/MoMo_Android/wiki/Splash)
* [Onboarding](https://github.com/Team-MoMo/MoMo_Android/wiki/Onboarding)
* [Sign 로그인, 회원가입](https://github.com/Team-MoMo/MoMo_Android/wiki/Sign-%EB%A1%9C%EA%B7%B8%EC%9D%B8,-%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85)
* [Main 홈, 스크롤](https://github.com/Team-MoMo/MoMo_Android/wiki/Main-%ED%99%88,-%EC%8A%A4%ED%81%AC%EB%A1%A4)
* [Upload 감정선택](https://github.com/Team-MoMo/MoMo_Android/wiki/Upload-%EA%B0%90%EC%A0%95%EC%84%A0%ED%83%9D)
* [Upload 문장선택](https://github.com/Team-MoMo/MoMo_Android/wiki/Upload-%EB%AC%B8%EC%9E%A5%EC%84%A0%ED%83%9D)
* [Upload 일기작성, Diary 일기수정](https://github.com/Team-MoMo/MoMo_Android/wiki/Upload-%EC%9D%BC%EA%B8%B0%EC%9E%91%EC%84%B1,-Diary-%EC%9D%BC%EA%B8%B0%EC%88%98%EC%A0%95)
* [Upload 깊이선택, Diary 깊이수정](https://github.com/Team-MoMo/MoMo_Android/wiki/Upload-%EA%B9%8A%EC%9D%B4%EC%84%A0%ED%83%9D,-Diary-%EA%B9%8A%EC%9D%B4%EC%88%98%EC%A0%95)
* [List 일기 리스트, 필터](https://github.com/Team-MoMo/MoMo_Android/wiki/List-%EC%9D%BC%EA%B8%B0-%EB%A6%AC%EC%8A%A4%ED%8A%B8,-%ED%95%84%ED%84%B0)
* [Diary 일기 상세보기](https://github.com/Team-MoMo/MoMo_Android/wiki/Diary-%EC%9D%BC%EA%B8%B0-%EC%83%81%EC%84%B8%EB%B3%B4%EA%B8%B0)
* [날짜 변환 Date format](https://github.com/Team-MoMo/MoMo_Android/wiki/%EB%82%A0%EC%A7%9C-%EB%B3%80%ED%99%98-Date-format)
* [날짜 선택 Bottom Sheet](https://github.com/Team-MoMo/MoMo_Android/wiki/%EB%82%A0%EC%A7%9C-%EC%84%A0%ED%83%9D-Bottom-Sheet)
* [환경설정](https://github.com/Team-MoMo/MoMo_Android/wiki/%ED%99%98%EA%B2%BD%EC%84%A4%EC%A0%95)
* [확장함수](https://github.com/Team-MoMo/MoMo_Android/wiki/%ED%99%95%EC%9E%A5%ED%95%A8%EC%88%98)
* [CoordinatorLayout & CollapsingToolbarLayout](https://github.com/Team-MoMo/MoMo_Android/wiki/CoordinatorLayout-&-CollapsingToolbarLayout)

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
