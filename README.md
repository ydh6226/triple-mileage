# 트리플여행자 클럽 마일리지 서비스

## 실행 방법

- Docker 설치 후 Querydsl 컴파일과 mysql, redis 컨테이너를 띄우기 위해 아래 명렁어를 실행해주세요.


```
chmod +x init.sh && ./init.sh
```


```
# init.sh

echo "=== chmod +x gradlew ==="
chmod +x gradlew

echo "=== gradlew clear ==="
./gradlew clean

echo "=== build compileQuerydsl ==="
./gradlew build compileQuerydsl

echo "=== docker compose up ==="
docker compose up -d
```
- `MileageApplication.java` 를 실행해 API 서버를 실행해주세요.
  - flyway 내용에 맞게 테이블이 생성됩니다.

## API

### 이벤트 생성
[POST] /events
```
POST localhost:8080/events
{
    "type" : "REVIEW",
    "action" : "ADD",
    "reviewId" : "00001f9f-9406-4790-9d40-c50e00251013",
    "attachedPhotoIds" : [
        "00001f9f-9406-4790-9d40-c50e00251999"
    ],
    "content" : "",
    "userId" : "00013bd2-12da-4157-b590-51a3b7716013",
    "placeId" : "0001d992-236b-4815-a3e8-7803e2fb4013"
}
```

### 유저 포인트 조회
[GET] /point/{userId}
```
GET localhost:8080/point/00013bd2-12da-4157-b590-51a3b7715555
```

### 유저 포인트 내역 조회
[GET] /point/{userId}/events
```
GET localhost:8080/point/00013bd2-12da-4157-b590-51a3b7715555/events
```

## 기술 스택
- SpringBoot
- JPA, Querydsl
- Mysql
- Redis
  - API가 동시에 호출됐을 때 '특정 장소 첫 리뷰 포인트'가 여러명에게 지급되는걸 방지하기 위해 분산 락 목적으로 사용했습니다.

## ERD
![mileage-erd](https://user-images.githubusercontent.com/53700256/177005373-5bc50b7b-af99-4e32-aadf-2ba2de702dd6.png)

- point
  - 누적 포인트 관리
- point_event
  - 포인트 변경 내역 관리

```
# Index

# 특정 장소의 첫 리뷰인지 조회하는 쿼리의 성능 최적화를 위해 커버링 인덱스 생성 (PointEventQueryRepository.existsActiveReviewAt())
create index point_event_index_place_Id_and_review_id_and_mileage
    on point_event (place_id, review_id, mileage);

create index point_event_index_place_Id_and_user_id_and_create_date
    on point_event (place_id, user_id, created_date);

create index point_event_index_user_id_and_created_date
    on point_event (user_id, created_date);

create index point_event_review_id
    on point_event (review_id, created_date);
```

