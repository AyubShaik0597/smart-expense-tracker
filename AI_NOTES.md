# AI_NOTES.md

## 1. What was AI-generated vs. written by me

### AI-assisted
I used Claude AI to help me:
- Generate the initial Spring Boot project structure.
- Create the REST API endpoints.
- Generate DTOs, model, service, repository, and controller classes.
- Create the exception handling and validation.
- Generate the initial JUnit test cases.
- Prepare the README template.

### What I personally changed
After generating the initial code, I reviewed and modified several parts myself:
- Changed the date format handling after encountering LocalDate parsing issues during testing.
- Verified and corrected the API responses for all endpoints.
- Improved validation messages and exception handling.
- Tested every endpoint using Postman and fixed issues I found.
- Added sample expense data to test filtering, totals, and delete functionality.
- Verified that the project builds successfully using Maven.

---

## 2. What I validated and tested

I manually tested the API using Postman.

I verified:
- Adding a new expense.
- Viewing all expenses.
- Filtering expenses by category.
- Calculating the overall total.
- Calculating the total for a specific category.
- Deleting an expense.
- Validation errors for missing fields and invalid input.
- Invalid expense IDs return the appropriate error response.

I also ran:

```bash
mvn clean install
mvn test
```

to ensure the project builds successfully and all tests pass.

I paid extra attention to validation and error handling because those are common areas where APIs fail.

---

## 3. AI suggestions I didn't use

Initially, AI suggested storing the data in a database such as MySQL.

I decided not to use a database because the assignment specifically mentions that in-memory storage or a local JSON file is sufficient.

I also considered implementing the monthly summary bonus endpoint, but instead chose Swagger/OpenAPI documentation because it makes the API easier for reviewers to understand and test.

---

## 4. Known limitations

- The application stores data only in memory, so all expenses are lost when the server restarts.
- There is no authentication because it was not required.
- Pagination and sorting are not implemented since the assignment only requires basic expense management.
- If I had more time, I would add persistent storage using a database and improve the test coverage further.