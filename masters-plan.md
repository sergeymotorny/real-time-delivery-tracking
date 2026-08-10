# Перетворення бакалаврського проєкту на магістерську роботу
> Тема: «Інтелектуальна система керування доставкою вантажів»

---

## 1. Реалізовані модулі

### AssignmentService — автопризначення курʼєра ✅
Адмін натискає "Auto-assign" → система автоматично обирає найкращого курʼєра.

```
Вхід:  нове замовлення (адреса, вага, дедлайн)
Логіка: перебирає курʼєрів → рахує cost для кожного → обирає мінімальний
Вихід: призначений курʼєр + статус замовлення → CONFIRMED
```

### PriorityService — пріоритизація замовлень ✅
Сортує чергу замовлень за формулою:
```
score = W1·(1/time_left) + W2·(weight/100) + W3·delivery_type_coefficient

де:
  W1 = 0.5  — терміновість (час до дедлайну)
  W2 = 0.3  — вага вантажу
  W3 = 0.2  — тип доставки (EXPRESS=2.0, STANDARD=1.0)
```
Рівні пріоритету: **HIGH** (≥1.5) / **MEDIUM** (≥0.5) / **LOW** (<0.5)

### RouteOptimizerService — оптимізація маршруту ✅
Nearest Neighbor алгоритм (O(n²)):
```
Старт з депо (46.974429, 32.019642) — Миколаїв
→ Знайти найближче невідвідане замовлення (Haversine відстань)
→ Перейти до нього, позначити як відвідане
→ Повторити поки всі замовлення не відвідані
```

### TelegramNotificationService — сповіщення ✅
- Клієнту надсилається повідомлення коли курʼєр приймає замовлення
- Реалізовано через Spring `RestClient` + Telegram Bot API (без бібліотек)
- Клієнт зберігає `telegramChatId` в профілі

### AnalyticsService — дашборд адміна ✅
Chart.js графіки:
- Замовлення по днях (останні 7 днів) — Line chart
- Розподіл по типу доставки — Doughnut
- Навантаженість курʼєрів — Bar chart
- Розподіл замовлень за пріоритетом — Doughnut
- Середній рейтинг курʼєрів — Bar chart

### FeedbackService — рейтинг курʼєрів ✅
- Клієнт залишає відгук (1-5 зірок + коментар) після доставки
- Один відгук на shipment — повторне оцінювання заблоковано
- Рейтинг впливає на вибір курʼєра в AssignmentService

---

## 2. Математична модель

### Задача призначення (Assignment Problem)

**Множини:**
- `C = {c₁, c₂, ..., cₙ}` — множина доступних курʼєрів
- `O = {o₁, o₂, ..., oₘ}` — черга нових замовлень

**Змінна рішення:**
```
xᵢⱼ ∈ {0,1} — призначити курʼєра i до замовлення j (1) чи ні (0)
```

**Цільова функція — мінімізувати загальну вартість:**
```
F = Σᵢ Σⱼ cost(i,j) · xᵢⱼ → min
```

**Функція вартості (реалізована):**
```
cost(i,j) = α·dist(i,j) + β·load(i) + γ·(1/priority(j)) - δ·rating(i)

де:
  dist(i,j)   — відстань від курʼєра до точки доставки (Haversine, км)
  load(i)     — кількість активних shipments курʼєра
  priority(j) — score від PriorityService (W1·urgency + W2·weight + W3·type)
  rating(i)   — середній рейтинг курʼєра з відгуків клієнтів (0–5)
  α = 0.5, β = 0.3, γ = 0.2, δ = 0.1 — вагові коефіцієнти
```

**Обмеження:**
```
Σⱼ xᵢⱼ ≤ MAX_LOAD      (курʼєр не може мати більше N замовлень)
Σᵢ xᵢⱼ = 1             (кожне замовлення призначається рівно одному курʼєру)
```

### Алгоритми

| Алгоритм | Складність | Використання в проєкті |
|---|---|---|
| Жадібний (Greedy) | O(n·m) | **Реалізовано** — AssignmentService |
| Nearest Neighbor | O(n²) | **Реалізовано** — RouteOptimizerService |
| Угорський (Hungarian) | O(n³) | Описується теоретично для порівняння |

---

## 3. Архітектура системи

```
Browser / Telegram
        ↓
  UserApplication (port 8081)
  ├── AuthModule          (Spring Security)
  ├── OrderModule         (CRUD + статуси)
  ├── AssignmentService   ← Greedy cost function
  ├── PriorityService     ← score formula
  ├── RouteOptimizerService ← Nearest Neighbor
  ├── TelegramNotificationService ← RestClient → Telegram API
  ├── AnalyticsService    ← Chart.js дашборд
  ├── FeedbackService     ← рейтинг курʼєрів
  └── WebSocket           ← real-time трекінг
        ↑
  DriverApplication (port 8082) → Kafka → UserApplication
```

---

## 4. UML-діаграми (для роботи)

| Діаграма | Статус |
|---|---|
| Use Case | Оновити — додати актора "Система" для авто-призначення |
| Class Diagram | Оновити — нові класи: Assignment/Priority/RouteOptimizer/Feedback |
| Sequence: автопризначення | **Нова** — флоу від створення замовлення до призначення |
| Sequence: Telegram сповіщення | **Нова** — флоу від прийняття замовлення до повідомлення |
| Component Diagram | **Нова** — всі модулі та їх залежності |
| Activity: алгоритм Nearest Neighbor | **Нова** — покроковий флоу маршрутизації |
| ER Diagram | Оновити — нові поля: `users.telegram_chat_id`, таблиця `feedback` |

---

## 5. Реальні зміни в БД (зроблено через Hibernate ddl-auto: update)

```
users          + telegram_chat_id VARCHAR
feedback       (нова таблиця: shipment_id, user_id, rating, comment, created_at)
```

Поля які планувались але **не реалізовані** (можна описати як напрям розвитку):
```
courier.capacity    — вантажопідйомність
courier.is_available — доступність
assignment_log      — лог призначень
```

---

## 6. Підсумок для захисту

| Елемент | Що демонструє |
|---|---|
| Математична модель (cost function) | "дослідив і формалізував задачу" |
| Порівняння Greedy vs Hungarian vs NN | "провів аналіз методів" |
| AssignmentService | "реалізував на основі моделі" |
| PriorityService | "багатокритеріальна оцінка" |
| RouteOptimizerService | "задача комівояжера (спрощена)" |
| FeedbackService + рейтинг | "зворотній зв'язок впливає на алгоритм" |
| AnalyticsService + Chart.js | "практична цінність, моніторинг системи" |
| Telegram-бот | "різні компоненти системи" (вимога керівника) |
