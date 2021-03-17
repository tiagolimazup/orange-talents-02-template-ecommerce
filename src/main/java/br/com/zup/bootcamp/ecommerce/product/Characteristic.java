package br.com.zup.bootcamp.ecommerce.product;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
class Characteristic {

    String name;

    String value;

    @Deprecated
    Characteristic() { }

    Characteristic(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Characteristic that = (Characteristic) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
