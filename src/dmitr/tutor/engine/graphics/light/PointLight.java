package dmitr.tutor.engine.graphics.light;

import dmitr.tutor.engine.graphics.PositionableObject;
import org.joml.Vector3f;

public class PointLight extends PositionableObject {

    private Vector3f color;

    protected float intensity;

    private Attenuation attenuation;

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this(color, position, intensity, new Attenuation(1, 0, 0));
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public static class Attenuation {

        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public float getLinear() {
            return linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }

    }

}
