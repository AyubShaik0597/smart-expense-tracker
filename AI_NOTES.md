# AI Notes

> **Before you submit:** this file is a starting draft. The sections marked
> `[PERSONALIZE]` below need to reflect what *you* actually did — run the
> project, read the code, try to break it, and fill those in honestly. A
> generic AI_NOTES.md is explicitly called out as something that costs marks,
> so don't submit this section as-is.

## 1. What was AI-generated vs. written by me

**AI-generated (Claude):**
- Overall project structure (Maven layout, package breakdown into
  `model` / `dto` / `repository` / `service` / `controller` / `exception`)
- All source files under `src/main/java` — entity, DTOs, in-memory
  repository, service layer (filtering/totals logic), REST controller,
  validation annotations, global exception handler
- Test suite under `tests/java` — service unit tests and MockMvc
  controller/integration tests
- `pom.xml`, `application.yml`, this README

**[PERSONALIZE] Written or changed by me:**
- *(e.g. "I changed the category matching from exact to case-insensitive
  after testing with 'food' vs 'Food' and getting empty results")*
- *(e.g. "I removed the Swagger dependency and added Docker support instead
  because ...")*
- *(e.g. "I rewrote the id-generation logic because ...")*

If you didn't change anything, say so explicitly rather than leaving this
blank — "I reviewed it and didn't find anything I needed to change, but I
did do X, Y, Z" is a legitimate answer with tests below.

## 2. What I validated or tested, and why

**[PERSONALIZE] — replace with your own findings, e.g.:**
- Ran `mvn test` on a clean checkout and confirmed all N tests pass
- Manually hit each endpoint with `curl` / Postman and checked responses
  against the spec (add expense, filter, totals, delete, error cases)
- Checked what happens when: amount is negative, title is blank, category
  doesn't exist yet, deleting a non-existent id, filtering by a category
  with mixed casing
- Read through `ExpenseService` line by line to confirm the totals
  calculation (rounding, grouping) does what I expect
- Confirmed the project actually imports and runs in Eclipse via
  `Import > Existing Maven Projects`, not just from the command line

Explain *why* these were the things worth checking — e.g. validation and
error paths matter more for a review than the happy path, since the happy
path is what AI tools get right most reliably.

## 3. AI suggestions I didn't use, and why

**[PERSONALIZE] — example format:**
- Claude's first draft used Lombok (`@Data`) for the model classes. I asked
  for it to be removed / removed it myself because it requires the Lombok
  Eclipse plugin to be installed, and I wanted the project to import cleanly
  without extra setup steps for a reviewer.
- I considered the "monthly summary" bonus endpoint instead of Swagger docs,
  but picked Swagger since it's more immediately useful for someone
  reviewing the API without reading the source.
- *(add your own — e.g. did you consider a database, a different framework,
  different error-handling approach, and decide against it?)*

## 4. Known limitations / things I'd do differently with more time

**[PERSONALIZE], e.g.:**
- Data resets on restart (in-memory only) — acceptable per the spec, but
  worth naming
- No pagination on `GET /api/expenses` — fine at small scale, would matter
  with thousands of expenses
- Test isolation: the Spring context (and its in-memory repository) is
  shared across all `@SpringBootTest` methods in `ExpenseControllerTest`,
  so tests are written to tolerate other tests' data (e.g. using
  `greaterThanOrEqualTo` instead of exact totals) rather than resetting
  state between tests
