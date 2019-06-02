package hotel.core.service;

import hotel.core.transformer.ItemDtoTransformer;
import hotel.db.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    private static final Long SURGE_THRESHOLD = new Long(10);

    private ItemRepository itemRepositoryMock;

    private ItemDtoTransformer itemDtoTransformerMock;

    private ItemService itemService;

    @Before
    public void setUp() {
        itemRepositoryMock = mock(ItemRepository.class);
        itemDtoTransformerMock = mock(ItemDtoTransformer.class);

        itemService = new ItemService(itemRepositoryMock, itemDtoTransformerMock);

        ReflectionTestUtils.setField(this.itemService, "surgePricingThresholdPerHour", SURGE_THRESHOLD);
    }

    @Test
    public void isEligibleForSurgePricing_1_less_false() {
        when(itemRepositoryMock.countByCreatedAfter(any(Date.class))).thenReturn(SURGE_THRESHOLD - 1l);
        assertFalse(itemService.isEligibleForSurgePricing());
    }

    @Test
    public void isEligibleForSurgePricing_exact_false() {
        when(itemRepositoryMock.countByCreatedAfter(any(Date.class))).thenReturn(SURGE_THRESHOLD);
        assertFalse(itemService.isEligibleForSurgePricing());
    }

    @Test
    public void isEligibleForSurgePricing_1_plus_true() {
        when(itemRepositoryMock.countByCreatedAfter(any(Date.class))).thenReturn(SURGE_THRESHOLD + 1l);
        assertTrue(itemService.isEligibleForSurgePricing());
    }

    @Test
    public void createPool() {
        // TODO
    }

    @Test
    public void findCurrentInventory() {
        // TODO
    }
}
