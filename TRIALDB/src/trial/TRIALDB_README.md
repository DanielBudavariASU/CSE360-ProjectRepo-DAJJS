- Implemented File Based Database using Serialization files (cuz sql was annoying)
- `Main.java` has CLI testing code which can be run without a build path or config
- I have re-implemented most classes cuz they had to be adapted to the `Database` class
- `users.ser` and `invitations.ser` are the 2 serialized files where the data is stored, and these are automatically created

Classes/Features implemented:
- `Admin.java` extends `User` class and has admin related functions. I have not implemented the reset password with OTP functionality here.
- Admin can invite users through invitation code which is automatically generated and printed to console in `Invitation.java`
- Invitation Code expires once it is used, and every invitation code has a "role" associated with it and the User registering with this code automatically registers as that "role"
- `Database.java` is kind of a mess rn, but ig we can clear it up later. It has all methods needed to query the Database and save Data.
- `Password.java` stores password in an ArrayList of Characters and has related methods
-  The rest is commeneted in the code.
