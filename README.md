# MovieHub — Android Developer Technical Assignment

A simple app that shows a paged list of TV shows and a details screen.

**Tech:** Kotlin, XML, MVVM, Clean Architecture, Hilt, Retrofit + Gson, Coroutines, Paging 3 (LiveData), Coil.

---

## API choice

I used the **TVMaze API** because it’s public and doesn’t require an API key.

- **List:** `GET /shows?page={page}`
- **Detail:** `GET /shows/{id}`

---

## Architecture decisions

- **Clean Architecture**
  - **presentation/**: single activity + fragments + viewModels + UI helpers
  - **domain/**: models, repository interface, use cases
  - **data/**: Retrofit service, DTOs + mapper, repository implementation, paging source
- **MVVM**
  - Fragments render UI and handle user actions.
  - ViewModels call use cases and expose `LiveData`.
- **Why detail fetches by endpoint**
  - Keeps a single source of truth and supports deep links/process death (only `movieId` is needed).

Project structure (high level):

```
presentation/
  MainActivity (NavHost)
  list/
  detail/
domain/
  model/ repository/ usecase/
data/
  remote/ dto/ mapper/
  paging/
  repository/
```

---

## Screens

### List
- Paging 3 list with pull-to-refresh
- Each item shows: image + title + rating + premiered date
- Loading / empty / error states + retry
- Footer spinner + footer retry for pagination errors

### Detail
- Loads details by `movieId`
- Large image + title + description (summary) + metadata (rating/language/premiered)
- Loading + error + retry

---

## Tradeoffs / future improvements

- **LiveData vs Flow:** This project uses **LiveData** (no Flow). Next improvement could be migrating to Flow where it makes sense.

---
