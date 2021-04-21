attribute vec3 pos;
attribute vec4 col;

varying mediump vec4 fColor;

void main()
{
    gl_Position = vec4(pos, 1);
    fColor = col;
}