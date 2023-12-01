package com.talentstream.service;

import java.util.List;
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
    	 JobRecruiter recruiter = recruiterRepository.findById(recruiterId)
                 .orElseThrow(() -> new CustomException("Recruiter with ID " + recruiterId + " not found.", HttpStatus.INTERNAL_SERVER_ERROR));

         TeamMember teamMember1 = modelMapper.map(teamMemberDTO, TeamMember.class);
         teamMember1.setRecruiter(recruiter);

         TeamMember savedTeamMember = teamMemberRepository.save(teamMember1);

         return modelMapper.map(savedTeamMember, TeamMemberDTO.class);
    }
    
    public List<TeamMemberDTO> getTeammembersByRecruiter(long recruiterId) {
    	  List<TeamMember> teamMembers = teamMemberRepository.findByJobRecruiterId(recruiterId);
          return teamMembers.stream()
                  .map(teamMember -> modelMapper.map(teamMember, TeamMemberDTO.class))
                  .collect(Collectors.toList());
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
