<template>
<CardView>

    <div class="row justify-content-md-center">
        <div class = "col-3">
            <!--  -->
            <form @submit.prevent="register">
                <div class="mb-3">
                    <label for="userName" class="form-label">account name</label>
                    <input v-model="username" type="text" class="form-control" id="userName" aria-describedby="emailHelp">
                    <!-- <div id="emailHelp" class="form-text">输入用户名</div> -->
                </div>
                <div class="mb-3">
                    <label for="InputPassword1" class="form-label">Password</label>
                    <input v-model="Password" type="password" class="form-control" id="InputPassword1">
                    <!-- <div id="emailHelp" class="form-text">输入密码</div> -->
                </div>
                <div class="mb-3">
                    <label for="InputPassword2" class="form-label">confirm Password</label>
                    <input v-model="confirmPassword" type="password" class="form-control" id="InputPassword2">
                    <!-- <div id="emailHelp" class="form-text">重复密码</div> -->
                </div>
                <div class="errorMessage">{{error_message}}</div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>


</CardView>
</template>


<script >
import CardView from '../../../components/CarView.vue'
import {ref } from 'vue'
import router from '@/router';
import $ from 'jquery'
export default{
    components:{
        CardView
    },
    setup(){
        let confirmPassword = ref('');
        let Password = ref('');
        let username = ref('');
        let error_message = ref('')

        let register = () => {
            error_message.value = ""
            $.ajax({
                url:"http://localhost:3030/user/account/register/",
                type:"post",
                contentType: 'application/json',
                data:JSON.stringify({
                    username : username.value,
                    password : Password.value,
                    confirmPassword : confirmPassword.value
                }),
                success(resp){
                    if(resp.error_message === "success"){
                        router.push({name : 'user_account_login'});
                    }else{
                        error_message.value = resp.error_message;
                    }
                }
            })
        }
        return {
            username,
            Password,
            confirmPassword,
            error_message,
            register,
        }
    }
}

</script>


<style scoped>
div.errorMessage{
    color: red;
}
</style>