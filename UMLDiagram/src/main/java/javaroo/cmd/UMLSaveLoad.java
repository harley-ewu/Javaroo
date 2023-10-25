package javaroo.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UMLSaveLoad {
    private static final Gson GSON = new Gson();

    static void saveData(String saveFilePath) {
        try (FileWriter fileWriter = new FileWriter(saveFilePath + ".json")) {
            JsonObject data = new JsonObject();
            data.add("classes", createClassesJsonArray());
            data.add("relationships", createRelationshipsJsonArray());
            GSON.toJson(data, fileWriter);
            System.out.println("Data saved to " + saveFilePath);
            UMLDiagram.setSaved(true);
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
        for (UMLAttributes attributes: umlClass.getAttributes()) {
            JsonObject attributeObject = new JsonObject();
            attributeObject.addProperty("name", attributes.getName());
            attributesArray.add(attributeObject);
        }
        classObject.add("attributes", attributesArray);
        return classObject;
    }

    private static JsonArray createRelationshipsJsonArray() {
        JsonArray relationshipArray = new JsonArray();
        for (UMLRelationships relationship : UMLDiagram.getRelationships()) {
            JsonObject relationshipObject = serializeUMLRelationship(relationship);
            relationshipArray.add(relationshipObject);
        }
        return relationshipArray;
    }

    private static JsonObject serializeUMLRelationship(UMLRelationships relationship) {
        JsonObject relationshipObject = new JsonObject();
        relationshipObject.addProperty("source", relationship.getSource().getName());
        relationshipObject.addProperty("destination", relationship.getDest().getName());
        return relationshipObject;
    }

    static void loadData(String saveFilePath) {
        try (FileReader fileReader = new FileReader(saveFilePath + ".json")) {
            JsonObject data = GSON.fromJson(fileReader, JsonObject.class);
            if (data == null) {
                System.err.println("Error loading data: Data file is empty or corrupted.");
                return;
            }

            JsonArray classesArray = data.getAsJsonArray("classes");
            loadClasses(classesArray);
            loadRelationships(data.getAsJsonArray("relationships"));
            System.out.println("Data loaded from " + saveFilePath);
            UMLDiagram.setSaved(true);
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static void loadClasses(JsonArray classesArray) {
        for (JsonElement classElement : classesArray) {
            JsonObject classObject = classElement.getAsJsonObject();
            String className = classObject.get("name").getAsString();

            // Create a new UMLClass instance and add it to the static map
            UMLClass.addClass(className);

            // Load attributes for this class
            JsonArray attributesArray = classObject.getAsJsonArray("attributes");
            for (JsonElement attributeElement : attributesArray) {
                String attributeName = attributeElement.getAsJsonObject().get("name").getAsString();
                UMLAttributes.addAttribute(UMLClass.getClass(className), attributeName);
            }
        }
    }

    private static void loadRelationships(JsonArray relationshipsArray) {
        for (JsonElement relationshipElement : relationshipsArray) {
            UMLRelationships umlRelationship = deserializeUMLRelationship(relationshipElement.getAsJsonObject());
            if (umlRelationship != null) {
                UMLRelationships.addRelationship(umlRelationship.getSource(), umlRelationship.getDest());
            }
        }
    }


    private static UMLRelationships deserializeUMLRelationship(JsonObject relationshipObject) {
        UMLClass source = UMLDiagram.getClasses().get(relationshipObject.get("source").getAsString());
        UMLClass destination = UMLDiagram.getClasses().get(relationshipObject.get("destination").getAsString());

        if (source != null && destination != null) {
            return new UMLRelationships(source, destination);
        } else {
            return null;
        }
    }
}



