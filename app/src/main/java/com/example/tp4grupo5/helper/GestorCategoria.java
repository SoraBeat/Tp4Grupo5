package com.example.tp4grupo5.helper;

import com.example.tp4grupo5.entidades.Categoria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GestorCategoria {
    private DatabaseReference mDatabase;

    public GestorCategoria() {
        mDatabase = FirebaseDatabase.getInstance().getReference("categorias"); // Cambia "categorias" por el nombre de tu nodo en Firebase.
    }

    public void agregarCategoria(Categoria categoria) {
        // Generar una clave única para la nueva categoría.
        //String key = mDatabase.push().getKey();
        //categoria.setId(key);

        // Guardar la categoría en Firebase.
        mDatabase.child(categoria.getId()).setValue(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        // Actualizar la categoría en Firebase usando su ID.
        mDatabase.child(categoria.getId()).setValue(categoria);
    }

    public void eliminarCategoria(String categoriaId) {
        // Eliminar la categoría de Firebase usando su ID.
        mDatabase.child(categoriaId).removeValue();
    }

    public CompletableFuture<List<Categoria>> obtenerTodasLasCategorias() {
        final CompletableFuture<List<Categoria>> future = new CompletableFuture<>();
        final List<Categoria> categorias = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Categoria categoria = snapshot.getValue(Categoria.class);
                    categorias.add(categoria);
                }
                future.complete(categorias); // Completa el futuro con la lista de categorías cuando se hayan cargado todos los datos.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error si es necesario
                future.completeExceptionally(databaseError.toException()); // Completa el futuro con una excepción en caso de error.
            }
        });

        return future;
    }
}
