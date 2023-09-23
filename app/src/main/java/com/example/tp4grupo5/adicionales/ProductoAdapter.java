package com.example.tp4grupo5.adicionales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp4grupo5.R;
import com.example.tp4grupo5.entidades.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {
    private Context context;
    private List<Producto> productos;

    public ProductoAdapter(Context context, List<Producto> productos) {
        super(context, R.layout.list_item_producto, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_producto, parent, false);
        }

        // Obtener referencias a las vistas dentro del elemento personalizado
        TextView productId = convertView.findViewById(R.id.product_id);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productCategoria = convertView.findViewById(R.id.product_categoria);
        TextView productStock = convertView.findViewById(R.id.product_stock);
        TextView productEstado= convertView.findViewById(R.id.producto_estado);

        // Obtener el producto actual
        Producto producto = productos.get(position);

        // Asignar los datos del producto a las vistas
        productId.setText("ID: " + producto.getId());
        productName.setText("Nombre: " + producto.getNombre());
        productCategoria.setText("Categoría: " + producto.getCategoria_id());
        productStock.setText("Stock: " + producto.getStock());
        productEstado.setText("Estado: " + producto.isEstado());

        return convertView;
    }
}
