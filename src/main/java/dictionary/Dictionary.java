package dictionary;

import java.util.Map.Entry;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Dictionary {

	private BidiMap<Integer, String> dictionary = new DualHashBidiMap<Integer,String>();
	
	public BidiMap<Integer, String> getInstance() {
		return dictionary;
	}
	
	public String getElementByKey(Integer key) {
		return dictionary.get(key);
	}
	
	public void printDictionary() {
		System.out.println(this.toString());
	}
	
	
	public Integer getKeyByValue(String value) {
		return dictionary.inverseBidiMap().get(value);
				
	}
	
	public void putElement(Integer key, String value) {
		dictionary.put(key, value);
	}

	public boolean containsValue(String value) {
		return dictionary.inverseBidiMap().containsKey(value);
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(Entry<Integer, String> entry : dictionary.entrySet()) {
            builder.append("<")
                   .append(entry.getKey())
                   .append(",")
                   .append(entry.getValue())
                   .append(">\n");
        }

        return builder.toString();
    }
}
