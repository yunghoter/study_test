# Збірка Docker-образу для College Schedule App

Цей документ описує, як зібрати Docker-образ для застосунку College Schedule.

## Необхідні умови

1. **Docker Desktop**: Переконайтеся, що у вас встановлено та запущено Docker Desktop на вашому комп'ютері з Windows
   - [Завантажити Docker Desktop](https://www.docker.com/products/docker-desktop)
   - Перевірити встановлення: `docker --version`

2. **Доступ до GitHub Container Registry**: Переконайтеся, що у вас є доступ до GitHub Container Registry (ghcr.io)
   - Створіть Personal Access Token (PAT) з правами `read:packages` та `write:packages`
   - [Інструкція зі створення PAT](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)

3. **Java JAR-файл**: Переконайтеся, що у папці `build` є скомпільований JAR-файл
   - JAR-файл повинен називатися `app.jar`
   - Ви можете зібрати його за допомогою Maven: `mvn clean package -DskipTests`

## Збірка образу

### Метод 1: Використання build-docker-image.bat

Найпростіший спосіб зібрати образ — скористатися наданим batch-скриптом:

```cmd
build-docker-image.bat [version]
```

Приклад:
```cmd
build-docker-image.bat 0.3.0-SNAPSHOT
```

### Метод 2: Ручна збірка

1. **Увійдіть у GitHub Container Registry**:
   ```cmd
   docker login ghcr.io -u ВАШ_GITHUB_USERNAME
   ```
   Коли буде запропоновано, використайте ваш GitHub PAT як пароль.

2. **Зберіть образ**:
   ```cmd
   docker build -t ghcr.io/chdbc-samples/college-schedule-app:VERSION .
   ```
   Замініть `VERSION` на потрібний тег версії (наприклад, `0.3.0-SNAPSHOT`).

3. **Відправте образ** (необов'язково, якщо хочете опублікувати):
   ```cmd
   docker push ghcr.io/chdbc-samples/college-schedule-app:VERSION
   ```

## Огляд Dockerfile

`Dockerfile` у цій директорії:
- Використовує OpenJDK 17 як базовий образ
- Копіює JAR-файл застосунку
- Налаштовує контейнер для запуску Java-застосунку

## Вирішення проблем

1. **Помилка під час збірки образу**:
   - Переконайтеся, що Docker Desktop запущено
   - Перевірте, чи існує `app.jar` у папці build
   - Переконайтеся, що достатньо місця на диску
   - Перегляньте логи Docker для детальніших повідомлень про помилки

2. **Помилка під час відправки образу**:
   - Перевірте, чи має ваш PAT необхідні дозволи
   - Переконайтеся, що ви увійшли в ghcr.io
   - Перевірте, чи назва репозиторію відповідає вашому GitHub-репозиторію

3. **Проблеми з дозволами**:
   - Переконайтеся, що ваш GitHub-акаунт має права на запис у репозиторій
   - Перевірте, чи не закінчився термін дії PAT
   - Переконайтеся, що репозиторій дозволяє публікацію пакетів

## Змінні середовища

Зібраний образ підтримує такі змінні середовища:

- `SPRING_DATASOURCE_URL`: URL підключення до бази даних
- `SPRING_DATASOURCE_USERNAME`: Ім'я користувача бази даних
- `SPRING_DATASOURCE_PASSWORD`: Пароль до бази даних
- `SERVER_PORT`: Порт застосунку (за замовчуванням: 8080)

## Теги образів

Ми використовуємо таку конвенцію тегування:
- `latest`: Остання стабільна збірка
- `x.y.z`: Релізні версії (наприклад, `0.3.0`)
- `x.y.z-SNAPSHOT`: Розробницькі версії
- `x.y.z-pr-N`: Збірки для pull request

## Перевірка збірки

Після збірки перевірте образ:

```cmd
docker images | findstr college-schedule-app
```

Перевірте запуск контейнера:

```cmd
docker run --rm
```