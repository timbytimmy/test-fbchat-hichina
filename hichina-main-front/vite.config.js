import postCssPxToRem from 'postcss-pxtorem'
import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import createVitePlugins from './vite/plugins'

// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {

  // 加载.env文件，默认不会自动加载
  const env = loadEnv(mode, process.cwd())
  const { VITE_APP_ENV } = env
  return {
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，vite 会假设你的应用是被部署在一个域名的根路径上
    base: VITE_APP_ENV === 'production' ? '/' : './',
    plugins: createVitePlugins(env, command === 'build'),
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // 设置路径
        '~': path.resolve(__dirname, '/'),
        // 设置别名
        '@': path.resolve(__dirname, 'src')
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    // vite 相关配置
    server: {
      port: 9053,
      host: '0.0.0.0',
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        '/dev-api': {
          target: 'https://www.baidu.com', // 本地开发环境公用网关
          // target: 'http://0.0.0.0:8080',
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, '')
        }
      },
      hmr: true,
      usePolling: true
    },
    css: {
      devSourcemap: true,
      postcss: {
        plugins: [
          // postCssPxToRem({ // 自适应，px>rem转换
          //   rootValue: 192, // 1rem的大小
          //   propList: ['*'], // 需要转换的属性，这里选择全部都进行转换
          //   exclude: /node_modules/i
          // }),
// 前缀追加
          require('autoprefixer')({
            overrideBrowserslist: [
              'Android 4.1',
              'iOS 7.1',
              'Chrome > 31',
              'ff > 31',
              'ie >= 8',
              '> 1%'
            ],
            grid: true
          }),
          require('postcss-flexbugs-fixes'),
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule) => {
                if (atRule.name === 'charset') {
                  atRule.remove();
                }
              }
            }
          }
        ]
      },
      preprocessorOptions: {
        scss: {
          /**如果引入多个文件，可以使用
           * '@import "@/assets/scss/globalVariable1.scss";
           * @import"@/assets/scss/globalVariable2.scss";'
           **/
          additionalData: '@import "@/assets/style/global.scss";',
        }
      }
    },
    build: {
      assetsDir: 'static',
      rollupOptions: {
        external: [],
        output: {
          chunkFileNames: 'static/js/[name]-[hash].js',
          entryFileNames: 'static/js/[name]-[hash].js',
          assetFileNames: 'static/[ext]/name-[hash].[ext]',
          manualChunks(id) {
            // 将pinia的全局库实例打包进vendor，避免和页面一起打包造成资源重复引入
            if (id.includes(path.resolve(__dirname, '/src/store/index.js'))) {
              return 'vendor';
            }
          }
        }
      }
    }
  }
})
