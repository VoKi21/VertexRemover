package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.vertexremover.VertexRemover;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("3DModels/SimpleModelsForReaderTests/Teapot.obj");
        if (args.length > 0 && !args[0].isEmpty()) {
            fileName = Path.of(args[0]);
        }
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);

        System.out.println("Before:");
        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());

        int indexToRemove = 0;
        if (args.length > 1) {
            try {
                indexToRemove = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }
        VertexRemover.removeVertex(model, indexToRemove);

        System.out.println();
        System.out.println("After:");
        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());

        String outFileName = "outputs/out.obj";
        if (args.length > 2 && !args[2].isEmpty()) {
            outFileName = args[2];
        }
        ObjWriter.write(model, outFileName);
    }
}
