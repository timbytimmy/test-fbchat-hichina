<template>
  <div>
    <Survey :survey="survey" />
  </div>
</template>

<script setup>
  import 'survey-core/defaultV2.min.css';
  import { Survey } from 'survey-vue-ui';
  import { Model } from 'survey-core';

  const surveyJson = {
    elements: [{
      name: "FirstName",
      title: "Enter your first name:",
      type: "text"
    }, {
      name: "LastName",
      title: "Enter your last name:",
      type: "text"
    }]
  };
  
  const survey = ref({})
  function loadSurvey() {
     survey.value = new Model(surveyJson);
  }


  onMounted(() => {
    console.log("onload survey page")
    loadSurvey()
  })

 
</script>

<style scoped lang="scss">
 
</style>