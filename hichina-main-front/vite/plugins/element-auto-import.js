import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default function createElementAutoImport() {
  return Components({
    resolvers: [ElementPlusResolver()],
  })
}
