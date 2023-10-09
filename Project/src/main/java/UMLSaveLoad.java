import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class UMLSaveLoad {

    @SuppressWarnings("unchecked")
    private void saveData(){
        JSONArray classArray = new JSONArray();
        for (Class Class : UMLDiagram.getClasses().values()){
            JSONObject classObj = new JSONObject();
            classObj.put("name", Class.getName());
            JSONArray attributesArray = new JSONArray();

            for (Map.Entry<String, String> entry : Class.getAttributes().entrySet()){
                JSONObject attrObj = new JSONObject();
                attrObj.put("name", entry.getKey());
                attrObj.put("type", entry.getValue());
                attributesArray.add(attrObj);
            }
            classObj.put("attributes", attributesArray);
            classArray.add(classObj);
        }

        JSONArray relationshipArray = new JSONArray();
        for (UMLRelationships relationship : UMLDiagram.getRelationships()) {
            JSONObject relationshipObj = new JSONObject();
            relationshipObj.put("source", relationship.getSource());
            relationshipObj.put("destination", relationship.getDestination());
            relationshipArray.add(relationshipObj);
        }

        JSONObject data = new JSONObject();
        data.put("classes", classArray);
        data.put("relationships", relationshipArray);

        try (FileWriter fileWriter = new FileWriter(UMLDiagram.safeFile)) {
            fileWriter.write(data.toJSONString());
            System.out.println("Data saved to " + UMLDiagram.safeFile);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try (FileReader fileReader = new FileReader(UMLDiagram.safeFile)) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(fileReader);

            JSONArray classArray = (JSONArray) data.get("classes");
            for (Object obj : classArray) {
                JSONObject classObj = (JSONObject) obj;
                String className = (String) classObj.get("name");
                Class classEntity = new Class(className);

                JSONArray attributesArray = (JSONArray) classObj.get("attributes");
                for (Object attrObj : attributesArray) {
                    JSONObject attribute = (JSONObject) attrObj;
                    String attrName = (String) attribute.get("name");
                    String attrType = (String) attribute.get("type");
                    classEntity.addAttribute(attrName, attrType);
                }
                UMLDiagram.getClasses().put(className, classEntity);
            }

            JSONArray relationshipArray = (JSONArray) data.get("relationships");
            for (Object obj : relationshipArray) {
                JSONObject relationshipObj = (JSONObject) obj;
                String source = (String) relationshipObj.get("source");
                String destination = (String) relationshipObj.get("destination");
                UMLDiagram.getRelationships().add(new UMLRelationships(source, destination));
            }

            System.out.println("Data loaded from " + UMLDiagram.safeFile);
        } catch (IOException | ParseException e) {
            System.err.println("Error loading data: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
