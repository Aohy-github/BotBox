<template>
<nav class="navbar navbar-expand-lg bg-body-tertiary bg-dark" data-bs-theme="dark">
  <div class="container-md">
    <a class="navbar-brand" href="#">Aohy</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link v-bind:class="route_name == 'pk_index' ? 'nav-link active': 'nav-link'" aria-current="page" :to="{name : 'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <router-link v-bind:class="route_name == 'ranklist_index' ? 'nav-link active': 'nav-link'"  :to="{name : 'ranklist_index'}">对局列表</router-link>
        </li>

      </ul>
      
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="true">
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><router-link :to="{name : 'user_account_bot'}" class="dropdown-item">My Bot</router-link></li>
              <li><a class="dropdown-item" href="#" @click="logout">login out </a></li>
              <!-- <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#">Something else here</a></li> -->
            </ul>
          </li>
        </ul>
        
        <ul class="navbar-nav" v-else>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="true">
              Self massage
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li><router-link :to="{name : 'user_account_login'}" class="dropdown-item">login</router-link></li>
              <li><router-link :to="{name : 'user_account_register'}" class="dropdown-item">register</router-link></li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
        </ul>
      
    </div>
  </div>
</nav>
</template>


<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { useStore } from 'vuex';
export default{
    setup(){
        const store = useStore();
        const route = useRoute();
      
        console.log(store.state.user.is_login)
        let route_name = computed(() =>{
            return route.name;
        })

        const logout = () =>{
          store.dispatch("logout")
        }
        return {
            route_name,
            logout,
        };
    }
}

</script>


<style scoped></style>