package com.cgvsu.vertexremover;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class VertexRemoverTest {
    public static final String RESOURCES_DIRECTORY = "/home/ralen/IdeaProjects/VertexRemover/3DModels/SimpleModelsForReaderTests/";

    @Test
    void removeVerticesTest1() throws IOException {
        Path fileName = Path.of(RESOURCES_DIRECTORY + "Teapot.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        int[] verticesDelete = {1, 2};

        int before = model.vertices.size();
        VertexRemover.removeVertices(model, verticesDelete);
        Assertions.assertNotEquals(before, model.vertices.size());
    }

    @Test
    public void removeVerticesTest2() throws IOException {
        Path fileName = Path.of(RESOURCES_DIRECTORY + "Test05.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        model.vertices.forEach(System.out::println);

        VertexRemover.removeVertex(model, 1);
        Assertions.assertEquals(0, model.vertices.size());
    }

    @Test
    public void removeVerticesTest3() throws IOException {
        Path fileName = Path.of(RESOURCES_DIRECTORY + "NonManifold2.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        model.vertices.forEach(System.out::println);
        System.out.println(model.vertices.size());

        VertexRemover.removeVertex(model, 2);
        System.out.println();
        model.vertices.forEach(System.out::println);
        System.out.println(model.vertices.size());

        List<Vector3f> expectedVertices = new ArrayList<>();
        expectedVertices.add(new Vector3f(1f,0f,0f));
        expectedVertices.add(new Vector3f(1f,2f,0f));
        expectedVertices.add(new Vector3f(0f,2f,0f));
        expectedVertices.add(new Vector3f(0f,1f,0f));
        expectedVertices.add(new Vector3f(1f,1f,0f));

        System.out.println(model.polygons);

        Assertions.assertEquals(expectedVertices, model.vertices);

        Assertions.assertEquals(3, model.polygons.size());
    }

    @Test
    public void removeVerticesTest5() throws IOException {
        Path fileName = Path.of(RESOURCES_DIRECTORY + "Test08.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        int[] verticesToDelete = {2, 3, 4};

        System.out.println(model.polygons);
        System.out.println();

        VertexRemover.removeVertices(model, verticesToDelete);

        /// Проверка вершин
        List<Vector3f> expectedVertices = new ArrayList<>();
        expectedVertices.add(new Vector3f(1.0f, 0.0f, 0.0f));
        expectedVertices.add(new Vector3f(2.0f, 0.0f, 0.0f));
        expectedVertices.add(new Vector3f(0.0f, 2.0f, 0.0f));
        expectedVertices.add(new Vector3f(1.0f, 1.0f, 0.0f));
        expectedVertices.add(new Vector3f(1.5f, 0.5f, 1.0f));

        System.out.println(model.polygons);

        Assertions.assertEquals(expectedVertices, model.vertices);

        ObjWriter.write(model, RESOURCES_DIRECTORY + "Test04Deleted234.obj");

        // Проверяем полигоны
        Assertions.assertEquals(2, model.polygons.size());
    }
}