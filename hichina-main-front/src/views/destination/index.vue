<template>
  <div class="head">
    <v-lazy-image
      src="https://photoprism.hichinatravel.com/api/v1/t/2bfc32550ae040956f7e861566d26c487c0143e7/32mcf2k4/fit_2048"
      alt=""
    />

    <!-- <div class="point-list">
      此处由于xiaopiu问题，补上一个空的div
    </div> -->
  </div>

  <p class="title mt-52">Seasonal Recommendation</p>
  <div
    style="
      font-family: arial;
      font-size: 24px;
      margin: 25px;
    "
  >
    <div style="margin-top: 10px;margin: 0 auto; width: 50%;">
      <el-input
        v-model="query"
        placeholder="Search by title"
        @input="search"
      />
    </div>
  </div>

  <div class="list mt-24">
    <div class="wrapper flex-start">
      <destination-card
        class="hover-item"
        :destinationSummary="item"
        v-for="(item, index) in globalDestinationCards"
        v-bind:key="index"
        :class="{ 'no-mar': (index + 1) % 4 === 0 }"
      />
    </div>
  </div>
  <div style="width: 100%;">
    <el-button type="primary" plain class="get-more mt-100" @click="loadMore" style="cursor: pointer;">Click to load more</el-button>
  </div>
  <div class="wrapper">
    <p class="text mt-26">Province and city list</p>

    <ul class="area-list mt-26">
      <li>
        Municipality·直辖市Shanghai·上海 Beijing·北京 Tianjin·天津
        Chongqing·重庆 Special Adaministration Region·特别行政区Hong Kong·香港
        Macau·澳门Hainan·
      </li>

      <li>海南</li>
      <li>
        Sanya·三亚 Haikou·海口 Wenchang·文昌 Sansha·三沙市 Wuzhishan·五指山市
        Wanning·万宁 Baoting·保亭 Danzhou·儋州 Lingshui·陵水黎族自治县Yunnan·
      </li>

      <li>云南</li>
      <li>
        Lijiang·丽江 Dali·大理 Kunming·昆明 Shangri-La·香格里拉 Xiānggélǐlā
        Xishuangbanna·西双版纳 Qujing·曲靖 Wenshan·文山壮族苗族自治州
        Honghe·红河哈尼族彝族自治州 Chuxiong·楚雄彝族自治州
        Puer·普洱Tibet·西藏Lhasa·拉萨 Nyingchi·林芝 Lín Zhī Shigatse·日喀则 Rì
        Kā Zé Mainling·米林 Ngari·阿里Shandong·
      </li>
      <li>山东</li>
      <li>
        Qingdao·青岛 Jinan·济南 Jǐ Nán Penglai·蓬莱 Taian·泰安 Yantai·烟台
        Weifang·潍坊 Weihai·威海 Binzhou·滨州 Dezhou·德州 Dongying·东营
        Heze·菏泽 Jining·济宁 Liaocheng·聊城 Linyi·临沂 Rizhao·日照
        Zaozhuang·枣庄 Zibo·淄博Zhejiang·
      </li>
      <li>浙江</li>
      <li>
        Hangzhou·杭州 Wenzhou·温州 Shaoxing·绍兴 Ningbo·宁波 Jiaxing·嘉兴
        Zhoushan·舟山 Jinhua·金华 Huzhou·湖州 Lishui·丽水 Taizhou·台州
      </li>
      <li>Fujian·福建</li>
      <li>
        Xiamen·厦门 Quanzhou·泉州 Fuzhou·福州 Zhangzhou·漳州 Ningde·宁德
        Wuyishan·武夷山 Longyan·龙岩 Nanping·南平 Putian·莆田 Sanming·三明
      </li>
      <li>Shaanxi·陕西</li>
      <li>
        Xi'an·Xi'an 西安 Huayin·华阴 Xianyang·咸阳 Hanzhong·汉中 Hànzhōng
        Yanan·Yan'an 延安 Ankang·安康 Lvliang·吕梁 Yulin·榆林 Baoji·宝鸡
      </li>
      <li>Shanxi·山西</li>
      <li>
        Datong·大同 Jinzhong·晋中 Xinzhou·忻州 Taiyuan·太原 Changzhi·长治
        Jincheng·晋城 Shuozhou·朔州 Yangquan·阳泉
      </li>
      <li>Hunan·湖南</li>
      <li>
        Changsha·长沙 Zhangjiajie·张家界 Chenzhou·郴州
        Xiangxi·湘西土家族苗族自治州 Changde·常德 Hengyang·衡阳 Shaoyang·邵阳
        Xingtan·湘潭 Yiyang·益阳 Yueyang·岳阳 Zhuzhou·株洲
      </li>
      <li>Hubei·湖北</li>
      <li>
        Wuhan·武汉 Shennongjia Forestry District·神农架 Enshi Tujia and Miao
        Autonomous Prefecture·恩施 Xiangyang·襄阳 Shiyan·十堰 Yichang·宜昌
        Ezhou·鄂州 Huangshi·黄石 Jingzhou·荆州 Suizhou·随州 Xianning·咸宁
      </li>
      <li>Guangdong·广东</li>
      <li>
        Guangzhou·广州 Shenzhen·深圳 Shaoguan·韶关 Foshan·佛山 Dongguan·东莞
        Zhuhai·珠海 Shantou·汕头 Huizhou·惠州 Chaozhou·潮州 Qingyuan·清远
        Heyuan·河源 Jiangmen·江门 Jieyang·揭阳 Maoming·茂名 Meihzou·梅州
        Yangjiang·阳江 Yunfu·云浮 Zhanjiang·湛江 Zhaoqing·肇庆
      </li>
      <li>Guangxi·广西</li>
      <li>
        Beihai·北海 Guilin·桂林 Nanning·南宁 Chongzuo·崇左 Laibin·来宾
        Liuzhou·柳州
      </li>
      <li>Anhui·安徽</li>
      <li>
        Huangshan·黄山 Hefei·合肥 Xuancheng·宣城 Chizhou·池州 Anqing·安庆
        Tongling·铜陵 Bengbu·蚌埠 Bozhou·亳州 Chuzhou·滁州 Fuyang·阜阳
        Huaibei·淮北 Huainan·淮南 Wuhu·芜湖 Lian·六安
      </li>
      <li>Jiangxi·江西</li>
      <li>
        Yingtan·鹰潭 Jingdezhen Porcelain·景德镇 Shangrao·上饶 Lushan·庐山市
        Jiujiang·九江 Nanchang·南昌 Pingxiang·萍乡 Xinyu·新余 Yichun·宜春
      </li>
      <li>Jiangsu·江苏</li>
      <li>
        Nanjing·南京 Suzhou·苏州 Wuxi·无锡 Yangzhou·扬州 Xuzhou·徐州
        Kunshan·昆山 Yixing·宜兴 Shaoxing·绍兴 Taizhou·泰州 changzhou·常州
        Huaian·淮安 Lianyungang·连云港 Nantong·南通 Suqian·宿迁 Yancheng·盐城
      </li>
      <li>Henan·河南</li>
      <li>
        Luoyang·洛阳 Anyang·安阳 Zhengzhou·郑州 Kaifeng·开封 Jiaozuo·焦作
        Dengfeng·登封 Zhoukou·周口 Luohe·漯河 Nanyang·南阳 Puyang·濮阳
        Sanmenxia·三门峡 Shangqiu·商丘 Xinxiang·新乡 Xuchang·许昌
      </li>
      <li>Hebei·河北</li>
      <li>
        Chengde·承德 Baoding·保定 Qinhuangdao·秦皇岛 Zhangjiakou·张家口
        Handan·邯郸 Zunhua·遵化 Langfang·廊坊 Cangzhou·沧州 Hengshui·衡水
        Shijiazhuang·石家庄 Tangshan·唐山 Xingtai·邢台
      </li>
      <li>Xinjiang·新疆</li>
      <li>
        Ürümqi·乌鲁木齐 Wūlǔmùqí Ili Kazakh Autonomous Prefecture·伊犁 Yīlí
        Turpan·吐鲁番 Tǔlǔfān Bayingolin Mongol Autonomous Prefecture·巴音郭楞
        Bā Yīn Guō Léng Kashgar Prefecture·喀什 Altay·阿勒泰地区 Aksu·阿克苏
        Changji Hui·昌吉回民自治州 Hami·哈密 Karamay·克拉玛依 Kuitun·奎屯
        Shihezi·石河子 Yining·伊宁
      </li>
      <li>Heilongjiang·黑龙江</li>
      <li>
        Harbin·哈尔滨 Hā'ěrbīn Mudanjiang· Daxing'anling Prefecture·大兴安岭
        Dàxīng'ānlǐng Mohe·漠河 Heihe·黑河 Daqing·大庆 Jiamusi·佳木斯
        Qiqihar·齐齐哈尔
      </li>
      <li>Jilin·吉林</li>
      <li>
        Changchun·长春 Yanbian Korean Prefecture·延边 Baishan·白山 Tonghua·通化
        Liaoyuan·辽源
      </li>
      <li>Liaoning·辽宁</li>
      <li>
        Dalian·大连 Shenyang·沈阳 Dandong·丹东 Anshan·鞍山 Chaoyang·朝阳
        Huludao·葫芦岛 Panjin·盘锦 Yingkou·营口 Jinzhou·锦州
      </li>
      <li>Qinghai·青海</li>
      <li>
        Xining·西宁 Golmud·格尔木 Gé'ěrmù Tibetan Autonomous Prefecture of
        Hainan·海南藏族自治州
      </li>
      <li>Inner Mongolia·内蒙古</li>
      <li>
        Hulunbuir·呼伦贝尔 Hūlúnbèi'ěr Alxa League·阿拉善 Ā Lā Shàn
        Hohhot·呼和浩特 Hūhéhàotè Ordos·鄂尔多斯 È'ěrduōsī Arxan·阿尔山
        Baynnur·巴彦淖尔 Baotou·包头 Bortala·博尔塔拉蒙古自治州 Chifeng·赤峰
        Wulanchabu·乌兰察布 Tongliao·通辽 Wuhai·乌海
      </li>

      <li>Gansu·甘肃</li>
      <li>
        Lanzhou·兰州 Dunhuang·敦煌 Jiayuguan·嘉峪关 Jiāyù Guān Zhangye·张掖
        Gannan Tibetan Autonomous Prefecture·甘南 Baiyin·白银 Baoji·宝鸡
        Dingxi·定西 Jiuquan·酒泉 Linxia Hui·临夏回族自治州 Pingliang·平凉
        Qingyang·庆阳 Tianshui·天水 Wuwei·武威
      </li>
      <li>Ningxia·宁夏</li>
      <li>
        Yinchuan·银川 Guyuan·固原 Wuzhong·吴忠 Zhongwei·中卫 Shizuishan·石嘴山
      </li>
      <li>Taiwan·台湾</li>
      <li>
        Taipei·台北 Taichung·台中 Kaohsiung·高雄 Gāoxióng Shì Hualien·花莲
        Huālián Pingtung·屏东 Yilan·宜兰 Taitung·台东 Nantou·南投 Chiayi·嘉义
        Xinzhu·新竹 Tainan·台南 Taoyuan·桃园
      </li>
      <li>Sichuan·四川</li>
      <li>
        Chengdu·成都 Ya'an·雅安 Yǎ'ān Leshan·乐山 Garzê Tibetan Autonomous
        Prefecture·甘孜 Gān Zī Ngawa·阿坝藏族羌族自治州 Deyang·德阳
        Guangyuan·广元 Nanchong·南充 Liangshan·凉山彝族自治州 Mianyang·绵阳
        Neijiang·内江 Panzhihua·攀枝花 Suining·遂宁 Yibin·宜宾 Zigong·自贡
      </li>
      <li>Guizhou·贵州</li>
      <li>
        Guiyang·贵阳 Southeast Guizhou·黔东南 Qián Dōng Nán Anshun·安顺
        Bijie·毕节 Qiannan·黔南布依族苗族自治州 Qianxinan·黔西南布依族苗族自治州
        Zunyi·遵义
      </li>
    </ul>
  </div>
</template>

<script setup>
import VLazyImage from "v-lazy-image";
import { AXIOS } from "@/common/http-commons";
import DestinationCard from "../../components/DestinationCard";
const pageSize = ref(50);
const currentPage = ref(1);
const destinationCards = ref([]);
const globalDestinationCards = ref([]);
const query = ref("");

function loadMore(){
  currentPage.value += 1
  loadDestinations()
}

function debounce(cb, delay) {
  let timer;
  return function (...args) {
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      cb.call(this, ...args);
    }, delay);
  };
}

const search = debounce((value) => {
  globalDestinationCards.value = []
  currentPage.value = 1;
  loadDestinations();
}, 500);

function loadDestinations() {
  destinationCards.value = [];
  var params = {};
  params.pageSize = pageSize.value;
  console.log("loading page: "+ currentPage.value)
  params.page = currentPage.value;
  (params.query = query.value),
    AXIOS.get("/api/public/destination/list", { params: params })
      .then(function (response) {
        console.log("got list of destination:");
        destinationCards.value = response.data.data.data;
        console.log(destinationCards.value);
        globalDestinationCards.value = globalDestinationCards.value.concat(destinationCards.value)
      })
      .catch(function (error) {
        console.log(error);
      });
}
onMounted(() => {
  loadDestinations();
});
</script>

<style scoped lang="scss">
.head {
  position: relative;
  height: 817px;

  img {
    width: 100%;
    height: 100%;
  }

  .point-list {
    position: absolute;
    width: 528px;
    height: 601px;
    right: 24px;
    top: 57px;
    background: white;
    font-size: 20px;
    border-radius: 16px;
    z-index: 999;
    overflow-y: scroll;

    &::-webkit-scrollbar {
      display: none;
    }
  }
}

.title {
  color: rgba(42, 130, 228, 1);
  font-size: 32px;
  text-align: center;
}

.list {
  padding-top: 28px;
  background: #f3f3f3;
}

.wrapper {
  flex-wrap: wrap;
}

.hover-item {
  width: 295px;
  height: 192px;
  margin-right: 70px;
  margin-bottom: 39px;
}

.text {
  color: rgba(80, 80, 80, 1);
  font-size: 19px;
  text-align: center;
}

.area-list {
  color: rgba(80, 80, 80, 1);
  font-size: 14px;
  line-height: 30px;
}
.get-more {
    margin: 0 auto;
    color: rgba(80, 80, 80, 1);
    font-size: 14px;
    width: 100%;
    text-align: center;
  }
</style>