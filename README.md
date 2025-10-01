# 🗺️ 모여행

## 🙌🏻 멤버
| [<img src="https://github.com/Cori1304-Seong.png" width="100" alt="성종민" />](https://github.com/Cori1304-Seong) | [<img src="https://github.com/jhx23.png" width="100" alt="박정훈" />](https://github.com/jhx23) | [<img src="https://github.com/currysoda.png" width="100" alt="석진용" />](https://github.com/currysoda) | [<img src="https://github.com/Yunsung-Jo.png" width="100" alt="조윤성" />](https://github.com/Yunsung-Jo) |
| :---: | :---: | :---: | :---: |
| **[성종민](https://github.com/Cori1304-Seong)** | **[박정훈](https://github.com/jhx23)** | **[석진용](https://github.com/currysoda)** | **[조윤성](https://github.com/Yunsung-Jo)** |

## 📋 목차
- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [아키텍처](#%EF%B8%8F-아키텍처)
- [모니터링 & 운영](#-모니터링--운영)

## 📖 프로젝트 개요
모여행은 **함께 계획하고, 함께 즐기는 여행의 시작점**으로,<br>
사람들을 연결하여 모두가 즐거운 출발점을 만드는 협업 기반 여행 계획 서비스입니다.

### 📅 진행 기간
- MVP 기획 및 개발 : 2025. 08. 11. ~ 2025. 09. 15.
- 지속 개선 : 2025. 09. 30. ~ endless

### 핵심목표
- 4개(PM, PD, FE, BE) 직군 협업 경험
- SSE를 통한 실시간 동기화 기능 구현
- 실무에서 사용되는 기술 스택 학습 및 경험

## 🔧 기술 스택
### Backend
[![backend](https://skillicons.dev/icons?i=java,spring,redis,mysql,gradle)](https://skillicons.dev)
- **Language** : Java 21
- **Framework** : Spring Boot 3.5.4
- **ORM** : Spring Data JPA, QueryDSL
- **DB** : MySQL
- **Cache** : Redis
- **Authentication** : JWT
- **Build Tool** : Gradle

### Infrastructure & DevOps
[![infra,devops](https://skillicons.dev/icons?i=git,github,docker,aws,prometheus,grafana,terraform,githubactions)](https://skillicons.dev)
- **Cloud Platform** : AWS (ECS, RDS, ElastiCache)
- **Container** : Docker
- **CI/CD** : GitHub Actions
- **Monitoring** : Prometheus, Grafana, Alloy, Loki
- **IaC** : Terraform
- **VCS** : Git, GitHub

## 🏗️ 아키텍처
<img width="2030" height="882" alt="infra" src="https://github.com/user-attachments/assets/6957e7a5-789a-4c7d-8d97-0b6f556cc709" />

## 📊 모니터링 & 운영
- `GitHub Actions`를 활용해 코드 변경 시 자동 빌드 및 `ECS` 서비스로 배포
- `AWS ECS` 기반으로 애플리케이션과 인프라 구성 요소를 컨테이너 단위로 관리
- `Grafana Alloy`를 사용해 애플리케이션과 시스템 로그 및 메트릭을 수집하고 `Prometheus`와 `Loki`로 전송
- `Prometheus`와 `Grafana`를 통해 서비스 상태, 리소스 사용량, 로그를 통합 가시화하여 실시간 모니터링
- `RDS`, `Route53`, `ALB` 등 `AWS 서비스`를 사용하여 데이터 관리, 트래픽 분산 및 도메인 운영 지원
