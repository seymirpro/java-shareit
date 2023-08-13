package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ItemRequestCreateUpdateDtoTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    public void testItemRequestCreateUpdateDtoWithValidDescriptionNoViolations() {
        ItemRequestCreateUpdateDto dto = ItemRequestCreateUpdateDto.builder()
                .description("Valid description")
                .build();

        Set<ConstraintViolation<ItemRequestCreateUpdateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testItemRequestCreateUpdateDtoWithBlankDescriptionViolationNotBlank() {
        ItemRequestCreateUpdateDto dto = ItemRequestCreateUpdateDto.builder()
                .description("")
                .build();

        Set<ConstraintViolation<ItemRequestCreateUpdateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
        assertFalse(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                        && violation.getMessage().equals("must not be blank")));
    }

    @Test
    public void testItemRequestCreateUpdateDtoWithNullDescriptionViolationNotNull() {
        ItemRequestCreateUpdateDto dto = ItemRequestCreateUpdateDto.builder()
                .description(null)
                .build();

        Set<ConstraintViolation<ItemRequestCreateUpdateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
        assertFalse(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                        && violation.getMessage().equals("must not be null")));
    }
}