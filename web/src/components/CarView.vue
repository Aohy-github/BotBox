<template>
    <div class="container content-feild">
        <div class="card">
            <div class="card-body">
                <slot></slot>
            </div>
        </div>
    </div>
</template>


<script>

</script>

<style>
div.content-feild{
    margin-top: 20px;    
}
.card {
  background: rgba(255, 255, 255, 0.5); /* 白色半透明 */
  /* 你也可以调整颜色和透明度 */
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border-radius: 8px;
}
</style>