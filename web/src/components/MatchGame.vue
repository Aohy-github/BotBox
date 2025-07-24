<template lang="">
    <div class="matchground">
        <!-- 匹配布局 左边4份 中间选择bot4份 右边4份 -->
        <div class="row">
            <div class="col-6">
                <div class="user-photo" >
                    <img :src="store.state.user.photo" alt="">
                </div>
                <div class="user-name">
                    {{ store.state.user.username }}
                </div>
            </div>
            <!-- <div class="col-4">
                <div class="user-select" style="padding-top: 20vh; width: 60%; margin: 0 auto;">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>In person</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>           -->
            <div class="col-6">
                <div class="user-photo" >
                    <img :src="store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-name" >
                    {{ store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 5vh;">
                <button type="button" class="btn btn-warning btn-lg" style="width: 70%;" @click="update_button"> {{ match_button_info }} </button>
            </div>
        </div>
    </div>
</template>
<script>

import { useStore } from 'vuex'
import { ref } from 'vue'

// import $ from 'jquery'
export default {
    setup(){
        const store = useStore();
        let match_button_info = ref("Start Match");
        const update_button = ()=>{
            if(match_button_info.value === "Start Match"){
                match_button_info.value = "Cancel",
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching"
                }))
                
            }else{
                match_button_info.value = "Start Match",
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching"
                }))
            }
        }
        return{
            store,
            match_button_info,
            update_button
        }
    }
}
</script>
<style scoped>
div.matchground{
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
}
div.user-photo{
    text-align: center;
    padding-top: 15vh;
}
div.user-photo > img{
    border-radius: 50%;
    width: 20vh;
}
div.user-name{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    padding-top: 10px;
    color: white;
}

</style>