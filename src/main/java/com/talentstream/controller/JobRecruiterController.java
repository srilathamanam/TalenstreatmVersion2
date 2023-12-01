package com.talentstream.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.talentstream.service.JwtUtil;
import com.talentstream.service.MyUserDetailsService;
import com.talentstream.service.OtpService;
import com.talentstream.response.ResponseHandler;
import com.talentstream.dto.JobRecruiterDTO;
import com.talentstream.entity.AuthenticationResponse;
import com.talentstream.entity.JobRecruiter;
import com.talentstream.entity.RecruiterLogin;
import com.talentstream.entity.ResetPasswordRequest;
import com.talentstream.service.EmailService;
import com.talentstream.service.JobRecruiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@CrossOrigin("*")
@RequestMapping("/recuriters")
public class JobRecruiterController {
	@Autowired
    private OtpService otpService;
	 private Map<String, Boolean> otpVerificationMap = new HashMap<>();
	 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);

    @Autowired
    private EmailService emailService; // Your email service
	@Autowired
     JobRecruiterService recruiterService;
     @Autowired
	private AuthenticationManager authenticationManager;
     @Autowired
	private JwtUtil jwtTokenUtil;
     @Autowired
     MyUserDetailsService myUserDetailsService;
    @Autowired
    public JobRecruiterController(JobRecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }
    @PostMapping("/saverecruiters")
    public ResponseEntity<String> registerRecruiter(@RequestBody JobRecruiterDTO recruiterDTO) {
        JobRecruiter recruiter = convertToEntity(recruiterDTO);
        return recruiterService.saveRecruiter(recruiter);
    }


    @PostMapping("/recruiterLogin")
    public ResponseEntity<Object> login(@RequestBody RecruiterLogin loginRequest) throws Exception {
       JobRecruiter recruiter = recruiterService.login(loginRequest.getEmail(), loginRequest.getPassword());
        System.out.println(loginRequest.getEmail());
        System.out.println(recruiter.getEmail());

        if (recruiter!=null) {
        	return createAuthenticationToken(loginRequest,recruiter);
        } else {
            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registration-send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody ResetPasswordRequest request) {
        String userEmail = request.getEmail();
        JobRecruiter jobRecruiter = recruiterService.findByEmail(userEmail);
        if (jobRecruiter == null) {
            String otp = otpService.generateOtp(userEmail);
            emailService.sendOtpEmail(userEmail, otp);
            otpVerificationMap.put(userEmail, true); 
            return ResponseEntity.ok("OTP sent to your email.");
        }
        else {
        	 return ResponseEntity.badRequest().body("Email is already  registered.");
        }
    }

    private ResponseEntity<Object> createAuthenticationToken(RecruiterLogin login, JobRecruiter recruiter) throws Exception {
		    	try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
    	final UserDetails userDetails = myUserDetailsService.loadUserByUsername(recruiter.getEmail());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseHandler.generateResponse("Login successfully"+userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt),recruiter.getEmail(),recruiter.getCompanyname(),recruiter.getRecruiterId());
	}

    @GetMapping("/viewRecruiters")
    public ResponseEntity<List<JobRecruiterDTO>> getAllJobRecruiters() {
        try {
            List<JobRecruiterDTO> jobRecruiters = recruiterService.getAllJobRecruiters();
            return ResponseHandler.generateResponse1("List of Job Recruiters", HttpStatus.OK, jobRecruiters);
        } catch (Exception e) {
            return ResponseHandler.generateResponse1("Error retrieving job recruiters", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
	
	private JobRecruiter convertToEntity(JobRecruiterDTO recruiterDTO) {
        JobRecruiter recruiter = new JobRecruiter();
        recruiter.setRecruiterId(recruiterDTO.getRecruiterId());
        recruiter.setCompanyname(recruiterDTO.getCompanyname());
        recruiter.setMobilenumber(recruiterDTO.getMobilenumber());
        recruiter.setEmail(recruiterDTO.getEmail());
        recruiter.setPassword(recruiterDTO.getPassword());
        recruiter.setRoles(recruiterDTO.getRoles());        

        return recruiter;
    }
}