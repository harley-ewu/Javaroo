import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class UMLSaveLoad {
    private static final Gson GSON = new Gson();

    private static void saveData(String saveFilePath) {
        try (FileWriter fileWriter = new FileWriter(saveFilePath)) {
            JsonObject data = new JsonObject();
            data.add("classes", createClassesJsonArray());
            data.add("relationships", createRelationshipsJsonArray());
            GSON.toJson(data, fileWriter);
            System.out.println("Data saved to " + saveFilePath);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private static JsonArray createClassesJsonArray() {
        JsonArray classArray = new JsonArray();
        for (UMLClass umlClass : UMLDiagram.getClasses().values()) {
            JsonObject classObject = serializeUMLClass(umlClass);
            classArray.add(classObject);
        }
        return classArray;
    }

    private static JsonObject serializeUMLClass(UMLClass umlClass) {
        JsonObject classObject = new JsonObject();
        classObject.addProperty("name", umlClass.getName());
        JsonArray attributesArray = new JsonArray();
        for (Map.Entry<String, UMLAtributes> attribute : umlClass.getAttributes().entrySet()) {
            JsonObject attributeObject = new JsonObject();
            attributeObject.addProperty("name", attribute.getKey());
            attributesArray.add(attributeObject);
        }
        classObject.add("attributes", attributesArray);
        return classObject;
    }

    private static JsonArray createRelationshipsJsonArray() {
        JsonArray relationshipArray = new JsonArray();
        for (UMLRelationship relationship : UMLDiagram.getRelationships()) {
            JsonObject relationshipObject = serializeUMLRelationship(relationship);
            relationshipArray.add(relationshipObject);
        }
        return relationshipArray;
    }

    private static JsonObject serializeUMLRelationship(UMLRelationship relationship) {
        JsonObject relationshipObject = new JsonObject();
        relationshipObject.addProperty("source", relationship.getSource());
        relationshipObject.addProperty("destination", relationship.getDestination());
        return relationshipObject;
    }

    private static void loadData(String saveFilePath) {
        try (FileReader fileReader = new FileReader(saveFilePath)) {
            JsonObject data = GSON.fromJson(fileReader, JsonObject.class);
            if (data == null) {
                System.err.println("Error loading data: Data file is empty or corrupted.");
                return;
            }
            loadClasses(data.getAsJsonArray("classes"));
            loadRelationships(data.getAsJsonArray("relationships"));
            System.out.println("Data loaded from " + saveFilePath);
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static void loadClasses(JsonArray classesArray) {
        for (JsonElement classElement : classesArray) {
            UMLClass umlClass = deserializeUMLClass(classElement.getAsJsonObject());
            UMLDiagram.addClass(umlClass);
        }
    }

    private static UMLClass deserializeUMLClass(JsonObject classObject) {
        String className = classObject.get("name").getAsString();
        UMLClass umlClass = new UMLClass(className);
        JsonArray attributesArray = classObject.getAsJsonArray("attributes");
        for (JsonElement attributeElement : attributesArray) {
            JsonObject attributeObject = attributeElement.getAsJsonObject();
            umlClass.addAttribute(attributeObject.get("name").getAsString());
        }
        return umlClass;
    }

    private static void loadRelationships(JsonArray relationshipsArray) {
        for (JsonElement relationshipElement : relationshipsArray) {
            UMLRelationship umlRelationship = deserializeUMLRelationship(relationshipElement.getAsJsonObject());
            UMLDiagram.addRelationship(umlRelationship);
        }
    }

    private static UMLRelationship deserializeUMLRelationship(JsonObject relationshipObject) {
        String source = relationshipObject.get("source").getAsString();
        String destination = relationshipObject.get("destination").getAsString();
        return new UMLRelationship(source, destination);
    }
}

