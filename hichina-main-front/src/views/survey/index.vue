<template>
  <div>
  </div>
</template>

<script setup>
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

  }


  onMounted(() => {
    console.log("onload survey page")
    loadSurvey()
  })

 
</script>

<style scoped lang="scss">
 
</style>