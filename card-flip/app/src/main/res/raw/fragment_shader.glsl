precision mediump float;

// Using texture array.
uniform sampler2D uTexture[2];

varying vec2 vTexCoordinate;
varying float vTextureIndex;

void main() {
    gl_FragColor = vec4(texture2D(uTexture[int(vTextureIndex)], vTexCoordinate).rgb, 1.0);
}