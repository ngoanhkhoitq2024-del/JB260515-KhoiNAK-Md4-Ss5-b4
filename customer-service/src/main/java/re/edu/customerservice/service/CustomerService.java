package re.edu.customerservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.customerservice.dto.CustomerLoginDto;
import re.edu.customerservice.dto.CustomerRequestDto;
import re.edu.customerservice.dto.CustomerResponseDto;
import re.edu.customerservice.entity.Customer;
import re.edu.customerservice.exception.EmailAlreadyExistsException;
import re.edu.customerservice.exception.InvalidCredentialsException;
import re.edu.customerservice.exception.ResourceNotFoundException;
import re.edu.customerservice.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CustomerResponseDto register(CustomerRequestDto dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        Customer customer = new Customer();
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());

        // Mã hóa trước khi lưu database
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));

        Customer savedCustomer = customerRepository.save(customer);
        return toResponseDTO(savedCustomer);
    }

    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + id)
                );
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail()
        );
    }

    public CustomerResponseDto getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy khách hàng có id: " + id)
                );
        return toResponseDTO(customer);
    }

    public CustomerResponseDto login(CustomerLoginDto dto) {
        Customer customer = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "email or password incorrect")
                );
        // matches: kiểm tra 2 mk có khớp kh
        boolean passwordMatches = passwordEncoder.matches(
                dto.getPassword(),
                customer.getPassword()
        );
        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "email or password incorrect"
            );
        }
        return toResponseDTO(customer);
    }

    private CustomerResponseDto toResponseDTO(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail()
        );
    }
}