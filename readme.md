# Root README.md (Repository Overview)

## JavaTask — Collection of Small Java Projects

This repository contains several standalone Java mini‑projects and a simple web module. Each project can be compiled and run independently. Some projects are console‑based; others use a MySQL database. A `lib/` folder provides external JARs (e.g., MySQL JDBC driver) for database projects, and `schema.sql` / `mysql_schema.sql` contain sample schemas.

### Projects Included

* **ATMWithConsole.java** – Basic ATM simulation (console)
* **ATMWithUserBalanceConsole.java** – ATM simulation with user balance persistence (console)
* **NumberGuessingGameColorfulConsole.java** – Number guessing game with colored console output
* **OnlineExamSystem.java** – Simple exam system with login, timer, scoring (console; optional persistence via files)
* **ReservationSystem.java** – Console reservation demo (may use MySQL via `DBUtil.java`)
* **DigitalLibrarySys/** – Simple digital library module (web UI folder)

### Common Requirements

* JDK 17+ (or JDK 11+ if you prefer)
* VS Code or any Java IDE
* **For DB projects:** MySQL Server (8.x recommended) and MySQL Connector/J (included in `lib/`)

### How to Compile & Run (console projects at repo root)

```bash
# from repo root
javac -cp .:lib/* ATMWithConsole.java
java  -cp .:lib/* ATMWithConsole
```

> **Windows:** Replace `:` with `;` in classpath.

### Database Setup (if needed)

1. Start MySQL and create a database (e.g., `javaprojects`).
2. Execute `schema.sql` or `mysql_schema.sql` in your MySQL client to create tables.
3. Update DB credentials/URL inside `DBUtil.java` (user, password, database).
4. Ensure the MySQL JDBC JAR is available via `lib/` in the classpath.

---

# README — ATMWithConsole

## Overview

A minimal console‑based ATM simulator that supports login, check balance, deposit, withdraw, and exit. Designed as a single‑file Java app for quick testing and learning.

## Features

* Simple user authentication (demo credentials)
* Balance inquiry, deposit, withdraw
* Input validation and looped menu

## Build & Run

```bash
javac ATMWithConsole.java
java  ATMWithConsole
```

## Usage

Follow on‑screen menu prompts. Enter amounts as integers. The app maintains an in‑memory balance for the session.

## Notes

* No database; all data resets each run.
* Extend by saving transactions to a file or DB.

---

# README — ATMWithUserBalanceConsole

## Overview

An expanded ATM simulator that persists per‑user balance during the session and may load/store balances from simple files.

## Features

* Multiple users with IDs
* Balance operations (deposit, withdraw) with validation
* Optional persistence via text files (e.g., `users.txt`)

## Build & Run

```bash
javac ATMWithUserBalanceConsole.java
java  ATMWithUserBalanceConsole
```

## Configuration (optional)

* **User store:** If present, `users.txt` holds demo users/balances. Ensure it’s in the working directory.

## Notes

* For real persistence, integrate with `DBUtil.java` and MySQL.

---

# README — NumberGuessingGameColorfulConsole

## Overview

A console number‑guessing game that picks a random number within a range. Provides attempts, hints (higher/lower), and a colorful output experience.

## Features

* Random target number
* Limited attempts and hints
* Maintains a simple `leaderboard.txt` with scores (if present)

## Build & Run

```bash
javac NumberGuessingGameColorfulConsole.java
java  NumberGuessingGameColorfulConsole
```

## Files

* `leaderboard.txt` (optional) — stores top scores or attempt counts.

## Extend

* Add difficulty levels, multiple rounds, and file/DB‑based leaderboards.

---

# README — OnlineExamSystem

## Overview

A console‑based online exam simulator featuring login, timed questions, scoring, pass/fail display, and optional profile update.

## Features

* Integer user ID/password input
* Countdown timer for the exam
* MCQs with immediate correctness tracking
* Final score, pass/fail status, and per‑question right/wrong indicators
* Optional: update profile/password, logout after result

## Build & Run

```bash
javac OnlineExamSystem.java
java  OnlineExamSystem
```

## Data & Config

* May read/write simple files (e.g., `users.txt`) for demo credentials.
* Timer and number of questions are configurable in code.

## Extend

* Persist users/questions in MySQL via `DBUtil.java`.
* Add admin mode to load questions from a file or database.

---

# README — ReservationSystem

## Overview

A console reservation demonstration (e.g., tickets/slots). Supports create, list, update/cancel reservations. Optionally integrates with MySQL.

## Features

* Create & view reservations
* Simple search/filter
* Optional persistence to MySQL using `DBUtil.java`

## Build & Run (no DB)

```bash
javac ReservationSystem.java
java  ReservationSystem
```

## Build & Run (with MySQL)

```bash
# compile with JDBC on classpath
javac -cp .:lib/* ReservationSystem.java DBUtil.java
java  -cp .:lib/* ReservationSystem
```

## Database Setup

1. Import `schema.sql` or `mysql_schema.sql` into your MySQL DB.
2. Edit `DBUtil.java` to set `DB_URL`, `USER`, `PASSWORD`.
3. Ensure the MySQL Connector/J JAR is present in `lib/`.

---

# README — DigitalLibrarySys (Web Module)

## Overview

A simple digital library module meant to demonstrate a minimal web UI and optional Java/MySQL backing. Use it as a starting point to browse books, members, and basic lending flows.

## Structure

* `DigitalLibrarySys/` — web assets (HTML/CSS/JS) and/or Java sources if included.
* `Member.java`, `User.class` (if present in root) — sample domain classes.
* `schema.sql` — tables for books, members, loans (example schema).

## Run (Static Web Only)

If the folder contains only HTML/JS/CSS:

1. Open the folder in VS Code.
2. Use the **Live Server** extension or just open `index.html` in a browser.

## Run (With Java Backend)

If the module includes Java classes to serve data:

```bash
# Example: compile domain and utility classes
javac -cp .:lib/* DBUtil.java Member.java
# run your server/entry class if provided
java  -cp .:lib/* <YourServerOrMainClass>
```

> Configure DB in `DBUtil.java` and ensure tables from `schema.sql` exist.

## Suggested Tables (from `schema.sql`)

* `books(id, title, author, isbn, available)`
* `members(id, name, email, phone)`
* `loans(id, book_id, member_id, issue_date, due_date, returned)`

## Extend

* Admin pages to add/edit books and members.
* Search with pagination and filters.
* Authentication/authorization for admin vs user roles.

---

# README — DBUtil & Schemas

## DBUtil.java

A small utility that centralizes JDBC connection handling.

### Configure

Edit constants within `DBUtil.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/javaprojects";
private static final String USER   = "root";  // change me
private static final String PASSWORD = "";    // change me
```

> Ensure the MySQL Connector/J JAR is on the classpath via `lib/`.

### Test Connection (snippet)

```java
public static void main(String[] args) throws Exception {
    try (Connection c = DBUtil.getConnection()) {
        System.out.println("Connected: " + (c != null));
    }
}
```

## schema.sql / mysql\_schema.sql

* Create tables needed by `ReservationSystem` and `DigitalLibrarySys`.
* Adjust names and sizes to your needs. Import with MySQL Workbench or CLI.

---

# Tips & Conventions

* **Classpath on Windows:** use `;` instead of `:` between entries.
* **Encoding:** save files in UTF‑8.
* **Java Version:** target 17+ unless your environment requires 11.
* **Project Layout:** consider moving each app into its own subfolder with its code and a README for easier builds.

---

# License

Add your preferred license (e.g., MIT) to `LICENSE` at the repository root.
