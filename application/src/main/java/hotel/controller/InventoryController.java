package hotel.controller;

import hotel.Paths;
import hotel.core.service.ItemService;
import hotel.model.ItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Paths.INVENTORY,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Inventory endpoints",
     tags = "Inventory")
public class InventoryController extends AbstractRestController {

    private ItemService itemService;

    public InventoryController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiOperation(value = "getInventory")
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    public ResponseEntity<List<ItemDto>> getCurrentInventory() {

        List<ItemDto> currentInventory = itemService.findCurrentInventory();

        return ResponseEntity.ok(currentInventory);
    }
}
