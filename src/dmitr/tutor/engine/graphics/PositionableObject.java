package dmitr.tutor.engine.graphics;

import org.joml.Vector3f;

public abstract class PositionableObject {

    protected Vector3f position;

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        position.x += offsetX;
        position.y += offsetY;
        position.z += offsetZ;
    }

}
