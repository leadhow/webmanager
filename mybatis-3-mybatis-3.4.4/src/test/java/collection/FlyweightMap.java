package collection;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class FlyweightMap extends AbstractMap<String, String>{

	public static final String[][] DATA = {{"CHINESE", "BEIJING"}};
	
	private static class Entry implements Map.Entry<String, String>{
		
		int index;
		
		Entry(int index){
			this.index = index;
		}
		
		public boolean equals(Object o){
			return DATA[index][0].equals(o);
		}

		@Override
		public String getKey() {
			return DATA[index][0];
		}

		@Override
		public String getValue() {
			return DATA[index][1];
		}

		@Override
		public String setValue(String value) {
			throw new UnsupportedOperationException();
		}
		
		public int hashCode(){
			return DATA[index][0].hashCode();
		}
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		
		return null;
	}

}
