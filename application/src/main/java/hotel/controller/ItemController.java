package hotel.controller;

import hotel.Paths;
import hotel.core.exception.HotelRuntimeException;
import hotel.core.service.ItemService;
import hotel.model.ItemDto;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = Paths.ITEM,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Item endpoints",
     tags = "Item")
public class ItemController extends AbstractRestController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ItemDto> createItem(
            @Valid
            @RequestBody
            final ItemDto itemDto) {
        validateItemDto(itemDto);
        ItemDto savedItemDto = itemService.createItem(itemDto);
        return ResponseEntity.ok(savedItemDto);
    }

    private void validateItemDto(final ItemDto itemDto) {
        if (itemDto.getName()
                .isEmpty()) {
            throw new HotelRuntimeException(HttpStatus.BAD_REQUEST, "Name must not be empty");
        }
        if (BigDecimal.ZERO.compareTo(itemDto.getAmount()) > 0) {
            throw new HotelRuntimeException(HttpStatus.BAD_REQUEST, "Amount must be positive.");
        }
    }
}
