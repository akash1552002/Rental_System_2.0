////package com.indifarm.machineryrental.controller;
////
////import com.indifarm.machineryrental.model.Farmer;
////import com.indifarm.machineryrental.repository.FarmerRepository;
////import com.indifarm.machineryrental.service.BookingService;
////import org.springframework.format.annotation.DateTimeFormat;
////import org.springframework.stereotype.Controller;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////
////import java.security.Principal;
////import java.time.LocalDate;
////
////@Controller
////public class BookingController {
////
////    private final BookingService bookingService;
////    private final FarmerRepository farmerRepository;
////
////    public BookingController(BookingService bookingService, FarmerRepository farmerRepository) {
////        this.bookingService = bookingService;
////        this.farmerRepository = farmerRepository;
////    }
////
////    @PostMapping("/book")
////    public String createBooking(@RequestParam("machineId") Long machineId,
////                                @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
////                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
////                                Principal principal) {
////
////        // Get the logged-in farmer
////        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
////                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
////
////        bookingService.createBooking(farmer.getId(), machineId, startDate, endDate);
////
////        return "redirect:/farmer/dashboard?bookingSuccess";
////    }
////
////    @PostMapping("/booking/approve")
////    public String approveBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
////        bookingService.approveBooking(bookingId);
////        // Redirect back to the correct dashboard
////        String role = getRole(principal);
////        return "redirect:" + getDashboardUrl(role);
////    }
////
////    @PostMapping("/booking/reject")
////    public String rejectBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
////        bookingService.rejectBooking(bookingId);
////        String role = getRole(principal);
////        return "redirect:" + getDashboardUrl(role);
////    }
////
////    // Helper to get role
////    private String getRole(Principal principal) {
////        // In a real app, you'd get this from the Authentication object
////        if (principal.getName().contains("owner")) return "OWNER";
////        if (principal.getName().contains("admin")) return "ADMIN";
////        return "FARMER";
////    }
////
////    // Helper to get dashboard URL
////    private String getDashboardUrl(String role) {
////        if ("OWNER".equals(role)) return "/owner/dashboard";
////        if ("ADMIN".equals(role)) return "/admin/dashboard";
////        return "/farmer/dashboard";
////    }
////}
//
//package com.indifarm.machineryrental.controller;
//
//import com.indifarm.machineryrental.model.Farmer;
//import com.indifarm.machineryrental.repository.FarmerRepository;
//import com.indifarm.machineryrental.service.BookingService;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//import java.time.LocalDate;
//import java.util.Collection;
//
//@Controller
//public class BookingController {
//
//    private final BookingService bookingService;
//    private final FarmerRepository farmerRepository;
//
//    public BookingController(BookingService bookingService, FarmerRepository farmerRepository) {
//        this.bookingService = bookingService;
//        this.farmerRepository = farmerRepository;
//    }
//
//    @PostMapping("/book")
//    public String createBooking(@RequestParam("machineId") Long machineId,
//                                @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//                                Principal principal) {
//
//        // Get the logged-in farmer
//        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
//
//        bookingService.createBooking(farmer.getId(), machineId, startDate, endDate);
//
//        return "redirect:/farmer/dashboard?bookingSuccess";
//    }
//
//    @PostMapping("/booking/approve")
//    public String approveBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
//        bookingService.approveBooking(bookingId);
//        // Redirect back to the correct dashboard
//        String role = getRole(principal);
//        return "redirect:" + getDashboardUrl(role);
//    }
//
//    @PostMapping("/booking/reject")
//    public String rejectBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
//        bookingService.rejectBooking(bookingId);
//        String role = getRole(principal);
//        return "redirect:" + getDashboardUrl(role);
//    }
//
//    // Helper to get role
//    private String getRole(Principal principal) {
//        if (principal instanceof Authentication) {
//            Authentication authentication = (Authentication) principal;
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                return "ADMIN";
//            }
//            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"))) {
//                return "OWNER";
//            }
//        }
//        return "FARMER"; // Default
//    }
//
//    // Helper to get dashboard URL
//    private String getDashboardUrl(String role) {
//        if ("OWNER".equals(role)) return "/owner/dashboard";
//        if ("ADMIN".equals(role)) return "/admin/dashboard";
//        return "/farmer/dashboard";
//    }
//}

package com.indifarm.machineryrental.controller;

import com.indifarm.machineryrental.model.Farmer;
import com.indifarm.machineryrental.model.Machine;
import com.indifarm.machineryrental.repository.FarmerRepository;
import com.indifarm.machineryrental.repository.MachineRepository; // Import needed
import com.indifarm.machineryrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired; // Import needed
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import needed
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Import needed
import java.util.Collection;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FarmerRepository farmerRepository;
    @Autowired
    private MachineRepository machineRepository; // Inject MachineRepository

    public BookingController(BookingService bookingService, FarmerRepository farmerRepository) {
        this.bookingService = bookingService;
        this.farmerRepository = farmerRepository;
    }

    // STEP 1: Show Payment Page (Calculate but don't save yet)
    @PostMapping("/booking/initiate")
    public String initiateBooking(@RequestParam("machineId") Long machineId,
                                  @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                  Model model,
                                  Principal principal) {

        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        // Calculate Days
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Calculate Amounts (Using Service Logic)
        double basePrice = machine.getRentalPricePerDay() * days;
        double subsidyRate = bookingService.getSubsidyRate(farmer, machine);
        double subsidyAmount = basePrice * subsidyRate;
        double finalAmount = basePrice - subsidyAmount;

        // Pass data to Payment Page
        model.addAttribute("machine", machine);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("days", days);
        model.addAttribute("totalAmount", basePrice);
        model.addAttribute("subsidyAmount", subsidyAmount);
        model.addAttribute("finalAmount", finalAmount);

        return "payment-page";
    }

    // STEP 2: Final Confirmation (Save to DB)
    @PostMapping("/booking/confirm")
    public String confirmBooking(@RequestParam("machineId") Long machineId,
                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                 @RequestParam("paymentMode") String paymentMode, // Receive Payment Mode
                                 Principal principal) {

        Farmer farmer = farmerRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));

        // Save the booking (You might want to save the paymentMode too in future, but for now this works)
        bookingService.createBooking(farmer.getId(), machineId, startDate, endDate);

        return "redirect:/farmer/my-bookings?success=true";
    }

    // ... (Keep existing approve/reject/getRole methods below) ...
    @PostMapping("/booking/approve")
    public String approveBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
        bookingService.approveBooking(bookingId);
        String role = getRole(principal);
        return "redirect:" + getDashboardUrl(role);
    }

    @PostMapping("/booking/reject")
    public String rejectBooking(@RequestParam("bookingId") Long bookingId, Principal principal) {
        bookingService.rejectBooking(bookingId);
        String role = getRole(principal);
        return "redirect:" + getDashboardUrl(role);
    }

    private String getRole(Principal principal) {
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "ADMIN";
            }
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"))) {
                return "OWNER";
            }
        }
        return "FARMER";
    }

    private String getDashboardUrl(String role) {
        if ("OWNER".equals(role)) return "/owner/dashboard";
        if ("ADMIN".equals(role)) return "/admin/dashboard";
        return "/farmer/dashboard";
    }
}