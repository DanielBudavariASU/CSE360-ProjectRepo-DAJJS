**Updates**

- `HelpArticle.java`: A new class to handle Help Articles, with all necessary fields, handles multiple groups. Has a Long Article ID which is automatically set by the Database while inserting articles based on the current number. Has other basic getter, setter methods.
- `HelpGroup.java`: A new class for managing multiple groups of articles. A Hewlpgroup has a group name and a list of articles having that group. Has other basic getter, setter methods.
- `Instructor.java`: A new class that inherits from the User class and has all functions related to handling help articles (CRUD, backup, restore).
- `Admin.java`: Added functions for handling help articles (CRUD, backup, restore).
- `Database.java`: A major update here. Now we store 4 Arraylists in 4 different files: `users.ser` and `invitations.ser` + `helpArticles.ser` and `helpGroups.ser`. Help Articles can be backed up and restored, and again this backup is stored in serialized files (.ser). Backup can be made for specific groups as well. I have written most of the functions needed for this phase, but we might need more based on how we want stuff on the UI. All new functions implemented are below: 
// --------------------------------------------------------------------------------------------------------------------------------

- `Main.java`: Updated CLI testing with phase 1 features + phase 2 features (Admin/Instructor Menu with Article Management options, backup, etc.). Run this file for a quick look at the classes and database working.

**Also I am not sure if we have to allow users to search for articles and then display them based on the search query. If that is required, we still need to implement it since I did not write anything for that.**
