# ❗클라우드 서버 비용으로 인한 운영 중지❗

<div id="project_logo" align="center" >
  <img src="https://user-images.githubusercontent.com/15176192/144699806-1ce60d8b-3f9d-4c86-8b74-75baa37eb4d4.png" width="500" height="500" />
</div>


# sago 🎨
- Rest API를 활용한 실시간 온라인 경매 거래 플랫폼 애플리케이션입니다.
- 회원가입 후 이용 가능하며 실시간 채팅창을 통해 경매에 참여할 수 있습니다.
- 해당 프로젝트는 Spring boot & MyBatis를 활용한 백엔드 기술 학습을 목적으로 만든 프로젝트입니다.
- 프론트앤드의 경우 프로토타입 화면으로 대체하였습니다.
- 개발 시 IntelliJ IDE, DataGrip IDE를 사용하였습니다.(Database IDE인 DataGrip 권장)
- Business Rule 및 기술 이슈 해결사항은 Wiki 페이지를 참고해주세요.

# Architecture 🏛
![sago application architecture drawio](https://user-images.githubusercontent.com/15176192/151936706-1787b93b-8ea5-4a20-9fc0-c8ef6e197a91.png)

# ERD 💿
![sago_database_schema drawio](https://user-images.githubusercontent.com/15176192/149649387-dfda335a-75e7-4be1-9fb7-b4acfa867ab7.png)

# Reporting Issues 👩‍💻
- 실시간 경매 채팅에서의 채팅 메시지 처리 성능 이슈(RedditMQ, Kafka 등 메시지 큐잉 기술 도입 검토)
- DB 장애 대응(Master-Slave)
- Redis Sharding

# Prototype 🏙

실제 테스트 화면: https://ovenapp.io/view/3S6tipmCwIm8jLibBCRX2yekB9PndvhP/oMnl5
