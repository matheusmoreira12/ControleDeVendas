package Data;

import java.util.function.Supplier;

public class ClienteSupplier implements Supplier<Cliente> {
    @Override
    public Cliente get() {
        return new Cliente (0, 0, "", "", "");
    }
}
