# Smart24x7 - A Woman Safety App for SOS situations.

## Overview
Smart24x7 is an Android application designed to provide emergency assistance to users in distress situations. The app allows users to send their location details and distress messages to pre-registered contacts, along with providing quick access to helpline numbers for various emergencies.

## Project Structure

### Java Files

- **AddRelative.java**: Activity for adding emergency contacts.
- **DatabaseHelper.java**: Helper class for database operations.
- **DeleteContacts.java**: Activity for deleting emergency contacts.
- **ForgotPassword.java**: Activity for resetting password via email.
- **helplineCall.java**: Activity for calling helpline numbers.
- **HowToSwipe.java**: Activity displaying a tutorial on how to use the app.
- **Login.java**: Activity for user login.
- **MainActivity.java**: Main activity of the app, containing the main functionality.
- **MyModel.java**: Model class representing user data.
- **MyProfile.java**: Activity for viewing and updating user profile information.
- **Registration.java**: Activity for user registration.
- **SendMessage.java**: Utility class for sending emergency SMS messages.
- **Splash.java**: Splash screen activity displayed upon app launch.
- **TrigActivity.java**: Activity for triggering emergency alerts.
- **ViewListContents.java**: Activity for viewing emergency contacts.

### XML Layout Files

- **activity_add_relative.xml**: Layout file for adding a relative contact.
- **activity_delete_contacts.xml**: Layout file for deleting contacts.
- **activity_forgot_password.xml**: Layout file for password recovery.
- **activity_helpline_call.xml**: Layout file for displaying helpline contacts.
- **activity_how_to_swipe.xml**: Layout file for displaying instructions on how to use swipe gestures.
- **activity_login.xml**: Layout file for user login.
- **activity_main.xml**: Main layout file for the home screen.
- **activity_my_profile.xml**: Layout file for displaying user profile information.
- **activity_registration.xml**: Layout file for user registration.
- **activity_splash.xml**: Layout file for the splash screen.
- **activity_trig.xml**: Layout file for triggers.
- **viewlistcontents_layout.xml**: Layout file for displaying the contents of a list.


### Additional Resources

- **drawable/**: Directory containing drawable resources such as icons and images.
- **menu/**: Directory containing menu resources for the app.
- **values/**: Directory containing resource files for strings, colors, dimensions, etc.

## Installation
To use the Smart24x7 app, follow these steps:
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.
4. Alternatively, you can download the APK file from the [Releases](https://github.com/ganeshch0209/Smart-24x7/releases/tag/v1.0.0) section
   and install it on your Android device.

## Usage
1. Upon launching the app, users are prompted to register or log in.
2. After registration/login, users can access the main features of the app:
   - Trigger emergency alerts by shaking the device.
   - Access helpline numbers for various emergencies.
   - Manage user profile and registered contacts.

## Contributors
- [Ganesh](https://github.com/ganeshch0209)

## License
This project is licensed under the MIT License.
