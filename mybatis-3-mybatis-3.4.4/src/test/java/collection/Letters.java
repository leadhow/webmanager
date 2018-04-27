package collection;

import java.util.Iterator;

/**
 * 类功能描述:LettersGenerator通过产生一个Iterator还实现了Iterable.通过这种方式,他可以被用来测试MapData.map()方法,而这些方法都要用到Iterable
 * @author guoyangyang
 *
 */
public class Letters implements Generator<Pair<Integer, String>>, Iterable<Integer>{

	private int size = 9;
	
	private int number = 1;
	
	private char letter = 'A';
	
	@Override
	public Pair<Integer, String> next() {
		return new Pair<Integer, String>(number++, "" + letter++);
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>(){

			@Override
			public boolean hasNext() {
				return number < size;
			}

			@Override
			public Integer next() {
				return number++;
			}
			
			public void remove() { 
				throw new UnsupportedOperationException();
			}
			
		};
	}

}
