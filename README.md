# UI Automation Tests (Selenium & Appium)

Проект содержит автоматизированные тесты для веб‑версии Wikipedia и Android‑приложения Wikipedia. 
Для веба используется Selenium в связке с TestNG, для мобильных сценариев — Appium и TestNG.

## Предварительные требования
- JDK версии 11 или новее
- Maven 3.8 и выше
- Любой поддерживаемый браузер (по умолчанию используется Chrome, также работают Firefox и Edge)
- Для Android‑тестов: установленный Android SDK, эмулятор или реальное устройство, запущенный Appium Server и установленное приложение Wikipedia (`org.wikipedia`)

## Веб‑тесты (Selenium)
Реализованные проверки:
- загрузка англоязычной главной страницы Wikipedia;
- поиск статьи «Selenium (software)»;
- проверка наличия блока Featured article;
- открытие случайной статьи.

Запуск только веб‑сценариев:
```bash
mvn test -Dgroups=web
```

Доступные параметры:
- `-Dbrowser=chrome|firefox|edge`
- `-DwebBaseUrl=https://en.wikipedia.org`

## Мобильные тесты (Appium, Wikipedia для Android)
Покрываемые сценарии:
- поиск запроса «Selenium» с проверкой списка результатов и описаний;
- открытие статьи «Software testing» из поиска;
- очистка поисковой строки и проверка отсутствия результатов.

### Настройка окружения
- Android SDK установлен (для macOS стандартный путь: `~/Library/Android/sdk`);
- переменные окружения:
  ```bash
  export ANDROID_HOME="$HOME/Library/Android/sdk"
  export ANDROID_SDK_ROOT="$ANDROID_HOME"
  export PATH="$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator"
  ```
- установлен Appium и драйвер UiAutomator2:
  ```bash
  npm install -g appium
  appium driver install uiautomator2
  ```
- запущен Appium Server (по умолчанию используется `http://127.0.0.1:4723`, код поддерживает также путь `/wd/hub`):
  ```bash
  appium --address 127.0.0.1 --port 4723
  ```
  При занятом порте завершите процесс, который его использует (например, через `lsof -i :4723`).
- запущен Android‑эмулятор или подключено устройство;
- приложение Wikipedia установлено на устройство.

### Запуск мобильных тестов
Пример запуска для эмулятора:
```bash
mvn test -Dgroups=mobile   -DappiumServerUrl=http://127.0.0.1:4723/wd/hub   -DdeviceName="emulator-5554"   -DplatformVersion=16
```

Параметры, которые можно переопределять:
- `-DappiumServerUrl=http://127.0.0.1:4723`
- `-DdeviceName=Pixel_6_API_34` (имя вашего эмулятора или устройства)
- `-DplatformVersion=14` (необязательно)

## Организация проекта
- `src/test/java/com/uitesting/pages/web` — Page Object’ы для веб‑версии;
- `src/test/java/com/uitesting/pages/mobile` — Page Object’ы для Android‑приложения;
- `src/test/java/com/uitesting/tests/web` — TestNG‑тесты для веба;
- `src/test/java/com/uitesting/tests/mobile` — TestNG‑тесты для мобильной части;
- `src/test/resources/testng.xml` — конфигурация TestNG с группами `web` и `mobile`.

## Дополнительная информация
- Драйверы браузеров подтягиваются автоматически через WebDriverManager; Chrome запускается в развернутом окне.
- В мобильных тестах используется UiAutomator2 и параметр `noReset=true`; экран онбординга пропускается при наличии кнопки Skip.
- Все основные настройки можно менять через системные свойства, не редактируя исходный код.
