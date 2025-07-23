<template>
  <NavBar />
  <router-view></router-view>
</template>

<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'

export default {
  components: {
    NavBar
  },
  setup() {
  }
}

</script>

<style>
/* body{
  background-image: url("@/assets/Snipaste_2023-08-04_16-32-22.png");
} */
</style>
