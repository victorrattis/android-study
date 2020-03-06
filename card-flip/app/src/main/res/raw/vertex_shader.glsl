
attribute vec3 vPosition;
attribute vec2 aTexCoordinate;
attribute float aTextureIndex;

varying vec2 vTexCoordinate;
varying float vTextureIndex;

uniform mat4 uMatrix;

void main() {
    gl_Position = uMatrix * vec4(vPosition, 1.0);

    vTexCoordinate = aTexCoordinate;
    vTextureIndex = aTextureIndex;
}