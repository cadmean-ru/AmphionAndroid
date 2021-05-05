varying mediump vec2 fTexCoord;

uniform sampler2D uTexture;

void main() {
    gl_FragColor = texture2D(uTexture, fTexCoord);
    gl_FragColor = vec4(0.69, 0.69, 0, 1);
}
