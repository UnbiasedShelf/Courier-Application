package by.bstu.vs.stpms.courier_application.model.network.dto;

import java.util.Collection;

public class ProductDto extends AbstractDto {
    private String name;
    private double weight;
    private double price;
    private Collection<Long> orderedIds;

    public ProductDto() {
    }

    public String getName() {
        return this.name;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getPrice() {
        return this.price;
    }

    public Collection<Long> getOrderedIds() {
        return this.orderedIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrderedIds(Collection<Long> orderedIds) {
        this.orderedIds = orderedIds;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProductDto))
            return false;
        final ProductDto other = (ProductDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (Double.compare(this.getWeight(), other.getWeight()) != 0) return false;
        if (Double.compare(this.getPrice(), other.getPrice()) != 0) return false;
        final Object this$orderedIds = this.getOrderedIds();
        final Object other$orderedIds = other.getOrderedIds();
        if (this$orderedIds == null ? other$orderedIds != null : !this$orderedIds.equals(other$orderedIds))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProductDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final long $weight = Double.doubleToLongBits(this.getWeight());
        result = result * PRIME + (int) ($weight >>> 32 ^ $weight);
        final long $price = Double.doubleToLongBits(this.getPrice());
        result = result * PRIME + (int) ($price >>> 32 ^ $price);
        final Object $orderedIds = this.getOrderedIds();
        result = result * PRIME + ($orderedIds == null ? 43 : $orderedIds.hashCode());
        return result;
    }

    public String toString() {
        return "ProductDto(name=" + this.getName() + ", weight=" + this.getWeight() + ", price=" + this.getPrice() + ", orderedIds=" + this.getOrderedIds() + ")";
    }
}
