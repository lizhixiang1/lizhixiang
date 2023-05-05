package edu.uob;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameEntityLoad {

    public void load(File entitesFile){
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitesFile);
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            help1(sections);
            help2(sections);
        }catch (Exception e){
        }
    }

    public void help1(ArrayList<Graph> sections){
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();
        for(int i=0;i<locations.size();i++){
            Graph location = locations.get(i);
            Node locationDetails = location.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            String locationDescription = locationDetails.getAttribute("description");
            LocationEntity locationEntity = new LocationEntity(locationName,locationDescription );

            ArrayList<Graph> subgraphs = location.getSubgraphs();
            for(int j=0;j<subgraphs.size();j++){
                Graph graph=subgraphs.get(j);
                String graphId = graph.getId().getId();
                List<Node> childrenNodes=graph.getNodes(false);
                for(int k=0;k<childrenNodes.size();k++){
                    Node itemDetails=childrenNodes.get(k);
                    String itemName = itemDetails.getId().getId();
                    String itemDescription = itemDetails.getAttribute("description");
                    locationAddEntity(locationEntity,graphId,itemName);
                    generalEntity(graphId,itemName,itemDescription);
                }
            }
            if(i==0){
                DispatchCenter.bornLocation=locationEntity;
            }
            DispatchCenter.locations.put(locationName,locationEntity);
        }
    }
    public void help2(ArrayList<Graph> sections){
        ArrayList<Edge> paths = sections.get(1).getEdges();
        for(int i=0;i<paths.size();i++){
            Edge path = paths.get(i);
            Node fromLocation = path.getSource().getNode();
            String fromName = fromLocation.getId().getId();
            Node toLocation = path.getTarget().getNode();
            String toName = toLocation.getId().getId();
            LocationEntity fromLocationEntity = DispatchCenter.locations.get(fromName);
            LocationEntity toLocationEntity = DispatchCenter.locations.get(toName);
            fromLocationEntity.toLocations.put(toLocationEntity.getName(),toLocationEntity);
        }
    }

    private void locationAddEntity(LocationEntity location,String graphId, String itemName){
        if(graphId.equals("artefacts")){
            Map<String,Integer> artefactsMap=location.artefacts;
            artefactsMap.put(itemName,1);
        }

        if(graphId.equals("furniture")){
            Map<String,Integer> furnitureMap=location.furniture;
            furnitureMap.put(itemName,1);
        }

        if(graphId.equals("characters")){
            Map<String,Integer> charactersMap=location.characters;
            charactersMap.put(itemName,1);
        }
    }

    private void generalEntity(String graphId,String itemName,String itemDescription){
        if(graphId.equals("artefacts")){
            if(!DispatchCenter.artefacts.containsKey(itemName)){
                ArtefactEntity artefactEntity = new ArtefactEntity(itemName, itemDescription);
                DispatchCenter.artefacts.put(itemName,artefactEntity);
            }

        }

        if(graphId.equals("furniture")){
            if(!DispatchCenter.furniture.containsKey(itemName)){
                FurnitureEntity furnitureEntity = new FurnitureEntity(itemName, itemDescription);
                DispatchCenter.furniture.put(itemName,furnitureEntity);
            }
        }

        if(graphId.equals("characters")){
            if(!DispatchCenter.characters.containsKey(itemName)){
                CharacterEntity characterEntity = new CharacterEntity(itemName, itemDescription);
                DispatchCenter.characters.put(itemName,characterEntity);
            }
        }
    }
}
