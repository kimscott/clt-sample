## 게이트웨이에 spring security 적용

테스트 방식은

-- 접근 못하는 것을 확인
http localhost:8080/budget/check/b  

-- 다이렉트 접근은 가능
http localhost:8084/budget/check/b  

-- 발행된 jwt 토큰을 header 에 심어서 넘기면 값이 나오는 것을 확인
http localhost:8080/budget/check/b "Authorization: Bearer 토큰"  