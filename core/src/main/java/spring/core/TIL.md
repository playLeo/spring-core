# 스프링 학습

참고링크 https://www.junyoung.xyz/2021/02/12/spring-core.html#%EC%A2%8B%EC%9D%80-%EA%B0%9D%EC%B2%B4-%EC%A7%80%ED%96%A5-%EC%84%A4%EA%B3%84%EC%9D%98-5%EA%B0%80%EC%A7%80-%EC%9B%90%EC%B9%99%EC%9D%98-%EC%A0%81%EC%9A%A9

## 목차
1. [스프링이란?](#1-스프링이란)
2. [좋은 객체 지향 프로그래밍이란?](#2-좋은-객체-지향-프로그래밍이란)
3. [객체 지향 원리 적용](#3-객체-지향-원리-적용)
   1. [스프링을 사용하지 않은 코드](#3-객체-지향-원리-적용)
   2. [IoC, DI, 컨테이너](#IoC-DI-컨테이너)
   3. [스프링을 적용한 코드](#스프링-적용)
4. [스프링 컨테이너와 스프링빈](#4-스프링-컨테이너와-스프링빈)
   1. [스프링 컨테이너](#스프링-컨테이너)
   2. [스프링 빈 등록, 조회](#스프링-빈-등록)
5. [싱글톤 컨테이너](#싱글톤-컨테이너)
6. [컴포넌트 스캔](#컴포넌트-스캔)


## 1. 스프링이란?

스프링 구성 요소
* 스프링 프레임워크, 스프링 부트, 스프링 데이터, 스프링 세션, 스프링 시큐리티, 스프링 Rest Docs, 스프링 배치, 스프링 클라우드

스프링 프레임워크
* 핵심기술 : 스프링 DI 컨테이너, AOP, 이벤트, 기타
* 웹 기술 : 스프링 MVC, 스프링 WebFlux
* 데이터 접근 기술 : 트랜잭션, JDBC, ORM 지원, XML 지원
* 기술 통합 : 캐시, 이메일, 원격 접근, 스케줄링
* 테스트 : 스프링 기반 테스트 지원
* 언어 : 코틀린, 그루비

스프링 부트
* 단독으로 실행할 수 있는 스프링 애플리케이션을 쉽게 설정
* Tomcat 같은 웹 서버를 내장해서 별도의 웹서버를 설치하지 않아도 된다.
* 손쉬운 빌드 구성을 위한 starter 종속성 제공
* 스프링과 3rd parthy 라이브러리 자동 구성
* 메트링, 상태확인, 외부 구성 같은 프로덕션 준비 기능 제공
* 관례에 의한 간결한 설정

핵심
* 좋은 객체 지향 애플리케이션을 개발할 수 있게 도와주는 프레임워크
___

## 2. 좋은 객체 지향 프로그래밍이란?

### 역할과 구현을 분리(다형성 활용)

장점
1. 클라이언트는 대상의 역할(인터페이스)만 알면 된다.
2. 클라이언트는 구현 대상의 내부 구조를 몰라도 된다.
3. 클라이언트는 구현 대상의 내부 구조가 변경되어도 영향을 받지 않느다.
4. 클라이언트는 구현 대상 자체를 변경해도 영향을 받지 않는다.

한계
1. 역할(인터페이스) 자체가 변하면, 클라이언트, 서버 모두에게 큰 변경이 발생한다.

### 5가지 원칙(SOLID)
1. SRP(Single responsibility principle) 단일 책임 원칙  - 한 클래스는 하나의 책임만 가진다 -> 변경이 있을 때, 파급혀과가 적으면 단일 책임 원칙을 잘 따른 것.
2. OCP(Open/closed principle) 개방-폐쇄 원칙 - 확장은 열려있으나 변경에는 닫혀 있어야 한다. -> 다형성을 활용
3. LSP(Liskov substitution principle) 리스코프 치환 원칙 - 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.
4. ISP(Interface segregation principle) 인터페이스 분리 원칙 - 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
5. DIP(Dependency inversion principle) 의존관계 역전 원칙 - 프로그래머는 구체화에 의존하지 않고 추상화에 의존해야한다.

요약
1. SRP - 한 기능 한 클래스
2. OCP - 다형성의 확정성, 변경성
3. LSP - 하위클래스를 인터페이스에 맞게
4. ISP - 인터페이스 쪼개기
5. DIP - 하위클래스보다 인터페이스로 사용

다형성으로만 OCP, DIP를 지킬 수 없다.
-> 인터페이스 레퍼런스로 생성해서 인터페이스를 의존해도, new 로 할당할 때, 하위클래스인 구체화 클래스로 할당해야 하기 때문에 변경에는 닫혀있지 못하는 OCP 위반과, 하위클래스에 의존해 DIP 위반.

-> 추상화(인터페이스)에만 의존하게 설계해야 한다.

-> 관심사를 분리하여 구현객체를 생성하고 연결(의존관계 설정)하는 외부 클래스 AppConfig를 만든다. 의존관계 주입(DI)

## 3. 객체 지향 원리 적용
<details>
<summary>MemberServiceImpl</summary>
<div mardown="1">

    public class MemberServiceImpl implements  MemberService{

        //인터페이스 memberRepository와 귀체화 클래스 MemoryMemberRepository 둘다 의존에서 생성자 주입을 통한 인터페이스 의존으로 변경
        //private final MemberRepository memberRepository = new MemoryMemberRepository();
        private final MemberRepository memberRepository;
    
        public MemberServiceImpl(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    
        @Override
        public void join(Member member) {
            memberRepository.save(member);
        }
    
        @Override
        public Member findMember(Long memberId) {
            return memberRepository.findById(memberId);
        }
    }

</div>
</details>

<details>
<summary>OrderServiceImpl</summary>
<div mardown="1">

    public class OrderServiceImpl implements OrderService {
    
        // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
        // privat final MemberRepository memberRepository = new MemoryMemberRepository();
        private final MemberRepository memberRepository; 
        private final DiscountPolicy discountPolicy;
    
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    
        @Override
        public Order createOrder(Long memberId, String itemName, int itemPrice) {
            Member member = memberRepository.findById(memberId);
            int discountPrice = discountPolicy.discount(member, itemPrice);
    
            return new Order(memberId, itemName, itemPrice, discountPrice);
        }
    }


</div>
</details>

<details>
<summary>MemeberApp</summary>
<div mardown="1">

    //Spring을 사용하지 않고 순수 java로만 실행
    public class MemberApp {

        public static void main(String[] args) {
    //        MemberService memberService = new MemberServiceImpl();
    
            AppConfig appConfig = new AppConfig();
            MemberService memberService = appConfig.memberService();
    
            Member member = new Member(1L, "memberA", Grade.VIP);
            memberService.join(member);
    
            Member findMember = memberService.findMember(1L);
            System.out.println("new member = " + member.getName());
            System.out.println("find findMember : " + findMember.getName());
        }
    }
</div>
</details>

<details>
<summary>OrderApp</summary>
<div mardown="1">

    //Spring을 사용하지 않고 순수 java로만 실행
    public class OrderApp {

    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        // MemberService memberService = new MemberServiceImpl();
        // OrderService orderService = new OrderServiceImpl();
        MemberService memberService = appConfig.memberService();
        OrderService orderService = appConfig.orderService();


        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order : " + order.toString());
    }
}
</div>
</details>

<details>
<summary>OrderServiceTest</summary>
<div mardown="1">
    
    class OrderServiceTest {
    
    //    MemberService memberService = new MemberServiceImpl();
    //    OrderService orderService = new OrderServiceImpl();
    MemberService memberService;
    OrderService orderService;
    
        @BeforeEach
        public void beforeEach() {
            AppConfig appConfig = new AppConfig();
            memberService = appConfig.memberService();
            orderService = appConfig.orderService();
        }
    
        @Test
        void createOrder() {
    
            Long memberId = 1L;
            Member member = new Member(memberId, "memberA", Grade.VIP);
            memberService.join(member);
    
            Order order = orderService.createOrder(memberId, "itemA", 10000);
    
            Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    
        }
    }
    
</div>
</details>

<details>
<summary>MemberServiceTest</summary>
<div mardown="1">

    public class MemberServiceTest  {
    
    //    MemberService memberService = new MemberServiceImpl();
    MemberService memberService;
    
        @BeforeEach
        public void beforeEach() {
            AppConfig appConfig = new AppConfig();
            memberService = appConfig.memberService();
        }
    
        @Test
        void join(){
            //given
            Member member = new Member(1L, "memberA", Grade.VIP);
    
            //when
            memberService.join(member);
            Member findMember = memberService.findMember(1L);
    
            //then
            Assertions.assertThat(member).isEqualTo(findMember);
        }
    }
</div>
</details>

Q. 왜 test에서 @BeforeEach를 사용해서 받아줘야할까 ? 

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```

중복을 제거하고 역할에 따른 구현이 보이도록 리펙토링
```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(MemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(MemberRepository(), DiscountPolicy());
    }

    public DiscountPolicy DiscountPolicy() {
        return new FixDiscountPolicy();
    }

    public MemberRepository MemberRepository() {
        return new MemoryMemberRepository();
    }
}
```

* SRP 단일 책임 원칙에 따라 관심사를 분리하여 직접 객체를 생성하고, 연결하고, 실행하는 책임을 갖는 클라이언트에서 객체를 생성하고 연결하는 책임을 AppConfig가 담당한다.
* DIP 의존관계 역전 원칙을 위반하는 추상화와 구체 클래스를 모두 의존하는 클라이언트 코드에서 AppConfig를 통해 의존관계 주입(DI)을 하여 DIP 원칙을 지킨다.
* OCP 개방-폐쇄 원칙을 AppConfig를 통해 애플리케이션을 사용 영역과 구성 영역으로 분리하여 지켜졌다.


## IoC, DI, 컨테이너

### 제어의 역전 IOC(Inversion of Control)
* 인스턴스 생성부터 소멸까지의 인스턴스 생명주기 관리(제어 흐름)를 직접 하는 것이 아닌, 외부에서 대신 하는 방식.

프레임워크 vs 라이브러리
* 프레임워크는 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크다. (JUit)
* 내가 작성한 코드가 직접  제어의 흐름을 담당한다면 라이브러리다.
 
### 의존관계 주입 DI(Dependency Injection)
* 의존하는 객체를 직접 생성하는 대신 의존 객체를 전달받는 방식.
* 정적인 클래스 의존관계 - 클래스가 사용하는 import 코드만 보고도 의존관계를 판단할 수 있다. 어플리케이션을 실행하지 않아도 분석할 수 있다.
* 동적인 객체 클래스 의존관계 - 어플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 연결하는 관계이다. (DI)

1. 생성자 주입
2. setter 주입
3. 필드 주입

### IoC 컨테이너, DI 컨테이너
* 객체 생성을 관리하고 의존관계를 연결 시켜 주는 것.
* BeanFactory, ApplicationContext, AppConfig

### 스프링 적용

* 스프링 컨테이너 ApplicationContext를 사용하여 AppConfig를 사용하여 직접 객체를 생성하고 DI하지 않고, 스프링 컨테이너를 통하여 사용한다.
* 설정정보에 @Bean 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 등록된 객체를 스프링 빈이라 한다.
* 스프링 빈은 @Bean이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다.

Q. 스프링 컨테이너를 사용하면 어떤 장점이 있을까?

<details>
<summary>MemberApp</summary>
<div mardown="1">

    public class MemberApp {
    
        public static void main(String[] args) {
            // MemberService memberService = new MemberServiceImpl();
            
            // AppConfig appConfig = new AppConfig();
            // MemberService memberService = appConfig.memberService();
        
            ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
            MemberService memberService = ac.getBean(MemberService.class);
    
            Member member = new Member(1L, "memberA", Grade.VIP);
            memberService.join(member);
    
            Member findMember = memberService.findMember(1L);
            System.out.println("new member = " + member.getName());
            System.out.println("find findMember : " + findMember.getName());
        }
    }

</div>
</details>

<details>
<summary>OrderApp</summary>
<div mardown="1">

    public class OrderApp {

    public static void main(String[] args) {


        // MemberService memberService = new MemberServiceImpl();
        // OrderService orderService = new OrderServiceImpl();
        // AppConfig appConfig = new AppConfig();
        // MemberService memberService = appConfig.memberService();
        // OrderService orderService = appConfig.orderService();

        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ac.getBean(MemberService.class);
        OrderService orderService = ac.getBean(OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order : " + order.toString());
        }
    }
</div>
</details>

```java
@Configuration    
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(MemberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(MemberRepository(), DiscountPolicy());
    }

    @Bean
    public DiscountPolicy DiscountPolicy() {
        return new FixDiscountPolicy();
    }

    @Bean
    public MemberRepository MemberRepository() {
        return new MemoryMemberRepository();
    }
}
```

## 4. 스프링 컨테이너와 스프링빈

### 스프링 컨테이너
* ApplicationContext 는 인터페이스이고, 스프링 컨테이너다.
* 스프링 컨테이너 생성
```java
ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
```

![BeanFactory계층구조](https://velog.velcdn.com/images/justin403502/post/90b8dab5-0841-4ae5-ab81-1426a81e3b6b/image.png)

* BeanFactory - 객체 생성, 객체 검색 기능(getBean()), 싱글톤, 프로토타입 빈 확인 기능
* AnnotationConfigApplicationContext - 메세지 소스를 활용한 국제화, 환경 변수(로컬, 개발, 운영)구분해서 처리, 어플리케이션 이벤트, 리소스 조회


![AnnotationConfigApplicationContext](https://www.junyoung.xyz/assets/images/spring-core/0024-application-context.png)

스프링 빈을 관리하는 스프링 컨테이너
* AnnotationConfigApplicationContext - 자바 애노테이션을 이용하여 클래스로부터 객체 설정 정보를 가져온다.
* GenericXmlApplicationContext - XML로부터 객체 설정 정보를 가져온다.
* GenericGroovyApplicationContext - 그루비 코드를 이용해 설정 정보를 가져온다.

DI 컨테이너 - ApplicationContext, AppCofig 와 같이 의존성을 외부에서 외존관계 설정
Spring 컨테이너 - 스프링 빈을 관리하는 컨테이너(ApplicationContext)

### 스프링 빈

#### 스프링 빈 등록 
![스프링빈등록이미지](https://www.junyoung.xyz/assets/images/spring-core/0019-enroll-spring-bean.png)
* 스프링 컨테이너는 파라미터로 넘어온 클래스 정보를 사용해서 스프링 빈을 등록한다.
* 빈 이름은 메서드 이름을 사용한다
* @Bean(name="beanName")을 통해 빈 이름을 직접 부여할 수 있다.
* <U>**빈 이름은 항상 다른 이름을 부여**</U>해야 한다. 중복되면 다른 빈이 무시되가너 기존 빈을 덮어버리거나 오류가 발생할 수 있다.

> **참고**
> 
> 스프링인 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다. 근런데 이렇게 자바 코드로  스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다. 

getBeanDefinitionNames() - 리턴값 배열

getBeanDefinition() - 빈의 메타정보, 파라미터로 빈이름

getBeansOfType() - 리턴값 Map

### 컨테이너에 등록된 모든 빈 조회

```java
public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinationName);
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("어플리케이션 빈 출력하기")
    void findApplicationBean() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            // Bean메타정보를 가지는 클래스 BeanDefinition
            // AnnotationConfigApplicationContext에 정의 되어있기 때문에 ac를 업케스팅해서 쓰면 사용하지 못한다.
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            //Role ROLE_APPLICATIION : 직접 등록한 어플리케이션 빈
            //Role ROLE_iNFRASTRUCTURE : 스프링이 내무에서 사용한는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
}
```

### 스프링 빈 조회 - 기본
스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법
* ac.getBean(빈이름, 타입)
* ac.getBean(타입)
* 조회 대상 스프링 빈이 없으면 예외 발생 - **NoSuchBeanDefinitionException: No bean named 'xxxxx' available**

```java
public class ApllicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    //구체 타입으로 조회 - 유연성이 떨어진다
    @Test
    @DisplayName("구체타입으로 조회")
    void findBeanByType2() {
        MemberService memberService = ac.getBean(MemberServiceImpl.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("조회하는 빈이 없는 경우")
    void findBeanByNameX() {
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("abcd", MemberService.class));
    }
}
```

### 스프링 빈 조회 - 동일한 타입이 둘 이상
* 타입으로 조회시 같은 타입의 스프링 민이 둘 이상이면 오류 발생. 이름을 추가해 조회해야한다. - **NoUniqueBeanDefinitionException**
* ac.getBeanofType()을 사용하면 해당 타입의 모든 빈을 조회할 수 있다.

 ```java
 public class ApplicationContextSameBeanFindTest {

   AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

   @Test
   @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면, 중복 오류가 발생한다.")
   void findBeanByTypeDuplicate() {
      assertThrows(NoUniqueBeanDefinitionException.class,
              () -> ac.getBean(MemberRepository.class));
   }

   @Test
   @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
   void findBeanByName() {
      MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class);
      assertThat(memberRepository).isInstanceOf(MemberRepository.class);
   }

   @Test
   @DisplayName("특정 타입을 모두 조회하기")
   void findBeanByType() {
      Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);

      for (String key: beansOfType.keySet()) {
         System.out.println("key = " + key + " value = " + beansOfType.get(key));
      }

      System.out.println("beansOfType = " + beansOfType);
      assertThat(beansOfType.size()).isEqualTo(2);
   }

   @Configuration
   static class SameBeanConfig {

      @Bean
      public MemberRepository memberRepository1() {
         return new MemoryMemberRepository();
      }

      @Bean
      public MemberRepository memberRepository2() {
         return new MemoryMemberRepository();
      }

   }

}
```

### 스프링 빈 조회 - 상속관계
* 부모 타입으로 조회하면, 자식타입도 함께 조회된다.
* 'Object' 타입으로 조회하면, 모든 스프링 빈이 조회된다.

```java
 class ApplicationContextExtendsFindTest {

   AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

   @Test
   @DisplayName("부모 타입으로 조회 시 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
   void findBeanByParentTypeDuplicate() {
      assertThrows(NoUniqueBeanDefinitionException.class,
              () -> ac.getBean(DiscountPolicy.class));
   }

   @Test
   @DisplayName("부모 타입으로 조회 시 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다.")
   void findBeanByParentTypeBeanName() {
      DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
      assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
   }

   @Test
   @DisplayName("특정 하위 타입으로 조회")
   void findBeanBySubType() {
      RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);
      assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
   }

   @Test
   @DisplayName("부모 타입으로 모두 조회하기")
   void findAllBeanByParentType() {
      Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
      assertThat(beansOfType.size()).isEqualTo(2);
      for (String key: beansOfType.keySet()) {
         System.out.println("key = " + key + " value " + beansOfType.get(key));
      }
   }

   @Test
   @DisplayName("부모 타입으로 모두 조회하기 - Object")
   void findAllBeanByObjectType() {
      Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
      for (String key: beansOfType.keySet()) {
         System.out.println("key = " + key + " value " + beansOfType.get(key));
      }
   }

   @Configuration
   static class TestConfig {

      @Bean
      public DiscountPolicy rateDiscountPolicy() {
         return new RateDiscountPolicy();
      }

      @Bean
      public DiscountPolicy fixDiscountPolicy() {
         return new FixDiscountPolicy();
      }
   }

}
```

### XML 설정 사용
최근에는 스프링 부트를 많이 사용하면서 XML 기반의 설정은 잘 사용하지 않는다. 많은 레거시 프로젝트들이 XML로 되어있고, XML을 사용하면 컴파일 없이 빈 설정 정보를 변경할 수 있는 장점이 있어 알아두자.

<details>
<summary>XML 설정</summary>
<div mardown="1">

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="memberService" class="hello.core.member.MemberServiceImpl">
           <constructor-arg name="memberRepository" ref="memberRepository" />
       </bean>
   
       <bean id="memberRepository" class="hello.core.member.MemoryMemberRepository" />
   
       <bean id="orderService" class="hello.core.order.OrderServiceImpl">
           <constructor-arg name="memberRepository" ref="memberRepository" />
           <constructor-arg name="discountPolicy" ref="discountPolicy" />
       </bean>
   
       <bean id="discountPolicy" class="hello.core.discount.RateDiscountPolicy" />
   </beans>

</div>
</details>

### 스프링 빈 설정 메타 정보 - BeanDifinition
* 스프링은 BeanDefinition 추상화 인터페이스를 사용해 다양한 설정 형식을 지원한다.
* BeanDefinition을 빈 설정 메타정보라고 한다.
* @Bean, <bean> 당 하나의 메타 정보가 생성된다.
* 스프링 컨테이너는 이 메타 정보를 기반으로 스프링 빈을 생성한다.
* 
![BeanDefinitionImg](https://www.junyoung.xyz/assets/images/spring-core/0026-bean-definition.png)
![BeanDefinitionImg](https://www.junyoung.xyz/assets/images/spring-core/0027-application-context.png)


## 싱글톤 컨테이너

* 스프링이 없는 순수한 DI 컨테이너를 사용하면 요청 할 때마다 객체를 새로 생성해야한다. -> 메모리 낭비

```java
public class pureContainer {
    
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        // 1. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        // 참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        assertThat(memberService1).isNotSameAs(memberService2);
    }

}
```

해당 객체를 단 한 개만 생성하고, 공유하도록 설계 -> 싱글톤 패턴

### 싱글톤 패턴
* 클래스의 인스턴스가 단 한 개만 생성되는 것을 보장하는 디자인 패턴이다.
```java
public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
    
}
```
* static 영역에 객체 인스턴스를 미리 하나를 올려두고 공유해서 사용한다.
* 단 하나의 객체 인스턴스를 보장하기 위해 생성자를 private로 선언하여 외부에서 new를 통한 객체 생성을 막아준다.
```java
public class SingletonTest {

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
        // private으로 생성자 생성을 막아두어 컴파일 오류 발생
//        new SingletonService();

        // 1. 조회: 호출할 때마다 같은 객체를 반환
        SingletonService singletonService1 = SingletonService.getInstance();
        // 2. 조회: 호출할 때마다 같은 객체를 반환
        SingletonService singletonService2 = SingletonService.getInstance();

        // 참조 값이 같은 것을 확인
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        // singletonService1 == singletonService2
        assertThat(singletonService1).isSameAs(singletonService2);
        // same: ==
        // equal: equals 비교

        singletonService1.logic();
    }

}
```

### 싱글톤 패턴의 문제
* 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
* 의존관계상 클라이언트가 구체 클래스에 의존해야한다.
* 테스트하기 어렵다.
* private 생성자로 자식 클래스를 만들기 어렵다.
* 유연성이 떨어진다.
* 안티패턴으로 불리기도 한다.

## 싱글톤 컨테이너(스프링 컨테이너)
* 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
* 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 한다.
```java
public class SingletonTest {

   @Test
   @DisplayName("스프링 컨테이너와 싱글톤")
   void springContainer() {
      AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
      MemberService memberService1 = ac.getBean("memberService", MemberService.class);
      MemberService memberService2 = ac.getBean("memberService", MemberService.class);

      // 참조값이 다른 것을 확인
      System.out.println("memberService1 = " + memberService1);
      System.out.println("memberService2 = " + memberService2);

      assertThat(memberService1).isSameAs(memberService2);
   }

} 
```
> **참고**
>
> 스프링의 기본 빈 등록방식은 싱글톤 이지만, 요청할 때 마다 새로운 객체를 생성해서 반환하는 스코프 방식도 있다.

### 싱글톤 방식의 주의점
* 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기
때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
* 무상태(stateless)로 설계해야 한다.
  * 특정 클라이언트에 의존적인 플드가 있으면 안된다.
  * 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
  * 가급적 읽기만 가능해야 한다.
  * 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
* 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다!!

```java
public class StatefulService{
    
    private int price; // 상태를 유지하는 필드

   public static void main(String[] args) {
      System.out.println("name = " + name + " price = " + price);
      this.price = price; // 문제 발생 구간
   }
   
   public  int getPrice() {
       return price;
   }    
    
}
```
```java
class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // Thread A: A 사용자가 10000원 주문
        statefulService1.order("userA", 10000);
        // Thread B: B 사용자가 20000원 주문
        statefulService1.order("userB", 20000);

        // Thread A: A 사용자 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

    }

}
```

### @Configuration과 싱글톤

```java
@Configuration    
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(MemberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(MemberRepository(), DiscountPolicy());
    }

    @Bean
    public DiscountPolicy DiscountPolicy() {
        return new FixDiscountPolicy();
    }

    @Bean
    public MemberRepository MemberRepository() {
        return new MemoryMemberRepository();
    }
}
```

MemmoryMemberRepository가 3번 객체생성되는 자바코드가 실행되지만, 동일한 객체 반환이 보장된다.

@Bean 을 사용하여 Bean객체 생성을 선언하고, @Configuration을 통해 바이트조작 라이브러리(CGLIB)를 사용하여 Bean 생성하고, 이미 존재한는 Bean인 경우, 기존 객체를 리턴하며 singleton을 유지한다.



> CGLIB 확인해 보기
>
> System.out.println(ac.getBean(AppConfig.class).getClass()); 을 실행했을 때,
>
> @Configuration 제거 -> class spring.core.AppConfig
>
> @Configuration 설정 -> class spring.core.AppConfig$$EnhancerBySpringCGLIB$$11083357

CGLIB를 통해 생성된 Bean은 AppConfig를 상속받아 생성되었기 때문에, getBean(AppConfig.class)로 조회가 가능하다

@Configuration 없이 Bean을 등록하고 사용한다면 스프링 컨테이너가 관리하는 스프링빈을 사용한다고 할 수 없다.

## 컴포넌트 스캔

* 스프링 빈은 등록할 때, @Bean 이나 <bean> 등을 통해서 설정 정보에 직접 등록할 스프링 빈을 나열했다.
* 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔 기능을 제공한다.
* 의존관계도 자동으로 주입하는 @Autowired 기능도 제공한다.

@ComponentSacn 사용
```java
@Configuration
@ComponentScan(
        // 프로젝트에 수동으로 등록한 Bean을 제외하기위해 Configuration을 제외
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
```
* 스프링 빈으로 등록되야 하는 대상을 컴포넌트 스캔 대상으로 설정하기위한 @Component 애노테이션 추가
  * DI를 해줘야 할 객체를 Bean으로 등록 시켜야한다.(구체화 클래스 구현한 객체)
* @Autowired 로 자동 의존관계 주입 -> AppConfig로 return하며 의존관계를 수동으로 주입하던 방식에서 변경
  * 타입이 같은 빈을 찾아 주입한다.
* MemoryMemberRepository, RateDiscountPolicy, MemeberServiceImpl, OrderServiceImpl

<details>
<summary>MemberServiceImpl</summary>
<div mardown="1">

    @Component
    public class MemberServiceImpl implements MemberService{
    
        private final MemberRepository memberRepository;
    
        @Autowired  // ac.getBean(MemberRepository.class)과 유사하지만 @Autowired가 기능이 더 많다.;
        public MemberServiceImpl(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }
    
        ......
    
    }
</div>
</details>

![컴포넌트 스캔 이미지](https://www.junyoung.xyz/assets/images/spring-core/0031-component-scan.png)
* ComponentScan 은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다.
* 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞 글자만 소만자를 사용한다.
  * @Component("직접등록이름")으로 빈 이름을 직접 지정할 수 있다.

### 탐색 위치와 기본 스캔 대상
#### 탐색 위치
* 모든 자바 클래스를 컴포넌트 스캔하면 시간이 오래 걸리기 때문에 필요한 위치부터 탐색해야한다.
  * ComponentScan(basePackages = "패키지 경로")
* 지정하지 않으면 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치로 된다. 
  * 경로를 지정하지 않고 프로젝트 최상단(프로젝트 루트)에 설정 정보를 두고 사용하는 것이 권장된다. 

#### 기본 스캔 대상과 스프링 부가 기능
* @Component : 컴포넌트 스캔에서 사용한다.
* @Controller : 스프링 MVC 컨트롤러에서 사용한다. - 스프링 MVC 컨트롤러로 인식한다.
* @Service : 스프링 비지니스 로직에서 사용한다. - 부가기능은 없고, 개발자들이 비지니스 계층으로 인식한다.
* @Repository : 스프링 데이터 접근 계층에서 사용한다. - 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환한다.
* @Configuration : 스프링 설정 정보에서 사용한다. - 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리한다.

> **참고**
> 
> 애노테이션은 상속관계라는 것이 없다.
> 
> 애노테이션이 특정 애노테이션을 들고 있는 것을 인식하는 것은 자바가 아닌, 스프링이 지원하는 기능이다.

### 컴포넌트 스캔 필터
* includeFilters : 컴포넌트 스캔 대상을 추가로 지정
* excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정

<details>
<summary>MyIncludeComponent Annotation</summary>
<div mardown="1">

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface MyIncludeComponent {
    }
</div>
</details>
<details>
<summary>MyExcludeComponent Annotation</summary>
<div mardown="1">

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface MyExcludeComponent {
    }
</div>
</details>
<details>
<summary>MyIncludeComponent Annotation 이 적용된 BeanA</summary>
<div mardown="1">

    @MyIncludeComponent
    public class BeanA {
    }
</div>
</details>
<details>
<summary>MyExcludeComponent Annotation 이 적용된 BeanB</summary>
<div mardown="1">

    @MyExcludeComponent
    public class BeanB {
    }
</div>
</details>

```java
public class ComponentFilterAppConfigTest {
    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> ac.getBean("beanB", BeanB.class)
        );
    }

    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class ComponentFilterAppConfig {
    }
}
```

#### FilterType 옵션
* ANNOTATION: 기본값, 애노테이션을 인식해서 동작한다.
  * ex) org.example.SomeClass
* ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작한다.
  * ex) org.example.SomeClass
* ASPECTJ: AspectJ 패턴 사용
  * ex) org.example..*Service+
* REGEX: 정규 표현식
  * ex) org\.example\.Default.*
* CUSTOM: TypeFilter이라는 인터페이스를 구현해서 처리
  * ex) org.example.MyTypeFilter

> **참고**
> 
> @Component면 충분하기 때문에, includeFilters를 사용할 일은 거의 없다.
> 
> excludeFilters는 여러가지 이유로 간혹 사용할 때가 있지만 많지 않다.
> 
> 최신 스프링 부트는 컴포넌트 스캔을 기본으로 제공하고, 스프링의 기본 설정에 맞추어 사용하는 것을 권장한다.


### 스프링빈 중복 등록과 충돌
스프링 빈의 충돌은 2가지로 볼 수 있다.

1. 자동 빈 등록 vs 자동 빈 등록
* 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 이름이 같은 경우 스프링 오류가 발생한다.
  * ConflictingBeanDefinitionException 예외 발생

2. 수동 빈 등록 vs 자동 빈 등록
* 이 경우 수동 빈 등록이 우선권을 가진다.(오버라이딩 된다)
* 수동 빈 등록시 남는 로그
> Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing
* 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌하면 오류를 발생하도록 수정되었다.
> Consider renaming one of the beans or enabling overriding by setting
>
> spring.main.allow-bean-definition-overriding=true

## 의존관계 자동 주입

1. 생성자 주입
2. 수정자 주입(setter 주입)
3. 필드 주입
4. 일반 메서드 주입

### 생성자 주입
* 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
* 불변, 필수 의존관계에 사용된다.

```java
@Component
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

}
```
**생성자가 딱 한 개만 있으면 @Autowired를 생햑해도 자동 주입된다.(스프링 빈만 해당) 


### 수정자 주입(setter 주입)

* setter라 불리는 필드의 값을 변경하느 수장저 메서드를 통해서 의존관계를 주입하는 방법이다.
* 선택, 변경 가능성이 있는 의존관계에 사용된다.
* 자바 빈 프로퍼티 규약의 수정자 메서드 방식을 사용한느 방법이다.
```java
@Component
public class OrderServiceImpl implements OrderService {

    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired(required = false)
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```
> **참고**
> 
> @Autowired는 주입 대상이 없으면 오류가 발생한다. 주입할 대상이 없어도 작동하려면 
> 
> @Autowired(required = false) 로 지정하면 된다.

### 필드 주입
* 코드가 간결하지만 외부에서 변경이 분라능해 테스트 하기 힘들다.
* DI 프레임워크가 없으면 아무것도 할 수 없다.
* 테스트 코드, 스프링 설정을 목적으로 한느 @Configuration 같은 특별한 용도에만 사용한다.
```java
@Component
public class OrderServiceImpl implements OrderService {
    @Autowired private MemberRepository memberRepository;

    @Autowired private DiscountPolicy discountPolicy;
} 
```

### 일반 메서드 주입
* 한 번에 여러 필드를 주입 받을 수 있다.
* 일반적으로 잘 사용하지 않는다.
```java
@Component
public class OrderServiceImpl implements OrderService {

    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

}
```

### 옵션 처리
1. @Autowired(required = false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출이 안된다.
2. org.springframwork.lan.@Nullabe : 자동 주입할 대상이 없으면 null이 입력된다.
3. Ontional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.

```java
public class AutowiredTest {

    @Test
    void AutowiredOption() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    //Member type의 스프링 빈이 등록 안된상태
    static class TestBean {

        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);//실행 X
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);// noBean2 = null
        }

        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);//noBean3 = Optional.empty
        }

    }

}
```

### 결론은 생성자 주입
* 과거에는 수정자 주입과 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다.

불변
* 대부분의 의존관계 주입은 한 번 일어나면 애플리케이션 종료 시점까지 의존관계를 변경할 일이 없다.
  * 오히려 대부분 의존관계가 변하면 안되는 경우가 많다.
* 수정자 주입을 사용하면 setter 메서드를 public으로 열어두어야 하고, 변경될 가능성이 있다.
* 생성자 주입은 객체를 생성할 때 단 한 번만 호출되므로 불변한게 설계할 수 있다.

누락
* 생성자 주입을 사용하면 주입 데이터를 누락했을 때 컴파일 오류가 발생한다.
  * 수정자 의존관계의 경우 테스트가 실행된 후 exception 오류가 발생할 수 있다.

final 키워드
* 생성자 주입을 사용하면 final 키워드를 사용하여 값이 설정되지 않는 오류를 컴피알 시점에 막을 수 있다.

### 롬복과 최신 트랜드
final을 통한 생성자 주입을 간소화

1. gradle 설정
2. Preference -> Plugin -> Lombok 플러그인 설치
3. Preference -> Anootation Processors -> Enable annotation processing 체크

<details>
<summary>build.gradle 설정</summary>
<div mardown="1">

    //lombok 설정 추가
    
    ......
    
    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }
    
    dependencies {

    ......

    //lombok 라이브러리 추가
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'

    ......
    }

</div>
</details>

```java
Import lombok.Getter;
Import lombok.Setter;

@Getter
@Setter
@ToString
public class HelloLombok {
    
    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("name");
        helloLombok.getName(); // name
        helloLombok; // toString 호출
    }
}
```

<details>
<summary>Lombok 미적용 기존 OrderServiceImpl</summary>
<div mardown="1">
@Component

    public class OrderServiceImpl implements OrderService {
    
        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;
    
        @Autowired
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    
    }
</div>
</details>

Lombok 적용한 OrderServiceImpl

```java
@Component
@RequiredArgsConstructor //final이 붙은 필드의 생성자를 만들어 준다.
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
}
```

### 조회 빈이 2개 
@Autowired는 타입으로 조회한다. 조회된 타입이 2개 이상일 때, NoUniqueBeanDefinitionException 에러 발생

해결방법
1. @Autowired 필드명 매칭
2. @Qualifier 매칭
3. @Primary 사용

* @Autowired 필드명 매칭
@Autowired는 타입 매칭을 시도한 다음, 빈이 여러개 있으면 필드 이름, 파라미터 이름으로 빈을 조회한다.

```java
//DiscountPolicy를 상속받은 rateDiscountPolicy와 fixDiscountPolicy 모두 Bean으로 등록 되어 있다면
//@Autowired
//private DiscountPolicy discountPolicy //NoUniqueBeanDefinitionException 에러 발생
@Autowired
private DiscountPolicy rateDiscountPolicy
```

* @Qualifier 매칭
빈 등록시 @Qualifier를 붙이고, 이름을 지정한다 -> @Autowired로 받을 때, @Qualifier이름으로 매칭

@Qualifier로 주입할 때 해당 이름을 갖는 Qualifier를 찾지 못한다면, 해당 이름을 갖는 스프링 빈을 추가로 탐색한다.

@Qualifier는 @Qualifier를 찾는 용도로만 사용하는 것이 좋다.

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}
```
```java
@Autowired
public DiscountPolicy setDiscountPolicy(@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    return discountPolicy;
}
```

* @Primary 사용
우선수위를 정하는 방법이다.
```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {} 
```

* @Primary, @Qualifier 활용

메인 데이터베이스의 커넥션을 획득하는 스프링 빈은 @Primary를 적용해서 조회하는 곳에서 @Qualifier 지정 없이 편리하게 조회 가능하다.

서브 데이터베이스 커넥션 빈을 획득할 때는 @Qualifier를 지정해서 명시적으로 획득 하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있다.

* @Qualifier가 @Primary보다 우선순위가 높음

스프링은 자동보다는 수동, 넓은 범위의 선택권보다는 좁은 범위의 선택권이 우선 순위가 높음


### 애노테이션 직접 만들기
@Qualifier("mainDiscountPolicy")처럼 문자를 적으면 컴파일시 타입 체크가 안된다.

다음과같이 애노테이션ㅇ르 만들어서 문제를 해결할 수 있다.
```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```
```java
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{
    ......
}
```
```java
@Component
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    ......

}
```

### 조회한 빈이 모두 필요할 때, List, Map
의도적으로 정말 해당 타입의 스프링 빈이 다 필요한 경우도 있다.

예를 들어 할인 서비스를 제공하는데, 클라이언트가 할인종류(rate, fix)를 선택할 수 있다고 가정해보자.

스프링을 사용하면 소위 말하는 전략 패턴을 매우 간단하게 구현할 수 있다.

```java
public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(2000);
    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("polices = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }

    }

}
```

로직분석

* DiscountService는 Map으로 모든 DiscountPolicy를 주입받는다
* Map으로 fixDiscountPolicy, rateDiscountPolicy가 모두 주입된다
* discount() 메서드는 discountCode을 통해 스프링 빈을 찾아서 실행한다
* “fixDiscountPolicy”가 넘어오면 fixDiscountPolicy 스프링 빈을 찾아서 실행한다
* “rateDiscountPolicy”가 넘어오면 rateDiscountPolicy 스프링 빈을 찾아서 실행한다

주입 분석

* Map<String, DiscountPolicy>: Map의 키에 스프링 빈의 이름을 넣어주고, 그 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담는다.
* List<DiscountPolicy>: DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담는다.
* 만약 해당하는 타입의 스프링 빈이 없으면, 빈 컬렉션이나 Map을 주입한다.

* DiscountPolicy타입의 스프링빈을 Map으로 생성자 주입으로 받은 DiscountService를 빈을로 등록하고, getBean을 통해 꺼내 사용한다.

### 자동, 수동의 올바른 실무 운영 기준

* 스프링 출시 이후 점점 자동을 선호하는 추세다.
* 스프링은 @Component, @Controller, @Service, @Repository처럼 계층에 맞춰 애플리케이션 로직을 자동으로 스캔할 수 있도록 지원한다.
* 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있음
 
수동 빈 등록은 언제 사용하면 좋을까?
* 애플리케이션은 크게 업무 로직과 기술 지원 로직으로 나눌 수 있다.
* 업무 로직(자동 빈 등록 권장)
  * 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리 등, 보통 비지니스 요구사항을 개발할때 추가 변경된다.
  * 보통 문제가 발생해도 어떤곳에서 문제가 발생했는지 명확하게 파악하기 쉽다.
  * 업무 로직은 숫자도 매우 많고, 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리 처럼유사한 패턴이 있다.
  * 비지니스 로직 중에서 다형성을 적극 활용한 경우는 수동 빈 등록을 사용해서 구조가 명확하게 이해되도록 설계해야 할 때도 있다.
* 기술 지원 로직(수동 빈 등록 권장)
  * 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술이다.
  * 업무 로직과 달리 로직이 잘 적용되고 있는지 아닌지 파악하기 힘든 경우도 있다.


## 빈 생명주기 콜백

데이터베이스 커넥션 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 종료하는 작업을
진행하려면, 객체의 초기화와 종료 작업이 필요하다.

간단하게 외부 네트워크에 미리 연결하는 객체를 하나 생성한다고 가정해보자.
```java
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메시지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

}
```
```java
public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
        /**
         * close는 ApplicationContext에서 지원하지 않음
         * AnnotationConfigApplicationContext 이나 ConfigurableApplicationContext 을 사용해야 함
         */
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }

    }

}
```

> 생성자 호출, url = null
> 
> connect: null
> 
> call: null message = 초기화 연결 메시지

객체를 생성하는 단계에는 url이 없고, 객체 생성 후 외부에서 수정자 주입을 통해 setUrl()이 호출되어야 url이 존재한다.

생성자의 파라미터로 url을 받아 처리할 수 있지만 권장하지 않는다.

이유는 객체의 생성과 초기화를 분리해야하기 때문이다.

생성자는 필수 정보(파라미터)를 받고 메머리를 ㅎ살당해서 객체를 생성하는 책임을 갖는다. 반면 초기화는
이렇게 생성된 값을 활용해서 외부 커넥션을 연결하는 등 무거운 동작을 수행한다.

따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것보다는 객체를 생성하는 부분과 초기화 하는 부분을 명확하게  나누는 것이 유지보수 관점에서 좋다.

### 스프링 빈 라이프 사이클
객체 생성 -> 의존관계 주입
* 스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 이용할 수 있는 준비가 완료된다.
* 초기화 작업은 의존관계 주입이 모두 완료된 후 호출해야한다.
* 스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메스드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.
* 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다.

### 스프링 빈 이벤트 라이프사이클
스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> 스프링 종료

### 스프링의 3가지 빈 생명주기 콜백
* 인터페이스(InitializingBean, DisposableBean)
* 설정 정보에 초기화 메서드, 종료 메서드 지정
* @PostConstruct, @preDestroy 애노테이션 지원


### 인터페이스(InitializingBean, DisposableBean)
```java
public class NetworkClient implements InitializingBean, DisposableBean {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
```

> 생성자 호출, url = null
> NetworkClient.afterPropertiesSet
> connect: http://hello-spring.dev
> call: http://hello-spring.dev message = 초기화 연결 메시지
> 10:54:12.230 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@f5ac9e4, started on Thu Aug 04 10:54:11 KST 2022
> NetworkClient.destroy
> close: http://hello-spring.dev

초기화, 소멸 인터페이스 단점
* 코드가 스프링 전용 인터페이스에 의존한다.
* 초기화, 소멸 메서드의 이름 변경이 불가능하다.
* 코드를 고칠 수 없는 외부 라이브러리에는 적용 불가하다.

### 빈 등록 초기화, 소멸 메서드
```java
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
    
}
```
```java
public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
        /**
         * close는 ApplicationContext에서 지원하지 않음
         * AnnotationConfigApplicationContext 이나 ConfigurableApplicationContext 을 사용해야 함
         */
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }

    }

}
```
초기화, 소멸 메서드 특징
* 메서드 이름을 자유롭게 줄 수 있다.
* 스프링 코드에 의존하지 않는다.
* 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.

종료 메서드 추론
* 대부분의 라이브러리는 close, sutdown 이름의 종료 메서드를 사용한다.
* @Bean의 dstroyMethod는 기본값이 추론인 (inffered)로 등록되어 있어 close, shutdown 이름의 메서드를 자동 호출한다.
* 추론기능을 사용하지 않는다면 distroyMethod = "" 로 지정하면 된다.

### 애노테이션 @PostConstruct, @PreDestory
```java
public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }

}
```

@PostConstruct, @PreDestroy 애너테이션 특징
* 최신 스프링에서 가장 권장하는 방법이다.
* 스프링에 종속적인 기술이 아닌 javax.annotation.PostConstruct 패키지의 JSR-250 자바 표준이라 스프링이 아닌 다른 컨테이너에서도 동작한다.
* 컴포넌트 스캔과 잘 어울린다.
* 코드를 고칠 수 없는 외부 라이브러리에는 적용할 수 없다 -> 초기화, 종료 메서드 이용

## 빈 스코프

스프링 빈의 기본값은 싱글톤 스코프로 생성되기 때문에, 스프링 컨테이너의 시작과 함께 생성되어서 스프링 컨테이너가 종료될 때 까지 유지되었다.
@Scope를 통해 싱글톤 이외의 다양한 범위를 지정할 수 있다.

* 싱글톤 : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다.
* 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프이다.
* 웹 관련 스코프
  * request : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프이다.
  * session : 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프이다.
  * application : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프이다.

컴포넌트 스캔 자동 등록
```java
@Scope("prototype")
@Component
public class HelloBean {}
```
수동 등록
```java
@Scope("prototype")
@Bean
PrototypeBean helloBean() {
        return new HelloBean();
}
```


### 프로토타입 스코프
싱글톤 스코프의 빈을 조회하면 스프링 컨테이넌느 항상 같은 인스턴스의 스프링 빈을 반환한다. 반면에 프로토타입 스코프를 
스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환한다.

![프로토타입 빈](https://www.junyoung.xyz/assets/images/spring-core/0036-request-prototype-bean-2.png)

1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청한다
2. 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고 필요한 의존관계를 주입한다.
3. 스프링 컨네이너는 새엇ㅇ한 프로토타입 빈을 클라이언트에 반환한다.
4. 이후 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환한다.

* 스프링 컨테이너는 프로토타입 빈을 생성하고 의존관계 주입, 초기화 까지만 처리한다.
* 클라이언트에 빈을 반환 후, 스프링 컨테이너는 생성된 프로토타입 빈을 관리하지 않는다.
* @PreDestory와 같은 종료 메서드가 호출되지 않는다. 프로토타입 빈을 관리할 책임은 클라이언트에 있다.


### 싱글톤 스코프 빈 테스트
```java
public class SingleTonTest {

    @Test
    void singletonBean() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close();

    }

    @Scope("singleton")
    static class SingletonBean {
        @PostConstruct
        public  void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }

}
```
> SingletonBean.init
> 11:56:24.277 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@59fd97a8, started on Thu Aug 04 11:56:24 KST 2022
> SingletonBean.destroy


### 프로토타입 스코프 빈 테스트
```java
public class PrototypeTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

    @Test
    public void prototypeBeanFind() {
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);
    }

    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
```

> find prototypeBean1
> PrototypeBean.init
> find prototypeBean2
> PrototypeBean.init
> 12:00:03.061 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@647e447, started on Thu Aug 04 12:00:02 KST 2022

프로토타입 빈의 특징 정리
* 스프링 컨테이너에 요청이 있을 때 마다 새로 생성된다.
* 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입, 초기화 까지만 관여한다.
* 종료 메서드가 호출되지 않는다.
* 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 한다. 종료 메서드 호출도 클라이언트가 직접 해야한다.


### 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점

프로토타입 스코프의 빈을 요청하면 항상 새로운 객체 인스턴스를 생성해서 반환해야 하지만, 싱글톤 빈과 함께 사용할 때는 의도한대로 잘 동작하지 않는다.

![싱글톤빈과 프로토타입빈 함께사용 이미지](https://www.junyoung.xyz/assets/images/spring-core/0040-singleton-prototype-2.png)

PrototypeBean은 프로토타입 스코프 이지만 싱글톤인 ClientBean의 인스턴스가 생성될 때, 의존성 주입 시기에 PrototypeBean의 인스턴스를 요청해 한번 생성하고 
받은 후, 다시는 PrototypeBean 객체를 요청하지 않고 기존에 받은 PrototypeBean 인스턴스를 사용하기 때문에 문제가 발생한다.

```java
public class SingletonWithPrototypeTest {

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean1.logic();
        assertThat(count2).isEqualTo(2);
    }

    @Scope("singleton")
    static class ClientBean{

        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }

    }

}
```
이 test결과는 성공한다. ClientBean의 인스턴스를 만들고 logic 메서드를 호출하더라도 프로토타입 빈을 활용하여 매번 count 1이 반환되도록 하지 못한 것이다.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SingletonWithPrototypeTest {

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean1.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean {

        @Autowired
        ApplicationContext ac;

        public int logic() {
            PrototypeBean prototypeBean = ac.getBean(Prototype.class);
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    ....

}
```

* logic 메서드를 호출 할 때마다 스프링 컨테이너에서 직접 받아와서 해결할 수 있다.
* 의존관계를 외부에서 주입(DI) 받는게 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 Dependecy Lookup(DL) 의존관계 조회라 한다.
* 이렇게 스프링 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위테스트도 어려워진다.
* 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는 DL정도의 기능만 제공하는 기능을 사용하자.

### ObjectFactory, ObjectProvider
```java
public class SingletonWithPrototypeTest {

    ......

    @Scope("singleton")
    static class ClientBean{

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    ......

}
```

* prototypeBeanProvider.getObject()를 통해 항상 새로운 프로토타입 빈이 생성된다.
* 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다.(DL)
* 스프링의 기능을 사용하지만 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기 쉽다.
* ObjectFactoty : 기능이 단순하고 별도 라이브러리가 필요없다. 스프링에 의존적이다.
* ObjectProvider : ObjectFactoty를 상송 받고 옵션, 스트림 처리 등의 편의 기능을 제공하고 별도 라이브러리가 필요없다. 스프링에 의존적이다.

### JSR-330 Provider
javax.inject.Provider라는 JSR-330 자바 표준을 사용한다.
별도 라이브러리 추가가 필요하다.
> implementation 'javax.inject:javax.inject:1'

```java
public class SingletonWithPrototypeTest {
    ......

    @Scope("singleton")
    static class ClientBean{

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    ......
}
```
자바 표준 기술이라 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.

### 웹 스코프
스프링이 해당 스코프의 종료시점까지 관리한다. 웹 환경에서만 동작한다.

웹 스코프 종류
* request : HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리된다.
* session : HTTP Session과 동일한 생명주기를 가지는 스코프
* application : 서블릿 컨텍스트와 동일한 생명주기를 가지는 스코프
* websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

#### request 스코프 예제 만들기
웹 스코프는 웹 환경에서만 동작하므로 웹 환경이 동작하도록 라이브러리 추가
> implementation 'org.springframework.boot:spring-boot-starter-web'

* spring-boot-starter-web 라이브러리를 추가하면 스브링 부트는 내장 톰캣 서버를 활용하여 웹 서버와 스프링을 함께 실행한다.
  * 웹 라이브러리가 추가되면 웹과 관련된 추가 설정과 환경이 필요하므로 AnnotationConfigServletWebServerApplicationContext를 기반으로 애플리케이션 구동
  * 스프링 부트는 웹 라이브러리가 없으면 AnnotationConfigApplicationContext를 기반으로 애플리케이션 구동

다음과 같이 로그가 남도록 request 스코프를 활용한 기능 예제
> [d06b992f...] request scope bean create
> [d06b992f...][http://localhost:8080/log-demo] controller test
> [d06b992f...][http://localhost:8080/log-demo] service id = testId
> [d06b992f...] request scope bean close

```java
@Component
@Scope(value = "request")
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "][" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }

}
```

* 빈은 HTTP 요청 당 하나씩 생성되므로, uuid를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
* requestURL은 빈이 생성되는 시점에는 알 수 없으므로, 외부에서 setter로 입력 받는다.
Q. request 빈 이기 때문에 요청이 들어온 시점에 만들어 지기때문에 requestURL을 알 수 있는거 아닌가 ?
A. 요청이 들어온 시점은 controller가 작동하기도 전인 시점이다. url mapping된 시점이 아니다.

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testID");
        return "OK";
    }

}
```

> requestURL을 MyLogger에 저장하는 부분은 컨트롤러 보다는 공통 처리가 가능한 스프링 인터셉터나 서블릿 필터 같은 것을 활용하는 것이 좋다.
> 스프링 웹에 익숙하다면 인터셉터를 사용해서 구현해보자

```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}
```

* request scope를 사용하지 않고 파라미터로 이 모든 정보를 서비스 계층에 넘긴다면, 파라미터가 많아서 지저분해진다. 또한 requsetURL 같은 웹과 관련된 정보가 웹과 관련없는 서비스 계층까지 넘어가는 문제가 발생한다.
* 웹과 관련된 부분은 컨트롤러까지만 사용해야 한다. 서비스 계층은 웹 기술에 종속되지 않고, 가급적 순수하게 유지하는 것이 유지보수 관점에서 좋다.
* request scope의 MyLogger 덕분에 이런 부분을 파라미터로 넘기지 않고, MyLogger의 멤버변수에 저장해서 코드와 계층을 깔끔하게 유지할 수 있다.

실행결과

request 스코프 빈은 실제 요청이 들어와야 빈을 생성할 수 있으므로, 현재 애플리케이션은 실행시 오류가 발생한다.


### 스코프와 Provider
Provider








### 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider로 문제 해결

* Provider를 사용하여 requst 스코프 타입의 빈이 생성되고나서 DL을 통해 가져와 사용하면 된다.
* Controller나 Service 로직 이전에서 이미 request 스코프 빈이 생성 되어 있다.

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testID");
        return "OK";
    }

}
```
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final ObjectProvider<MyLogger> myLoggerProvider;

    MyLogger myLogger = myLoggerProvider.getObject();
    public void logic(String id) {myLogger.log("service id = " + id);
    }
}
```

### 스코프와 프록시
```java
@Component
//파라미터가 하나일 때는 value를 생량가능 
//적용대상이 class면 TARGET_CLASS , interface면 INTERFACES
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "][" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }

}
```
```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();

        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testID");
        return "OK";
    }

}
```
```java
@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}
```
> System.out.println("myLogger = " + myLogger.getClass());
> 
> 결과는
> 
> myLogger = class hello.core.common.MyLogger$$EnhancerBySpringCGLIB$$e126e0cb

* CGLIB 라이브러리를 사용해 MyLogger 클래스의 가짜  프록시 객체를 만들어 주입한다.
* 의존관계 주입도 가짜 프록시 객체가 주입된다.
![스코프와 프록시](https://www.junyoung.xyz/assets/images/spring-core/0043-proxy.png)

* 가짜 프록시는 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임로직이 들어있다.
* 가짜 프록시 빈은 내부에 실제 MyLogger를 찾는 방법을 가지고 있다.
* 클라이언트가 myLogger.logic()을 호출하면 사실 가짜 프록시 객체의 메서드를 호출하고, 가짜 프록시 겍체는 진짜 myLogger.logic()을 호출한다.
* 가짜 프록시는 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 동일하게 사용할 수 있다(다형성)

#### 특징 정리
* 프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사요할 수 있다.
* Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다.
* 애너테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI 컨테이너가 가진 큰 강점이다.

### 주의점
* 마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 결국 주의해서 사용해야 한다.
* 이런 특별한 scope는 꼭 필요한 곳에만 최소화해서사용해야한다. 무분별하게 사용하면 유지보수하기가 어려워진다.




헷갈린 부분
1. AppConfig or AppContext 로 수동 Bean 등록시 @Bean만으로 스프링이 관리하는 객체인 Bean으로 생성되나? new AnnotationConfigApplicationContext(AppConfig.class)로 객체를 생성할 때, Bean 이 생성되나 ?
2. AppConfig를 new 해서 Bean등록 안하고 직접 사용하는 것과 Bean등록하여 스프링이 관리하는 객체로 사용하는 것의 차이점은 ?
3. getBean으로 Bean 가져올 때와, 생성자 주입으로 받을 때는 어떤차이? 어떤때 어떤거 쓰는거지 ? 스프링 사용하는 곳에서 get.Bean 말고 @AutoWired로 받으면 어떻게 되는거지 :?
4. 

___
1. AnnotationConfigApplicationContext 로 객체 생성할 때, Bean 등록된다?
2. 스프링 컨테이너를 이용하면 싱글톤을 보장해준다.
3. 테스트 할때 말고는 @Autowired로 받는거 같은데 ?