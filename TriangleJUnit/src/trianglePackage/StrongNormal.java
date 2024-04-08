package trianglePackage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StrongNormal {

	@Test
	public void SN1() {
		assertEquals("Equilateral", Triangle.Triangle_Test(5, 5, 5));
	}
	
	@Test
	public void SN2() {
		assertEquals("Scalene", Triangle.Triangle_Test(3, 4, 5));
	}
	
	@Test
	public void SN3() {
		assertEquals("Isosceles", Triangle.Triangle_Test(2, 2, 3));
	}

	@Test
	public void SN4() {
		assertEquals("Not a triangle", Triangle.Triangle_Test(4, 1, 2));
	}

}
