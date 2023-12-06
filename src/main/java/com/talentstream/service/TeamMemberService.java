package com.talentstream.service;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.talentstream.dto.TeamMemberDTO;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.TeamMember;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.TeamMemberRepository;


@Service
public class TeamMemberService {
	private ModelMapper modelMapper;
    @Autowired
    private TeamMemberRepository teamMemberRepository; 
    @Autowired
    private JobRecruiterRepository recruiterRepository; 
    public TeamMemberDTO addTeamMemberToRecruiter(Long recruiterId, TeamMemberDTO teamMemberDTO) {
        System.out.println(teamMemberDTO.getName());

        JobRecruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new CustomException("Recruiter with ID " + recruiterId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR));

        TeamMember teamMember = new TeamMember();
        teamMember.setName(teamMemberDTO.getName());
        teamMember.setRole(teamMemberDTO.getRole());
        teamMember.setEmail(teamMemberDTO.getEmail());
       
        // Map other fields as needed

        teamMember.setRecruiter(recruiter);

        TeamMember savedTeamMember = teamMemberRepository.save(teamMember);

        TeamMemberDTO savedTeamMemberDTO = new TeamMemberDTO();
        savedTeamMemberDTO.setId(savedTeamMember.getId());
        savedTeamMemberDTO.setName(savedTeamMember.getName());
        savedTeamMemberDTO.setRole(savedTeamMember.getRole());
        savedTeamMemberDTO.setEmail(savedTeamMember.getEmail());
        
        // Map other fields as needed

        return savedTeamMemberDTO;
    }
    
    public List<TeamMemberDTO> getTeammembersByRecruiter(long recruiterId) {
    	  List<TeamMember> teamMembers = teamMemberRepository.findByJobRecruiterId(recruiterId);
          
    	  List<TeamMemberDTO> al = new ArrayList<>();
    	  for(TeamMember obj: teamMembers) {
    		  
    	  TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
          teamMemberDTO.setId(obj.getId());
          teamMemberDTO.setName(obj.getName());
          teamMemberDTO.setRole(obj.getRole());
          teamMemberDTO.setEmail(obj.getEmail());
          
          al.add(teamMemberDTO);
          
          
    	  }
    	  
    	  return al;
    }
    
    public void deleteTeamMemberById(Long teamMemberId) {
    	 if (teamMemberRepository.existsById(teamMemberId)) {
             teamMemberRepository.deleteById(teamMemberId);
         } else {
             throw new CustomException("Team Member with ID " + teamMemberId + " not found.",HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    public void resetTeamMemberPassword(Long teamMemberId, String newPassword) {
            TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new CustomException("Team Member with ID " + teamMemberId + " not found.",HttpStatus.INTERNAL_SERVER_ERROR));
        teamMember.setPassword(newPassword);
        teamMemberRepository.save(teamMember);

    }
}
