Welcome to the JAVAROO UML Editor.

To be able to run the program, please follow these steps. 

1. Download java development kit from https://www.oracle.com/java/technologies/downloads/

2. Install java development kit

3. Clone repository from github at https://github.com/harley-ewu/Javaroo.git

4. Keep .jar and .java files on same directory 

6. Create a directory to save .class files (optional)
7. If using a Mac computer Install Homebrew
      1. use this command: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
         Note: if using a Mac with Apple Silicon you need to add homebrew to your PATH folder using this
         command: `echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile eval "$(/opt/homebrew/bin/brew
         shellenv)"`
      2. Install Gradle using this command: `brew install gradle`
8. For Windows computer users ensure that you have `JAVA_HOME` added to your system variables
9. Open your Terminal (Linux or Mac Users) or CMD (Windows)
10. Navigate to the directory where you saved the repository from github
   using the terminal/CMD commands
11. Build the project using the `gradlew build` command
12. Navigate to the build directory using the terminal/CMD commands `.build\distributions`
13. Unzip the folder `UMLDiagram-1.0.zip`
14. Go into the unzipped folder and click into the bin folder
15. Double click the applictation to run it
16. Have fun with the JAVAROO UML Editor :)
