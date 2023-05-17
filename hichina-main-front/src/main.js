import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import { register } from 'swiper/element/bundle';
import myUpload from 'vue-image-crop-upload';
import { QuillEditor } from '@vueup/vue-quill'
import vueCountryRegionSelect from 'vue3-country-region-select'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import 'element-plus/dist/index.css'
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'
import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';
import QRCodeVue3 from "qrcode-vue3";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
// import 'swiper/dist/css/swiper.css'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons';
import Datepicker from 'vuejs3-datepicker';
library.add(fas)
const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.use(elementIcons)
app.use(vueCountryRegionSelect);
app.component('svg-icon', SvgIcon)
app.component('VueDatePicker', VueDatePicker);
app.component('QuillEditor', QuillEditor)
app.component('QRCodeVue3', QRCodeVue3)
app.component('VueJsonPretty', VueJsonPretty)
app.component('my-upload', myUpload)
app.component('Datepicker', Datepicker)
app.component("fa", FontAwesomeIcon)
app.mount('#app')
register();
app.directive('scale', {
  mounted(el,binding) {
    el.onmouseenter = () => {
      el.style.transform = 'scale('+ binding.value +')'
    }

    el.onmouseleave = () => {
      el.style.transform = 'scale(1)'
    }
  }
})