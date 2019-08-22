## 게이트웨이에 spring security 적용

테스트 방식은

-- 접근 못하는 것을 확인
http localhost:8080/budget/check/b  

-- 다이렉트 접근은 가능
http localhost:8084/budget/check/b  

-- 발행된 jwt 토큰을 header 에 심어서 넘기면 값이 나오는 것을 확인
http localhost:8080/budget/check/b "Authorization: Bearer 토큰"  

## 게이트웨이에서 인증을 사용안하는 방법
pom.xml 에서 security 부분을 모두 주석 처리한다.

주석 처리를 하고나면 JwkSetEndpointConfiguration 파일과 ResourceServerConfiguration 피일이 에러가 날것이기 떄문에
모두 주석 처리를 한다.

aplication.yaml 파일에 security 부분을 주석 차리한다.
```yaml
#  security:
#    oauth2:
#      resourceserver:
#        jwt:
#          jwk-set-uri: http://localhost:8080/.well-known/jwks.json
```