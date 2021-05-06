varying mediump vec2 fTexCoord;

uniform sampler2D uTexture;
uniform mediump vec4 uTextColor;

void main() {
    mediump vec4 sampled = vec4(1.0, 1.0, 1.0, texture2D(uTexture, fTexCoord).a);
    gl_FragColor = uTextColor * sampled;
//    gl_FragColor = vec4(0, 0.69, 0.69, 1);
}
