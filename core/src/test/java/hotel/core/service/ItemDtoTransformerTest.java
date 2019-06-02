package hotel.core.service;

import hotel.core.transformer.ItemDtoTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ItemDtoTransformerTest {

    private ItemDtoTransformer itemDtoTransformerMock;

    @Before
    public void setUp() {
        itemDtoTransformerMock = mock(ItemDtoTransformer.class);
    }

    @Test
    public void generate_toDto() {
        // TODO
    }

    @Test
    public void generate_toEntity() {
        // TODO
    }
}
