<template>
<CardView>
    <PalyGround v-if="store.state.pk.status === 'playing'"></PalyGround>
    <MatchGame v-if="store.state.pk.status === 'matching'"></MatchGame>
</CardView>
</template>


<script >
import CardView from '../../components/CarView.vue'
import PalyGround from '../../components/PlayGround.vue'
import MatchGame from '../../components/MatchGame.vue';
import { useStore } from 'vuex';
import { onMounted , onUnmounted } from 'vue';
export default{
    components:{
        CardView,
        PalyGround,
        MatchGame
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://localhost:3030/websocket/${store.state.user.token}/`


        let socket = null;
        onMounted(()=>{
            store.commit("updateOpponent",{
                username:"我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = ()=>{
                console.log("front connection!");
                store.commit("updateSocket",socket)
            }
            
            socket.onmessage = msg =>{
                console.log(msg)
                const data = JSON.parse(msg.data);
                if(data.event === "success"){ // 匹配成功

                    // 更新对手信息
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo:data.opponent_photo
                    })

                    store.commit("updateGameMap", {
                        g:data.gamemap
                    })
                    setTimeout(() =>{
                        //切换对战状态
                        //store.state.pk.status = "playing"
                        store.commit("updateStatus","playing");
                    }, 2000)
                    
                }
            }

            socket.onclose= ()=>{
                console.log("front disconnection");
            }
        })
        onUnmounted(()=>{
            socket.close();
        })

        
        return {
            store
        }
    }

}

</script>


<style scoped></style>