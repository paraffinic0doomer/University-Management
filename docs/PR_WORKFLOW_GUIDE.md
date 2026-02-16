# 🔄 Pull Request Workflow Guide

## University Management System — `testing/unit-integration-tests` → `main`

---

## 📋 Prerequisites

| Item | Status |
|------|--------|
| Branch `testing/unit-integration-tests` pushed to remote | ✅ |
| All 293 tests passing | ✅ |
| CI/CD pipeline configured (`.github/workflows/test.yml`) | ✅ |
| GitHub repository: `paraffinic0doomer/university-management` | ✅ |

---

## 🚀 Step 1: Create the Pull Request

### Option A: GitHub Web UI (Recommended)

1. Navigate to **https://github.com/paraffinic0doomer/university-management**
2. You'll see a banner: **"testing/unit-integration-tests had recent pushes — Compare & pull request"**
3. Click **Compare & pull request**
4. Fill in the PR form:

**Title:**
```
feat: comprehensive unit & integration test suite for all layers
```

**Description (paste this):**
```markdown
## Summary
Adds a comprehensive enterprise-level testing strategy covering all application layers
with 293 automated tests across entity, repository, service, and controller layers.

## Changes
### CI/CD
- Updated GitHub Actions workflow to target `main` branch (was `master`)

### Bug Fixes
- Fixed `StudentControllerTest` — added `@ActiveProfiles("test")` for H2 isolation
- Fixed `StudentControllerTest` — added `@WithMockUser` for Spring Security mock auth

### Test Suite (293 total tests)
| Layer | Test Class | Tests |
|-------|-----------|-------|
| Entity | StudentTest | 23 |
| Entity | CourseTest | 28 |
| Entity | DepartmentTest | 22 |
| Entity | TeacherTest | 25 |
| Repository | StudentRepositoryTest | 14 |
| Repository | CourseRepositoryTest | 14 |
| Repository | DepartmentRepositoryTest | 16 |
| Repository | TeacherRepositoryTest | 15 |
| Service | StudentServiceTest | 15 |
| Service | CourseServiceTest | 11 |
| Service | DepartmentServiceTest | 16 |
| Service | TeacherServiceTest | 16 |
| Controller | StudentControllerIntegrationTest | 14 |
| Controller | CourseControllerIntegrationTest | 13 |
| Controller | DepartmentControllerIntegrationTest | 22 |
| Controller | TeacherControllerIntegrationTest | 21 |
| Controller | StudentControllerTest (Unit) | 7 |
| App | ApplicationContextTest | 1 |

## Testing Stack
- JUnit 5, Mockito, MockMvc, AssertJ
- H2 in-memory database (PostgreSQL mode)
- Spring Security Test (`@WithMockUser`)
- `@DataJpaTest` for repository isolation
- `@SpringBootTest` + `@AutoConfigureMockMvc` for integration tests

## How to Verify
```bash
./mvnw clean test
# Expected: Tests run: 293, Failures: 0, Errors: 0
```
```

5. Set:
   - **Base branch:** `main`
   - **Compare branch:** `testing/unit-integration-tests`
6. Add **Reviewers** (repository owner / collaborators)
7. Add **Labels:** `testing`, `enhancement`
8. Click **Create pull request**

### Option B: GitHub CLI

```bash
# Install GitHub CLI if not already installed
# https://cli.github.com/

# Authenticate
gh auth login

# Create PR
gh pr create \
  --base main \
  --head testing/unit-integration-tests \
  --title "feat: comprehensive unit & integration test suite for all layers" \
  --body "Adds 293 automated tests covering entity, repository, service, and controller layers. Updates CI to target main branch. Fixes StudentControllerTest H2 isolation and security mock." \
  --reviewer paraffinic0doomer \
  --label "testing,enhancement"
```

### Option C: Git Command Line (Push + Web)

```bash
# Ensure you're on the testing branch
git checkout testing/unit-integration-tests

# Push with PR creation link
git push -u origin testing/unit-integration-tests

# Git will print a URL — click it to open the PR creation page
```

---

## 🔍 Step 2: Automated CI Checks

Once the PR is created, GitHub Actions will automatically:

1. **Checkout** the `testing/unit-integration-tests` branch
2. **Set up JDK 17** (Eclipse Temurin)
3. **Cache Maven dependencies** for faster builds
4. **Compile** the project (`mvn clean compile -DskipTests`)
5. **Run all 293 tests** (`mvn test`)
6. **Upload test results** as artifacts (surefire-reports)
7. **Package** the application JAR
8. **Code quality check** (`mvn verify`)
9. **Test summary** — fails the entire pipeline if any test fails

### What You'll See on the PR Page:

```
✅ build-and-test    — Build and Test         Passed
✅ code-quality      — Code Quality Check     Passed  
✅ test-summary      — Test Summary           Passed
```

> ⚠️ If any check fails, the **Merge** button will be blocked (with branch protection enabled).

---

## 👥 Step 3: Code Review Process

### For the Reviewer (Repository Owner):

1. Go to the **Files changed** tab on the PR
2. Review the changes:
   - `.github/workflows/test.yml` — CI workflow branch target update
   - `StudentControllerTest.java` — `@ActiveProfiles` and `@WithMockUser` fixes
3. Add **line-level comments** for any feedback
4. Submit review:
   - **Approve** ✅ if changes look good
   - **Request changes** 🔄 if modifications needed
   - **Comment** 💬 for general feedback

### Review Checklist:

- [ ] Tests follow AAA pattern (Arrange-Act-Assert)
- [ ] Test naming uses descriptive `@DisplayName` annotations
- [ ] H2 test profile properly configured
- [ ] Spring Security correctly mocked with `@WithMockUser`
- [ ] CI pipeline covers push and PR triggers
- [ ] No hardcoded production credentials in test files
- [ ] All 293 tests pass in CI

---

## ✅ Step 4: Merge the Pull Request

### Merge Strategy Options:

| Strategy | When to Use | Command |
|----------|-------------|---------|
| **Squash and merge** | Combine all commits into one clean commit | Recommended ✅ |
| **Merge commit** | Preserve full branch history | For complex features |
| **Rebase and merge** | Linear history, no merge commits | For small changes |

### Via GitHub UI:

1. After approval + all checks pass, click **Squash and merge**
2. Edit the commit message:
   ```
   feat: comprehensive unit & integration test suite (#1)
   
   - 293 automated tests across entity, repository, service, controller layers
   - CI workflow updated to target main branch
   - Fixed StudentControllerTest H2 isolation and security mock
   ```
3. Click **Confirm squash and merge**
4. Click **Delete branch** to clean up

### Via GitHub CLI:

```bash
# Merge with squash
gh pr merge --squash --delete-branch

# Or merge with merge commit
gh pr merge --merge --delete-branch
```

---

## 🔄 Step 5: Post-Merge Cleanup

```bash
# Switch back to main
git checkout main

# Pull the merged changes
git pull origin main

# Delete the local testing branch
git branch -d testing/unit-integration-tests

# Verify
git log --oneline -5
```

---

## 📊 Expected PR Timeline

```
 ┌─────────────────────────────────────────────────────────┐
 │  PR Created                                              │
 │  ↓                                                       │
 │  CI Checks Running (2-3 minutes)                        │
 │  ↓                                                       │
 │  ✅ All Checks Pass                                      │
 │  ↓                                                       │
 │  Reviewer Notified                                       │
 │  ↓                                                       │
 │  Code Review + Approval                                  │
 │  ↓                                                       │
 │  Squash and Merge                                        │
 │  ↓                                                       │
 │  Branch Deleted                                          │
 │  ↓                                                       │
 │  main branch updated with test suite                     │
 └─────────────────────────────────────────────────────────┘
```
