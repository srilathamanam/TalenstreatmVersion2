package com.talentstream.service;
 
import java.util.List;
import com.talentstream.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.talentstream.entity.Applicant;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.dto.RegistrationDTO;
import jakarta.persistence.EntityNotFoundException;
 
@Service
public class RegisterService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JobRecruiterRepository recruiterRepository;
 
	 @Autowired
	RegisterRepository applicantRepository;
    public RegisterService( RegisterRepository applicantRepository) {
	        this.applicantRepository = applicantRepository;
	    }
 
 
    public Applicant login(String email, String password) {
    	System.out.println("Login is Mached "+email);
    	try {
    	Applicant applicant = applicantRepository.findByEmail(email);
    	 if (applicant != null && passwordEncoder.matches(password, applicant.getPassword())) {
    	        return applicant;
    	    } else {
    	        return null;
    	    }
    	}
    	catch(Exception e)
    	{
    	System.out.println(e.getMessage());
    	return null;
    	} 
    }
    public boolean emailExists(String email) {
   	 return applicantRepository.existsByEmail(email);
   }
public Applicant findById(Long id) {
	try {
        return applicantRepository.findById(id);
    } catch (EntityNotFoundException e) {
        throw e;
    } catch (Exception e) {
        throw new CustomException("Error finding applicant by ID", HttpStatus.INTERNAL_SERVER_ERROR);
    }
   }
 
public List<Applicant> getAllApplicants() {
	 try {
         return applicantRepository.findAll();
     } catch (Exception e) {
         throw new CustomException("Error retrieving applicants", HttpStatus.INTERNAL_SERVER_ERROR);
     }
 
}
 
public void updatePassword(String userEmail, String newPassword) {
	try {
        Applicant applicant = applicantRepository.findByEmail(userEmail);
        if (applicant != null) {
            applicant.setPassword(passwordEncoder.encode(newPassword));
                       applicantRepository.save(applicant);
        } else {
            throw new EntityNotFoundException("Applicant not found for email: " + userEmail);
        }
    } catch (EntityNotFoundException e) {
        throw e;
    } catch (Exception e) {
        throw new CustomException("Error updating password", HttpStatus.INTERNAL_SERVER_ERROR);
    }
 
}
 
	public Applicant findByEmail(String userEmail) {
		try {
			System.out.println(userEmail);
            return applicantRepository.findByEmail(userEmail);
            
        } catch (Exception e) {
        	
            throw new CustomException("Error finding applicant by email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
 
	}
 
	
 
 
	public ResponseEntity<String> saveapplicant(RegistrationDTO registrationDTO) {
		try {
			
			  Applicant applicant = mapRegistrationDTOToApplicant(registrationDTO);
            if (applicantRepository.existsByEmail(applicant.getEmail()) || recruiterRepository.existsByEmail(applicant.getEmail())) {
                throw new CustomException("Email already registered",null);
            }
            if(applicantRepository.existsByMobilenumber(applicant.getMobilenumber())||recruiterRepository.existsByMobilenumber(applicant.getMobilenumber()))
            {
            	throw new CustomException("Mobile number already existed",null);
            }
            
            applicant.setPassword(passwordEncoder.encode(applicant.getPassword()));
            applicantRepository.save(applicant);
            return ResponseEntity.ok("Applicant registered successfully");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering applicant");
        }
	}
 
 
	public void addApplicant(Applicant applicant) {
		 try {
	            applicantRepository.save(applicant);
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	            throw new CustomException("Error adding applicant", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
	private Applicant mapRegistrationDTOToApplicant(RegistrationDTO registrationDTO) {
        Applicant applicant = new Applicant();
        applicant.setName(registrationDTO.getName());
        applicant.setEmail(registrationDTO.getEmail());
        applicant.setMobilenumber(registrationDTO.getMobilenumber());
        applicant.setPassword(registrationDTO.getPassword());       
        return applicant;
    }
	
	public String authenticateUser(Long id,String oldPassword, String newPassword) {
	       try {
	            Applicant opUser = applicantRepository.findById(id);
	            System.out.println(opUser.getPassword());
	            System.out.println(passwordEncoder.encode(oldPassword));	            
	            if (opUser != null) {
	            	if(passwordEncoder.matches(oldPassword, opUser.getPassword())) {
	            		if (passwordEncoder.matches(newPassword, opUser.getPassword())) {
	            			return "your new password should not be same as old password";
	            		}
	            		opUser.setPassword(passwordEncoder.encode(newPassword));
	                    applicantRepository.save(opUser);
	 
	                    return "Password updated and stored";
	            	}
	            	else {
	            		return "Your old password not matching with data base password";
	            	}
	            	            }
	            else {
	            	return "User not found with given id";
	            }
	        }
	               
	    	catch (Exception e) {	         
	            e.printStackTrace();	            
	           return "user not found with this given id";
	        }			
	    }	
	
	}
