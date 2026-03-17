# MovieHub — Android Developer Technical Assignment

A simple app that shows a paged list of TV shows and a details screen.

**Tech:** Kotlin, XML, MVVM, Clean Architecture, Hilt, Retrofit + Gson, Coroutines, Paging 3, Coil.

---

## API choice

I used the **TVMaze API** because it’s public and doesn’t require an API key.

- **List:** `GET /shows?page={page}`
- **Detail:** `GET /shows/{id}`

---

## Architecture decisions

- **Clean Architecture**
  - **presentation/**: `MainActivity`, Fragments, ViewModels, UI state
  - **domain/**: models, repository interface, use cases
  - **data/**: Retrofit service, DTOs + mappers, repository implementation, paging source
- **MVVM**
  - Fragments only render state and handle clicks.
  - ViewModels call use cases and expose `LiveData` state.
- **Why detail fetches by endpoint**
  - Keeps a single source of truth and supports deep links/process death (only `movieId` is needed).

---

## Screens

### List
- Paging 3 list with pull-to-refresh
- Loading / empty / error states + retry
- Footer spinner + footer retry for pagination errors

### Detail
- Loads details by `movieId`
- Loading + error + retry

---

## Tradeoffs / future improvements

- **LiveData vs Flow:** This project uses **LiveData** for state and paging.

---

## How to run

1. Open in Android Studio and sync Gradle.
2. Run the `app` module (no API key needed).
