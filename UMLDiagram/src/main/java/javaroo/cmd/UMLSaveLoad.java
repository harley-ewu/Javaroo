package javaroo.cmd;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLRelationships;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UMLSaveLoad {
    private static final Gson GSON = new Gson();
    private UMLDiagram umlDiagram;

    public UMLSaveLoad(UMLDiagram umlDiagram) {
        this.umlDiagram = umlDiagram;
    }

    public void saveData(String saveFilePath) {
        try (FileWriter fileWriter = new FileWriter(saveFilePath + ".json")) {
            JsonObject data = new JsonObject();
            data.add("classes", createClassesJsonArray());
            data.add("relationships", createRelationshipsJsonArray());
            GSON.toJson(data, fileWriter);
            System.out.println("Data saved to " + saveFilePath);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private JsonArray createClassesJsonArray() {
        JsonArray classArray = new JsonArray();
        for (UMLClass umlClass : umlDiagram.getClasses().values()) {
            JsonObject classObject = serializeUMLClass(umlClass);
            classArray.add(classObject);
        }
        return classArray;
    }

    private JsonObject serializeUMLClass(UMLClass umlClass) {
        JsonObject classObject = new JsonObject();
        classObject.addProperty("name", umlClass.getName());
        JsonArray fieldsArray = new JsonArray();
        for (UMLFields fields : umlClass.getFields()) {
            JsonObject fieldObject = new JsonObject();
            fieldObject.addProperty("name", fields.getName());
            fieldObject.addProperty("type", fields.getType());
            fieldObject.addProperty("visibility", fields.getVisibility());
            fieldsArray.add(fieldObject);
        }
        JsonArray methodsArray = new JsonArray();
        for (UMLMethods methods : umlClass.getMethods()) {
            JsonObject methodObject = new JsonObject();
            methodObject.addProperty("name", methods.getName());
            methodObject.addProperty("returnType", methods.getReturnType());

            // Serialize parameters into a JsonArray
            JsonArray parametersArray = new JsonArray();
            for (String parameter : methods.getParameters()) {
                parametersArray.add(new JsonPrimitive(parameter));
            }

            // Add the JsonArray to the methodObject
            methodObject.add("parameters", parametersArray);

            methodsArray.add(methodObject);
        }

        classObject.add("fields", fieldsArray);
        classObject.add("methods", methodsArray);
        return classObject;
    }

    private JsonArray createRelationshipsJsonArray() {
        JsonArray relationshipArray = new JsonArray();
        for (UMLRelationships relationship : umlDiagram.getRelationships()) {
            JsonObject relationshipObject = serializeUMLRelationship(relationship);
            relationshipArray.add(relationshipObject);
        }
        return relationshipArray;
    }

    private JsonObject serializeUMLRelationship(UMLRelationships relationship) {
        JsonObject relationshipObject = new JsonObject();
        relationshipObject.addProperty("source", relationship.getSource().getName());
        relationshipObject.addProperty("destination", relationship.getDest().getName());
        relationshipObject.addProperty("type", relationship.getType().toString());
        return relationshipObject;
    }

    public void loadData(String loadFilePath) {
        try (FileReader fileReader = new FileReader(loadFilePath + ".json")) {
            // Parse the JSON file
            JsonObject data = new JsonParser().parse(fileReader).getAsJsonObject();

            // Deserialize classes from JSON array
            JsonArray classesArray = data.getAsJsonArray("classes");
            for (JsonElement classElement : classesArray) {
                deserializeUMLClass(classElement.getAsJsonObject());
            }

            // Deserialize relationships from JSON array
            JsonArray relationshipsArray = data.getAsJsonArray("relationships");
            for (JsonElement relationshipElement : relationshipsArray) {
                deserializeUMLRelationship(relationshipElement.getAsJsonObject());
            }

            System.out.println("Data loaded from " + loadFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    private void deserializeUMLClass(JsonObject classObject) {
        String name = classObject.get("name").getAsString();
        UMLClass umlClass = new UMLClass(name);
        UMLDiagram.getClasses().put(name, umlClass);

        JsonArray fieldsArray = classObject.getAsJsonArray("fields");
        for (JsonElement fieldElement : fieldsArray) {
            JsonObject fieldObject = fieldElement.getAsJsonObject();
            String fieldName = fieldObject.get("name").getAsString();
            String fieldType = fieldObject.get("type").getAsString();
            String fieldVisibility = fieldObject.get("visibility").getAsString();
            umlClass.addField(fieldName, fieldType, fieldVisibility);
        }

        JsonArray methodsArray = classObject.getAsJsonArray("methods");
        for (JsonElement methodElement : methodsArray) {
            JsonObject methodObject = methodElement.getAsJsonObject();
            String methodName = methodObject.get("name").getAsString();
            String methodReturnType = methodObject.get("returnType").getAsString();
            ArrayList<String> parameters = new ArrayList<>();
            JsonArray parametersArray = methodObject.getAsJsonArray("parameters");
            for (JsonElement parameterElement : parametersArray) {
                parameters.add(parameterElement.getAsString());
            }
            umlClass.addMethod(methodName, methodReturnType, parameters);
        }
    }

    private void deserializeUMLRelationship(JsonObject relationshipObject) {
        String sourceName = relationshipObject.get("source").getAsString();
        String destName = relationshipObject.get("destination").getAsString();
        UMLClass source = UMLDiagram.getClasses().get(sourceName);
        UMLClass dest = UMLDiagram.getClasses().get(destName);
        UMLRelationships.RelationshipType type = UMLRelationships.RelationshipType.valueOf(relationshipObject.get("type").getAsString());
        UMLDiagram.getRelationships().add(new UMLRelationships(source, dest, type));
    }


}
