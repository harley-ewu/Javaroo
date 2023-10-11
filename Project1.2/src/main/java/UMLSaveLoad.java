import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class UMLSaveLoad {
    // A constant String that represents the name of the save file.
    private static final String SAVE_FILE = "class_manager.json";

    /**
     * saveData(): A method to serialize and save the data representing a UML diagram.
     * It serializes UML class entities and their relationships into JSON format and saves them to a file.
     */
    private void saveData() {
        // Creating a Gson object which is used for converting Java objects to JSON format.
        // Gson is a library used to convert Java Objects into their JSON representation.
        // It can also be used to convert a JSON string to an equivalent Java object.
        Gson gson = new Gson();

        // A try-with-resources block which ensures that the FileWriter is closed after the block is executed.
        try (FileWriter fileWriter = new FileWriter(SAVE_FILE)) {
            // Creating the main JsonObject which will hold all serialized data.
            // JsonObject is a class of Gson library that is used to create a JSON object
            JsonObject data = new JsonObject();

            // Creating a JsonArray to store JSON representations of UML classes.
            // JsonArray is a class of Gson library used to create a JSON array
            JsonArray classArray = new JsonArray();

            // Loop through all UML class entities stored in UMLDiagram.
            for (UMLClass classEntity : UMLDiagram.getClasses().values()) {
                // Creating a JsonObject to hold the serialized data of a single UML class.
                JsonObject classObj = new JsonObject();
                // Storing the name of the UML class in the JsonObject.
                classObj.addProperty("name", classEntity.getName());

                // Creating a JsonArray to store JSON representations of the UML class attributes.
                JsonArray attributesArray = new JsonArray();

                // Loop through all attributes of the UML class.
                for (Map.Entry<String, String> entry : classEntity.getAttributes().entrySet()) {
                    // Creating a JsonObject to hold the serialized data of a single attribute.
                    JsonObject attrObj = new JsonObject();
                    // Storing the name and type of the attribute in the JsonObject.
                    attrObj.addProperty("name", entry.getKey());
                    attrObj.addProperty("type", entry.getValue());
                    // Adding the attribute JsonObject to the attributes JsonArray.
                    attributesArray.add(attrObj);
                }

                // Linking the attributes JsonArray to the respective class JsonObject.
                classObj.add("attributes", attributesArray);
                // Adding the class JsonObject to the classes JsonArray.
                classArray.add(classObj);
            }

            // Creating a JsonArray to store JSON representations of UML relationships.
            JsonArray relationshipArray = new JsonArray();

            // Loop through all UML relationships stored in UMLDiagram.
            for (UMLRelationship relationship : UMLDiagram.getRelationships()) {
                // Creating a JsonObject to hold the serialized data of a single relationship.
                JsonObject relationshipObj = new JsonObject();
                // Storing the source and destination of the relationship in the JsonObject.
                relationshipObj.addProperty("source", relationship.getSource());
                relationshipObj.addProperty("destination", relationship.getDestination());
                // Adding the relationship JsonObject to the relationships JsonArray.
                relationshipArray.add(relationshipObj);
            }

            // Linking the classes and relationships JsonArrays to the main data JsonObject.
            data.add("classes", classArray);
            data.add("relationships", relationshipArray);

            // Converting the data JsonObject into a JSON formatted string and writing it to the file.
            gson.toJson(data, fileWriter);

            // Informing the user about successful data save via the console.
            System.out.println("Data saved to " + SAVE_FILE);
        } catch (IOException e) {
            // Handling potential IO exceptions and informing the user via the console.
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * loadData(): A method to deserialize and load the data representing a UML diagram.
     * It reads data from a file, deserializes it from JSON format, and repopulates UML class entities and relationships.
     */
    private void loadData() {
        // Creating a Gson object which is used for converting JSON formatted string to Java objects.
        Gson gson = new Gson();

        // A try-with-resources block which ensures that the FileReader is closed after the block is executed.
        try (FileReader fileReader = new FileReader(SAVE_FILE)) {
            // Reading and deserializing JSON formatted string from the file into a JsonObject.
            JsonObject data = gson.fromJson(fileReader, JsonObject.class);

            // Checking if the data object is null indicating that the file is empty or the data is not valid JSON.
            if (data == null) {
                // Informing the user about the issue via the console.
                System.err.println("Error loading data. Data file is empty.");
                return;
            }

            // Extracting the JsonArray of classes and iterating over each element to deserialize and recreate UML class entities.
            JsonArray classArray = data.getAsJsonArray("classes");
            for (JsonElement classElement : classArray) {
                JsonObject classObj = classElement.getAsJsonObject();
                // Extracting the name of the UML class.
                String className = classObj.get("name").getAsString();
                // Creating a new UMLClass object with the extracted name.
                UMLClass classEntity = new UMLClass(className);

                // Extracting the JsonArray of attributes and iterating over each element to deserialize and add attributes to the UMLClass object.
                JsonArray attributesArray = classObj.getAsJsonArray("attributes");
                for (JsonElement attrElement : attributesArray) {
                    JsonObject attribute = attrElement.getAsJsonObject();
                    // Extracting the name and type of the attribute.
                    String attrName = attribute.get("name").getAsString();
                    String attrType = attribute.get("type").getAsString();
                    // Adding the extracted attribute to the UMLClass object.
                    classEntity.addAttribute(attrName, attrType);
                }

                // Adding the recreated UMLClass object to the UMLDiagram’s data structure.
                UMLDiagram.getClasses().put(className, classEntity);
            }

            // Extracting the JsonArray of relationships and iterating over each element to deserialize and recreate UML relationships.
            JsonArray relationshipArray = data.getAsJsonArray("relationships");
            for (JsonElement relationshipElement : relationshipArray) {
                JsonObject relationshipObj = relationshipElement.getAsJsonObject();
                // Extracting the source and destination of the relationship.
                String source = relationshipObj.get("source").getAsString();
                String destination = relationshipObj.get("destination").getAsString();
                // Creating a new UMLRelationship object with the extracted source and destination and adding it to the UMLDiagram’s data structure.
                UMLDiagram.setRelationships().add(new UMLRelationship(source, destination));
            }

            // Informing the user about successful data load via the console.
            System.out.println("Data loaded from " + SAVE_FILE);
        } catch (IOException e) {
            // Handling potential IO exceptions and informing the user via the console.
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}

