import vue from '@vitejs/plugin-vue'

import createAutoImport from './auto-import'
import createElementAutoImport from "./element-auto-import";
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'
import OptimizationPersist from 'vite-plugin-optimize-persist'
import PkgConfig from 'vite-plugin-package-config'

export default function createVitePlugins(viteEnv, isBuild = false) {
  const vitePlugins = [vue()]
  if(isBuild) {
    vitePlugins.push(PkgConfig())
    vitePlugins.push(OptimizationPersist())
  }
  vitePlugins.push(createAutoImport())
  vitePlugins.push(createElementAutoImport())
  vitePlugins.push(createSetupExtend())
  vitePlugins.push(createSvgIcon(isBuild))
  isBuild && vitePlugins.push(...createCompression(viteEnv))
  return vitePlugins
}
