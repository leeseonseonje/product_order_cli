# 상품 주문 프로그램

## 기술 스택
IDE: IntelliJ
BackEnd: Java11, Spring Boot(2.7.3), Spring Data Jpa, JUnit5, Gradle

DB: H2 In Memory DB

## 프로젝트 구조
Application 클래스의 main 메서드로 실행한다.

InitData 클래스에서 @PostConstruct를 이용하여 애플리케이션 시작 시점에 제공받은 csv 파일을 불러와 H2 데이터베이스에 저장한다.

handler.CommandHandlerImpl에서 명령을 입력받는다. 현재는 order, quit 명령어만 존재한다.

핸들러에서 입력받은 명령어와 주입 받은 ApplicationContext를 Command.command enum 클래스에 주입시켜 입력받은 명령어에 맞는 포매터를 실행한다.

포매터에서 입력받은 명령에 맞는 서비스를 주입받아 비즈니스 로직을 수행 후 반환 받은 값을 이용해 콘솔에 출력해준다.

### ex) 주문 명령어를 입력 받았을 경우
<img width="693" alt="스크린샷 2022-06-01 오후 10 49 09" src="https://user-images.githubusercontent.com/72899707/171420653-2d3f6e29-9e35-4fdb-82ef-b17b35e5be89.png">
Command의 execute메서드 실행

<img width="945" alt="스크린샷 2022-06-01 오후 10 51 40" src="https://user-images.githubusercontent.com/72899707/171421001-e5139a20-5c2c-4261-922c-2b5a94e44272.png">
주문 명령어에 맞는 OrderFormatter클래스를 실행

<img width="623" alt="스크린샷 2022-06-01 오후 10 54 03" src="https://user-images.githubusercontent.com/72899707/171421482-2713e3cf-d631-4097-90ad-244eccfb8e7a.png">
OrderFormatter의 주문 번호와 수량을 입력받는 부분
<img width="687" alt="스크린샷 2022-06-01 오후 10 55 46" src="https://user-images.githubusercontent.com/72899707/171421871-acf42211-e60f-4773-bfc5-ed5dbb6fd3db.png">
주문 번호와 수량을 입력하는 부분에서 " "(공백)명령어를 입력하게 되면 주문이 종료되고, orderFormatter의 orderResult메서드에서 서비스계층을 호출하게 되고, 주문한 내역과 총 결제 금액을 보여주게 된다.
<img width="943" alt="스크린샷 2022-06-01 오후 11 01 49" src="https://user-images.githubusercontent.com/72899707/171423207-c8c483ae-2773-418c-952c-8f6cb198d684.png">
OrderFormatter에서 입력 받은 주문 번호와 수량을 이용하여 주문 결과를 만들어 OrderFormatter에 반환 한다.

OrderFormatter 의 orderResult메서드 에서 OrderService에게 반환 받은 값을 이용하여 주문 결과를 콘솔에 출력한다.

## 구현 방향
주문(order)외에 다른 명령어가 도입될수도 있다는 가정을 염두에 두고 확장성에 많이 신경을 썻습니다.
  
Handler, Reader, Formatter, Service, Repository 등의 계층으로 나누어 각각의 계층마다 관심사를 분리하여 응집도를 높여 변경에 있어 좀 더 자유로워 질 수 있게 설계를 하였고,
  
스프링 컨테이너와 추상화를 이용하여 결합도를 느슨하게 하여, 입력받은 명령어 마다 동적으로 다른 구현체를 주입받아 실행할 수 있게 함으로써 확장성을 높였습니다.
  
또한, JPA의 비관적 락(select for update)문을 사용하여 안정적인 동시성 처리를 할 수 있게 했습니다.
  

## 회고
지금 까지 Spring Boot를 이용한 애플리케이션을 개발한 경험중에 Controller없이 개발한 것은 처음이었습니다.  
  
Controller 계층이 없어 Controller의 역할을 하는 계층을 직접 요구사항에 맞춰 만들어야 하여 개발 초기 많이 당황했던것 같습니다.  
  
하지만 실무 경험이 없는 입장에서 생각하기 어려운 것들을 Controller없이 개발을 하다 보니 깨닫게 된 것들이 몇가지 있습니다.  
  
if문이나 switch문을 이용하여 각 명령마다 분기 처리를 하는 것이 마음에 들지 않아 고민 끝에 enum클래스와 applicationContext를 이용하여 
동적으로 명령을   
  
처리하게 하면서 추상화의 중요성을 더욱더 뼈저리게 깨닫게 되었습니다.  
  
또, 처음에 Console화면에 출력 하는 부분이 서비스, 도메인계층에까지 걸쳐 존재했었고, 이로 인해 테스트가 어려워져 formatter라는 계층을 만들어 콘솔에 출력  
  
하는 부분을 formatter에서 책임지게 하였습니다.
이로 인해 테스트가 수월해 졌고 각 계층의 책임이 확실해 지고, 응집도가 높아지게 되었습니다.  
  
"테스트가 어려운 코드는 잘못된 설계일 확률이 높다"라는 말을 깨닫게 되었습니다.  
  
또, 동시성에 대해 많은 생각을 하게 되어 동시성을 바라보는 시야가 많이 넓어진 것 같습니다.  
  
감사합니다.












