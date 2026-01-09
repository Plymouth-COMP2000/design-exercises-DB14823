# Restaurant app - COMP2000 assessment

## Youtube link for video submission - https://youtu.be/ypuafy8gniw

## Overview

This is a mobile application, designed for a restaurant, which is intended to be used by the customers and staff. 
The app suuports two user roles, one for each of these, each having their own functionality and restricted access
to their respective features.

My app focuses on clean and usable UI design, persistant data storage, role-based navigation and user interactions,
such as notifications and booking tables.

## Features
### User roles/authentication
- each user role has a separate login screen and navigation flow
- session management prevents unauthorised access to role-specific screens
- If a user tries to access a screen they shouldn't, they will be immediately redirected to the login screen

### Customer features
- Create table bookings with:
  - Date/time selection (time rounded to 5-minute intervals)
  - party size selection
- Edit existing bookings
- Delete bookings
- Receive notifications:
  - When the staff have updated the menu
  - If their booking is cancelled
- Manage notifications

### Staff features
- View all upcoming bookings in chronological order, including all necessary customer details
- Manage menu items
  - Add new items
  - Delete items
  - Edit existing items
- receive notifications when customers add, edit or delete bookings

## Technical details
### Architecture
- Written in java
- UI built using XML layouts
- Local persistence via SQLite using custom DatabaseHelper
- Background ops handled by ExecutorService
- Session state managed by SessionManager

### Database
A local SQLite databse is used to store:
- Menu items
- Customer bookings
- Notifications

### UI/UX
- Consistent visual styling across all screens
- Use of material design components when appropriate
- Layouts tested across multiple emulator configs ensures responsiveness
- Clear feedback to user via toasts

