package qt;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import qt.attribute.Attribute;

public class AttributeTest {
	@Test
	public void testLife() {
		Attribute attribute = new Attribute(100, 10);
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			attribute.updateLife(random.nextInt());
			assertTrue(attribute.getLife() >= 0);
		}
	}
}
