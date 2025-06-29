# BMI Calculator

A **BMI (Body Mass Index) Calculator** is a tool that helps determine whether an individual is underweight, normal weight, overweight, or obese based on their height and weight. It provides a quick overview of a person’s health status and is widely used in healthcare and fitness evaluations.

---

## 📱 Description

This project is a BMI Calculator Android application built using **Java** in **Android Studio**. The user interface is designed with **XML** to allow a clean and intuitive layout for entering:

- Weight  
- Height  
- Age  
- Gender  

The application calculates BMI in **two distinct ways**, depending on the user's age:

- 🔸 **For ages 2 to 20 years**:
  - Uses the **LMS (Lambda-Mu-Sigma)** method.
  - Calculates **BMI Z-scores** and **percentiles** based on age and gender.
  - Provides accurate health assessments using WHO/CDC growth standards.

- 🔸 **For ages above 20 years**:
  - Uses the **standard BMI formula**:
    \[
    \text{BMI} = \frac{\text{Weight (kg)}}{(\text{Height (m)})^2}
    \]
  - Offers quick BMI classification into standard categories (e.g., underweight, normal, overweight, obese).

---

## 🖼️ Preview of Application Pages

### 🔹 Splash Screen
![splash screen](https://github.com/user-attachments/assets/ca136854-b388-4d75-8b66-f55c0497db0b)

### 🔹 Main Application UI
![main application](https://github.com/user-attachments/assets/053e687f-a569-479e-8d78-920c70512761)

### 🔹 BMI Results Screens

#### Underweight
![underweight](https://github.com/user-attachments/assets/a38a538a-ca82-45a0-8bb6-5d60ae8035cf)

#### Overweight
![overweight](https://github.com/user-attachments/assets/c2d92252-8073-431a-a541-2749503aa235)

#### Obese Class
![obese class](https://github.com/user-attachments/assets/3dd4fac0-50d0-4946-9b06-e5b28f6ce5c3)

#### Speedometer-style Indicator
![speedometer](https://github.com/user-attachments/assets/2afd61bf-c0c3-4db2-a4a5-e0e543879cdd)

---

## 💡 Technologies Used

- **Java** (Core Logic)
- **XML** (UI Layout)
- **Android Studio** (Development Environment)
- **LMS CSV Dataset** (For age- and gender-based BMI Z-score calculations)

---

## 🧮 Future Improvements (Optional)

- Add chart visualization of growth percentiles.
- Store BMI history using local storage (Room DB or Shared Preferences).
- Add support for multiple languages (i18n).
