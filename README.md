Welcome to the JAVAROO UML Editor.

To be able to run the program, please follow these steps. 

1. Download java development kit from https://www.oracle.com/java/technologies/downloads/

2. Install java development kit

3. Clone repository from github at https://github.com/harley-ewu/Javaroo.git

4. Keep .jar and .java files on same directory 

6. Create a directory to save .class files (optional)
7. Install Homebrew
      1. use this command: /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
         Note: if using a Mac with Apple Silicon you need to add homebrew to your PATH folder using this
         command: echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile eval "$(/opt/homebrew/bin/brew shellenv)"
         /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
      2. Install Gradle using this command: brew install gradle
8. Open your Terminal (Linux or Mac Users) or CMD (Windows)
9. Navigate to the directory where you saved the repository from github
   using the terminal/CMD commands
10. Build the project using the `gradlew build` command
11. Navigate to the build directory using the terminal/CMD commands `.build\libs`
12. run JAVAROO UML Editor via the `java -jar Project1.2-1.0-SNAPSHOT.jar`
13. Have fun with the JAVAROO UML Editor :)
14. After saving a file, you can find them in the `build\libs` directory.
