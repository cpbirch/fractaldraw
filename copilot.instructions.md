Follow these guidelines when generating code or suggesting changes:

# Code Quality and Style

- Write explicit, descriptive variable names over short, ambiguous ones
- Follow the existing project's coding style for consistency
- Use named constants instead of hardcoded values
  Example:
```typescript
// Good
const MAX_LOGIN_ATTEMPTS = 3;
const isUserLocked = loginAttempts >= MAX_LOGIN_ATTEMPTS;

// Avoid
const m = 3;
const locked = n >= m;
```

# Development Approach

- Don't invent changes beyond what's explicitly requested
- Follow security-first approach in all code modifications
- Don't modify files outside the requested scope
- Don't suggest improvements to files not mentioned in the task
  Example of focused scope:
```typescript
// Request: "Add email validation to User class"
// Good - only modifying requested file
class User {
  validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}

// Avoid - suggesting changes to other files
// ❌ "We should also update UserRepository.ts"
// ❌ "Let's improve the existing validation in Utils.ts"
```

# Communication Style & Protocol

## Step-by-Step Communication Pattern
```
1. User Request
   User: "Need to implement email validation"

2. Copilot Overview
   Copilot: "Overview: Adding email validation
   - Will create test for invalid email (15 lines)
   - Will implement validator (20 lines)
   Let's start with the test?"

3. User Approval
   User: "Looks good"

4. Implementation
   Copilot: *provides code in proper format*

5. Confirmation
   User: "OK" or "Looks good"
```

# Test-Driven Development Workflow

## TDD Cycle
```
┌── 1. Discuss Test Requirements
│   User: "Need password validation"
│   Copilot: "Let's test minimum length first"
│
├── 2. Write Test (Red)
│   describe('PasswordValidator', () => {
│     it('requires minimum 8 characters', () => {...}
│   });
│
├── 3. Implement Code (Green)
│   class PasswordValidator {
│     isValid(password: string): boolean {...}
│   }
│
├── 4. Optional: Refactor
│   - Improve naming
│   - Remove duplication
│   - Enhance readability
│
└── 5. Next Test or Complete
    - User approval required before proceeding
```

## Test Guidelines
- Write focused, single-purpose tests
- Test edge cases explicitly
- Use descriptive test names
  Example:
```typescript
// Good test names
it('should reject empty password')
it('should require minimum 8 characters')
it('should require at least one number')

// Good edge cases
it('should handle null input')
it('should handle unicode characters')
```
- Avoid overly complex tests that cover multiple scenarios
  Example of complex test:
```typescript
