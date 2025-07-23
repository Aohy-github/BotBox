
<template>
<CardView v-if="show_content">
    <div class="row justify-content-md-center">
        <div class = "col-3">
            <form @submit.prevent="login">
                <div class="mb-3">
                    <label for="userName" class="form-label">User</label>
                    <input v-model="username" type="text" class="form-control" id="userName" aria-describedby="emailHelp">
                    <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                </div>
                <div class="mb-3">
                    <label for="InputPassword" class="form-label">Password</label>
                    <input v-model="password" type="password" class="form-control" id="InputPassword1">
                </div>
                <!-- <div class="mb-3">
                    <label for="InputSecondPassword" class="form-label">comfirm password</label>
                    <input type="password" class="form-control" id="InputSecondPassword">
                </div> -->
                <div class="errorMessage">{{error_message}}</div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</CardView>
</template>


<script>
import CardView from '../../../components/CarView.vue'
import { useStore } from 'vuex'; 
import { ref } from 'vue'
import router from '@/router'
export default{
    components:{
        CardView
    },
    setup(){
        const store = useStore();
        let username = ref('')
        let password = ref('');
        let error_message = ref('');
        let show_content = ref(false)

        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            store.commit("updateToken" , jwt_token);
            store.dispatch("getInfo", {
                success(){
                    router.push({name : "pk_index"})
                },error(){
                    show_content = true;
                }
            })
        }else{
            show_content = true;
        }
        const login = () => {
          // console.log("get message:" + username.value + " " + password.value)
          error_message.value = ""
          store.dispatch("login",{
              username:username.value,
              password:password.value,
              success(){
                store.dispatch("getInfo",{
                    success(){
                        console.log("getinfo suceess")
                        router.push({name : "pk_index"})
                    },
                    error(resp){
                      error_message.value = resp.error_message;
                    }
                })
              },
              error(resp){
                console.log("登陆失败！")
                error_message.value = resp.error_message;
              }
          })

        }
        return {
            username,
            password,
            error_message,
            login,
            show_content,
        }
    }
}

</script>


<style scoped>
button{
    width:100%
}
div.errorMessage{
    color: red;
}

</style>