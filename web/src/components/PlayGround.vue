<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>

<script>
import GameMap from './GameMap.vue';
export default {
    components:{
        GameMap,
    }
}
</script>


<style scoped>
div.playground{
    widows: 70vw;
    height: 70vh;
}
</style>