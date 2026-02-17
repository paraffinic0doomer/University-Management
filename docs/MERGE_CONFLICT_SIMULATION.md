# 🔥 Merge Conflict Simulation & Resolution Guide

## University Management System — Hands-On Conflict Demo

---

## 📌 Overview

This guide walks through creating an **intentional merge conflict** between `main` and a feature branch, then resolving it — demonstrating a real-world scenario in collaborative development.

---

## 🎯 Scenario

> Two developers modify the same method in `StudentService.java`:
> - **Developer A** (on `main`): Adds input validation
> - **Developer B** (on `feature/add-student-logging`): Adds logging

When Developer B tries to merge their PR, Git cannot auto-merge because both modified the same lines.

---

## 📝 Step-by-Step Conflict Creation

### Phase 1: Setup — Create the Conflict

#### Step 1: First, merge tests into main (if not done already)
```bash
# Make sure testing branch is merged into main first
# (Do this via PR or directly if branch protection isn't set up yet)
git checkout main
git pull origin main
```

#### Step 2: Create a change on `main` (Developer A)
```bash
git checkout main

# Create a temporary branch for Developer A's work
git checkout -b feature/add-student-validation
```

Edit `src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java`:

**Change the `save()` method from:**
```java
public Student save(Student student) {
    if(student == null){
        throw new IllegalArgumentException("Student cannot be null");
    }
    logger.info("Saving student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

**To (Developer A's version — adds email validation):**
```java
public Student save(Student student) {
    if(student == null){
        throw new IllegalArgumentException("Student cannot be null");
    }
    // DEVELOPER A: Added email format validation
    if (student.getEmail() != null && !student.getEmail().contains("@")) {
        throw new IllegalArgumentException("Invalid email format");
    }
    logger.info("Saving student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

```bash
# Commit Developer A's change
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java
git commit -m "feat(student): add email format validation to save method"

# Push and merge into main (via PR or direct push)
git push origin feature/add-student-validation
```

> **Merge this PR into `main` first** (via GitHub UI), then:

```bash
git checkout main
git pull origin main
```

#### Step 3: Create a conflicting change (Developer B)
```bash
# Create Developer B's branch FROM the OLD main (before Developer A's merge)
# Simulate this by branching from the commit before the validation was added
git checkout -b feature/add-student-logging main~1
# OR if main~1 doesn't have the right state, use:
# git checkout -b feature/add-student-logging <commit-hash-before-validation>
```

Edit the **same `save()` method** in `StudentService.java`:

**Developer B's version — adds detailed logging:**
```java
public Student save(Student student) {
    if(student == null){
        throw new IllegalArgumentException("Student cannot be null");
    }
    // DEVELOPER B: Added detailed audit logging
    logger.info("=== STUDENT SAVE OPERATION ===");
    logger.info("Roll Number: {}", student.getRollNumber());
    logger.info("Name: {}", student.getName());
    logger.info("Email: {}", student.getEmail());
    return studentRepository.save(student);
}
```

```bash
# Commit Developer B's change
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java
git commit -m "feat(student): add detailed audit logging to save method"
git push origin feature/add-student-logging
```

---

### Phase 2: The Conflict Appears

#### Step 4: Create PR from `feature/add-student-logging` → `main`

1. Go to GitHub → **Pull requests** → **New pull request**
2. Base: `main` | Compare: `feature/add-student-logging`
3. GitHub will show: **⚠️ Can't automatically merge — conflicts must be resolved**
4. Create the PR anyway (you can still create it with conflicts)

#### What Git Sees:

```
<<<<<<< feature/add-student-logging (Developer B)
    // DEVELOPER B: Added detailed audit logging
    logger.info("=== STUDENT SAVE OPERATION ===");
    logger.info("Roll Number: {}", student.getRollNumber());
    logger.info("Name: {}", student.getName());
    logger.info("Email: {}", student.getEmail());
    return studentRepository.save(student);
=======
    // DEVELOPER A: Added email format validation
    if (student.getEmail() != null && !student.getEmail().contains("@")) {
        throw new IllegalArgumentException("Invalid email format");
    }
    logger.info("Saving student: {}", student.getRollNumber());
    return studentRepository.save(student);
>>>>>>> main (Developer A)
```

---

### Phase 3: Resolve the Conflict

#### Option A: Resolve Locally (Recommended for Complex Conflicts)

```bash
# Switch to Developer B's branch
git checkout feature/add-student-logging

# Fetch latest main
git fetch origin main

# Rebase onto main (preferred) or merge main in
git rebase origin/main
# This will pause with a CONFLICT message

# Check which files have conflicts
git status
# You'll see: both modified: src/.../service/StudentService.java
```

**Open the file and resolve — keep BOTH changes (the correct approach):**

```java
public Student save(Student student) {
    if(student == null){
        throw new IllegalArgumentException("Student cannot be null");
    }
    // RESOLVED: Keep Developer A's validation AND Developer B's logging
    // Email validation (from Developer A)
    if (student.getEmail() != null && !student.getEmail().contains("@")) {
        throw new IllegalArgumentException("Invalid email format");
    }
    // Detailed audit logging (from Developer B)
    logger.info("=== STUDENT SAVE OPERATION ===");
    logger.info("Roll Number: {}", student.getRollNumber());
    logger.info("Name: {}", student.getName());
    logger.info("Email: {}", student.getEmail());
    return studentRepository.save(student);
}
```

```bash
# Mark as resolved
git add src/main/java/com/springproject/universitymanagementsystem/service/StudentService.java

# Continue the rebase
git rebase --continue

# Run tests to verify nothing broke
./mvnw test

# Force push (rebase rewrites history)
git push origin feature/add-student-logging --force-with-lease
```

#### Option B: Resolve via GitHub UI (For Simple Conflicts)

1. On the PR page, click **Resolve conflicts**
2. GitHub shows the conflict markers in a web editor
3. Edit the file to combine both changes (as shown above)
4. Click **Mark as resolved**
5. Click **Commit merge**

#### Option C: Resolve with `git merge` (Alternative to Rebase)

```bash
git checkout feature/add-student-logging

# Merge main into your branch
git merge origin/main
# CONFLICT — same resolution process

# After resolving:
git add .
git commit -m "merge: resolve conflict in StudentService.save() - keep both validation and logging"
git push origin feature/add-student-logging
```

---

### Phase 4: Complete the Merge

After resolving conflicts:

1. CI runs again on the updated PR ✅
2. Reviewer approves ✅
3. Click **Squash and merge** ✅
4. Delete the branch ✅

---

## 🆚 Rebase vs Merge — When to Use Which

| Aspect | `git rebase` | `git merge` |
|--------|-------------|-------------|
| **History** | Linear, clean | Preserves merge commits |
| **Best for** | Feature branches before PR | Long-running branches |
| **Force push** | Required after rebase | Not needed |
| **Risk** | Rewrites history | None |
| **Team convention** | Solo developer branches | Shared branches |

### Our Recommendation for This Project:
- **Feature branches → main**: Use **rebase** (clean history)
- **Long-running branches (testing/*)**: Use **merge** (preserve context)
- **PRs**: Always **squash and merge** (one clean commit per feature)

---

## 🔧 Conflict Prevention Best Practices

1. **Pull frequently**: `git pull --rebase origin main` before starting work
2. **Small PRs**: Merge often, don't let branches diverge too far
3. **Communication**: Coordinate when multiple developers touch the same file
4. **Feature flags**: Avoid modifying shared methods — extend instead
5. **Code ownership**: Use `CODEOWNERS` file to assign reviewers per directory

### Example CODEOWNERS File:
```
# .github/CODEOWNERS
# Global owner
*                                    @paraffinic0doomer

# Service layer changes need review
src/main/java/**/service/           @paraffinic0doomer
src/test/java/**/service/           @paraffinic0doomer

# CI/CD changes need review
.github/                            @paraffinic0doomer
```

---

## 🧹 Cleanup After Demo

```bash
# Delete the demo branches locally
git branch -D feature/add-student-validation
git branch -D feature/add-student-logging

# Delete remote demo branches
git push origin --delete feature/add-student-validation
git push origin --delete feature/add-student-logging

# Ensure main is clean
git checkout main
git pull origin main
```

---

## 📊 Conflict Resolution Flow Diagram

```
Developer B pushes PR
        │
        ▼
   ┌─────────┐     ┌──────────────────┐
   │ GitHub   │────▶│ ⚠️ Conflict       │
   │ detects  │     │ detected!         │
   └─────────┘     └──────────────────┘
        │
        ▼
   ┌─────────────────────────┐
   │ Developer B resolves:   │
   │  1. git fetch origin    │
   │  2. git rebase main     │
   │  3. Fix conflict markers│
   │  4. git rebase --continue│
   │  5. git push --force    │
   └─────────────────────────┘
        │
        ▼
   ┌─────────────────────┐
   │ CI runs again ✅     │
   │ Reviewer approves ✅ │
   │ Squash and merge ✅  │
   └─────────────────────┘
```
