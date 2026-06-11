package api.tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.User_Endpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest {
	
	Faker faker;
	User userPayload;
	
	@BeforeClass
	public void setupData()
	{
		//faker class is used to generate random data for testing purposes. It provides various methods to 
		//generate realistic data such as names, addresses, phone numbers, etc.
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone().replaceAll("[^0-9]", ""));
	}
		
		@Test(priority = 1)
		public void testCreateUser()
		{
			Response response = User_Endpoints.createUser(userPayload);
			response.then().log().all();
			AssertJUnit.assertEquals(response.getStatusCode(), 200);
		}
		
		@Test(priority = 2)
		public void testgetUserByName()
		{
			Response response = User_Endpoints.readUser(this.userPayload.getUsername());
			response.then().log().all();
			AssertJUnit.assertEquals(response.getStatusCode(), 200);
		}
		
		@Test(priority = 3)
		public void testUpdateUserByName()
		{
			userPayload.setFirstName(faker.name().firstName());
			userPayload.setLastName(faker.name().lastName());
			userPayload.setEmail(faker.internet().safeEmailAddress());
			
			Response response = User_Endpoints.updateUser(this.userPayload.getUsername(), userPayload);
			response.then().log().body();
			AssertJUnit.assertEquals(response.getStatusCode(), 200);
			
			Response responseAfterUpdate = User_Endpoints.readUser(this.userPayload.getUsername());
			responseAfterUpdate.then().log().all();
			AssertJUnit.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		}
		
		@Test(priority = 4)
		public void testDeleteUserByName()
		{
			Response response = User_Endpoints.deleteUser(this.userPayload.getUsername());
			response.then().log().all();
			AssertJUnit.assertEquals(response.getStatusCode(), 200);
			
			Response responseAfterDelete = User_Endpoints.readUser(this.userPayload.getUsername());
			responseAfterDelete.then().log().all();
			AssertJUnit.assertEquals(responseAfterDelete.getStatusCode(), 404);
		}
		
		
	

}
