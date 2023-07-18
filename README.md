# comall
쇼핑몰 백엔드 포트폴리오

## 사용 언어 및 프레임워크

Java 17 , Springboot 3.1.1

### 왜 17, 3?

- 현재 재직중인 회사에서 내부 고도화를 위해 사용 하기 때문에 적응을 위해.<br/>
- 1번 이유와 이어서 aws를 고려해 jdk도 다소 패키지적 제한이 있는 corretto17 을 사용.<br/>
- java8, 11과 어떤차이가 있나 찾아보고 경험 해보기 위해.




### 육각형 아키텍처는 왜?

1. 기본적으로 java 17을 쓰는 이유와 동일(회사적용)
2. 의존성 제거와 사이드이펙트를 최소화 하기 위해
    - 블로그에 따로 작성.
    - https://velog.io/@ksw_dev/%ED%8C%A8%ED%82%A4%EC%A7%80-%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4%EC%97%90-%EB%8C%80%ED%95%9C-%EC%83%9D%EA%B0%812

### 추후 구현 목록
- dev, prod profile 생성후 aws 연동 
- jwt 인증 로그인 및 기본 보안 설정
- 공지,이벤트 게시판 관련 api
- 주문(일단 기본적인것 위주)
