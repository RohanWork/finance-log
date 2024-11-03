# Finance Log

Finance Log is an Android application designed to help users effectively manage and track their financial transactions. The app automatically parses SMS messages related to credits and debits, logs daily transactions, provides an intuitive history view, and displays visual summaries of cash flow by bank. 

## Features

- **Automatic Transaction Logging**: Automatically detect and log SMS messages about financial transactions.
- **Transaction Summary**: View daily credited and debited amounts, along with a transaction count by bank.
- **Transaction History**: Access a detailed history of transactions, including date, time, and amount.
- **Profile Management**: Manage your profile with customizable details like first name, last name, email, and profile picture.
- **Onboarding Guide**: Get a quick introduction to the app's features upon first use.
- **Export Data**: Export transaction history for analysis or backup.
- **Secure Access**: Sign up and log in securely with Firebase Authentication, with options for password reset.
- **Multi-Language Support**: Available in multiple languages for a better user experience.

## Screenshots

*(Include screenshots here to provide a visual overview of the app's UI and features)*

## Getting Started

Follow these steps to set up the project on your local machine:

### Prerequisites

- **Android Studio**: Make sure you have the latest version installed.
- **Java Development Kit (JDK)**: Ensure JDK 8 or later is installed.
- **Firebase Account**: Set up Firebase for authentication and real-time database.

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/RohanWork/finance-log.git
   cd finance-log
   ```

2. **Open in Android Studio**:
   - Launch Android Studio and open the project directory.
   - Allow Android Studio to sync the project and download any dependencies.

3. **Firebase Setup**:
   - Add your Firebase project configuration (`google-services.json`) to the `app` directory.
   - Ensure Firebase Authentication and Realtime Database are enabled in the Firebase Console.

4. **Build and Run**:
   - Connect an Android device or use an emulator.
   - Build and run the app from Android Studio.

## Usage

1. **Sign Up / Log In**: New users can register or sign in to their accounts.
2. **Onboarding Guide**: First-time users will be guided through the app’s features.
3. **Track Transactions**: The app automatically detects and logs SMS messages about financial transactions.
4. **Transaction History**: View your transaction history sorted by date, with detailed transaction information.
5. **Profile Management**: Update your profile details including name and profile picture.

## Contributing

If you'd like to contribute to Finance Log:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch-name`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature-branch-name`.
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
