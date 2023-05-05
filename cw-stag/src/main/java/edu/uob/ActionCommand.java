package edu.uob;
import java.util.HashMap;
import java.util.Map;

public class ActionCommand {
    public String execute(GameAction action,String playerName){
        if(!DispatchCenter.players.containsKey(playerName)){
            PlayerEntity playerEntity = new PlayerEntity(playerName);
            playerEntity.location=DispatchCenter.bornLocation;
            DispatchCenter.players.put(playerName,playerEntity);
        }
        if(action.trigger.equals("look")){
            return lookCommand(playerName);
        }else if(action.trigger.equals("inv") || action.trigger.equals("inventory")){
            return inventoryCommand(playerName);
        }else if(action.trigger.equals("goto")){
            return gotoCommand(playerName,action.basicCommandInfo);
        }else if(action.trigger.equals("drop")){
            return dropCommand(playerName,action.basicCommandInfo);
        }else if(action.trigger.equals("get")){
            return getCommand(playerName,action.basicCommandInfo);
        }else{
            boolean flag=executeAction(action,playerName);
            if(flag){
                return action.narration;
            }else{
                return "Something goes wrong";
            }
        }
    }


    public boolean executeAction(GameAction action,String playerName){
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        LocationEntity location = playerEntity.location;
        Map<String, Integer> subjects = action.subjects;
        Map<String, Integer> inventory = playerEntity.getInventory();
        Map<String, Integer> furniture = location.furniture;
        Map<String, Integer> artefacts = location.artefacts;
        Map<String, Integer> characters = location.characters;

        for(Map.Entry<String,Integer> subject:subjects.entrySet()){
            String subjectName=subject.getKey();
            if(!inventory.containsKey(subjectName) &&
                    !artefacts.containsKey(subjectName) &&
                    !furniture.containsKey(subjectName) &&
                    !characters.containsKey(subjectName)){
                return false;
            }
        }
        if(!executeCheckProduceAvailable(action,playerEntity)){
            return false;
        }
        if(!executeCheckConsumedAvailable(action,playerEntity)){
            return false;
        }
        executeConsume(action,playerEntity);
        executeProduce(action,playerEntity);
        return true;
    }

    public void executeConsume(GameAction action,PlayerEntity playerEntity){
        Map<String, Integer> consumed = action.consumed;
        Map<String, Integer> inventory = playerEntity.getInventory();
        for(Map.Entry<String,Integer> consumedEntity:consumed.entrySet()){
            String consumedName=consumedEntity.getKey();
            executeConsumeHelp(consumedName);
            if(inventory.containsKey(consumedName)){
                moveConsumedToStoreroom(inventory,consumedName,1);
            }else if(consumedName.equals("health")){
                if(playerEntity.getHealth()>1){
                    playerEntity.setHealth(playerEntity.getHealth()-1);
                }else{
                    playerEntity.setHealth(3);
                    Map<String, Integer> inventory1 = playerEntity.getInventory();
                    for(Map.Entry<String,Integer> artefact:inventory1.entrySet()){
                        playerEntity.location.artefacts.put(artefact.getKey(),1);
                    }
                    playerEntity.setInventory(new HashMap<>());
                    playerEntity.location=DispatchCenter.bornLocation;
                }
            }else{
                playerEntity.location.toLocations.remove(consumedName);
            }
        }
    }

    public void executeConsumeHelp(String consumedName){
        for(Map.Entry<String,LocationEntity> locationEntityEntry:DispatchCenter.locations.entrySet()){
            LocationEntity value = locationEntityEntry.getValue();
            Map<String, Integer> artefacts = value.artefacts;
            Map<String, Integer> furniture = value.furniture;
            Map<String, Integer> characters = value.characters;
            if(artefacts.containsKey(consumedName)){
                moveConsumedToStoreroom(artefacts,consumedName,1);
                break;
            }else if(furniture.containsKey(consumedName)){
                moveConsumedToStoreroom(furniture,consumedName,2);
                break;
            }else if(characters.containsKey(consumedName)){
                moveConsumedToStoreroom(characters,consumedName,2);
                break;
            }
        }
    }

    public void moveConsumedToStoreroom(Map<String,Integer> inv,String consumedName,int content){
        inv.remove(consumedName);
        LocationEntity storeroom = DispatchCenter.locations.get("storeroom");
        if(content==1){
            storeroom.artefacts.put(consumedName,1);
        }else if(content==2){
            storeroom.furniture.put(consumedName,1);
        }else{
            storeroom.characters.put(consumedName,1);
        }
    }

    public boolean executeCheckConsumedAvailable(GameAction action,PlayerEntity playerEntity){
        Map<String, Integer> consumed = action.consumed;
        Map<String, Integer> inventory = playerEntity.getInventory();
        for(Map.Entry<String,Integer> consumedItem:consumed.entrySet()){
            boolean flag=false;
            for(Map.Entry<String,LocationEntity> locationEntity:DispatchCenter.locations.entrySet()){
                LocationEntity location = locationEntity.getValue();
                Map<String, Integer> furniture = location.furniture;
                Map<String, Integer> artefacts = location.artefacts;
                Map<String, Integer> characters= location.characters;
                String consumedName = consumedItem.getKey();
                if(consumedName.equals("health") || DispatchCenter.locations.containsKey(consumedName)){
                    flag=true;
                }else if(artefacts.containsKey(consumedName) || furniture.containsKey(consumedName)) {
                    flag=true;
                }else if(inventory.containsKey(consumedName) || characters.containsKey(consumedName)){
                    flag=true;
                }
            }
            if(flag){
                continue;
            }
            return false;
        }
        return true;
    }

    public void executeProduce(GameAction action, PlayerEntity playerEntity){
        Map<String, Integer> produced = action.produced;
        for(Map.Entry<String,Integer> producedEntity:produced.entrySet()){
            String produceName = producedEntity.getKey();
            if(produceName.equals("health")){
                int health = playerEntity.getHealth();
                if(health<=2){
                    playerEntity.setHealth(health+1);
                }
                continue;
            }
            if(DispatchCenter.locations.containsKey(produceName)){
                LocationEntity locationEntity = DispatchCenter.locations.get(produceName);
                playerEntity.location.toLocations.put(produceName,locationEntity);
                continue;
            }
            executeProduceHelp(playerEntity,produceName);
        }
    }

    public void executeProduceHelp(PlayerEntity playerEntity,String produceName){
        Map<String, Integer> furniture1 = playerEntity.location.furniture;
        Map<String, Integer> artefacts1 = playerEntity.location.artefacts;
        Map<String, Integer> characters1 = playerEntity.location.characters;
        for(Map.Entry<String,LocationEntity> location:DispatchCenter.locations.entrySet()){
            Map<String, Integer> artefacts = location.getValue().artefacts;
            Map<String, Integer> furniture = location.getValue().furniture;
            Map<String, Integer> characters = location.getValue().characters;
            if(artefacts.containsKey(produceName)){
                artefacts.remove(produceName);
                artefacts1.put(produceName,1);
            }
            if(furniture.containsKey(produceName)){
                furniture.remove(produceName);
                furniture1.put(produceName,1);
            }
            if(characters.containsKey(produceName)){
                characters.remove(produceName);
                characters1.put(produceName,1);
            }

        }
        Map<String, Integer> inventory = playerEntity.getInventory();
        if(inventory.containsKey(produceName)){
            inventory.remove(produceName);
            artefacts1.put(produceName,1);
        }
    }


    public boolean executeCheckProduceAvailable(GameAction action,PlayerEntity playerEntity){
        Map<String, Integer> produced = action.produced;
        for(Map.Entry<String,Integer> producedEntity:produced.entrySet()){
            String produceName = producedEntity.getKey();
            if(produceName.equals("health") || DispatchCenter.locations.containsKey(produceName)){
                continue;
            }
            boolean flag=false;
            for(Map.Entry<String,LocationEntity> location:DispatchCenter.locations.entrySet()){
                Map<String, Integer> artefacts1 = location.getValue().artefacts;
                Map<String, Integer> furniture1 = location.getValue().furniture;
                Map<String, Integer> characters1 = location.getValue().characters;
                if(artefacts1.containsKey(produceName) || furniture1.containsKey(produceName) || characters1.containsKey(produceName)){
                    flag=true;
                    break;
                }
            }

            Map<String, Integer> inventory = playerEntity.getInventory();
            if(inventory.containsKey(produceName)){
                flag=true;
            }
            if(!flag){
                return false;
            }
        }
        return true;
    }

    public String lookCommand(String playerName){
        StringBuilder response=new StringBuilder();
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        LocationEntity location = playerEntity.location;
        Map<String, Integer> artefacts = location.artefacts;
        Map<String, Integer> characters = location.characters;
        response.append(location.getDescription());
        response.append("\n");
        for(Map.Entry<String,Integer> character:characters.entrySet()){
            String key = character.getKey();
            CharacterEntity characterEntity = DispatchCenter.characters.get(key);
            response.append(characterEntity.getName())
                    .append(" : ")
                    .append(characterEntity.getDescription());
            response.append("\n");
        }
        for(Map.Entry<String,Integer> artefact:artefacts.entrySet()){
            String key = artefact.getKey();
            ArtefactEntity artefactEntity = DispatchCenter.artefacts.get(key);
            response.append(artefactEntity.getName())
                    .append(" : ")
                    .append(artefactEntity.getDescription());
            response.append("\n");
        }
        lookCommandHelp(playerName,response);
        return response.toString();
    }

    public void lookCommandHelp(String playerName,StringBuilder response){
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        LocationEntity location = playerEntity.location;
        Map<String, LocationEntity> toLocations = location.toLocations;
        Map<String, Integer> furnitures = location.furniture;
        for(Map.Entry<String,Integer> furniture:furnitures.entrySet()){
            String key = furniture.getKey();
            FurnitureEntity furnitureEntity = DispatchCenter.furniture.get(key);
            response.append(furnitureEntity.getName())
                    .append(" : ")
                    .append(furnitureEntity.getDescription());
            response.append("\n");
        }
        response.append("You can access from here:\n");
        for(Map.Entry<String,LocationEntity> toLocation:toLocations.entrySet()){
            response.append(toLocation.getKey());
            response.append("\n");
        }
        response.append("The other players:\n");
        for(Map.Entry<String,PlayerEntity> player:DispatchCenter.players.entrySet()) {
            if (player.getKey() != playerEntity.getName()) {
                response.append(player.getKey()).append(":");
                response.append(player.getValue().location.getName());
                response.append("\n");
            }
        }
    }

    public String getCommand(String playerName,String itemName){
        StringBuilder response=new StringBuilder();
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        Map<String, Integer> artefacts = playerEntity.location.artefacts;
        Map<String, Integer> inventory = playerEntity.getInventory();
        for(Map.Entry<String,Integer> artefact:artefacts.entrySet()){
            if(artefact.getKey().equals(itemName)){
                artefacts.remove(itemName);
                inventory.put(itemName,1);
                return response.append("You picked up a ").append(itemName).append("\n").toString();
            }
        }
        return response.append("Cannot find this item in current location").append("\n").toString();
    }

    public String dropCommand(String playerName,String itemName){
        StringBuilder response=new StringBuilder();
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        Map<String, Integer> inventory = playerEntity.getInventory();
        Map<String, Integer> artefacts = playerEntity.location.artefacts;
        for(Map.Entry<String,Integer> artefact:inventory.entrySet()){
            if(artefact.getKey().equals(itemName)){
                inventory.remove(itemName);
                artefacts.put(itemName,1);
                return response.append("You dropped a ").append(itemName).append("\n").toString();
            }
        }
        return response.append("Cannot find this item in your inventory").append("\n").toString();
    }
    public String gotoCommand(String playerName,String placeName){
        StringBuilder response=new StringBuilder();
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        LocationEntity location = playerEntity.location;
        Map<String, LocationEntity> toLocations = location.toLocations;
        boolean flag=false;
        for(Map.Entry<String,LocationEntity> toLocation:toLocations.entrySet()){
            if(toLocation.getKey().equals(placeName)){
                flag=true;
                playerEntity.location=DispatchCenter.locations.get(placeName);
            }
        }

        if(flag){
            return lookCommand(playerName);
        }
        return response.append("Cannot find this path").append("\n").toString();
    }
    public String inventoryCommand(String playerName){
        StringBuilder response=new StringBuilder();
        PlayerEntity playerEntity = DispatchCenter.players.get(playerName);
        Map<String, Integer> inventory = playerEntity.getInventory();
        response.append("You can find the following items in your inventory:\n");
        for(Map.Entry<String,Integer> artefact:inventory.entrySet()){
            ArtefactEntity artefactEntity = DispatchCenter.artefacts.get(artefact.getKey());
            response.append(artefactEntity.getDescription()).append("\n");

        }
        return response.toString();
    }

}
