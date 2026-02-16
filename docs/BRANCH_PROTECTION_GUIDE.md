# 🛡️ Branch Protection Rules Configuration

## University Management System — GitHub Repository Settings

---

## 📌 Why Branch Protection?

Branch protection rules enforce quality gates on your `main` branch:
- **Prevent direct pushes** — all changes must go through Pull Requests
- **Require CI checks** — tests must pass before merging
- **Require code review** — at least 1 approval needed
- **Maintain code quality** — automated checks enforce standards

---

## ⚙️ Step-by-Step Configuration

### Navigate to Settings

1. Go to **https://github.com/paraffinic0doomer/university-management**
2. Click **Settings** (gear icon, top right)
3. In the left sidebar, click **Branches** (under "Code and automation")
4. Click **Add branch protection rule** (or **Add classic branch protection rule**)

---

### Rule 1: Protect `main` Branch

#### Branch Name Pattern
```
main
```

#### ✅ Required Settings (Check These Boxes):

| Setting | Value | Purpose |
|---------|-------|---------|
| **Require a pull request before merging** | ✅ Enable | No direct pushes to main |
| → Require approvals | ✅ Enable | Code review is mandatory |
| → Required number of approvals | **1** | At least 1 reviewer must approve |
| → Dismiss stale pull request approvals when new commits are pushed | ✅ Enable | Re-review after changes |
| **Require status checks to pass before merging** | ✅ Enable | CI must pass |
| → Require branches to be up to date before merging | ✅ Enable | Branch must include latest main |
| → Status checks that are required | Add: `Build and Test`, `Test Summary` | Specific CI jobs |
| **Require conversation resolution before merging** | ✅ Enable | All review comments addressed |
| **Do not allow bypassing the above settings** | ✅ Enable | Even admins follow rules |

#### ❌ Leave These Unchecked (for this project):

| Setting | Value | Reason |
|---------|-------|--------|
| Require signed commits | ❌ | Not needed for academic projects |
| Require linear history | ❌ | Squash merge already provides this |
| Require deployments to succeed | ❌ | No deployment pipeline yet |
| Lock branch | ❌ | Branch should accept PRs |
| Allow force pushes | ❌ | Dangerous — never on main |
| Allow deletions | ❌ | Protect main from deletion |

5. Click **Create** / **Save changes**

---

### Rule 2: Protect `testing/**` Branches (Optional)

#### Branch Name Pattern
```
testing/**
```

#### Settings:

| Setting | Value |
|---------|-------|
| Require a pull request before merging | ❌ (developers push directly) |
| Require status checks to pass | ✅ Enable |
| → Required checks: `Build and Test` | ✅ |
| Allow force pushes | ✅ (for rebasing) |

---

## 🔗 Adding Required Status Checks

After your first CI run on a PR, GitHub will recognize the available status checks:

1. In the **Require status checks** section, search for:
   - `Build and Test` ← from the `build-and-test` job
   - `Test Summary` ← from the `test-summary` job
   - `Code Quality Check` ← from the `code-quality` job
2. Select all three
3. Save

> **Note:** Status checks only appear after the CI workflow has run at least once. Create a PR first, let CI run, then configure the required checks.

---

## 📋 Verification Checklist

After configuring, verify by testing:

### Test 1: Direct Push Should Fail
```bash
git checkout main
echo "test" >> README.md
git add . && git commit -m "test direct push"
git push origin main
# Expected: REJECTED — remote: error: GH006: Protected branch update failed
```

### Test 2: PR Without Approval Should Block Merge
1. Create a PR from any branch to `main`
2. Try to click **Merge** without any reviews
3. Expected: **Merge button disabled** with message "Review required"

### Test 3: PR With Failing Tests Should Block Merge
1. Create a PR with a broken test
2. CI runs and fails
3. Expected: **Merge button disabled** with message "Required status check failed"

---

## 🖼️ Visual Guide — What You'll See

### Protected Branch Indicator
On the repository main page, you'll see a 🔒 lock icon next to `main`:
```
🔒 main ← Protected
```

### PR Merge Checks Panel
On every PR, you'll see:
```
Checks:
  ✅ Build and Test          — Required
  ✅ Code Quality Check      — Required
  ✅ Test Summary            — Required

Review:
  ⏳ Waiting for review      — 1 approval required
  
Merge:
  🔒 Merging is blocked      — Requirements not met
```

After approval + passing checks:
```
Checks:
  ✅ Build and Test          — Passed
  ✅ Code Quality Check      — Passed
  ✅ Test Summary            — Passed

Review:
  ✅ paraffinic0doomer approved — 1/1 approvals

Merge:
  🟢 Squash and merge        — Ready to merge
```

---

## 🏢 Enterprise Best Practices

### Branching Strategy (Git Flow Lite)

```
main (protected)
 ├── testing/unit-integration-tests
 ├── feature/add-enrollment-api
 ├── feature/grade-management
 ├── bugfix/fix-student-validation
 └── hotfix/security-patch
```

### Branch Naming Convention

| Prefix | Purpose | Example |
|--------|---------|---------|
| `feature/` | New functionality | `feature/add-grade-system` |
| `testing/` | Test infrastructure | `testing/unit-integration-tests` |
| `bugfix/` | Non-critical fixes | `bugfix/fix-null-pointer` |
| `hotfix/` | Critical production fixes | `hotfix/security-vulnerability` |
| `refactor/` | Code improvements | `refactor/service-layer` |
| `docs/` | Documentation only | `docs/api-documentation` |

### Commit Message Convention (Conventional Commits)

```
<type>(<scope>): <short description>

<optional body>

<optional footer>
```

**Types:**
| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `test` | Adding/updating tests |
| `ci` | CI/CD changes |
| `docs` | Documentation |
| `refactor` | Code refactoring |
| `chore` | Maintenance tasks |

**Examples:**
```
feat(student): add email validation to StudentService
fix(auth): resolve 401 error on student profile endpoint  
test(department): add integration tests for CRUD operations
ci: update GitHub Actions to use JDK 17 temurin
docs: update README with testing instructions
```
