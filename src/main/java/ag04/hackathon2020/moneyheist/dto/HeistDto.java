package ag04.hackathon2020.moneyheist.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ag04.hackathon2020.moneyheist.entity.Heist;

public class HeistDto {

	private String name;
	
	private String location;
	
	private Date startTime;
	
	private Date endTime;
	
	public HeistDto() {
		super();
	}

	public HeistDto(String name, String location, Date startTime, Date endTime, List<HeistSkillDto> heistSkillDtos) {
		super();
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.heistSkillDtos = heistSkillDtos;
	}

	@JsonProperty("skills")
	private List<HeistSkillDto> heistSkillDtos;

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<HeistSkillDto> getHeistSkillDtos() {
		return heistSkillDtos;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setHeistSkillDtos(List<HeistSkillDto> heistSkillDtos) {
		this.heistSkillDtos = heistSkillDtos;
	}

	public static Heist toEntity(HeistDto dto) {
		Heist heist = new Heist();
		heist.setName(dto.getName());
		heist.setLocation(dto.getLocation());
		heist.setStartTime(dto.getStartTime());
		heist.setEndTime(dto.getEndTime());
		heist.setHeistSkills(dto.getHeistSkillDtos().stream().map(hsd -> HeistSkillDto.toEntity(hsd)).collect(Collectors.toList()));
		return heist;
	}

	public static HeistDto toDto(Heist entity) {
		HeistDto dto = new HeistDto();
		dto.setName(entity.getName());
		dto.setLocation(entity.getLocation());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		dto.setHeistSkillDtos(entity.getHeistSkills().stream().map(hs -> HeistSkillDto.toDto(hs)).collect(Collectors.toList()));
		return dto;
	}
	
}