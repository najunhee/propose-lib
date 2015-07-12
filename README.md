# Android Live GUI - Propose
**당신의 View에 Interection이 필요합니까?**<br>
**Propose는 Android 표준 Property Animation에 Interection을 부여 해줍니다.**<br>
```Property Animation에 Click, Drag, Fling, TapUp 등 Touch관련 제스처를 쉽게 연결할수 있습니다.<br>
당신은 이제 Propose를 사용하여 Live GUI를 실현할수 있습니다.```<br>

> ####[Click introduction video](https://youtu.be/FpXSwXSbTYE)
[![Android Propose Story book](https://raw.githubusercontent.com/JaeWoongOh/jaewoongoh.github.com/master/gist/images/propose/book flip.png)](http://youtu.be/FpXSwXSbTYE)<br>
**Only used Translation,Rotation,Scale.  Believe it?**


## Android Property Animation
안드로이드 3.0 이후 **Property Animation(ValueAnimator,ObjectAnimator..)**이라는<br>
강력한 애니메이션 클래스들이 추가되었다.<br>
Google에서 ValueAnimator 소개 영상을 처음 접했을때 실로 놀랐웠다.<br>
- [Honeycomb Animations 영상](https://youtu.be/-9nxx066eHE)<br>
- [DevBytes 영상](https://youtu.be/8sG3bAPOhyw?list=PLWENVpyNjgdFKwgBWj75IKQdM-DjvcPzx)<br>

나이스한 어떤 애니메이션이든 표현이 가능했고 단순하며 쉽게 구현되는 모습보고<br>
앞으로는 모든 앱들이 굉장히 액티브해질꺼라 여겨졌다.<br>
그런데 현실은 조금 달랐다. 앱은 가만히 보고만 있는 비디오가 아니기 때문이다.<br>
사용자에 의해 반응하고 서로 소통을해야 의미가 있었다.<br><br>
Drag, Fling 등 사용자의 제스처를 소화못하는 애니메이션은 반쪽짜리에 불과했다.<br>
실제 SlidingMenu가 유행했을때에도 개발자들은 CustomView를 제작하거나<br> 
인터넷을 뒤져 오픈소스를 찾기 바빴다. 단순 클릭만으로 슬라이딩을 하는것이라면<br>
ObjectAnimator를 써서 코드 몇줄이면 되지만 사용자들은 Drag나 Fling도 되길 원했다.<br>
그리하여 애니메이션의 갑 of 갑인 ValueAnimator,ObjectAnimator는<br>
개발자들에게 많이 알려지지 않았다.<br><br>

## CustomView와 Open Source
Web이 그랬듯이 사람들은 점점 액티브한 앱을 선호할것이다<br>
아직은 대부분 앱들이 정적이고 표현되는 모션들이 거의 비슷하여 특색을 찾기 힘들다.<br>
메이저급 앱에서 좋은 모션이 있다면 그에 관련되서 찾기 바쁠뿐<br>
스스로 만들수 있다는 생각을 하지 못하는것 같다.<br>
아니면 Touch관련 입력은 매우 어렵고 오래걸리는 작업이라 여겼을지도 모른다.<br><br>
이런 생각은 나를 비로서 알수 있었다. 나한테는 모션이 어렵고 힘든 작업이었다.<br>
또한 CustomView는 재활용이 힘들었고 매번 다시 재작하는 느낌이었다. <br>
나는 이것을 ```"CustomView Hell"``` 이라 불렀다.<br>
Hell에서 벗어나기 위해 많은 오픈소스로 눈을 돌렸지만 이것도 해결책이 되지는 못했다.<br>
오프소스 자체를 이해하기 어려웠고 요구사항에 맞게 수정도 힘들었다.<br>
이젠 ```"OpenSource Hell"``` 이 되어버렸다.<br>
<br>

## Propose는 무엇인가
처음 Propose는 이야기책을 만들어 청혼하기 위한 개인 프로젝트였다.<br>
생각처럼 제대로 되는건 하나도 없었고 청혼도 못하였다.<br>
물론 청혼도 Hell이 되어버렸다.<br><br>
Propose 초창기 버전의 시작은 CustomView의 재활용이라는 컨셉으로 시작되었다.<br>
CustomView를 재활용하기 위해 애니메이션 영역과 모션영역을 분리했다.<br>
모듈을 분리함으로써 새로운 interaction을 만들고 재활용했다.<br>
하지만 CustomView 형식의 애니메이션모듈은 제작하기 힘들었고 제공되는 종류가 적었다.<br>
또한 애니메이션의 필수 구현요소인 Thread는 안정성을 위해 많은 테스트를 거쳐야했다.<br><br>
이 문제들을 해결하기 위해 애니메이션 모듈의 개선이 필요했고 기존에 잘쓰고 있던<br>
Android Property Animation에 눈길을 돌렸다.<br>
무엇보다도 Property Animation은 Android 표준이며 애니메이션의 다양한 종류와 구현이 매우 쉬웠다.<br>

**Property Animation과 Propose의 모션모듈의 조합은 큰 이점을 얻을수 있었다.**
- **```Animation의 Thread와 라이프 사이클을 Android에서 관리하여 안정성을 보장받는다.```**<br>
Propose는 모션 변환만 할뿐 모든 Thread와 라이프 사이클을 Property Animation이 알아서 처리해준다.
<br><br>
- **```Android 표준 Animation으로 개발자의 접근성이 높다.```**<br>
Android Animation을 사용함으로 개발자는 쉽고 익숙한 방법으로 개발이 가능하다.
<br><br>
- **```Animation들 간의 조합과 Motion을 복합조합함으로 자유도 높은 Interaction을 구현할수 있다.```**<br>
Translation과 Rotation만을 사용해 StoryBook을 만들수 있을 정도로 고품질의 표현이 조합만으로 가능하다.<br>


이렇게 Proposes는 View의 customize 한계를 극복하기 위해 시작되었고<br>
안전하고, 쉽고, 익숙한 방법으로 고품질의 Interection을 구현할수 있게 도와준다.<br><br>
