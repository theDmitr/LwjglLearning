package dmitr.tutor.engine.graphics.model.item;

import dmitr.tutor.engine.graphics.PositionableRotableObject;
import dmitr.tutor.engine.graphics.model.Mesh;
import org.joml.Vector3f;

public class Item extends PositionableRotableObject {

    private final Mesh mesh;
    private float scale;

    public Item(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f();
        rotation = new Vector3f();
        scale = 1;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Mesh getMesh() {
        return mesh;
    }

}
