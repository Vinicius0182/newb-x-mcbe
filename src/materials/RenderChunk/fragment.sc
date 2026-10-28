$input v_color0, v_color1, v_fog, v_refl, v_texcoord0, v_lightmapUV, v_extra, v_sunparam, v_position

#include <bgfx_shader.sh>
#include <newb/main.sh>

#ifdef NL_CANOPY_DAPPLE
uniform vec4 CameraPosition;
#endif

SAMPLER2D_AUTOREG(s_MatTexture);
SAMPLER2D_AUTOREG(s_SeasonsTexture);
SAMPLER2D_AUTOREG(s_LightMapTexture);

void main() {
  #if defined(DEPTH_ONLY_OPAQUE) || defined(DEPTH_ONLY) || defined(INSTANCING)
    gl_FragColor = vec4(1.0,1.0,1.0,1.0);
    return;
  #endif

  vec4 diffuse = texture2D(s_MatTexture, v_texcoord0);
  vec4 color = v_color0;

  #ifdef ALPHA_TEST
    if (diffuse.a < 0.6) {
      discard;
    }
  #endif

  #if defined(SEASONS) && (defined(OPAQUE) || defined(ALPHA_TEST))
    diffuse.rgb *= mix(vec3(1.0,1.0,1.0), texture2D(s_SeasonsTexture, v_color1.xy).rgb * 2.0, v_color1.z);
  #endif

  vec3 glow = nlGlow(s_MatTexture, v_texcoord0, v_extra.a);

  diffuse.rgb *= diffuse.rgb;

  #if defined(TRANSPARENT) && !(defined(SEASONS) || defined(RENDER_AS_BILLBOARDS))
    if (v_extra.b > 0.9) {
      diffuse.rgb = vec3_splat(1.0 - NL_WATER_TEX_OPACITY*(1.0 - diffuse.b*1.8));
      diffuse.a = color.a;
    }
  #else
    diffuse.a = 1.0;
  #endif

  diffuse.rgb *= color.rgb;

  #if defined(NL_SUN_DIRECTIONAL) && !defined(RENDER_AS_BILLBOARDS)
    // directional sun shading: real per-pixel face normal from screen-space derivatives.
    // surfaces facing the sun get warm light, surfaces facing away get darker/cooler.
    if (v_extra.b < 0.9 && v_sunparam.w > 0.0) {
      vec3 dn = cross(dFdx(v_position), dFdy(v_position));
      vec3 N = dn*inversesqrt(max(dot(dn, dn), 0.000001));
      N = dot(N, v_position) > 0.0 ? -N : N; // make normal face camera
      float ndl = dot(N, v_sunparam.xyz);
      float facing = smoothstep(-0.15, 0.65, ndl);
      float sunlit = smoothstep(NL_SHADOW_SKY_LOW, NL_SHADOW_SKY_HIGH, v_lightmapUV.y);
      float away = (1.0-facing)*v_sunparam.w;
      float toward = facing*sunlit*v_sunparam.w;
      diffuse.rgb *= mix(vec3_splat(1.0), NL_SHADE_AWAY_COL, away);
      diffuse.rgb *= mix(vec3_splat(1.0), NL_SUN_FACE_COL, toward);
    }
  #endif

  #if defined(NL_CANOPY_DAPPLE) && !defined(RENDER_AS_BILLBOARDS)
    // tree shadows: patches of sun leaking through leaves, merged with the cloud shadows from vertex lighting
    if (v_extra.b < 0.9 && v_sunparam.w > 0.0) {
      float canopy = smoothstep(0.995, 0.93, v_lightmapUV.y)*smoothstep(0.8, 0.92, v_lightmapUV.y);
      if (canopy > 0.0) {
        highp vec3 gp = v_position + CameraPosition.xyz;
        float dp = smoothstep(0.42, 0.62, nlLeafDapple(gp, v_sunparam.xz));
        vec3 sunPatch = vec3_splat(1.0) + (NL_DAPPLE_LIGHT*canopy)*vec3(1.0, 0.86, 0.62);
        vec3 shadePatch = vec3_splat(1.0 - NL_DAPPLE_DARK*canopy);
        diffuse.rgb *= mix(vec3_splat(1.0), mix(shadePatch, sunPatch, dp), v_sunparam.w);
      }
    }
  #endif

  diffuse.rgb += glow;

  if (v_extra.b > 0.9) {
    diffuse.rgb += v_refl.rgb*v_refl.a;
  } else if (v_refl.a > 0.0) {
    // reflective effect - only on xz plane
    float dy = abs(dFdy(v_extra.g));
    if (dy < 0.0002) {
      float mask = v_refl.a*(clamp(v_extra.r*10.0,8.2,8.8)-7.8);
      diffuse.rgb *= 1.0 - 0.6*mask;
      diffuse.rgb += v_refl.rgb*mask;
    }
  }

  diffuse.rgb = mix(diffuse.rgb, v_fog.rgb, v_fog.a);

  diffuse.rgb = colorCorrection(diffuse.rgb);

  gl_FragColor = diffuse;
}
