# 💙 MoMo Android 💙

### Commit Convention
- [type] body
- feat : 새로운 파일 생성, 새로운 기능 추가
- refactor : 코드 리팩토링
- fix : 버그 수정
- chore : 동작에 영향을 주지 않는 사항들 (주석, 정렬 등등)
    
### Package
- ui - activity
- network - interface 등 통신에 필요한 기초 파일
- util - 커스텀 파일, 확장 함수 등
- 그 외 기능 별로 분류
    - ui
    - data
    - adapter
    - etc..

### git, branch 전략
* branch 명명 : feature/#00 (issue number)
* issue + ZenHub (칸반보드)

### 사용된 라이브러리

### 사용한 기술 스택 작성 ( ex. AAC - DataBinding, ViewModel.. ,  비동기 작업 - Coroutine , DI - Dagger2 .. ,디자인 패턴 - MVP, MVVM , Factory Pattern 등등 자유롭게 작성)

### 각자 맡은 부분 및 역할 작성

### 프로그램 구조 - package 분류 이미지
    
### 핵심 기능 구현 코드 및 방법 정리 ( 실제 구현 화면 포함 )

### A-1(회의록)이 적혀있는 링크 /  필요 시 A - 2의 5번 6번
5. 별도의 다른 Layout을 사용할 경우 사용 이유와  xml 파일 링크 README에 작성    (사용한 이유가 타당해야 인정)  
6. 단순한 도형 및 상태변환 Drawable은 ShapeDrawable과 StateListDrawable를 사용할 것.  
- 다음과 같은 경우 사용한 이유를 README에 작성  
- 단순 도형을 에셋 받아 사용했을 경우  
- 상태 변화가 나타나야하는 뷰에 StateListDrawable을 사용하지 않았을 경우
