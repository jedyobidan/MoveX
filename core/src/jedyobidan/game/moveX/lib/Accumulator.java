package jedyobidan.game.moveX.lib;

import java.util.HashMap;

public class Accumulator<K> extends HashMap<K, Integer> {
	private static final long serialVersionUID = 1L;

	public void increment(K key){
		put(key, get(key) + 1);
	}
	
	public void decrement(K key){
		if(get(key) - 1 < 0){
			put(key, 0);
		} else {
			put(key, get(key) - 1);
		}
	}
	
	public int sum(){
		int sum = 0;
		for(K key: keySet()){
			sum += get(key);
		}
		return sum;
	}
	
	@Override
	public Integer get(Object key){
		if(containsKey(key)){
			return super.get(key);
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer put(K key, Integer value){
		if(value != 0){
			return super.put(key, value);
		} else {
			return super.remove(key);
		}
	}
}
