package hotel.controller;

import hotel.Paths;
import hotel.core.service.ItemService;
import hotel.model.ItemDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ItemDto> createPool(
            @RequestBody
            final ItemDto itemDto) {
        ItemDto savedItemDto = itemService.createPool(itemDto);
        return ResponseEntity.ok(savedItemDto);
    }
}
