package dmitr.tutor.engine.util;

import dmitr.tutor.engine.graphics.model.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static Mesh loadMesh(String fileName) throws Exception {
        List<String> lines = Utils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v" -> {
                    Vector3f vector3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vector3f);
                }
                case "vt" -> {
                    Vector2f vector2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vector2f);
                }
                case "vn" -> {
                    Vector3f vector3fNormal = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vector3fNormal);
                }
                case "f" -> {
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                }
                default -> {
                }
            }
        }

        return reorderLists(vertices, textures, normals, faces);
    }

    private static Mesh reorderLists(List<Vector3f> positionsList, List<Vector2f> texturesCoordList,
                                     List<Vector3f> normalsList, List<Face> facesList) {
        List<Integer> indices = new ArrayList<>();
        float[] positionsArray = new float[positionsList.size() * 3];

        int i = 0;
        for (Vector3f position : positionsList) {
            positionsArray[i * 3] = position.x;
            positionsArray[i * 3 + 1] = position.y;
            positionsArray[i * 3 + 2] = position.z;
            i++;
        }

        float[] texturesCoordArray = new float[positionsList.size() * 2];
        float[] normalsArray = new float[positionsList.size() * 3];

        for (Face face : facesList) {
            IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IndexGroup indexValue : faceVertexIndices) {
                processFaceVertex(indexValue, texturesCoordList, normalsList,
                        indices, texturesCoordArray, normalsArray);
            }
        }

        int[] indicesArray = new int[indices.size()];
        indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();

        return new Mesh(positionsArray, texturesCoordArray, normalsArray, indicesArray);
    }

    private static void processFaceVertex(IndexGroup indices, List<Vector2f> texturesCoordList,
                                          List<Vector3f> normalsList, List<Integer> indicesList,
                                          float[] texturesCoordArray, float[] normalsArray) {
        int posIndex = indices.indexPos;
        indicesList.add(posIndex);

        if (indices.indexTextureCoord >= 0) {
            Vector2f textureCoord = texturesCoordList.get(indices.indexTextureCoord);
            texturesCoordArray[posIndex * 2] = textureCoord.x;
            texturesCoordArray[posIndex * 2 + 1] = 1 - textureCoord.y;
        }
        if (indices.indexVectorNormal >= 0) {
            Vector3f vectorNormal = vectorNormal = normalsList.get(indices.indexVectorNormal);
            normalsArray[posIndex * 3] = vectorNormal.x;
            normalsArray[posIndex * 3 + 1] = vectorNormal.y;
            normalsArray[posIndex * 3 + 2] = vectorNormal.z;
        }
    }

    protected static class Face {

        private final IndexGroup[] indexGroups = new IndexGroup[3];

        public Face(String v1, String v2, String v3) {
            indexGroups[0] = parseLine(v1);
            indexGroups[1] = parseLine(v2);
            indexGroups[2] = parseLine(v3);
        }

        private IndexGroup parseLine(String line) {
            IndexGroup indexGroup = new IndexGroup();

            String[] lineTokens = line.split("/");
            indexGroup.indexPos = Integer.parseInt(lineTokens[0]) - 1;

            if (lineTokens.length > 1) {
                String textureCoord = lineTokens[1];
                indexGroup.indexTextureCoord = !textureCoord.isEmpty() ? Integer.parseInt(textureCoord) - 1 : IndexGroup.NO_VALUE;
                if (lineTokens.length > 2) {
                    indexGroup.indexVectorNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return indexGroup;
        }

        public IndexGroup[] getFaceVertexIndices() {
            return indexGroups;
        }

    }

    protected static class IndexGroup {

        public static final int NO_VALUE = -1;

        public int indexPos;
        public int indexTextureCoord;
        public int indexVectorNormal;

        public IndexGroup() {
            indexPos = NO_VALUE;
            indexTextureCoord = NO_VALUE;
            indexVectorNormal = NO_VALUE;
        }

    }

}
