package com.cgvsu.vertexremover;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;

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

    private static void reindex(Model model) {
        Set<Integer> unusedVertices = new HashSet<>();
        Set<Integer> unusedTextures = new HashSet<>();
        Set<Integer> unusedNormals = new HashSet<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            unusedVertices.add(i);
        }
        for (int i = 0; i < model.textureVertices.size(); i++) {
            unusedTextures.add(i);
        }
        for (int i = 0; i < model.normals.size(); i++) {
            unusedNormals.add(i);
        }
        for (Polygon polygon : model.polygons) {
            for (int vertexIndex : polygon.getVertexIndices()) {
                unusedVertices.remove(vertexIndex);
            }
            for (int textureIndex : polygon.getTextureVertexIndices()) {
                unusedTextures.remove(textureIndex);
            }
            for (int normalIndex : polygon.getNormalIndices()) {
                unusedNormals.remove(normalIndex);
            }
        }

        Stack<Integer> verticesToRemove = new Stack<>();
        Stack<Integer> texturesToRemove = new Stack<>();
        Stack<Integer> normalsToRemove = new Stack<>();

        verticesToRemove.addAll(unusedVertices.stream().sorted().toList());
        texturesToRemove.addAll(unusedTextures.stream().sorted().toList());
        normalsToRemove.addAll(unusedNormals.stream().sorted().toList());

        while (!verticesToRemove.isEmpty()) {
            int index = verticesToRemove.pop();
            model.vertices.remove(index);
            for (Polygon polygon : model.polygons){
                for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                    if (polygon.getVertexIndices().get(i) > index) {
                        polygon.getVertexIndices().set(i, polygon.getVertexIndices().get(i) - 1);
                    }
                }
            }
        }

        while (!texturesToRemove.isEmpty()) {
            int index = texturesToRemove.pop();
            model.textureVertices.remove(index);
            for (Polygon polygon : model.polygons){
                for (int i = 0; i < polygon.getTextureVertexIndices().size(); i++) {
                    if (polygon.getTextureVertexIndices().get(i) > index) {
                        polygon.getTextureVertexIndices().set(i, polygon.getTextureVertexIndices().get(i) - 1);
                    }
                }
            }
        }

        while (!normalsToRemove.isEmpty()) {
            int index = normalsToRemove.pop();
            model.normals.remove(index);
            for (Polygon polygon : model.polygons){
                for (int i = 0; i < polygon.getNormalIndices().size(); i++) {
                    if (polygon.getNormalIndices().get(i) > index) {
                        polygon.getNormalIndices().set(i, polygon.getNormalIndices().get(i) - 1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1);
        l.add(2);
        Stack<Integer> s = new Stack<>();
        s.addAll(l);
        while (!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }
}
