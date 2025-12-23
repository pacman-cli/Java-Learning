# 🔐 Patient Registration Guide

## Overview

This guide provides step-by-step instructions for patient registration in the Hospital Management System, including troubleshooting common issues.

---

## 📝 Registration Process

### Step 1: Access the Registration Page

1. Open your web browser
2. Navigate to: `http://localhost:3000/register`
3. You should see the registration form

### Step 2: Fill Out the Registration Form

**Required Fields:**

1. **Username**
   - Minimum 3 characters
   - Maximum 50 characters
   - Must be unique (not already taken)
   - Example: `john_doe`

2. **Full Name**
   - Your complete name
   - Maximum 100 characters
   - Example: `John Doe`

3. **Email Address**
   - Must be a valid email format
   - Must be unique (not already in use)
   - Example: `john.doe@example.com`

4. **Password**
   - Minimum 6 characters
   - Maximum 100 characters
   - Use a strong password
   - Example: `MyStr0ngP@ss`

5. **Confirm Password**
   - Must match the password field exactly
   - Re-enter your password

6. **Role Selection**
   - Select "Patient" for regular user registration
   - Other roles are for administrative purposes

7. **Terms and Conditions**
   - Read and check the terms agreement box
   - Registration cannot proceed without acceptance

### Step 3: Submit Registration

1. Click the "Create Account" button
2. Wait for the system to process your registration
3. You should see a success message
4. You will be automatically logged in and redirected to the dashboard

---

## ✅ What Happens After Registration

### Automatic Login

After successful registration:
1. ✅ Your account is created in the system
2. ✅ You are automatically logged in
3. ✅ You are redirected to the patient dashboard
4. ✅ Your session token is stored securely

### First Login Experience

You'll be taken to the Patient Dashboard where you can:
- View your profile information
- Book appointments
- Access medical records
- View prescriptions
- Check lab reports
- Manage billing
- Track health metrics
- Update settings

---

## 🔧 Troubleshooting

### Common Errors and Solutions

#### 1. "Username is already taken"

**Problem:** The username you chose is already registered in the system.

**Solution:**
- Try a different username
- Add numbers or underscores to make it unique
- Example: `john_doe` → `john_doe2` or `john_doe_2024`

#### 2. "Email is already in use"

**Problem:** The email address is already associated with another account.

**Solution:**
- Use a different email address
- If you already have an account, use the login page instead
- Contact support if you believe this is an error

#### 3. "Password must be at least 6 characters long"

**Problem:** Your password doesn't meet the minimum length requirement.

**Solution:**
- Create a password with at least 6 characters
- Consider using a mix of letters, numbers, and symbols
- Example: `Pass123!` or `MySecurePass2024`

#### 4. "Passwords do not match"

**Problem:** The password and confirm password fields don't match.

**Solution:**
- Carefully re-enter your password in both fields
- Check for typos or extra spaces
- Use the eye icon to show/hide password and verify

#### 5. "Email should be valid"

**Problem:** The email format is incorrect.

**Solution:**
- Ensure your email follows the format: `name@domain.com`
- Check for typos
- Valid examples:
  - ✅ `john@example.com`
  - ✅ `jane.doe@hospital.org`
  - ❌ `johnexample.com` (missing @)
  - ❌ `john@` (incomplete domain)

#### 6. "Please accept the terms and conditions"

**Problem:** The terms agreement checkbox is not checked.

**Solution:**
- Read the terms and conditions
- Check the agreement box before submitting
- Cannot proceed without acceptance

#### 7. "An unexpected error occurred"

**Problem:** This was a bug that has been fixed in the latest version.

**Solution:**
- ✅ **Fixed!** The registration now works correctly
- The system now properly handles user registration
- You will be automatically logged in after successful registration

**If the error persists:**
1. Clear your browser cache and cookies
2. Try using a different browser
3. Check that the backend server is running
4. Verify your internet connection
5. Contact system administrator

---

## 🔒 Security Best Practices

### Creating a Strong Password

**Do's:**
- ✅ Use at least 8 characters (6 is minimum, but longer is better)
- ✅ Mix uppercase and lowercase letters
- ✅ Include numbers
- ✅ Add special characters (!@#$%^&*)
- ✅ Make it unique for this system

**Don'ts:**
- ❌ Don't use common words or phrases
- ❌ Don't use personal information (birthdate, name)
- ❌ Don't reuse passwords from other websites
- ❌ Don't share your password with anyone

**Good Password Examples:**
- `H0sp!tal2024`
- `Secure#Pass123`
- `MyHealth@2024`

### Protecting Your Account

1. **Never share your credentials**
   - Keep your username and password private
   - Don't write them down in unsecure places

2. **Logout when finished**
   - Always logout after using the system
   - Especially on shared computers

3. **Use secure networks**
   - Avoid using public Wi-Fi for registration
   - Use trusted internet connections

4. **Keep your email secure**
   - Your email is used for account recovery
   - Use a secure email account

---

## 📱 Mobile Registration

The registration page is fully responsive and works on:
- ✅ Desktop computers
- ✅ Tablets (iPad, Android tablets)
- ✅ Smartphones (iPhone, Android phones)
- ✅ All modern web browsers

**Supported Browsers:**
- Chrome (recommended)
- Firefox
- Safari
- Edge
- Opera

---

## 🆘 Getting Help

### Before Contacting Support

1. **Check this guide** for common issues and solutions
2. **Try different browsers** if experiencing problems
3. **Clear cache and cookies** and try again
4. **Verify all fields** are filled correctly

### Contact Support

If you still need help:

**Email:** support@hospital-system.com

**Phone:** (555) 123-4567

**Hours:** Monday - Friday, 9:00 AM - 5:00 PM

**What to Include:**
- Description of the problem
- Error messages you received
- Browser and device information
- Steps you've already tried

---

## 🎯 Quick Checklist

Before submitting registration, verify:

- [ ] Username is unique and 3-50 characters
- [ ] Full name is entered correctly
- [ ] Email is valid format and not already in use
- [ ] Password is at least 6 characters
- [ ] Confirm password matches exactly
- [ ] "Patient" role is selected
- [ ] Terms and conditions are accepted
- [ ] No red error messages are showing

---

## 🔄 After Registration

### What to Do Next

1. **Complete Your Profile**
   - Go to Settings → Profile
   - Add phone number
   - Update address
   - Add emergency contact

2. **Book Your First Appointment**
   - Navigate to "My Appointments"
   - Click "Book New Appointment"
   - Select doctor and date

3. **Explore Features**
   - View medical records
   - Check prescriptions
   - Access lab reports
   - Monitor billing
   - Track health metrics

### Account Management

- **Change Password:** Settings → Security → Change Password
- **Update Email:** Settings → Profile → Email Address
- **Notification Preferences:** Settings → Notifications
- **View Activity:** Dashboard → Recent Activity

---

## 📊 Technical Details (For Developers)

### Registration API Endpoint

```
POST /api/auth/register
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "fullName": "string",
  "email": "string",
  "roles": ["PATIENT"]
}
```

### Response Flow

1. Backend validates input and creates user
2. Returns `UserDto` (without token)
3. Frontend automatically calls login endpoint
4. Login returns `AuthResponse` with JWT token
5. User is redirected to dashboard

### Error Codes

- `400` - Validation error (bad input)
- `409` - Conflict (username/email already exists)
- `500` - Server error (contact administrator)

---

## 📝 Notes

- Registration is open to all new patients
- Admin approval is not required
- Account is activated immediately
- Default role is "PATIENT"
- Session expires after 24 hours of inactivity

---

## 🎓 Video Tutorial

*Coming Soon: Step-by-step video walkthrough of the registration process*

---

**Last Updated:** December 2024  
**Version:** 2.0.0  
**Status:** ✅ Fully Functional