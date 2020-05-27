# WorkTrack - TNA Android App

### Description:
>Simple TNA/Time-tracking application (registration time) - the user adds/edits/deletes entries representing the time of his work to the base. On their basis, working time is calculated and charts are created. The data is information for supervisors about progress, and can also be a "diary" for an employee (what was done/what is missing).

![screenshots](https://raw.githubusercontent.com/bvdzynski/worktime-tracking-app/master/screenshots.png)

### Features (division of features into a team leader and a developer)
DEVELOPER:
 - timeLogs (time entries):
   - CRUD functions
   - list of all timelogs + filtered list by project
 - projects:
   - list of assigned projects
 - displaying chart of percentage distribution of time for projects
 
TEAM LEADER:
 - timeLogs (time entries):
   - displaying all timelogs
   - different layout of view for team leader, additional informations
 - projects:
   - CRUD functions
 - locations:
   - CRUD functions

### Used technologies:
 - MySQL (database)
 - PHP (server-side language)
 - Android (Kotlin) (app)
 
### Additional external libraries:
 - [klaxon](https://github.com/cbeust/klaxon) (Kotlin)
 - [androidplot](https://github.com/halfhp/androidplot) (Kotlin)
 - [volley](https://github.com/google/volley) (Kotlin)
   
### Developer setup info
Needed for run (on linux):
 - LAMP stack installed
 - Android Studio

Local run with emulator:
 1. clone repository
 2. import database (.api_db/worktime-tracking-app.sql) to your mysql server
 3. copy folder '.api_db/worktime-tracking-app-backend/' to your localhost
 3. change values for yours in '.api_db/worktime-tracking-app-backend/src/config.php'
 4. change 'ROOT_URL' const in 'app/src/main/java/com/project/worktimetrackingapp/Globals.kt' with your local IP
 5. build and run project in Android Studio
 
Running with .apk (not tested):
 1. clone repository
 2. import database (.api_db/worktime-tracking-app.sql) to your mysql server
 3. copy folder '.api_db/worktime-tracking-app-backend/' to your server
 3. change values for yours in '.api_db/worktime-tracking-app-backend/src/config.php'
 4. change 'ROOT_URL' const in 'app/src/main/java/com/project/worktimetrackingapp/Globals.kt' with yours
 5. build .apk in Android Studio
 6. install and run on your device
