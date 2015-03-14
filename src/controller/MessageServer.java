
package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;


public class MessageServer implements Iterable<Message>{
 private Map<Integer, List<Message>> messages;
 
 private List<Message> selected;
 
 public MessageServer(){
     selected = new ArrayList<>();
     messages = new TreeMap<>();
     
     List<Message> list = new ArrayList<>();
     list.add(new Message("The cat is mising", "Have you seen it"));
     list.add(new Message("See you later?", "Are we still meeting in the park"));
     messages.put(0, list);
     
     list = new ArrayList<>();
     list.add(new Message("How about dinner later?", "Are you doing anything later?"));
     messages.put(1, list);
 }
    public void setSelectedServers(Set<Integer> servers){
        selected.clear();
        for(Integer id : servers){
            if(messages.containsKey(id)){
                selected.addAll(messages.get(id));
            }
        }
    }
    public int getMessageCount(){
        return selected.size();
    }

    @Override
    public Iterator<Message> iterator() {
       return new MessageIterator(selected);
    }
    class MessageIterator implements Iterator{

        private Iterator<Message> iterator;
        
        public MessageIterator(List<Message> messages){
            iterator = messages.iterator();
        }
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
           return iterator.next();
        }

        @Override
        public void remove() {
           iterator.remove();
        }
        
    }
}
