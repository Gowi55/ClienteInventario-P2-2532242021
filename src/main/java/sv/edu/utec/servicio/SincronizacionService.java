package sv.edu.utec.servicio;

import sv.edu.utec.api.ProveedorAPI;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SincronizacionService {

    private final ProveedorAPI proveedorAPI;
    private final ProductoDAO productoDAO;

    public SincronizacionService(ProveedorAPI proveedorAPI, ProductoDAO productoDAO) {
        this.proveedorAPI = proveedorAPI;
        this.productoDAO = productoDAO;
    }

    public int[] sincronizar(int limite) throws IOException, InterruptedException, SQLException {
        List<Producto> productos = proveedorAPI.obtenerProductos(limite);
        int insertados = 0;
        int actualizados = 0;

        for (Producto p : productos) {
            if (productoDAO.existe(p.getId())) {
                productoDAO.actualizar(p);
                actualizados++;
            } else {
                productoDAO.insertar(p);
                insertados++;
            }
        }

        return new int[]{insertados, actualizados};
    }
}