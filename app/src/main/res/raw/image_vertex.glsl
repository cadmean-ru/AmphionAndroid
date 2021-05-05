attribute vec3 pos;
attribute vec2 texCoord;

varying mediump vec2 fTexCoord;

void main() {
    gl_Position = vec4(pos, 1);
    fTexCoord = texCoord;
}
