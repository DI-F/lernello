# PM4 · Lernello

Hi 👉👈

Curious about the concept behind Lernello?  
Check the project sketch (German, PDF):

- **[Lernello_krea_Projektskizze_PM4_FS25.pdf](documentation/Lernello_krea_Projektskizze_PM4.pdf)**

---

## 📦 Table of Contents
1. [Backend](#backend-backend)
2. [PostgreSQL (Dev)](#-postgresql-dev)
3. [Frontend](#frontend-frontend)
4. [Swagger / API Docs](#swagger)

---

## Backend (`/backend`)

> **Stack:** Java 23 · Gradle 8+ · Spring Boot

### Requirements
| Tool  | Version |
|-------|---------|
| Java  | 23      |
| Gradle| 8 (Wrapper included) |

### Common Commands
```bash
# Live-reload backend (auto-restart on rebuild):
gradle bootRun

# Continuous build on file changes (no run):
gradle build --continuous
```

### OpenAI API Key (required for AI features)

1. **Set an environment variable** before starting the backend

   ```powershell
   # PowerShell (Windows)
   $env:OPENAI_API_KEY = "sk-..."
   ```
   ```cmd
   :: CMD (Windows)
   set OPENAI_API_KEY=sk-...
   ```
   ```bash
   # Bash / macOS / Linux
   export OPENAI_API_KEY=sk-...
   ```

2. **Or** add it once in *IntelliJ*  
   `Run → Edit Configurations → Environment variables`

3. **(Last resort)** place it in `application.properties`  
   *Not recommended for production – commits leak secrets.*
   ```properties
   OPENAI_API_KEY=sk-...
   ```

---

## 🐳 PostgreSQL (Dev)

We run Postgres via **Docker Compose** – no local install needed.

| Action | Command |
|--------|---------|
| **Start DB** | `cd docker && docker compose up -d` |
| **Restart (keep data)** | `cd docker && docker compose restart` |
| **Reset (delete data)** | `cd docker && docker compose down -v && docker compose up -d` |

> **Volume note**    
> Data lives in the named Docker volume **`pgdata`**.  
> Removing with `docker compose down -v` creates a fresh database.

### Connection details

| Host      | Port | Database | User     | Password |
|-----------|------|----------|----------|----------|
| localhost | 15432| lernello | postgres | secret   |

```bash
# quick psql inside the container
docker exec -it lernello-dev-db   psql -U postgres -d lernello
```

### 🚀 One-shot dev start

```bash
gradle startDev
```

This will

1. Start PostgreSQL via Docker Compose
2. Run Spring Boot with the **`local`** profile (`gradlew bootRun`)

---

## Frontend (`/frontend`)

> **Stack:** SvelteKit · TypeScript · Skeleton UI · TailwindCSS · Prettier · ESLint

### Requirements
- Node.js 18 +

### Common Commands
```bash
npm i            # install dependencies
npm run dev      # local dev server
npm run lint     # eslint
npm run format   # prettier
```

---

## Swagger

Browse the generated API docs at

```
http://localhost:8080/swagger-ui/index.html
```

---

_Happy coding!_ 🦉
