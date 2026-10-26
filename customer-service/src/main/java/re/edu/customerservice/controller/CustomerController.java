package re.edu.customerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import re.edu.customerservice.dto.CustomerLoginDto;
import re.edu.customerservice.dto.CustomerRequestDto;
import re.edu.customerservice.dto.CustomerResponseDto;
import re.edu.customerservice.service.CustomerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    // Đăng ký khách hàng
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> register(
            @RequestBody CustomerRequestDto dto) {
        CustomerResponseDto response = customerService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy thông tin khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // Đăng nhập
    @PutMapping("/login")
    public CustomerResponseDto login(@RequestBody CustomerLoginDto dto) {
        return customerService.login(dto);
    }
}