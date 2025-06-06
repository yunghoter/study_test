# Система управління розкладом занять

Веб-додаток на Spring Boot для управління розкладом занять та реєстрації студентів на курси.

## Необхідні компоненти

- Java 17
- Maven
- PostgreSQL


# Кроки для запуску програми і відкриття web-interface в Windows PowerShell
1. Встановіть локально PostgreSQL, Maven та Java.
2. Створіть базу даних college_db.
3. Відкрийте PowerShell і виконайте неступну команду для відображення символів в UTF-8 кодуванні: `chcp 65001`.
4. Зберіть проект за допомогою команди: `mvn clean install`.
5. Запустіть програму за допомогою команди: `mvn -DDB_PASSWORD="postgres" exec:java -D"exec.mainClass=com.college.MainApp"`.
6. Відкрийте web-interface в браузері за адресою localhost

## Публікація артефактів

Проект налаштовано на автоматичну публікацію артефактів у GitHub Packages при кожному push в гілку main. 

### Використання пакетів

Для використання опублікованих пакетів у вашому проекті, додайте наступну конфігурацію до вашого `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>com.college</groupId>
        <artifactId>college-schedule</artifactId>
        <version>0.2.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/OWNER/college-schedule-app</url>
    </repository>
</repositories>
```

Замініть `OWNER` на ім'я власника репозиторію.

### Налаштування автентифікації

Для доступу до пакетів вам потрібно налаштувати автентифікацію GitHub:

#### Windows
1. Створіть файл `settings.xml` в директорії `%USERPROFILE%\.m2\` (зазвичай `C:\Users\YourUsername\.m2\`)
2. Додайте наступну конфігурацію:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

Якщо директорія `.m2` не існує:
```cmd
mkdir "%USERPROFILE%\.m2"
```

#### Linux/macOS
Додайте конфігурацію в файл `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

#### Отримання токену GitHub
1. Перейдіть в налаштування GitHub:
   - Settings -> Developer settings -> Personal access tokens
   - або використайте пряме посилання: https://github.com/settings/tokens
2. Натисніть "Generate new token"
3. Надайте токену необхідні права:
   - `read:packages` - для завантаження пакетів
   - `write:packages` - для публікації пакетів
4. Скопіюйте згенерований токен і збережіть його в безпечному місці
5. Використайте цей токен у файлі `settings.xml` замість `YOUR_GITHUB_TOKEN`

Замініть у конфігурації:
- `YOUR_GITHUB_USERNAME` на ваше ім'я користувача GitHub
- `YOUR_GITHUB_TOKEN` на токен, який ви щойно створили

## Релізи

### Створення нового релізу

Для створення нового релізу:

1. Переконайтеся, що всі необхідні зміни знаходяться в гілці `main`
2. Створіть новий тег з версією:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```
3. GitHub Actions автоматично:
   - Збере проект
   - Згенерує changelog на основі комітів та PR
   - Створить новий реліз на GitHub
   - Прикріпить до релізу JAR-файли та документацію

