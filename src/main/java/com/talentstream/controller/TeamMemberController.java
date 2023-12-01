package com.talentstream.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.talentstream.exception.CustomException;
import com.talentstream.dto.TeamMemberDTO;
import com.talentstream.service.TeamMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@CrossOrigin("*")
@RequestMapping("/team")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
    @PostMapping("/{recruiterId}/team-members")
    public ResponseEntity<TeamMemberDTO> addTeamMemberToRecruiter(
        @PathVariable Long recruiterId,
        @RequestBody TeamMemberDTO teamMember
    ) {
    	 try {
    	        TeamMemberDTO savedTeamMember = teamMemberService.addTeamMemberToRecruiter(recruiterId, teamMember);
    	        return new ResponseEntity<>(savedTeamMember, HttpStatus.CREATED);
    	    } catch (CustomException e) {
    	        return ResponseEntity.notFound().build();
    	    } catch (Exception e) {
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	    }
    }

  
    
    @GetMapping("/teammembers/{recruiterId}")
    public ResponseEntity<List<TeamMemberDTO>> getTeammembersByRecruiter(@PathVariable("recruiterId") long recruiterId) {
    	try {
            List<TeamMemberDTO> teamMembers = teamMemberService.getTeammembersByRecruiter(recruiterId);
            return ResponseEntity.ok(teamMembers);
        } catch (CustomException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{teamMemberId}")
    public ResponseEntity<String> deleteTeamMember(@PathVariable Long teamMemberId) {
    	 try {
    	        teamMemberService.deleteTeamMemberById(teamMemberId);
    	        return ResponseEntity.ok("Team Member deleted successfully");
    	    } catch (CustomException e) {
    	        return ResponseEntity.notFound().build();
    	    } catch (Exception e) {
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	    }
    }
    
    @PutMapping("/{teamMemberId}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long teamMemberId, @RequestParam String newPassword) {
    	try {
            teamMemberService.resetTeamMemberPassword(teamMemberId, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (CustomException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
    
    


