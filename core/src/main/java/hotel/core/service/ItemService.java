package hotel.core.service;

import hotel.core.transformer.ItemDtoTransformer;
import hotel.db.entity.Item;
import hotel.db.repository.ItemRepository;
import hotel.model.ItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // FIXME make configurable (of course)
    private static final BigDecimal SURGE_PRICING_PERCENT = BigDecimal.TEN;

    @Value("${surge.pricing.threshold.per.hour:10}")
    private Long surgePricingThresholdPerHour;

    private final ItemRepository itemRepository;

    private final ItemDtoTransformer itemDtoTransformer;

    public ItemService(final ItemRepository itemRepository, final ItemDtoTransformer itemDtoTransformer) {
        this.itemRepository = itemRepository;
        this.itemDtoTransformer = itemDtoTransformer;
    }

    public List<ItemDto> findCurrentInventory() {
        List<Item> items = itemRepository.findAll();
        if (isEligibleForSurgePricing()) {
            addSurgePricing(items);
        }

        return items.stream()
                .map(itemDtoTransformer::generate)
                .collect(Collectors.toList());
    }

    public boolean isEligibleForSurgePricing() {
        if (countByCreatedAfter(new Date()).compareTo(surgePricingThresholdPerHour) > 0) {
            return true;
        }
        return false;
    }

    public Long countByCreatedAfter(final Date date) {
        return itemRepository.countByCreatedAfter(date);
    }

    public ItemDto createPool(final ItemDto itemDto) {
        Item item = itemRepository.saveAndFlush(itemDtoTransformer.generate(itemDto));
        return itemDtoTransformer.generate(item);
    }

    // FIXME put into own class SurgePricingService with tests (of course)
    public void addSurgePricing(final List<Item> items) {
        items.stream()
                .forEach(i -> i.setAmount(i.getAmount()
                                                  .multiply(SURGE_PRICING_PERCENT)));
    }
}
