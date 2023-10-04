package dmitr.tutor.engine.graphics;

import org.joml.Vector3f;

public class Camera extends PositionableRotableObject {

    public Camera() {
        this(new Vector3f(), new Vector3f());
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    @Override
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        position.y += offsetY;
    }

}
