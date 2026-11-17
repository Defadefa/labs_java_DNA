# Chess Club

Лабораторная работа №1, дисциплина «Программирование кроссплатформенных приложений»:
сервис шахматного клуба на Java. Система хранит пользователей клуба с разными ролями,
турниры и сыгранные партии, считает турнирную таблицу (в том числе в несколько потоков)
и ведёт очередь заявок на вступление в клуб.
(клиент-серверное приложение на Spring с базой данных PostgreSQL).

## Технологии

- Java 17
- Spring Boot 4.1.2
- Maven
- Lombok
- JUnit 5 (`spring-boot-starter-test`)
- SLF4J (логирование в многопоточном расчёте)
- CI: запуск unit-тестов при каждом push и Merge/Pull Request

## Структура проекта

Базовый пакет: `org.chessclub`

| Пакет / класс | Назначение |
|---|---|
| `ChessClubApplication` | Точка входа Spring Boot приложения |
| `ChessClubDemo` | Демонстрация работы: создание пользователей, поиск, турнир, партии, турнирная таблица, многопоточный расчёт |
| `enums` | `UserRole` (`ADMIN`, `MANAGER`, `MEMBER`), `GameResult` (`WHITE_WIN`, `BLACK_WIN`, `DRAW`), `TournamentStatus` (`PLANNED`, `IN_PROGRESS`, `FINISHED`) |
| `model` | Доменные классы (см. ниже) |
| `repository` | Хранение объектов в памяти: обобщённый интерфейс `Repository<T, ID>` и реализации `InMemoryUserRepository`, `InMemoryTournamentRepository` |
| `service` | Бизнес-логика: `StandingsService`, `MembershipQueueService` |
| `concurrent` | Многопоточность: `ParallelStandingsCalculator`, `NamedThreadFactory` |
| `util` | Вспомогательные классы (пока пуст) |

### Модель (`model`)

- `User` — абстрактный базовый класс пользователя (id, имя, email, роль). Проверяет, что имя не пустое, а email содержит `@`. Содержит абстрактный метод `describePermissions()`.
- `AdminUser`, `ManagerUser`, `MemberUser` — наследники `User` для ролей `ADMIN`, `MANAGER`, `MEMBER`; каждый по-своему описывает права. `ManagerUser` дополнительно хранит специализацию, `MemberUser` — рейтинг, дату вступления и признак активного членства.
- `Game` — партия между двумя участниками (белые, чёрные, результат, дата). Нельзя создать партию с `null` или игрока с самим собой.
- `Tournament` — турнир: название, дата, статус, множество участников и список партий. Партию можно добавить, только если оба игрока — участники турнира.

### Репозитории (`repository`)

`Repository<T, ID>` задаёт операции: `add`, `findById`, `findAll`, `remove`, `count`.

- `InMemoryUserRepository` — хранит пользователей; дополнительно поиск по роли (`findByRole`) и по части имени без учёта регистра (`findByNameContains`). Добавление пользователя с существующим id запрещено.
- `InMemoryTournamentRepository` — хранит турниры; дополнительно поиск по статусу (`findByStatus`).

### Сервисы (`service`)

- `StandingsService` — подсчёт очков игрока в турнире (победа — 1, ничья — 0,5), построение таблицы (сортировка по очкам, при равенстве — по рейтингу) и её форматированный вывод.
- `MembershipQueueService` — очередь заявок на вступление: подать заявку (`submit`), одобрить следующую (`approveNext`), посмотреть следующую (`peekNext`), количество ожидающих (`pendingCount`).

### Многопоточность (`concurrent`)

- `ParallelStandingsCalculator` — считает очки всех участников турнира параллельно в пуле потоков (`ExecutorService`, заданное число потоков). В лог выводится, какой поток обработал какого игрока. Результат — `Map<Long, Double>` (id игрока → очки).
- `NamedThreadFactory` — фабрика потоков, которая даёт потокам имена вида `standings-worker-1`, `standings-worker-2`, …

### Использованные структуры данных

| Структура | Где используется |
|---|---|
| `LinkedHashMap` | репозитории (хранение по id с сохранением порядка добавления), турнирная таблица |
| `LinkedHashSet` | участники турнира (без дубликатов, порядок сохраняется) |
| `ArrayList` | партии турнира, результаты поиска |
| `ArrayDeque` (`Queue`) | очередь заявок на вступление |
| `ConcurrentHashMap` | результаты параллельного расчёта |

## Принципы

- каждый класс решает одну задачу (принцип единственной ответственности);
- хранение данных отделено от логики: интерфейс `Repository` и сервисы;
- роли пользователей реализованы через enum `UserRole` и иерархию `User` → `AdminUser` / `ManagerUser` / `MemberUser`;
- явные типы вместо `var`.

## Тесты

Каталог `src/test/java/org/chessclub`:

| Тест | Что проверяет |
|---|---|
| `model/UserHierarchyTest` | иерархию пользователей и роли |
| `model/TournamentTest` | участников и партии турнира |
| `repository/UserRepositoryTest` | добавление, запрет дубликатов id, поиск по id / роли / имени, удаление |
| `service/StandingsServiceTest` | очки за победу белых / чёрных и за ничью, сортировку таблицы по очкам, разрешение ничьей по рейтингу, форматированный вывод |
| `service/MembershipQueueServiceTest` | обработку заявок в порядке поступления (FIFO), пустую очередь, запрет пустой заявки, счётчик ожидающих |
| `concurrent/ParallelStandingsCalculatorTest` | параллельный результат совпадает с последовательным; имена потоков |
| `ChessClubApplicationTests` | загрузку контекста Spring (`@SpringBootTest`) |

## Запуск

Требуется JDK 17 и Maven (или встроенный wrapper).

```bash
# сборка и запуск unit-тестов
./mvnw verify          # Windows: mvnw.cmd verify
```

Демонстрацию можно запустить из IntelliJ IDEA: метод `main` в классе `org.chessclub.ChessClubDemo`.

## CI

При каждом push и Merge/Pull Request автоматически запускается `mvn verify`
(GitLab: `.gitlab-ci.yml`, GitHub: `.github/workflows/ci.yml`).

## Ветки

Работа ведётся в ветке `lab1`, изменения отправляются через открытый Merge/Pull Request
в основную ветку.
