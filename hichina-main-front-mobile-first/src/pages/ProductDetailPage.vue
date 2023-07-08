<template>
  <q-page>
    <div class="row" style="background-color: #e5f2fa; height: 50px">
      <div class="col-sm-3"></div>
      <div
        class="col-12 col-sm-9 text-body1 q-pl-md flex"
        style="align-items: center"
      >
        Travel shop > {{ productTypeName }} > {{ productName }}
      </div>
    </div>
    <div class="row q-pa-md" style="min-height: 340px">
      <div class="col-12 col-sm-4" style="height: 340px">
        <q-carousel
          animated
          v-model="slide"
          navigation
          infinite
          height="100%"
          :autoplay="autoplay"
          arrows
          transition-prev="slide-right"
          transition-next="slide-left"
          @mouseenter="autoplay = false"
          @mouseleave="autoplay = 2000"
        >
          <q-carousel-slide
            v-for="imageUrl in imageContainer.imageList"
            :key="imageUrl"
            :name="imageUrl"
            :img-src="imageUrl"
          >
          </q-carousel-slide>
        </q-carousel>
      </div>
      <div class="col-12 col-sm-8 column" style="height: 340px">
        <div class="text-h4 q-pl-md" style="height: 50px">
          {{ productName }}
        </div>
        <div class="q-ml-md q-pl-md col" style="background-color: #fff9c6">
          {{ removeHtmlTag(productDescription).substring(0, 300) }}...
        </div>
      </div>
    </div>
    <div class="row" style="min-height: 500px">
      <div class="col-12 col-sm-4 row justify-center" style="height: 500px">
        <!-- https://github.com/shubhadip/vuejs3-datepicker#install -->
        <datepicker
          v-if="renderComponent && productTypeId != LOCALSPECIALTYPRODUCTTYPE"
          @selected="handleSelectDate"
          use-utc="true"
          :disabled-dates="state.disabledDates"
          v-model="selectedDate"
          :prevent-disable-date-selection="true"
          inline="true"
          :icon-width="40"
          calendar-class="calendarclass"
        ></datepicker>
      </div>
      <div
        class="col-12 col-sm-8 q-pl-xl q-pt-md column"
        style="min-height: 500px"
      >
        <div class="row">
          <div class="col-3 col-sm-2 text-weight-bold text-h4">Days:</div>
          <div
            v-if="productTypeId != FLIGHTPRODUCTTYPE"
            class="col-11 col-sm-10 flex text-h6 q-pl-sm"
            style="align-items: center"
          >
            {{ days - 1 }} nights {{ days }} days
          </div>
          <div
            v-if="productTypeId === FLIGHTPRODUCTTYPE"
            class="col-11 col-sm-10 flex text-h6 q-pl-sm"
            style="align-items: center"
          >
            {{ days }} days
          </div>
        </div>
        <div class="row">
          <div class="col-3 col-sm-2 text-weight-bold text-h4">Option:</div>
          <div
            class="col-11 col-sm-10 flex text-h6 q-pl-sm"
            style="align-items: center"
          >
            <q-btn-toggle
              v-model="activeIndex"
              spread
              no-caps
              toggle-color="blue-6"
              color="white"
              text-color="black"
              @click="
                setActiveCate(packageCategories[activeIndex], activeIndex)
              "
              :options="labelValuePair(packageCategories)"
            />
          </div>
        </div>
        <div class="row text-weight-bold text-h4 q-mt-xl">Passengers</div>
        <div class="row q-mt-md" style="border-top: 2px solid black">
          <div
            v-if="productTypeId != LOCALSPECIALTYPRODUCTTYPE"
            class="row col-12 q-pr-md q-mt-md"
          >
            <div class="col-11 col-sm-4 q-pr-md">
              <q-input
                label="Adult Count"
                v-model.number="adultCount"
                type="number"
                min="1"
                outlined
              />
            </div>
            <div class="col-11 col-sm-4 q-pr-md">
              <q-input
                label="Child Count"
                v-model.number="childCount"
                type="number"
                min="0"
                outlined
              />
            </div>
            <div class="col-11 col-sm-4 q-pr-md">
              <q-input
                label="Infant Count(less than 2 years old)"
                v-model.number="infantCount"
                type="number"
                min="0"
                outlined
              />
            </div>
          </div>
          <div
            v-if="productTypeId === LOCALSPECIALTYPRODUCTTYPE"
            class="row col-12 q-pr-md q-mt-md"
          >
            <div class="col-4 q-pr-md">
              <q-input
                label="Count"
                v-model.number="buyCount"
                type="number"
                min="1"
                outlined
              />
            </div>
          </div>
          <div class="row col-12" style="height: 50px"></div>
          <div
            class="row col-12 q-pr-md q-mt-md"
            style="background-color: #e9e9e9; min-height: 50px"
          >
            <div
              class="col-4 text-blue-6 text-h5 flex"
              style="align-items: center; height: 50px"
            >
              CNY
            </div>
            <div
              v-if="
                productTypeId != LOCALSPECIALTYPRODUCTTYPE &&
                productTypeId != HOTELPRODUCTTYPE
              "
              class="col-4 col-sm-3 flex"
              style="align-items: center; height: 50px"
            >
              {{
                adultCount * adultUnitPrice +
                childCount * childUnitPrice +
                infantCount * infantUnitPrice
              }}
            </div>
            <div
              v-if="
                productTypeId == LOCALSPECIALTYPRODUCTTYPE ||
                productTypeId == HOTELPRODUCTTYPE
              "
              class="col-4 col-sm-3 flex"
              style="align-items: center; height: 50px"
            >
              {{ generalPrice * buyCount }}
            </div>
            <div
              class="col-7 col-sm-3 flex"
              style="align-items: center; height: 50px"
            >
              <q-btn
                class="glossy"
                rounded
                color="deep-orange"
                label="Pre-book Consult"
              />
            </div>
            <div
              class="col-5 col-sm-2 flex"
              style="align-items: center; height: 50px"
            >
              <q-btn class="glossy" rounded color="blue-6" label="Book Now" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="row q-pa-md"
      style="min-height: 500px; background-color: #d9d9d9"
    >
      <div
        class="col-12"
        style="background-color: white"
        v-html="productDescription"
      ></div>
    </div>
  </q-page>
</template>

<script>
import { ref, onMounted, nextTick } from "vue";
import { api } from "boot/axios";
import { useSeoMeta } from "unhead";
import { useRoute } from "vue-router";
import Datepicker from "vuejs3-datepicker";
export default {
  name: "ProductDetailPage",
  components: {
    Datepicker,
  },
  setup() {
    const route = useRoute();
    const buyCount = ref(1);
    const adultCount = ref(1);
    const childCount = ref(0);
    const infantCount = ref(0);
    const skusInGroup = { skus: [] };
    const productName = ref("");
    const productDescription = ref("");
    const productTypeId = ref("");
    const productTypeName = ref("");
    const packageCategories = ref([]);
    const imageList = ref([]);
    const days = ref("");
    const imageContainer = { imageList: [] };
    const activeCategory = ref("");
    const filteredSkuIndex = ref([]);
    const activeIndex = ref(0);
    const adultUnitPrice = ref(0);
    const generalPrice = ref(0);
    const childUnitPrice = ref(0);
    const infantUnitPrice = ref(0);
    const singleMatchedSku = ref({});
    const selectedDate = ref(new Date());
    const maxNum = ref(1000000);
    const renderComponent = ref(true);

    const TYPEOFPACKAGEPROP = "11cd8b32-c4f6-47db-8b8a-486c992bf43b";
    const LOCALSPECIALTYPRODUCTTYPE = "fd264cab-ee8d-4571-a477-03d7e7c090b3";
    const HOTELPRODUCTTYPE = "a9f5adbe-c09b-49bc-a614-8a1c5d5e5337";
    const MAXNUM = "720f4f2e-e114-4003-9806-bc56a9366278";
    const PRODUCTIMAGEURLPROP = "2dea54f4-9b9c-413a-8b3a-0caf273283d2";
    const DAYSPROP = "8cc865ff-b30f-4f00-b426-9e64418e5100";
    const AVAILABLEDATEPROP = "f0b807e5-d1a6-4454-a400-7905a4fea492";
    const TOURPRODUCTTYPE = "3a53caed-b788-4290-896d-7922532ad769";
    const FLIGHTHOTELPRODUCTTYPE = "e05d07a3-a717-45b8-b009-47a349890e41";
    const FLIGHTPRODUCTTYPE = "d7b95089-e278-4522-8f71-dacac41ba32f";
    const GENERALPRICE = "e16df480-b17d-4442-91c2-d6c30d0d7ab0";
    const ADULTPRICEPROP = "e228b843-e054-41f8-91dd-19663460df54";
    const CHILDPRICEPROP = "c4c845a7-4bef-46d8-a5ad-d72a5464e8b1";
    const INFANTPRICEPROP = "448406cb-b68f-439e-9da8-148d78ae8404";

    const forceRerender = async () => {
      // Remove MyComponent from the DOM
      renderComponent.value = false;

      // Wait for the change to get flushed to the DOM
      await nextTick();

      // Add the component back in
      renderComponent.value = true;
    };

    function handleSelectDate(dt) {
      console.log("dt");
      console.log(dt);
      var matchedIndex = -1;
      for (var i in filteredSkuIndex.value) {
        var index = filteredSkuIndex.value[i];
        var availableDatesInString =
          extractAttributeValueFromProductPropertyBag(
            skusInGroup.skus[index],
            AVAILABLEDATEPROP
          );
        var dateObjArray = multiDateString2DateObjectArray(
          availableDatesInString
        );
        console.log("dateObjArray");
        console.log(dateObjArray);
        if (dateObjArray.some((e) => e.getDate() == dt.getDate())) {
          matchedIndex = index;
          break;
        }
      }
      console.log("matchedIndex");
      console.log(matchedIndex);
      singleMatchedSku.value = skusInGroup.skus[matchedIndex];

      maxNum.value = extractAttributeValueFromProductPropertyBag(
        singleMatchedSku.value,
        MAXNUM
      );

      console.log("maxNum");
      console.log(maxNum.value);
      setPrice(singleMatchedSku.value);
    }

    var state = {
      disabledDates: {
        to: new Date(2023, 3, 15), // Disable all dates up to specific date
        from: new Date(2023, 4, 20), // Disable all dates after specific date

        dates: [
          // Disable an array of dates
          new Date(2016, 9, 16),
          new Date(2016, 9, 17),
          new Date(2016, 9, 18),
        ],
        preventDisableDateSelection: true,
      },
    };

    function removeHtmlTag(input) {
      var div = document.createElement("div");
      div.innerHTML = input;
      var text = div.textContent || div.innerText || "";
      return text;
    }

    function logPv() {
      api
        .post(
          "/api/public/pagestats/pv/product-detail-" + route.params.skuGroupId
        )
        .then((res) => {
          console.log("log pv:");
          console.log(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }

    function logView() {
      api
        .post("/api/public/pagestats/view-product/" + route.params.skuGroupId)
        .then((res) => {
          console.log("view cnt of this product:");
          console.log(res.data);
        })
        .catch((err) => {
          console.error("Error:", err);
        });
    }

    function extractAttributeValueFromProductPropertyBag(inputObject, attrId) {
      var valArray = inputObject.productPropertyBag.filter((obj) => {
        return obj.attributeId == attrId;
      });
      if (valArray.length > 0) {
        return valArray[0].attributeValue;
      }
      return null;
    }

    function getFilteredSkuIndexAndSetActiveValue(item, index) {
      activeCategory.value = item;
      filteredSkuIndex.value = [];
      for (var i in skusInGroup.skus) {
        var sku = skusInGroup.skus[i];
        var selectedSkuPropertyBags = sku["productPropertyBag"].filter(
          (obj) => {
            return obj.attributeId == TYPEOFPACKAGEPROP;
          }
        );
        if (selectedSkuPropertyBags[0].attributeValue == item) {
          filteredSkuIndex.value.push(i);
        }
      }
      activeIndex.value = index;
    }

    function multiDateString2DateObjectArray(candidateAvailableDates) {
      var dateObjArray = [];
      var datestringArray = candidateAvailableDates.split(";");
      for (var i in datestringArray) {
        var datestr = datestringArray[i];
        if (datestr != null && datestr.length > 0) {
          var dateObj = new Date(datestr);
          // console.log("...converting string to date:"+ datestr)
          // console.log(dateObj)
          dateObjArray.push(dateObj);
        }
      }
      return dateObjArray;
    }

    function compareDate(a, b) {
      if (a.getDate() < b.getDate()) {
        return -1;
      }
      if (a.getDate() > b.getDate()) {
        return 1;
      }
      return 0;
    }

    function setCandidateAvailableDates(indexArray) {
      var candidateAvailableDates = "";
      for (var i in indexArray) {
        var availableDates = extractAttributeValueFromProductPropertyBag(
          skusInGroup.skus[indexArray[i]],
          AVAILABLEDATEPROP
        );
        candidateAvailableDates = candidateAvailableDates + availableDates;
      }

      var dateObjArray = multiDateString2DateObjectArray(
        candidateAvailableDates
      );

      dateObjArray.sort(compareDate);

      var minDate = dateObjArray[0];
      var maxDate = dateObjArray.slice(-1)[0];

      state.disabledDates.to = minDate;
      var nextDayOfMax = new Date(maxDate);
      nextDayOfMax.setDate(nextDayOfMax.getDate() + 1);
      state.disabledDates.from = nextDayOfMax;

      var segmentDisabledDates = [];
      var pointer = new Date(minDate);

      while (pointer < maxDate) {
        // check contains
        if (!dateObjArray.some((e) => e.getDate() == pointer.getDate())) {
          segmentDisabledDates.push(new Date(pointer));
        }
        // update pointer
        pointer.setDate(pointer.getDate() + 1);
      }
      state.disabledDates.dates = segmentDisabledDates;

      forceRerender();
    }

    function setPrice(sku) {
      if (sku == null) {
        if (
          productTypeId.value == LOCALSPECIALTYPRODUCTTYPE ||
          productTypeId.value == HOTELPRODUCTTYPE
        ) {
          console.log("001");
          generalPrice.value = 0;
        } else {
          adultUnitPrice.value = 0;
          childUnitPrice.value = 0;
          infantUnitPrice.value = 0;
        }
      } else {
        if (
          productTypeId.value == LOCALSPECIALTYPRODUCTTYPE ||
          productTypeId.value == HOTELPRODUCTTYPE
        ) {
          console.log("003");
          generalPrice.value = extractAttributeValueFromProductPropertyBag(
            sku,
            GENERALPRICE
          );
        } else {
          console.log("004");
          adultUnitPrice.value = extractAttributeValueFromProductPropertyBag(
            sku,
            ADULTPRICEPROP
          );
          childUnitPrice.value = extractAttributeValueFromProductPropertyBag(
            sku,
            CHILDPRICEPROP
          );
          infantUnitPrice.value = extractAttributeValueFromProductPropertyBag(
            sku,
            INFANTPRICEPROP
          );
        }
      }
    }

    function setActiveDescription() {
      var indexToFilter =
        filteredSkuIndex.value.length < 1 ? 0 : filteredSkuIndex.value[0];
      productDescription.value =
        skusInGroup.skus[indexToFilter]["hichinaProductBasicDTO"][
          "productContent"
        ];
    }

    function setActiveCate(item, index) {
      console.log("picking package cate: " + item);
      console.log("index of it is:   " + index);
      getFilteredSkuIndexAndSetActiveValue(item, index);
      setCandidateAvailableDates(filteredSkuIndex.value);
      setPrice(null);
      selectedDate.value = new Date();
      singleMatchedSku.value = null;
      setActiveDescription();
    }

    function setTourProductData(inputArray) {
      packageCategories.value = [];
      imageList.value = [];
      for (var index in inputArray) {
        var packageCat = extractAttributeValueFromProductPropertyBag(
          inputArray[index],
          TYPEOFPACKAGEPROP
        );
        if (packageCat != null) {
          packageCategories.value.push(packageCat);
        }

        var url = extractAttributeValueFromProductPropertyBag(
          inputArray[index],
          PRODUCTIMAGEURLPROP
        );
        if (url != null) {
          imageContainer.imageList.push(url);
        }

        // this if means only need to calculate days once because all this property in the same sku group should be same
        if (days.value == "") {
          days.value = extractAttributeValueFromProductPropertyBag(
            inputArray[index],
            DAYSPROP
          );
        }
      }
      console.log("imageContainer");
      console.log(imageContainer);
      // trick: remove duplicates
      packageCategories.value = [...new Set(packageCategories.value)];
      // force select the first package category on entering page
      setActiveCate(packageCategories.value[0], 0);
    }

    function labelValuePair(valArray) {
      var ret = [];
      valArray.forEach((x, i) => {
        var obj = { label: x, value: i };
        ret.push(obj);
      });
      return ret;
    }

    function processSkuGroups(inputArray) {
      if (inputArray.length > 0) {
        productName.value =
          inputArray[0]["hichinaProductBasicDTO"]["productName"];
        productTypeId.value =
          inputArray[0]["hichinaProductBasicDTO"]["productTypeId"];
        productTypeName.value =
          inputArray[0]["hichinaProductBasicDTO"]["productTypeName"];

        useSeoMeta({
          title: productName.value,
          description: productName.value,
          ogDescription: productName.value,
          ogTitle: productName.value,
          ogImage: "https://www.hichinatravel.com/static/png/name-67280b81.png",
          twitterCard: "summary_large_image",
        });

        if (productTypeId.value === TOURPRODUCTTYPE) {
          // 跟团游，则按照跟团游的页面模板来设置各个参数
          setTourProductData(inputArray);
        } else if (productTypeId.value === FLIGHTHOTELPRODUCTTYPE) {
          // 机票酒店套餐
          setFlightHotelProductData(inputArray);
        } else if (productTypeId.value === FLIGHTPRODUCTTYPE) {
          // 机票
          setFligtProductData(inputArray);
        } else if (productTypeId.value === HOTELPRODUCTTYPE) {
          // 酒店
          setHotelProductData(inputArray);
        } else if (productTypeId.value === LOCALSPECIALTYPRODUCTTYPE) {
          // 本地土特产
          setLocalSpecialtyProductData(inputArray);
        }
      }
    }

    function loadSkusInGroup() {
      var params = {};
      params.skuGroupId = route.params.skuGroupId;
      api
        .get("/api/public/productsku/bygroupidwithpropertybag", {
          params: params,
        })
        .then(function (response) {
          skusInGroup.skus = response.data.data;
          processSkuGroups(skusInGroup.skus);
        })
        .catch(function (error) {
          console.log(error);
        });
    }

    onMounted(() => {
      logPv();
      logView();
      loadSkusInGroup();
    });
    return {
      productTypeName,
      productName,
      productTypeId,
      days,
      slide: ref(1),
      autoplay: ref(true),
      imageContainer,
      productDescription,
      removeHtmlTag,
      handleSelectDate,
      selectedDate,
      state,
      renderComponent,
      LOCALSPECIALTYPRODUCTTYPE,
      FLIGHTPRODUCTTYPE,
      HOTELPRODUCTTYPE,
      packageCategories,
      labelValuePair,
      setActiveCate,
      activeIndex,
      adultCount,
      childCount,
      infantCount,
      adultUnitPrice,
      childUnitPrice,
      infantUnitPrice,
      generalPrice,
      buyCount,
    };
  },
};
</script>
