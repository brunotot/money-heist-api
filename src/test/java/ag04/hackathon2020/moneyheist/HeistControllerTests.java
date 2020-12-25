package ag04.hackathon2020.moneyheist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HeistControllerTests {

	@Autowired
	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void createHeist_addValidHeistObject_shouldReturnCreatedStatusWithLocationHeader() throws Exception {
		
		// HeistDto
		/**********************************************/
		String name = "Test heist 2";
		String location = "Croatia";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date startTime = sdf.parse("2020-12-27T22:00:00.000Z");
		Date endTime = sdf.parse("2020-12-28T18:00:00.000Z");
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "**", 3)
		);
		/**********************************************/
		
		HeistDto heistDto = new HeistDto(name, location, startTime, endTime, heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(heistDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/heist").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isCreated()).andReturn();		
		String locationExpected = "/heist/2";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Location");
		Assert.assertEquals(locationExpected, locationActual);
	}

	@Test
	public void createHeist_addHeistWithNameThatAlreadyExists_shouldReturnBadRequest() throws Exception {
		
		// HeistDto
		/**********************************************/
		String name = "Test heist";
		String location = "Croatia";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date startTime = sdf.parse("2020-12-27T22:00:00.000Z");
		Date endTime = sdf.parse("2020-12-28T18:00:00.000Z");
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "**", 3)
		);
		/**********************************************/
		
		HeistDto heistDto = new HeistDto(name, location, startTime, endTime, heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(heistDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/heist").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist already exists";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void createHeist_addHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {
		
		// HeistDto
		/**********************************************/
		String name = "Test heist";
		String location = "Croatia";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date startTime = sdf.parse("2020-12-27T22:00:00.000Z");
		Date endTime = sdf.parse("2020-12-28T18:00:00.000Z");
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "*****", 3)
		);
		/**********************************************/
		
		HeistDto heistDto = new HeistDto(name, location, startTime, endTime, heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(heistDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/heist").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
	@Test
	public void createHeist_addHeistWithInvalidDates_shouldReturnBadRequest() throws Exception {
		
		// HeistDto
		/**********************************************/
		String name = "Test heist 2";
		String location = "Croatia";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Date startTime = sdf.parse("2019-12-27T22:00:00.000Z");
		Date endTime = sdf.parse("2020-12-28T18:00:00.000Z");
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("combat", "*****", 1),
			new HeistSkillDto("combat", "*****", 3)
		);
		/**********************************************/
		
		HeistDto heistDto = new HeistDto(name, location, startTime, endTime, heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(heistDto);
		MvcResult mvcResult = this.mvc.perform(
				post("/heist").contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Invalid dates";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}

	@Test
	public void updateHeist_updateHeistWithValidData_shouldReturnNoContentStatusWithContentLocationHeader() throws Exception {
		
		Long heistId = 1L;
		
		String path = "/heist/" + heistId + "/skills";
		// HeistSkillArrayDto
		/**********************************************/
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "**", 3)
		);
		/**********************************************/
		
		HeistSkillArrayDto hsad = new HeistSkillArrayDto(heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(hsad);
		MvcResult mvcResult = this.mvc.perform(
				patch(path).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isNoContent()).andReturn();		
		String locationExpected = "/heist/1/skills";
		String locationActual = (String) mvcResult.getResponse().getHeaderValue("Content-Location");
		Assert.assertEquals(locationExpected, locationActual);

	}
	
	@Test
	public void updateHeist_updateNonExistingHeist_shouldReturnNotFound() throws Exception {
		
		Long heistId = 99L;
		
		String path = "/heist/" + heistId + "/skills";
		// HeistSkillArrayDto
		/**********************************************/
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "**", 3)
		);
		/**********************************************/
		
		HeistSkillArrayDto hsad = new HeistSkillArrayDto(heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(hsad);
		MvcResult mvcResult = this.mvc.perform(
				patch(path).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isNotFound()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Heist not found";
		Assert.assertTrue(response.contains(problemTitleExpected));

	}

	@Test
	public void updateHeist_updateHeistWithDuplicateSkills_shouldReturnBadRequest() throws Exception {

		Long heistId = 1L;
		
		String path = "/heist/" + heistId + "/skills";
		// HeistSkillArrayDto
		/**********************************************/
		List<HeistSkillDto> heistSkillDtos = List.of(
			new HeistSkillDto("driving", "****", 1),
			new HeistSkillDto("driving", "****", 3)
		);
		/**********************************************/
		
		HeistSkillArrayDto hsad = new HeistSkillArrayDto(heistSkillDtos);
		String memberDtoString = mapper.writeValueAsString(hsad);
		MvcResult mvcResult = this.mvc.perform(
				patch(path).contentType(MediaType.APPLICATION_JSON_VALUE).content(memberDtoString))
				.andExpect(status().isBadRequest()).andReturn();		
		String response = mvcResult.getResponse().getContentAsString();
		String problemTitleExpected = "Duplicate heist skills";
		Assert.assertTrue(response.contains(problemTitleExpected));
	}
	
}