#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation attenuation;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textureCoord)
{
    if (material.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, textureCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else
    {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calculatePointLight(PointLight pointLight, vec3 position, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);

    vec3 light_direction = pointLight.position - position;
    vec3 to_light_source = normalize(light_direction);
    float diffuseFactor = max(dot(normal, to_light_source), 0.0);
    diffuseColor = diffuseC * vec4(pointLight.color, 1.0) * pointLight.intensity * diffuseFactor;

    vec3 camera_direction = normalize(-position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specularFactor = max(dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularC * pointLight.intensity * specularFactor * material.reflectance * vec4(pointLight.color, 1.0);

    float distance = length(light_direction);
    float attenuationInv = pointLight.attenuation.constant + pointLight.attenuation.linear * distance +
    pointLight.attenuation.exponent * distance * distance;
    return (diffuseColor + specularColor) / attenuationInv;
}

void main()
{
    setupColors(material, outTexCoord);

    vec4 diffuseSpecularComp = calculatePointLight(pointLight, mvVertexPos, mvVertexNormal);

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}
