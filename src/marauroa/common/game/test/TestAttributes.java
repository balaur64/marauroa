package marauroa.common.game.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import marauroa.common.game.Attributes;
import marauroa.common.game.RPClass;
import marauroa.common.net.InputSerializer;
import marauroa.common.net.OutputSerializer;

import org.junit.Test;

/**
 * Test case for attributes.
 *
 * @author miguel
 *
 */
public class TestAttributes {

	/**
	 * Test if methods put, has and get of attributes work as expected.
	 * It add and attribute, then assert it is present and finally compare the values.
	 * It also assert that a non added attribute doesn't exists.
	 */
	@Test
	public void testPutHasGet() {
		Attributes attr=new Attributes(null);

		attr.put("a",1);
		attr.put("b","2");
		attr.put("c",3.0);

		assertTrue(attr.has("a"));
		assertTrue(attr.has("b"));
		assertTrue(attr.has("c"));
		assertFalse(attr.has("d"));

		assertEquals(1,attr.getInt("a"));
		assertEquals("2",attr.get("b"));
		assertEquals(3.0,attr.getDouble("c"));
	}

	/**
	 * Test if an attribute is removed when it is removed.
	 * assert that the attribute is not longer there.
	 *
	 */
	@Test
	public void testRemove() {
		Attributes attr=new Attributes(null);

		attr.put("a",1);

		assertTrue(attr.has("a"));
		assertFalse(attr.has("b"));

		assertEquals("1", attr.remove("a"));

		assertFalse(attr.has("a"));
		assertEquals(null, attr.remove("a"));
	}

	/**
	 * Test the serialization process of an attribute.
	 * It serialize the attribute and then deserialize it and check they are the same.
	 *
	 * @throws IOException if there is a problem serializing the data.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		Attributes attr=new Attributes(RPClass.getBaseRPObjectDefault());

		attr.put("a",1);
		attr.put("b","2");
		attr.put("c",3.0);
		attr.put("e","a short string");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputSerializer os = new OutputSerializer(out);

		os.write(attr);

		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		InputSerializer is = new InputSerializer(in);

		Attributes result=(Attributes) is.readObject(new Attributes(null));

		assertEquals(attr, result);
	}

	/**
	 * Assert that an exception is thrown when a long string is added on a string that can host up to 128 characters.
	 * @throws IOException the expected exception
	 * @throws ClassNotFoundException
	 */
	@Test(expected=IOException.class)
	public void testSerializationException() throws IOException, ClassNotFoundException {
		Attributes attr=new Attributes(RPClass.getBaseRPObjectDefault());

		attr.put("a",1);
		attr.put("b","2");
		attr.put("c",3.0);
		attr.put("d","a long string that I would hardly imagine how to add it because no language procesor would be able to handle a soooo long string without having problems with...");
		attr.put("e","a short string");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputSerializer os = new OutputSerializer(out);

		os.write(attr);
	}
}