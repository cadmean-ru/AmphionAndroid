attribute vec3 vPos;
attribute vec2 vTexCoord;

varying mediump vec2 fTexCoord;

void main() {
    gl_Position = vec4(vPos, 1);
    fTexCoord = vTexCoord;
}
