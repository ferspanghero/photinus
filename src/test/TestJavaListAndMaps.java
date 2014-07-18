package test;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestJavaListAndMaps {

	private ArrayList<String> list;
	private HashMap<String,String> map;

	@Before
	public void setUp() throws Exception {
		this.list = new ArrayList<String>();
		this.map = new HashMap<String,String>();
	}

	@Test
	public void testList() {
		this.list.add(null);
		Assert.assertNull(list.get(0));
		List<String> listInterface = this.list;
		listInterface.add(null);
		Assert.assertNull(listInterface.get(0));
	}
	
	@Test
	public void testMap(){
		String result = this.map.get(null);
		Assert.assertNull(result);
	}

}
