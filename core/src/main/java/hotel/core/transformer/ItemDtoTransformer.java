package hotel.core.transformer;

import hotel.db.entity.Item;
import hotel.model.ItemDto;
import org.springframework.stereotype.Component;

@Component
public class ItemDtoTransformer {

    public ItemDtoTransformer() {
    }

    public ItemDto generate(final Item item) {
        if (item == null) {
            return null;
        }

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setCreated(item.getCreated());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAmount(item.getAmount());

        return dto;
    }

    public Item generate(final ItemDto dto) {
        return new Item(dto.getName(), dto.getDescription(), dto.getAmount());
    }
}
