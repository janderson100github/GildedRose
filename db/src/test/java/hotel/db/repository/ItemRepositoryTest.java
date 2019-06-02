package hotel.db.repository;

import hotel.db.entity.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Environment.class})
@ComponentScan({"hotel.db"})
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void crudTest() {
        String name = "name";
        String desc = "desc";
        BigDecimal amount = BigDecimal.ONE;
        BigDecimal newAmount = BigDecimal.TEN;

        Item item = new Item(name, desc, amount);

        Item itemSaved = itemRepository.saveAndFlush(item);
        assertEquals(new Long(1l), itemSaved.getId());
        assertEquals(name, itemSaved.getName());
        assertEquals(amount, itemSaved.getAmount());

        itemSaved.setAmount(newAmount);
        itemSaved = itemRepository.saveAndFlush(itemSaved);
        assertEquals(newAmount, itemSaved.getAmount());

        itemRepository.delete(itemSaved);

        assertEquals(0, itemRepository.count());
    }

    @Test
    public void readAll() {
        List<Item> items = new ArrayList<>();
        items.add(generateItem("one"));
        items.add(generateItem("two"));
        items.add(generateItem("three"));
        items.add(generateItem("four"));

        for (Item item : items) {
            Item itemSaved = itemRepository.saveAndFlush(item);
        }

        List<Item> all = itemRepository.findAll();

        assertEquals(items.size(), all.size());
    }

    @Test
    public void countByCreatedAfter() {
        long timeMillis = (new Date()).getTime();
        for (int i = 0; i < 10; i++) {
            Date date = new Date(timeMillis - (i * TEN_MINUTES_IN_MILLIS));
            itemRepository.saveAndFlush(generateItem("i" + i, date));
        }

        List<Item> items = itemRepository.findAll();

        // -- After now --
        Date date = new Date(timeMillis);
        expectCountAfterDate(date, 0);

        // -- After 10m  ago --
        date = new Date(timeMillis - TEN_MINUTES_IN_MILLIS);
        expectCountAfterDate(date, 1);

        // -- After 25m  ago --
        date = new Date(timeMillis - (15 * 60 * 1000));
        expectCountAfterDate(date, 2);
    }

    private void expectCountAfterDate(Date date, long expectedCount) {
        Long actualCount = itemRepository.countByCreatedAfter(date);
        assertEquals(new Long(expectedCount), actualCount);
    }

    private Item generateItem(String name, Date created) {
        Item item = generateItem(name);
        item.setCreated(created);
        return item;
    }

    private Item generateItem(String name) {
        String desc = name + "-desc";
        BigDecimal amount = BigDecimal.ONE;
        return new Item(name, desc, amount);
    }
}
