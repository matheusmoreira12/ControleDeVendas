import java.util.function.Supplier;

public class ClienteSupplier implements Supplier<Cliente> {
    private final int defaultId;
    private final String defaultNome;

    public ClienteSupplier(int defaultId, String defaultNome) {
        this.defaultId = defaultId;
        this.defaultNome = defaultNome;
    }

    @Override
    public Cliente get() {
        return new Cliente (defaultId, defaultNome);
    }
}
