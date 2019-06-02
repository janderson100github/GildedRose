package hotel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("created")
    private Date created;

    @JsonProperty("name")
    @NotEmpty
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("amount")
    private BigDecimal amount;

    public ItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
