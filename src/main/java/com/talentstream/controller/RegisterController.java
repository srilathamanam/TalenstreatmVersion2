package com.talentstream.controller;
import java.util.HashMap;

import java.util.List;
import com.talentstream.dto.LoginDTO;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.talentstream.dto.RegistrationDTO;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.AuthenticationResponse;
import com.talentstream.entity.Login;
import com.talentstream.entity.NewPasswordRequest;
import com.talentstream.entity.OtpVerificationRequest;
import com.talentstream.exception.CustomException;
import com.talentstream.response.ResponseHandler;
import com.talentstream.service.EmailService;
import com.talentstream.service.JwtUtil;
import com.talentstream.service.MyUserDetailsService;
import com.talentstream.service.OtpService;
import com.talentstream.service.RegisterService;
import com.talentstream.service.JobRecruiterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@CrossOrigin("*")
@RestController
@RequestMapping("/applicant")
public class RegisterController {
	@Autowired
    MyUserDetailsService myUserDetailsService;
	
	 @Autowired
	    private OtpService otpService;
	 
	 
		 private Map<String, Boolean> otpVerificationMap = new HashMap<>();
		 private static final Logger logger = LoggerFactory.getLogger(ApplicantProfileController.class);
		 @Autowired
			private AuthenticationManager authenticationManager;
		     @Autowired
			private JwtUtil jwtTokenUtil;
		    
	    @Autowired
	    private EmailService emailService;
	    
		@Autowired
	     RegisterService regsiterService;
		@Autowired
		JobRecruiterService recruiterService;	 
		@Autowired
		private PasswordEncoder passwordEncoder;

	    @Autowired
	    public RegisterController(RegisterService regsiterService)
	    {
	        this.regsiterService = regsiterService;	     

	    }

 	    @PostMapping("/saveApplicant")
	    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
 	    	try {
 	            return regsiterService.saveapplicant(registrationDTO);
 	        } catch (CustomException e) {
 	            return ResponseEntity.badRequest().body(e.getMessage());
 	        } catch (Exception e) {
 	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering applicant");
 	        }
	    }

     	    @PostMapping("/applicantLogin")
	    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) throws Exception {
     	    	try {
     	            Applicant applicant =regsiterService.login(loginDTO.getEmail(), loginDTO.getPassword());
     	            if (applicant != null) {
     	                return createAuthenticationToken(loginDTO, applicant);
     	            } else {
     	                return ResponseEntity.badRequest().body("Login failed");
     	            }
     	        } catch (BadCredentialsException e) {
     	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
     	        } catch (Exception e) {
     	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
     	        }
	    }

	    private ResponseEntity<Object> createAuthenticationToken(LoginDTO loginDTO,  Applicant applicant ) throws Exception {
	    	try {
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
	            );
	            UserDetails userDetails = myUserDetailsService.loadUserByUsername(applicant.getEmail());
	            final String jwt = jwtTokenUtil.generateToken(userDetails);
	            return ResponseHandler.generateResponse("Login successfully" + userDetails.getAuthorities(), HttpStatus.OK, new AuthenticationResponse(jwt), applicant.getEmail(), applicant.getName(), applicant.getId());
	        } catch (BadCredentialsException e) {
	            throw new CustomException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
	        }
	           
		}


	    @PostMapping("/applicantsendotp")
	    public ResponseEntity<String> sendOtp(@RequestBody Applicant  request) {
	    	String userEmail = request.getEmail();
	        try {
	            Applicant applicant = regsiterService.findByEmail(userEmail);
	            if (applicant == null) {
	                String otp = otpService.generateOtp(userEmail);
	                emailService.sendOtpEmail(userEmail, otp);
	                otpVerificationMap.put(userEmail, true);
	                return ResponseEntity.ok("OTP sent to your email.");
	            } else {
	                throw new CustomException("Email is already registered.", null);
	            }
	        } catch (CustomException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending OTP");
	        }
	    }
	    @PostMapping("/forgotpasswordsendotp")
	    public ResponseEntity<String> ForgotsendOtp(@RequestBody Applicant  request) {
	    	String userEmail = request.getEmail();
	        Applicant applicant = regsiterService.findByEmail(userEmail);
	        System.out.println(applicant);
	        if (applicant != null) {     
	            String otp = otpService.generateOtp(userEmail);
	         	            emailService.sendOtpEmail(userEmail, otp);
	 	            otpVerificationMap.put(userEmail, true);
	 	            System.out.println(otp);
	 	            return ResponseEntity.ok("OTP sent successfully");
	        }
	        else {
	        	 return ResponseEntity.badRequest().body("Email not found."); 
	        } 
	    }

	    @PostMapping("/applicantverify-otp")
	    public ResponseEntity<String> verifyOtp( @RequestBody  OtpVerificationRequest verificationRequest

	    ) 
	    {
	    	try {
	            String otp = verificationRequest.getOtp();
	            String email = verificationRequest.getEmail();
	            System.out.println(otp + email);

	            if (otpService.validateOtp(email, otp)) {
	                return ResponseEntity.ok("OTP verified successfully");
	            } else {
	                throw new CustomException("Incorrect OTP or Timeout.", HttpStatus.BAD_REQUEST);
	            }
	        } catch (CustomException e) {
	            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OTP");
	        }
	    }

	    @PostMapping("/applicantreset-password/{email}")
	    public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordRequest request,@PathVariable String email) {
	    	try {
	            String newpassword = request.getPassword();
	            String confirmedPassword = request.getConfirmedPassword();
	            	
	            if (email == null) {
	                  throw new CustomException("Email not found.", HttpStatus.BAD_REQUEST);
	            }
	           
	            Applicant applicant = regsiterService.findByEmail(email);
	                if (applicant == null) {
	                 throw new CustomException("User not found.", HttpStatus.BAD_REQUEST);
	            }
	            	
	            applicant.setPassword(passwordEncoder.encode(newpassword));
	           regsiterService.addApplicant(applicant);
	               return ResponseEntity.ok("Password reset was done successfully");
	        } catch (CustomException e) {
	        	
	            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password");
	        }
	    }

		@GetMapping("/viewApplicants")

	    public ResponseEntity<List<Applicant>> getAllApplicants() {

	        try {
	            List<Applicant> applicants = regsiterService.getAllApplicants();
	            return ResponseEntity.ok(applicants);
	        } catch (Exception e) {
	             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }

	    }
		@PostMapping("/applicantsignOut")
	    public ResponseEntity<Void> signOut(@AuthenticationPrincipal Applicant user) {
			 try {
		            SecurityContextHolder.clearContext();
		            return ResponseEntity.noContent().build();
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
		    }

		public void setOtpService(OtpService otpService2) {
			otpService=otpService2;
			
		}
}