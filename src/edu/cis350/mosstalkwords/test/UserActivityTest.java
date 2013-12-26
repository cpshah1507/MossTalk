package edu.cis350.mosstalkwords.test;

import edu.cis350.mosstalkwords.UserActivity;
import android.test.ActivityInstrumentationTestCase2;

public class UserActivityTest extends /*AndroidTestCase{//*/ActivityInstrumentationTestCase2<UserActivity> {

	public UserActivityTest() {
		super(edu.cis350.mosstalkwords.UserActivity.class);
		// TODO Auto-generated constructor stub
	}



	
	private UserActivity uA;
	

	protected void setUp() throws Exception {
		super.setUp();
		
		uA = this.getActivity();
	}
	
	public void testOnCreate()
	{
		assertNotNull(uA.getIntent());
	}

	public void testSetUserNameAnyString() {
		
		assertFalse(uA.setUserName("a q z Az 1"));
		assertTrue(uA.setUserName("asdf"));
	}
	
	public void testSetUserNameNull() {
		assertFalse(uA.setUserName(null));
	}
	
	public void testSetUserNameEmpty() {
		assertFalse(uA.setUserName(""));
	}

	public void testSetEmailValid() {
		assertTrue(uA.setEmail("email@mail.com"));
		
	}
	
	public void testSetEmailInvalidSpaces() {
		assertFalse(uA.setEmail("ema  il@mail.com"));
		
	}
	
	public void testSetEmailInvalidNoAtSym() {
		assertFalse(uA.setEmail("emailamail.com"));
	}
	
	public void testSetEmailInvalidNoPeriod() {
		assertFalse(uA.setEmail("email@mailcom"));
	}
	public void testSetAndGetScore() {
		
		uA.setScore(999);
		assertEquals(999, uA.getScore());
	}

	public void testLogin() {
		String name = "testName";
		String email = "test@email.edu";
		uA.login(name, email);
		assertEquals(uA.getUserName(),name);
		assertEquals(uA.getEmail(),email);
		assertEquals(uA.getScore(),0);
	}

	

	public void testIsLoggedIn() {
		assertTrue(uA.isLoggedIn());
		uA.login("test", "abc@123.com");
		assertTrue(uA.isLoggedIn());
	}
	
	public void testLogout() {
		uA.login("test", "abc@123.com");
		uA.setScore(1000);
		uA.logout();
		assertNull(uA.getUserName());
		assertNull(uA.getEmail());
		assertEquals(uA.getScore(),0);
	}

	public void testGetUserName() {
		String name = "name";
		uA.setUserName(name);
		assertEquals(name, uA.getUserName());
	}

	public void testGetEmail() {
		String email = "N@email.c";
		uA.setEmail(email);
		assert(email.equals(uA.getEmail()));
	}
}
