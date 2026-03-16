# MovieHub — Android Developer Technical Assignment

Submission for the Android Developer Technical Assignment: a small app that displays a list of media items (TV shows) and allows users to view detailed information for each item.

**Stack:** Kotlin, MVVM, Clean Architecture, XML layouts, Retrofit, Paging 3, Coil, Hilt.

---

## Requirements checklist

### API requirement

- **Chosen API:** [TVMaze](https://www.tvmaze.com/api) (TV shows).
- **Endpoints:** `GET /shows?page={page}` for the list, `GET /shows/{id}` for detail.
- **Why TVMaze:** Public REST API, no API key, JSON, supports pagination and single-item fetch. Fits the “media list + detail” use case.

### Screen 1 — Media list

| Requirement | Implementation |
|-------------|----------------|
| Fetch items from API | `ApiService.getMoviesPage(page)` via `MovieRepositoryImpl` and `GetPagedMoviesUseCase`. |
| Vertically scrollable list | `RecyclerView` with `LinearLayoutManager` inside `SwipeRefreshLayout`. |
| Each item: image, title, secondary info | List item shows poster image (Coil), title, rating, premiered date. |
| Click opens Details | Navigation with Safe Args: `movieId` + `imageUrl` (medium) to `MovieDetailFragment`. |
| Loading state | Progress bar visible while initial load; `LoadState.Loading` drives `MovieListUiState.isLoading`. |
| Error state | Error message + retry button; `LoadState.Error` and `viewModel.onError()`. |
| Retry | Retry button calls `movieAdapter.retry()`; error state shows message and retry. |
| Empty state | `text_empty` and `MovieListUiState.isEmpty` when refresh completes with zero items. |
| Pull-to-refresh | `SwipeRefreshLayout`; refresh triggers `movieAdapter.refresh()`. |

### Screen 2 — Details

| Requirement | Implementation |
|-------------|----------------|
| Large image | Full-width poster at top; loads medium from cache first, then original with crossfade (Coil). |
| Title | From API; shown in detail layout. |
| Description | Summary (HTML stripped) from API. |
| Additional metadata | Rating, language, premiered. |
| Pass data OR fetch via endpoint | **Fetch via endpoint** — see [Detail screen decision](#detail-screen-decision-pass-data-vs-fetch-via-endpoint) below. |
| Loading state | Progress bar; `MovieDetailUiState.isLoading`. |
| Error state | Error message + retry button; content hidden. |

### Technical requirements (mandatory)

- **Kotlin** — Entire codebase in Kotlin.
- **MVVM** — ViewModels expose state (e.g. `MovieListUiState`, `MovieDetailUiState`); Fragments observe and render. No business logic in UI.
- **XML** — All layouts are XML (`activity_main`, `fragment_movie_list`, `fragment_movie_detail`, `item_movie`).

### Extra feature (one chosen)

- **Pagination** — Implemented with **Paging 3**: `MoviePagingSource` loads pages via `getMoviesPage(page)`; list uses `PagingDataAdapter` and `LoadState` for loading/error/empty and pull-to-refresh. Data is `cachedIn(viewModelScope)` in the list ViewModel.

---

## Detail screen decision: pass data vs fetch via endpoint

**Decision:** Details are **fetched via the detail endpoint** (`GET /shows/{id}`). The list screen passes only **`movieId`** and **`imageUrl`** (medium poster URL).

**Reasons**

1. **Single source of truth** — Detail comes from the same API; no duplicate or stale copy from the list.
2. **Richer payload** — Detail endpoint can expose more fields than the list; detail screen can evolve independently.
3. **Deep links / process death** — Detail can be opened with just `movieId` (e.g. from a link); no dependency on list state.
4. **Consistency** — List and detail both use repository/use-case layer; only “paged list” vs “single item by ID” differs.

**What is passed from the list**

- **`movieId`** — Used to call the detail API.
- **`imageUrl`** — Medium image URL; used as Coil `placeholderMemoryCacheKey` so the poster appears from cache immediately while the full-size image loads.

**Loading and error handling**

- **Loading:** `loadMovie(id)` sets `isLoading = true`; fragment shows progress bar, hides content and error.
- **Success:** State updated with `movie`; fragment shows image, title, description, metadata.
- **Error:** State updated with `errorMessage`; fragment shows message and retry button; retry calls `loadMovie(args.movieId)` again.
- All outcomes are reflected in `MovieDetailUiState`; no silent failures.

---

## Architecture decisions

### Clean Architecture + MVVM

- **Presentation** — Fragments, ViewModels, UI state. Observes state and dispatches user actions.
- **Domain** — Models, repository interfaces, use cases. No Android or framework dependencies.
- **Data** — Retrofit `ApiService`, DTOs, `MoviePagingSource`, `MovieRepositoryImpl`.

Dependencies point inward (presentation → domain ← data). ViewModels depend on use cases; use cases depend on repository interfaces; data layer implements repositories.

### State management

- **List:** `MovieListUiState` (loading, errorMessage, isEmpty) + Paging `LoadState` for list data and refresh/retry/empty.
- **Detail:** `MovieDetailUiState` (loading, errorMessage, movie) with explicit loading and error paths.

### Networking

- **Retrofit** + **Gson** for REST; **OkHttp** with logging interceptor. Base URL in `di/AppModule.kt` (default: `https://api.tvmaze.com/`).
- **Hilt** for DI: `ApiService`, `MovieRepository`, use cases provided in `AppModule`; ViewModels injected with `@HiltViewModel`.

### Image loading

- **Coil** for list thumbnails and detail poster. List uses `memoryCacheKey(url)`; detail uses passed `imageUrl` as `placeholderMemoryCacheKey` for instant cached image, then loads original with crossfade.

---

## API choice

The app uses the **TVMaze API** (`https://api.tvmaze.com/`).

- **List:** `GET /shows?page={page}` — paginated list of shows.
- **Detail:** `GET /shows/{id}` — full show details by ID.

Public, no API key required for basic use; REST/JSON; fits list + detail and Paging 3.

---

## Tradeoffs and improvements

| Area | Current choice | Possible improvement |
|------|----------------|----------------------|
| Networking | Retrofit + Gson | Moshi or kotlinx.serialization; sealed error types. |
| Errors | `try/catch` in ViewModel | Sealed class (e.g. `NetworkError`, `NotFound`) for UI messaging. |
| Offline | No persistence | Room + RemoteMediator for offline list/detail. |
| List state | LiveData + Paging | Flow from use case + `cachedIn` (already used for data). |
| Detail | Fetch by ID + Coil cache | In-memory cache for recently viewed details. |

---

## Project structure

```
app/src/main/java/com/abov/moviehub/
├── data/
│   ├── remote/          # ApiService, DTOs, MoviePagingSource
│   └── repository/       # MovieRepositoryImpl
├── di/                   # Hilt AppModule (Retrofit, repository, use cases)
├── domain/
│   ├── model/            # Movie
│   ├── repository/       # MovieRepository interface
│   └── usecase/           # GetPagedMoviesUseCase, GetMovieDetailUseCase
└── presentation/
    ├── list/             # MovieListFragment, ViewModel, adapter, UI state
    └── detail/           # MovieDetailFragment, ViewModel, UI state
```

---

## Evaluation criteria (mapping)

- **Code readability and architecture** — Clean Architecture layers, clear naming, separation of concerns.
- **State and error handling** — Explicit UI states (loading, error, content/empty); retry and pull-to-refresh.
- **Networking** — Retrofit + suspend functions; errors propagated to UI state.
- **Clean Kotlin** — Data classes, extension functions where useful, coroutines in ViewModel.
- **UI clarity** — XML layouts; loading, error, and content states clearly separated.
- **Problem-solving** — Detail screen: fetch by ID + pass image URL for instant poster; Paging 3 for list.

---

## Deliverables

- **GitHub repository:** [Add your repository link here]
- **README:** This file — architecture, API choice, tradeoffs, and improvements.
- **Build and run:** Project builds and runs on Android (minSdk 24); no API key needed for default TVMaze base URL.

---

## How to run

1. Clone the repository and open in Android Studio.
2. Sync Gradle.
3. (Optional) Change `BASE_URL` in `di/AppModule.kt` if using a different backend (default: `https://api.tvmaze.com/`).
4. Run on an emulator or device (minSdk 24).

No API key is required for the default TVMaze API.
