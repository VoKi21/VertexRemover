package com.cgvsu.vertexremover;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;
import java.util.stream.IntStream;

public class VertexRemover {
    public static void removeVertex(Model model, int index) {
        if (index < 0 || index >= model.vertices.size()) {
            throw new IllegalArgumentException(String.format(
                    "Index %s out of bounds for vertices list of length %s",
                    index,
                    model.vertices.size()
            ));
        }

        model.polygons.removeIf(polygon -> polygon.getVertexIndices().contains(index));

        reindex(model);
    }

    public static void removeVertices(Model model, int[] indices) {
        for (int index : indices) {
            if (index < 0 || index >= model.vertices.size()) {
                throw new IllegalArgumentException(String.format(
                        "Index %s out of bounds for vertices list of length %s",
                        index,
                        model.vertices.size()
                ));
            }

            model.polygons.removeIf(polygon -> polygon.getVertexIndices().contains(index));
        }
        reindex(model);
    }

    private static void reindex(Model model) {
        Set<Integer> unusedVertices = IntStream
                .range(0, model.vertices.size()).boxed().collect(HashSet::new, Set::add, Set::addAll);
        Set<Integer> unusedTextures = IntStream
                .range(0, model.textureVertices.size()).boxed().collect(HashSet::new, Set::add, Set::addAll);
        Set<Integer> unusedNormals = IntStream
                .range(0, model.normals.size()).boxed().collect(HashSet::new, Set::add, Set::addAll);

        for (Polygon polygon : model.polygons) {
            polygon.getVertexIndices().forEach(unusedVertices::remove);
            polygon.getTextureVertexIndices().forEach(unusedTextures::remove);
            polygon.getNormalIndices().forEach(unusedNormals::remove);
        }

        removeAndReindex(model, unusedVertices, model.vertices);
        removeAndReindex(model, unusedTextures, model.textureVertices);
        removeAndReindex(model, unusedNormals, model.normals);
    }

    private static <T> void removeAndReindex(Model model, Set<Integer> unusedIndices, List<T> originalVertices) {
        Stack<Integer> indicesToRemove = new Stack<>();
        indicesToRemove.addAll(unusedIndices.stream().sorted().toList());

        while (!indicesToRemove.isEmpty()) {
            int index = indicesToRemove.pop();
            originalVertices.remove(index);

            for (Polygon polygon : model.polygons) {
                ArrayList<Integer> polygonIndices = new ArrayList<>();
                if (originalVertices == model.vertices) {
                    polygonIndices = polygon.getVertexIndices();
                } else if (originalVertices == model.textureVertices) {
                    polygonIndices = polygon.getTextureVertexIndices();
                } else if (originalVertices == model.normals) {
                    polygonIndices = polygon.getNormalIndices();
                }

                for (int i = 0; i < polygonIndices.size(); i++) {
                    if (polygonIndices.get(i) > index) {
                        polygonIndices.set(i, polygonIndices.get(i) - 1);
                    }
                }
            }
        }
    }
}
